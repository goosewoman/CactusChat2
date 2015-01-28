package com.radthorne.CactusChat.Events;

import com.radthorne.CactusChat.Events.EventClasses.MessageReceivedEvent;

public
interface Listener
{

	public
	MessageReceivedEvent onMessageReceivedEvent( MessageReceivedEvent event );
}
