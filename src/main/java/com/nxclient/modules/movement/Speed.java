package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {

    public static boolean active = false;
    private static final double SPEED_MULT = 1.8;

    public Speed() {
        super("Speed", "Move faster on the ground.", Category.MOVEMENT);
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

        ClientPlayerEntity player = client.player;
        if (!player.isOnGround()) return;

        Vec3d vel = player.getVelocity();
        double hSpeed = Math.sqrt(vel.x * vel.x + vel.z * vel.z);

        if (hSpeed > 0.01) {
            double factor = SPEED_MULT / hSpeed;
            player.setVelocity(vel.x * factor, vel.y, vel.z * factor);
        }
    }
}
