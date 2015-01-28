package com.radthorne.CactusChat.Events.EventClasses;

import com.radthorne.CactusChat.Events.Cancellable;
import com.radthorne.CactusChat.Events.Event;

public
class MessageReceivedEvent extends Event implements Cancellable
{
	private boolean cancelled;
	private String message;

	public MessageReceivedEvent(String message)
	{
		this.message = message;
	}

	@Override
	public
	boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public
	void setCancelled( boolean cancelled )
	{
		this.cancelled = cancelled;
	}

	public
	String getMessage()
	{
		return message;
	}

	public
	void setMessage( String message )
	{
		this.message = message;
	}
}
