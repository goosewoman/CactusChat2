package com.radthorne.CactusChat.msg;

import org.fusesource.jansi.Ansi;

import java.util.EnumMap;
import java.util.Map;

public
class AnsiColour
{

    private static final Map<ChatColour, String> replacements = new EnumMap<>( ChatColour.class );
    private static final ChatColour[]            colors       = ChatColour.values();

    public static
    String strip( String message )
    {
        if ( message == null )
        {
            return null;
        }
        for ( ChatColour color : colors )
        {
            String replacement = "";
            message = message.replaceAll( "(?i)" + color.getCode(), replacement );
        }
        return message + Ansi.ansi().reset().toString();
    }

    public static
    String colour( String message )
    {
        if ( message == null )
        {
            return null;
        }
        for ( ChatColour color : colors )
        {
            String replacement = replacements.get( color );
            message = message.replaceAll( "(?i)" + color.getCode(), replacement != null ? replacement : "" );
        }
        return message;
    }

	public static
	String getColourCode( String name )
	{
		return ChatColour.valueOf( name.toUpperCase() ).getCode();
	}

	public static
	String reset()
	{
		return Ansi.ansi().reset().toString();
	}

    static
    {
        replacements.put( ChatColour.BLACK, Ansi.ansi().fg( Ansi.Color.BLACK ).boldOff().toString() );
        replacements.put( ChatColour.DARK_BLUE, Ansi.ansi().fg( Ansi.Color.BLUE ).boldOff().toString() );
        replacements.put( ChatColour.DARK_GREEN, Ansi.ansi().fg( Ansi.Color.GREEN ).boldOff().toString() );
        replacements.put( ChatColour.DARK_AQUA, Ansi.ansi().fg( Ansi.Color.CYAN ).boldOff().toString() );
        replacements.put( ChatColour.DARK_RED, Ansi.ansi().fg( Ansi.Color.RED ).boldOff().toString() );
        replacements.put( ChatColour.DARK_PURPLE, Ansi.ansi().fg( Ansi.Color.MAGENTA ).boldOff().toString() );
        replacements.put( ChatColour.GOLD, Ansi.ansi().fg( Ansi.Color.YELLOW ).boldOff().toString() );
        replacements.put( ChatColour.GRAY, Ansi.ansi().fg( Ansi.Color.WHITE ).boldOff().toString() );
        replacements.put( ChatColour.DARK_GRAY, Ansi.ansi().fg( Ansi.Color.BLACK ).bold().toString() );
        replacements.put( ChatColour.BLUE, Ansi.ansi().fg( Ansi.Color.BLUE ).bold().toString() );
        replacements.put( ChatColour.GREEN, Ansi.ansi().fg( Ansi.Color.GREEN ).bold().toString() );
        replacements.put( ChatColour.AQUA, Ansi.ansi().fg( Ansi.Color.CYAN ).bold().toString() );
        replacements.put( ChatColour.RED, Ansi.ansi().fg( Ansi.Color.RED ).bold().toString() );
        replacements.put( ChatColour.LIGHT_PURPLE, Ansi.ansi().fg( Ansi.Color.MAGENTA ).bold().toString() );
        replacements.put( ChatColour.YELLOW, Ansi.ansi().fg( Ansi.Color.YELLOW ).bold().toString() );
        replacements.put( ChatColour.WHITE, Ansi.ansi().fg( Ansi.Color.WHITE ).bold().toString() );
        replacements.put( ChatColour.OBFUSCATED, Ansi.ansi().a( Ansi.Attribute.BLINK_SLOW ).toString() );
        replacements.put( ChatColour.BOLD, Ansi.ansi().a( Ansi.Attribute.INTENSITY_BOLD ).toString() );
        replacements.put( ChatColour.STRIKETHROUGH, Ansi.ansi().a( Ansi.Attribute.STRIKETHROUGH_ON ).toString() );
        replacements.put( ChatColour.UNDERLINED, Ansi.ansi().a( Ansi.Attribute.UNDERLINE ).toString() );
        replacements.put( ChatColour.ITALIC, Ansi.ansi().a( Ansi.Attribute.ITALIC ).toString() );
        replacements.put( ChatColour.RESET, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.DEFAULT ).toString() );
    }
}
