package com.radthorne.CactusChat.console;

import com.radthorne.CactusChat.Main;
import com.radthorne.CactusChat.bot.Bot;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public
class Console
{

    private ConsoleReader        reader;
    private ConsoleCommandThread thread;
    private BotConsoleHandler    consoleHandler;
    private Bot                  bot;
    private boolean running = true;

    public
    Console( Bot bot )
    {
        this.bot = bot;
        this.consoleHandler = new BotConsoleHandler();
        Logger logger = Logger.getLogger( "" );
        for ( Handler handler : logger.getHandlers() )
        {
            logger.removeHandler( handler );
        }
        logger.addHandler( this.consoleHandler );
        this.reader = Main.getReader();
    }

    public
    void stop()
    {
        this.consoleHandler.flush();
        this.running = false;
    }

    public
    void setup()
    {
        this.thread = new ConsoleCommandThread();
        this.thread.setDaemon( true );
        this.thread.start();
    }

    private
    class ConsoleCommandThread extends Thread
    {

        @Override
        public
        void run()
        {
            String command;
            while ( running )
            {
                try
                {
                    command = reader.readLine( "", null );
                    if ( command == null || command.trim().length() == 0 )
                    {
                        continue;
                    }
                    bot.chat( command );
                }
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    private
    class BotConsoleHandler extends ConsoleHandler
    {

        @Override
        public synchronized
        void flush()
        {
            try
            {
                reader.print( ConsoleReader.RESET_LINE + "" );
                reader.flush();
                super.flush();
                try
                {
                    reader.drawLine();
                }
                catch ( Throwable ex )
                {
                    reader.getCursorBuffer().clear();
                }
                reader.flush();
            }
            catch ( IOException ex )
            {
                System.err.println( "Exception flushing console output" );
                ex.printStackTrace();
            }
        }
    }
}
