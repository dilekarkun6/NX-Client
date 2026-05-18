package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Module {

    public static boolean active = false;

    public NoFall() {
        super("NoFall", "Canonical GrimNoFall: send a near-zero upward Full packet then onLanding() — exploits MC-171969 to keep server fallDistance at 0.", Category.MOVEMENT);
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
        ClientPlayerEntity p = client.player;
        if (p.getAbilities().flying) return;
        if (p.isGliding()) return;
        if (p.isOnGround()) return;
        if (p.fallDistance <= 1.0f) return;

        p.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(
                p.getX(),
                p.getY() + 0.000000001,
                p.getZ(),
                p.getYaw(),
                p.getPitch(),
                false,
                false
        ));
        p.onLanding();
    }
}
