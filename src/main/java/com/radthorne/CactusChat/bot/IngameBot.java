package com.radthorne.CactusChat.bot;


import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.radthorne.CactusChat.Main;
import com.radthorne.CactusChat.console.Console;
import com.radthorne.CactusChat.console.ReadThread;
import com.radthorne.CactusChat.util.CactusChatClient;

import java.io.IOException;

public class IngameBot extends Bot
{

	private CactusChatClient conn;
	private Console          console;
	private boolean loggedin = false;
	private String username;
	private int    entityId;
	private double y;
	private double x;
	private double z;
	private float  yaw;
	private float  pitch;
	private boolean onGround;

	public void start( Console console, String username, String password, String host, int port )
	{
		this.username = username;
		this.console = console;
		Runtime.getRuntime().addShutdownHook( new Thread()
		{
			@Override
			public void run()
			{
				if ( loggedin )
				{
					quit();
				}
			}
		} );

		this.conn = new CactusChatClient();

		try
		{
			this.conn.login( username, password );
		}
		catch ( RequestException e )
		{
			System.out.println( e.getMessage() );
			System.out.println( "Login error: Press any key to exit" );
			try
			{
				Main.getReader().readCharacter();
				System.exit( -1 );
			}
			catch ( IOException e1 )
			{
				e1.printStackTrace();
			}
		}
		this.conn.connect( host, port, new SessionAdapter(this) );
		new ReadThread().start();
		Main.debug( "sucessfully started");
	}

	public void chat( String message )
	{
		this.conn.getSession().send( new ClientChatPacket( message ) );
	}

	@Override
	public void error( String message ){}
	@Override
	public void console( String message ){}
	@Override
	public void raw( String message ) {}

	public String getUsername()
	{
		return username;
	}

	public CactusChatClient getConn()
	{
		return conn;
	}

	public void quit()
	{
		if ( this.conn.isConnected() )
		{
			this.conn.disconnect( "Quitting." );
		}
		this.console.stop();
	}

	public void setEntityId( int entityId )
	{
		this.entityId = entityId;
	}

	public int getEntityId()
	{
		return entityId;
	}

	public void setY( double y )
	{
		this.y = y;
	}

	public double getY()
	{
		return y;
	}

	public void setX( double x )
	{
		this.x = x;
	}

	public double getX()
	{
		return x;
	}

	public void setZ( double z )
	{
		this.z = z;
	}

	public double getZ()
	{
		return z;
	}

	public void setYaw( float yaw )
	{
		this.yaw = yaw;
	}

	public float getYaw()
	{
		return yaw;
	}

	public void setPitch( float pitch )
	{
		this.pitch = pitch;
	}

	public float getPitch()
	{
		return pitch;
	}

	public void setOnGround( boolean onGround )
	{
		this.onGround = onGround;
	}

	public boolean isOnGround()
	{
		return onGround;
	}

	public void lookEast()
	{

	}
}
