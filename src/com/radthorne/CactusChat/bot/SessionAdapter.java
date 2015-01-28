package com.radthorne.CactusChat.bot;

import com.radthorne.CactusChat.Main;
import com.radthorne.CactusChat.msg.AnsiColour;
import org.apache.commons.lang.StringEscapeUtils;
import org.spacehq.mc.protocol.data.message.ChatColor;
import org.spacehq.mc.protocol.data.message.ChatFormat;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.packetlib.event.session.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SessionAdapter implements SessionListener
{

    private IngameBot bot;

    public SessionAdapter( IngameBot bot )
    {
        this.bot = bot;
    }

    @Override
    public void packetReceived( PacketReceivedEvent event )
    {
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
    public void disconnected( DisconnectedEvent event )
    {
        System.out.println( "Disconnected: " + Message.fromString( event.getReason() ).getFullText() );
        Main.reconnect();
    }

    @Override
    public void packetSent( PacketSentEvent packetSentEvent )
    {
        // TODO: onPacketSentEvent(PacketSentEvent packetSentEvent)
    }

    @Override
    public void connected( ConnectedEvent connectedEvent )
    {

    }

    @Override
    public void disconnecting( DisconnectingEvent disconnectingEvent )
    {
    }

    private void handleChat( Message mes )
    {
        if ( mes == null )
        {
            return;
        }
        String message = "";

        List<Message> subMessages = mes.getExtra();
        for ( Message m : subMessages )
        {
            if ( m == null || m.toJson() == null )
            {
                continue;
            }
            if ( m.getStyle().getColor() != null )
            {
                ChatColor colour = m.getStyle().getColor();
                message += AnsiColour.getColourCode( colour.toString() );
            }
            if ( m.getStyle().getFormats().size() > 0 )
            {
                for ( ChatFormat format : m.getStyle().getFormats() )
                {
                    message += AnsiColour.getColourCode( format.toString() );
                }
            }
            message += m.getText();
            message = message + "§r";
        }
        String time = new SimpleDateFormat( "[HH:mm:ss] " ).format( new Date() );
        message = StringEscapeUtils.unescapeJava( message );
        //TODO: onMessageReceivedEvent(MessageReceivedEvent event);
        Main.println( time + message, true );
    }
}
