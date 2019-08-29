package com.radthorne.CactusChat.bot;

import com.github.steveice10.mc.protocol.data.message.ChatColor;
import com.github.steveice10.mc.protocol.data.message.ChatFormat;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TranslationMessage;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.packetlib.event.session.*;
import com.radthorne.CactusChat.Main;
import com.radthorne.CactusChat.msg.AnsiColour;
import org.apache.commons.lang.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SessionAdapter implements SessionListener
{

    private IngameBot bot;

    public SessionAdapter( IngameBot bot )
    {
        this.bot = bot;
    }

    public void packetReceived( PacketReceivedEvent event )
    {
        if ( event.getPacket() instanceof ServerPlayerListEntryPacket )
        {
            return;
        }
        if ( event.getPacket() instanceof ServerChatPacket )
        {
            ServerChatPacket packet = event.getPacket();
            if ( packet.getMessage() != null )
            {
                handleChat( packet.getMessage() );
            }
        }
        else if ( event.getPacket() instanceof ServerJoinGamePacket )
        {
            ServerJoinGamePacket packet = event.getPacket();
            bot.setEntityId( packet.getEntityId() );
            // TODO: onJoinGameEvent
        }
        else if ( event.getPacket() instanceof ServerRespawnPacket )
        {
            ServerRespawnPacket packet = event.getPacket();
            // TODO: onRespawnEvent
        }
        else if ( event.getPacket() instanceof ServerPlayerPositionRotationPacket )
        {
            ServerPlayerPositionRotationPacket packet = event.getPacket();
            bot.setX( packet.getX() );
            bot.setY( packet.getY() );
            bot.setZ( packet.getZ() );
            bot.setYaw( packet.getYaw() );
            bot.setPitch( packet.getPitch() );
            // TODO: onPositionRotationEvent
            event.getSession().send( new ClientPlayerPositionRotationPacket( true, bot.getX(), bot.getY(), bot.getZ(), bot.getYaw(), bot.getPitch() ) );
            Main.debug( "updated position" );
        }
    }

    @Override
    public void packetSending( PacketSendingEvent event )
    {

    }

    public void disconnected( DisconnectedEvent event )
    {
        System.out.println( "Disconnected: " + Message.fromString( event.getReason() ).getFullText() );
        Main.reconnect();
    }

    public void packetSent( PacketSentEvent packetSentEvent )
    {
        // TODO: onPacketSentEvent(PacketSentEvent packetSentEvent)
    }

    public void connected( ConnectedEvent connectedEvent )
    {

    }

    public void disconnecting( DisconnectingEvent disconnectingEvent )
    {
        if ( disconnectingEvent.getCause() != null )
        {
            disconnectingEvent.getCause().printStackTrace();

        }
    }

    private void handleChat( Message mes )
    {
        if ( mes == null )
        {
            return;
        }

        StringBuilder message = new StringBuilder();
        List<Message> messages = mes.getExtra();
        if ( mes instanceof TranslationMessage )
        {
            Message[] params = ( ( TranslationMessage ) mes ).getTranslationParams();
            String key = ( ( TranslationMessage ) mes ).getTranslationKey();
            String prefix = "";
            String suffix = "";
            if ( key.equals( "chat.type.text" ) )
            {
                prefix = "<";
                suffix = "> ";
            }
            if ( key.equals( "chat.type.announcement" ) )
            {
                prefix = "[";
                suffix = "] ";
            }
            Message username = params[0];
            message.append( prefix );
            message.append( getFormatting( username ) );
            message.append( username.getText() );
            message.append( suffix );
            params = Arrays.copyOfRange( params, 1, params.length );
            messages.addAll( Arrays.asList( params ) );
        }
        for ( Message m : messages )
        {
            if ( m == null || m.toJson() == null )
            {
                continue;
            }
            message.append( getFormatting( m ) );
            message.append( m.getText() );
            message.append( "§r" );
        }
        String time = new SimpleDateFormat( "[HH:mm:ss] " ).format( new Date() );
        String formattedMessage = StringEscapeUtils.unescapeJava( message.toString() );
        //TODO: onMessageReceivedEvent(MessageReceivedEvent event);
        Main.println( time + formattedMessage, true );
    }

    private StringBuilder getFormatting( Message m )
    {
        StringBuilder sb = new StringBuilder();
        if ( m.getStyle().getColor() != null )
        {
            ChatColor colour = m.getStyle().getColor();
            sb.append( AnsiColour.getColourCode( colour.toString() ) );
        }
        if ( m.getStyle().getFormats().size() > 0 )
        {
            for ( ChatFormat format : m.getStyle().getFormats() )
            {
                sb.append( AnsiColour.getColourCode( format.toString() ) );
            }
        }
        return sb;
    }
}
