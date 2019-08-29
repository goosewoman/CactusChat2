package com.radthorne.CactusChat.util;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.SessionListener;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import com.radthorne.CactusChat.Main;

public class CactusChatClient
{

	private String  HOST;
	private int     PORT;
	private Session session;
	private Client client;
	MinecraftProtocol protocol;
	private boolean connected = false;

	public void login( String username, String password ) throws RequestException
	{
		Main.debug( "Attempting to log in..." );
		protocol = new MinecraftProtocol( username, password );
		Main.debug( "Logged in successfully" );
	}

	public void connect( String host, int port, SessionListener listener )
	{
		HOST = host;
		PORT = port;
		Main.debug( "Attempting to connect to " + HOST + "..." );
		client = new Client( HOST, PORT, protocol, new TcpSessionFactory() );
		session = client.getSession();
		session.addListener( listener );
		session.connect();
		Main.debug( "Connected successfully" );
		connected = true;
	}

	public int getPORT()
	{
		return PORT;
	}

	public String getHOST()
	{
		return HOST;
	}

	public Session getSession()
	{
		return session;
	}

	public Client getClient()
	{
		return client;
	}

	public boolean isConnected()
	{
		return connected;
	}

	public void disconnect()
	{
		disconnect( "Quitting" );
	}

	public void disconnect(String reason)
	{
		session.disconnect( reason );
	}
}
