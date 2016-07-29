
package com.radthorne.CactusChat.msg;

import com.radthorne.CactusChat.Main;

import java.io.OutputStream;
import java.io.PrintStream;

public
class ConsoleOutput extends PrintStream
{

    public
    ConsoleOutput( OutputStream out )
    {
        super( out, true );
    }

    public
    void println( String x )
    {
        try
        {
            Main.getReader().println( '\r' + x );
            Main.getReader().drawLine();
            Main.getReader().flush();
        }
        catch ( Throwable ex )
        {
            Main.getReader().getCursorBuffer().clear();
        }
    }
}