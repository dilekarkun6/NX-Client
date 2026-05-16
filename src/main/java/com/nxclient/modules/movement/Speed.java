package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import com.nxclient.modules.settings.DoubleSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module {

    public static boolean active = false;
    public final DoubleSetting speed = new DoubleSetting("Speed", 0.35, 0.2, 1.0, 0.05);

    public Speed() {
        super("Speed", "Sprint-like ground speed (no momentum).", Category.MOVEMENT);
        settings.add(speed);
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

        double s = speed.value;
        Vec3d forwardVec = Vec3d.fromPolar(0, player.getYaw());
        Vec3d rightVec = Vec3d.fromPolar(0, player.getYaw() + 90);

        double vx = 0;
        double vz = 0;
        if (forward) { vx += forwardVec.x * s; vz += forwardVec.z * s; }
        if (back)    { vx -= forwardVec.x * s; vz -= forwardVec.z * s; }
        if (right)   { vx += rightVec.x * s;   vz += rightVec.z * s; }
        if (left)    { vx -= rightVec.x * s;   vz -= rightVec.z * s; }

        Vec3d vel = player.getVelocity();
        player.setVelocity(vx, vel.y, vz);
    }
}
