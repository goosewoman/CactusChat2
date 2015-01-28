package com.radthorne.CactusChat.util;

public
class HostPortPair
{

    private final String host;
    private final int    port;

    public
    HostPortPair( String host, int port )
    {
        this.host = host;
        this.port = port;
    }

    public
    String getHost()
    {
        return host != null ? host : "localhost";
    }

    public
    int getPort()
    {
        return port > 0 ? port : 25565;
    }
}

