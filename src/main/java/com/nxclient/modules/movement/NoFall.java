package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public static boolean active = false;
    private static final float TRIGGER = 1.0f;

    public NoFall() {
        super("NoFall", "Multi-layered fall protection: packet onGround spoof + OnGroundOnly resync + client fallDistance reset.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        active = true;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() { active = false; }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;
        if (client.player.getAbilities().flying) return;
        if (client.player.isGliding()) return;
        if (client.player.fallDistance <= TRIGGER) return;

        ClientPlayNetworkHandler nh = client.player.networkHandler;
        if (nh == null) return;

        nh.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, false));

        client.player.fallDistance = 0;
    }
}
