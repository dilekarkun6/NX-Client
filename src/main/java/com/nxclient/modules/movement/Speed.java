package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {

    public static boolean active = false;
    private static final double SPEED = 0.35;

    public Speed() {
        super("Speed", "Sprint-like ground speed boost (no momentum drift).", Category.MOVEMENT);
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

        boolean forward = client.options.forwardKey.isPressed();
        boolean back = client.options.backKey.isPressed();
        boolean left = client.options.leftKey.isPressed();
        boolean right = client.options.rightKey.isPressed();

        if (!forward && !back && !left && !right) return;

        Vec3d forwardVec = Vec3d.fromPolar(0, player.getYaw());
        Vec3d rightVec = Vec3d.fromPolar(0, player.getYaw() + 90);

        double vx = 0;
        double vz = 0;

        if (forward) { vx += forwardVec.x * SPEED; vz += forwardVec.z * SPEED; }
        if (back)    { vx -= forwardVec.x * SPEED; vz -= forwardVec.z * SPEED; }
        if (right)   { vx += rightVec.x * SPEED;   vz += rightVec.z * SPEED; }
        if (left)    { vx -= rightVec.x * SPEED;   vz -= rightVec.z * SPEED; }

        Vec3d vel = player.getVelocity();
        player.setVelocity(vx, vel.y, vz);
    }
}
