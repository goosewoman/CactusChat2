package com.radthorne.CactusChat;

import com.radthorne.CactusChat.util.OS;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.CodeSource;

public
class LaunchWrapper
{
    public static
    void main( String args[] )
    {
        try
        {
            CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
            String path = codeSource.getLocation().getFile();
            String decodedPath = URLDecoder.decode( path, "UTF-8" );
            if( OS.isWindows() && System.console() == null)
            {
                new ProcessBuilder( "cmd", "/c", "start", "java", "-cp", decodedPath, "com.radthorne.CactusChat.Main" ).start();
            }
            else if(OS.isMac() && System.console() == null)
            {
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName( "AppleScript" );
                String scriptLaunch = "tell application \"Terminal\" to activate";
                String scriptLaunch2 = "tell application \"System Events\" to tell process \"Terminal\" to keystroke \"t\" using command down";
                String script = "tell application \"Terminal\" to do script \"java -cp " + decodedPath + " com.radthorne.CactusChat.Main\" in window 1";
                engine.eval(scriptLaunch);
                engine.eval(scriptLaunch2);
                engine.eval(script);
            }
            else if (OS.isLinux() && System.console() == null)
            {
                new ProcessBuilder( "xterm", "-e", "java", "-cp", decodedPath, "com.radthorne.CactusChat.Main" ).start();
            }
            else
            {
                Main.main( args );
            }
        }
        catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        catch ( ScriptException e )
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
