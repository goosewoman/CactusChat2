package com.radthorne.CactusChat.bot;

import com.radthorne.CactusChat.console.Console;

public abstract
class Bot
{

    public
    Bot()
    {
    }

    public abstract
    void start( Console console, String username, String password, String host, int port );

    public abstract
    void chat( String message );

    public abstract
    void error( String message );

    public abstract
    void console( String message );

    public abstract
    void raw( String message );

    public abstract
    String getUsername();

    public abstract
    void quit();
}
