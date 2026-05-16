package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Blink extends Module {

    public static boolean blinking = false;
    public static final List<Packet<?>> queuedPackets = new ArrayList<>();

    public Blink() {
        super("Blink", "Freezes outgoing position packets and releases them on disable.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        blinking = true;
        queuedPackets.clear();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§b[Blink] §fOn — your packets are now frozen."), true);
        }
    }

    @Override
    public void onDisable() {
        blinking = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null) {
            for (Packet<?> packet : queuedPackets) {
                client.getNetworkHandler().getConnection().send(packet);
            }
        }
        int count = queuedPackets.size();
        queuedPackets.clear();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§b[Blink] §fOff — released " + count + " packets."), true);
        }
    }
}
