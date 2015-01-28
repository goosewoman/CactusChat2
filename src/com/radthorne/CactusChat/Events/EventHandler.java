package com.radthorne.CactusChat.Events;

import com.radthorne.CactusChat.Main;

import java.util.ArrayList;
import java.util.List;

public
class EventHandler
{
	private List<EventListener> listeners = new ArrayList<EventListener>();

	public List<EventListener> getListeners()
	{
		return listeners;
	}

	public void addHandler(EventListener listener)
	{
		if(!listeners.contains( listener ))
		{
			listeners.add( listener );
		}
	}
}
