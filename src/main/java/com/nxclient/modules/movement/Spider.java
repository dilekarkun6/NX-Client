package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Spider extends Module {

    public static boolean active = false;

    public Spider() {
        super("Spider", "Climb walls by pressing into them.", Category.MOVEMENT);
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

        if (player.horizontalCollision) {
            Vec3d vel = player.getVelocity();
            player.setVelocity(vel.x, 0.2, vel.z);
            player.fallDistance = 0;
        }
    }
}
