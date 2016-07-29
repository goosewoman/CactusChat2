package com.radthorne.CactusChat.util;

import com.radthorne.CactusChat.Main;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.SessionListener;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

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
		protocol = new MinecraftProtocol( username, password, false );
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
