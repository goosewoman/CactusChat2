package com.radthorne.CactusChat.msg;

public enum ChatColour
{
	BOLD( 'l' ),
	UNDERLINED( 'n' ),
	STRIKETHROUGH( 'm' ),
	ITALIC( 'o' ),
	OBFUSCATED( 'k' ),
	RESET( 'r' ),
	WHITE( 'f' ),
	YELLOW( 'e' ),
	LIGHT_PURPLE( 'd' ),
	RED( 'c' ),
	AQUA( 'b' ),
	GREEN( 'a' ),
	BLUE( '9' ),
	DARK_GRAY( '8' ),
	GRAY( '7' ),
	GOLD( '6' ),
	DARK_PURPLE( '5' ),
	DARK_RED( '4' ),
	DARK_AQUA( '3' ),
	DARK_GREEN( '2' ),
	DARK_BLUE( '1' ),
	BLACK( '0' );
	private final String toString;

	private ChatColour( char code )
	{
		this.toString = "§" + code;
	}

	public String getCode()
	{
		return this.toString;
	}
}
