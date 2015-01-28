package com.radthorne.CactusChat.util;

import java.io.File;

public
class OS
{

    private
    OS()
    {
        // nada
    }

    public static
    String getOsName()
    {
        return System.getProperty( "os.name", "unknown" );
    }

    public static
    boolean isWindows()
    {
        return ( getOsName().toLowerCase().contains( "windows" ) );
    }

    public static
    boolean isLinux()
    {
        return getOsName().toLowerCase().contains( "linux" );
    }

    public static
    boolean isMac()
    {
        final String os = getOsName().toLowerCase();
        return os.startsWith( "mac" ) || os.startsWith( "darwin" );
    }

    public static
    String getAppDir()
    {
        if ( OS.isWindows() )
        {
            return System.getenv( "APPDATA" ) + File.separator + "CactusChat";
        }
        else if ( OS.isLinux() )
        {
            return System.getProperty( "user.home" ) + File.separator + ".CactusChat";
        }
        else if ( OS.isMac() )
        {
            return System.getProperty( "user.home" ) + File.separator + "Library" + File.separator + "Application Support" + File.separator + "CactusChat";
        }
        return ".";
    }
}