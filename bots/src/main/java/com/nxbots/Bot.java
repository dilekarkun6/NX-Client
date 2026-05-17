package com.nxbots;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.protocol.MinecraftConstants;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.protocol.data.ProtocolState;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatPacket;

import java.net.InetSocketAddress;
import java.time.Instant;

public class Bot {

    private final String username;
    private final String host;
    private final int port;
    private final String authPass;
    private Session session;
    private volatile boolean loggedIn = false;

    public Bot(String username, String host, int port, String authPass) {
        this.username = username;
        this.host = host;
        this.port = port;
        this.authPass = authPass;
    }

    public void connect() {
        try {
            MinecraftProtocol protocol = new MinecraftProtocol(username);
            session = ClientNetworkSessionFactory
                    .factory()
                    .setRemoteSocketAddress(new InetSocketAddress(host, port))
                    .setProtocol(protocol)
                    .create();

            session.addListener(new SessionAdapter() {
                @Override
                public void packetReceived(Session session, org.geysermc.mcprotocollib.network.packet.Packet packet) {
                    if (packet instanceof ClientboundLoginPacket) {
                        loggedIn = true;
                        System.out.println("[" + username + "] joined the world.");
                        if (authPass != null) {
                            try { Thread.sleep(800); } catch (InterruptedException ignored) {}
                            sendChat("/register " + authPass + " " + authPass);
                            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
                            sendChat("/login " + authPass);
                        }
                    }
                }

                @Override
                public void disconnected(DisconnectedEvent event) {
                    loggedIn = false;
                    System.out.println("[" + username + "] disconnected: " + event.getReason());
                }
            });

            System.out.println("[" + username + "] connecting to " + host + ":" + port + " ...");
            session.connect();
        } catch (Throwable t) {
            System.err.println("[" + username + "] failed to connect: " + t.getMessage());
        }
    }

    public void sendChat(String message) {
        if (session == null || session.getPacketProtocol().getState() != ProtocolState.GAME) return;
        if (message.startsWith("/")) {
            session.send(new org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket(message.substring(1)));
        } else {
            session.send(new ServerboundChatPacket(message, Instant.now().toEpochMilli(), 0L, null, 0, new java.util.BitSet(), 0));
        }
    }

    public void disconnect() {
        if (session != null) session.disconnect("NXBots shutdown");
    }

    public String getUsername() { return username; }
    public boolean isLoggedIn() { return loggedIn; }
}
