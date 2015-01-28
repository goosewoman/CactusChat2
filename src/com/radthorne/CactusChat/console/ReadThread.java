package com.radthorne.CactusChat.console;

import com.radthorne.CactusChat.Main;
import org.apache.commons.lang.WordUtils;

import java.io.IOException;

public
class ReadThread extends Thread
{

    public static
    String[] breakString( String str, int size )
    {
        str = WordUtils.wrap( str, size );
        return str.split( "\n" );
    }

    public
    void run()
    {
        while ( true )
        {
            try
            {
                String s = Main.getReader().readLine( "" );
                if ( ! s.equals( "" ) )
                {
                    String[] arr = breakString( s, 100 );
                    for ( String message : arr )
                    {
                        Main.getBot().chat( message );
                        wait( 1500 );
                    }
                }
            }
            catch ( IOException ex )
            {
                ex.printStackTrace();
            }
            catch ( Exception e )
            {
                Main.debug( e.getMessage() );
            }
        }
    }
}