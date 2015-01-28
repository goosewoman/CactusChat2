package com.radthorne.CactusChat.Events;

public
interface Cancellable
{

	public
	boolean isCancelled();

	public
	void setCancelled( boolean cancelled );
}
