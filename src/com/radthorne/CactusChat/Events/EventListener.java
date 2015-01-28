package com.radthorne.CactusChat.Events;

import com.radthorne.CactusChat.Events.EventClasses.MessageReceivedEvent;
import com.radthorne.CactusChat.Main;

public
class EventListener implements Listener
{

	public EventListener(Main main)
	{

	}

	@Override
	public
	MessageReceivedEvent onMessageReceivedEvent( MessageReceivedEvent event )
	{
		return event;
	}
}
