package com.radthorne.CactusChat;

import com.radthorne.CactusChat.bot.IngameBot;
import com.radthorne.CactusChat.console.Console;
import com.radthorne.CactusChat.msg.AnsiColour;
import com.radthorne.CactusChat.msg.ConsoleOutput;
import com.radthorne.CactusChat.util.HostPortPair;
import com.radthorne.CactusChat.util.OS;
import jline.console.ConsoleReader;
import org.apache.commons.cli.*;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.xbill.DNS.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.CodeSource;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.steveice10.mc.protocol.data.message.Message.fromString;

public
class Main
{

	private static boolean       reconnect;
	private static boolean       debug;
	private static String        username;
	private static boolean       colour;
	private static String        password;
	private static String        host;
	private static ConsoleReader reader;
	private static IngameBot     bot;
	private static Console       console;
	private static int           entityId;
	private static boolean       inGame;
	private static boolean log = true;
	private static int port        = 25565;
	private static int restartTime = 30;

	public static
	void main(String[] args)
	{
		try
		{
			reader = new ConsoleReader();
			Runtime.getRuntime().addShutdownHook( new ShutDownThread() );
			System.setOut( new ConsoleOutput( System.out ) );
			CommandLineParser parser = new PosixParser();
			Options options = new Options();
			options.addOption( "u", "username", true, "Your minecraft username. (requires --password)" );
			options.addOption( "p", "password", true, "Your minecraft Password. (requires --username)" );
			options.addOption( "s", "server", true, "Minecraft server hostname. (<hostname<:port>>)" );
			options.addOption( "r", "reconnect", true, "Automatically reconnects on kick/quit after 5 seconds." );
			options.addOption( "h", "help", false, "Shows this help" );
			options.addOption( "q", "quiet", false, "Lessens the amount of system messages you receive." );
			options.addOption( "l", "log", false, "Turns chat logging off" );
			options.addOption( "c", "colour", false, "If it's used, it doesn't strip colour codes before logging to the chat log." );
			options.addOption( "d", "debug", false, "turns debugging messages on" );
			options.addOption( "e", "error", false, "logs error messages" );
			options.addOption( "b", "bot", false, "Enables plugin loading" );
			CommandLine line = parser.parse( options, args );
			if ( line.hasOption( 'e' ) )
			{
				System.setErr( new PrintStream( new FileOutputStream( OS.getAppDir() + File.separator + "error.log" ) ) );
			}
			if ( line.hasOption( 'l' ) )
			{
				log = true;
			}
			if ( line.hasOption( 'c' ) )
			{
				colour = true;
			}
			if ( line.hasOption( 'd' ) )
			{
				debug = true;
				System.out.println( "DEBUGGING IS ON!" );
			}
			if ( line.hasOption( 'h' ) )
			{
				CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
				String jarFile = new File( codeSource.getLocation().getFile() ).getName();
				System.out.println( "usage: java -jar " + jarFile + " [OPTIONS]" );
				System.out.println( " -c,--colour           If it's used, it doesn't strip colourcodes before logging to the chatlog." );
				System.out.println( " -l,--log              turns chatlogging off" );
				System.out.println( " -h,--help             Shows this help" );
				System.out.println( " -p,--password <pass>  Your minecraft Password. (requires --username)" );
				System.out.println( " -r,--reconnect <time> Automatically reconnects on disconnect after <time> seconds" );
				System.out.println( " -s,--server <host>    Minecraft server hostname. (<hostname[:port]>)" );
				System.out.println( " -u,--username <user>  Your minecraft username. (requires --password)" );
				System.out.println( " -d,--debug            Enables debugging messages" );
				System.out.println( " -b,--bot              Enables plugin loading" );
				System.exit( 0 );
			}
			if ( line.hasOption( 'l' ) )
			{
				log = false;
			}
			if ( line.hasOption( 'r' ) )
			{
				reconnect = true;
				restartTime = Integer.parseInt( line.getOptionValue( 'r' ) );
				Main.debug( "Automatic reconnecting enabled." );
			}
			if ( ( line.hasOption( "u" ) ) && ( line.hasOption( "p" ) ) )
			{
				username = line.getOptionValue( "u" );
				password = line.getOptionValue( "p" );
			}
			else
			{
				System.out.println( "Username:" );
				username = reader.readLine();
				System.out.println( "Password:" );
				password = reader.readLine( '*' );
			}
			if ( line.hasOption( 's' ) )
			{
				host = line.getOptionValue( 's' );
			}
			else
			{
				System.out.println( "Server:" );
				host = reader.readLine();
			}
			HostPortPair hpp = getHostPortPairFromSRV( host );
			if ( hpp == null )
			{
				if ( host.contains( ":" ) )
				{
					String[] hostsplit = host.split( ":" );
					if ( ! hostsplit[ 0 ].equals( "" ) )
					{
						host = hostsplit[ 0 ];
					}
					else
					{
						host = "localhost";
					}
					if ( ! hostsplit[ 1 ].equals( "" ) )
					{
						port = Integer.parseInt( hostsplit[ 1 ] );
					}
					else
					{
						port = 25565;
					}
				}
			}
			else
			{
				host = hpp.getHost();
				port = hpp.getPort();
			}
			bot = new IngameBot();
			console = new Console( bot );
			Runtime.getRuntime().addShutdownHook( new Thread()
			{
				@Override
				public
				void run()
				{
					console.stop();
				}
			} );
			bot.start( console, username, password, host, port );
		}
		catch ( ParseException e )
		{
			e.printStackTrace();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public static
	HostPortPair getHostPortPairFromSRV( String name )
	{
		String query = "_minecraft._tcp." + name;
		try
		{
			Record[] records = new Lookup( query, Type.SRV ).run();
			SRVRecord srv = ( SRVRecord ) records[ 0 ];
			String hostname = srv.getTarget().toString().replaceFirst( "\\.$", "" );
			int port = srv.getPort();
			return new HostPortPair( hostname, port );
		}
		catch ( TextParseException e )
		{
			e.printStackTrace();
		}
		catch ( Exception e )
		{
		}
		return null;
	}

	public static
	boolean isLog()
	{
		return log;
	}

	public static
	void setLog( boolean log )
	{
		Main.log = log;
	}

	public static
	boolean isReconnect()
	{
		return reconnect;
	}

	public static
	void setReconnect( boolean reconnect )
	{
		Main.reconnect = reconnect;
	}

	public static
	boolean isColour()
	{
		return colour;
	}

	public static
	void setColour( boolean colour )
	{
		Main.colour = colour;
	}

	public static
	String getUsername()
	{
		return username;
	}

	public static
	void setUsername( String username )
	{
		Main.username = username;
	}

	public static
	boolean isDebug()
	{
		return debug;
	}

	public static
	void setDebug( boolean debug )
	{
		Main.debug = debug;
	}

	public static
	String getPassword()
	{
		return password;
	}

	public static
	void setPassword( String password )
	{
		Main.password = password;
	}

	public static
	String getHost()
	{
		return host;
	}

	public static
	void setHost( String host )
	{
		Main.host = host;
	}

	public static
	ConsoleReader getReader()
	{
		return reader;
	}

	public static
	int getPort()
	{
		return port;
	}

	public static
	void setPort( int port )
	{
		Main.port = port;
	}

	public static
	IngameBot getBot()
	{
		return bot;
	}

	public static
	void setBot( IngameBot bot )
	{
		Main.bot = bot;
	}

	public static
	void log( String message ) throws IOException
	{
		Main.debug( "logging to file");
		String time = new SimpleDateFormat( "[HH:mm:ss] " ).format( new Date() );
		String date = new SimpleDateFormat( "yyyy-MM-dd" ).format( new Date() );
		char newline = '\n';
		String host = Main.host.contains( ":" ) ? Main.host.split( ":" )[ 0 ] : Main.host;
		String filename = "Chatlog-" + host + "-" + date + ".log";
		String appdata = OS.getAppDir();
		File userDir = new File( appdata + File.separator + Main.username + File.separator );
		File dir = new File( userDir, "Chatlogs" + File.separator );
		File logfile = new File( dir, filename );
		if ( ! userDir.getParentFile().exists() )
		{
			userDir.getParentFile().mkdir();
		}
		if ( ! userDir.exists() )
		{
			userDir.mkdir();
		}
		if ( ! dir.getParentFile().exists() )
		{
			dir.getParentFile().mkdir();
		}
		if ( ! dir.exists() )
		{
			dir.mkdir();
		}
		if ( ! logfile.exists() )
		{
			logfile.createNewFile();
		}
		String endOfLine = "\033[m";
		if ( logfile.canWrite() )
		{
			if ( message.endsWith( endOfLine ) )
			{
				message = message.substring( 0, message.length() - endOfLine.length() );
			}
			FileUtils.writeStringToFile( logfile, message + newline, true );
		}
		else
		{
			debug( "CAN'T WRITE TO FILE!!" );
		}
	}

	public static
	void println( String message )
	{
		println( message, false );
	}

	public static
	void println( String message, boolean log )
	{
		if ( Main.getReader().getTerminal().isAnsiSupported() )
		{
			System.out.println( AnsiColour.colour( message ) );
		}
		else
		{
			System.out.println( message );
		}
		if ( isLog() )
		{
			if ( ! isColour() )
			{
				message = fromString( message ).toJsonString();
			}
			try
			{
				log( message );
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}
	}

	public static
	void debug( String message )
	{
		if ( isDebug() )
		{
			System.out.println( message );
		}
	}

	public static
	void reconnect()
	{
		if ( ! isReconnect() )
		{
			return;
		}
		try
		{
			int time = Main.restartTime;
			System.out.print( "\rRestarting in " + time + " seconds.\n" );
			String dots = "";
			String space = StringUtils.repeat( " ", time );
			while ( time > 0 )
			{
				dots = dots.concat( "=" );
				space = space.substring( 0, space.length() - 1 );
				System.out.print( "\r[" + dots + space + "]\r" );
				time--;
				Thread.sleep( 1000L );
			}
			System.out.print( '\n' );
			bot.start( Main.console, Main.getUsername(), Main.getPassword(), Main.getHost(), Main.getPort() );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			reconnect();
		}
	}

	public static
	boolean isInGame()
	{
		return inGame;
	}

	public static
	int getEntityId()
	{
		return entityId;
	}

	static
	class ShutDownThread extends Thread
	{

		public
		void run()
		{
			try
			{
				Main.reader.getTerminal().restore();
				bot.quit();
			}
			catch ( Throwable ex )
			{
			}
		}
	}
}
