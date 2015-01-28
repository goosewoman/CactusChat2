package com.radthorne.CactusChat.Events;

public
class Event
{
	private String name;

	public String getEventName() {
		if (name == null) {
			name = getClass().getSimpleName();
		}
		return name;
	}


}
