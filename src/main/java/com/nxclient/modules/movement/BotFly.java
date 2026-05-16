package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class BotFly extends Module {

    public static boolean active = false;
    private static final double UP_SPEED = 1.0;
    private static final double HORIZONTAL_SPEED = 1.5;
    private static final double GENTLE_FALL = 0.04;

    public BotFly() {
        super("BotFly", "Ride a boat and fly with it. Jump = up, release = slow descent.", Category.MOVEMENT);
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

        Entity vehicle = client.player.getVehicle();
        if (vehicle == null) return;

        double vx = 0;
        double vy;
        double vz = 0;

        if (client.options.jumpKey.isPressed()) {
            vy = UP_SPEED;
        } else if (client.options.sneakKey.isPressed()) {
            vy = -UP_SPEED;
        } else {
            vy = -GENTLE_FALL;
        }

        float yaw = client.player.getYaw();
        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);

        if (client.options.forwardKey.isPressed()) { vx += forward.x * HORIZONTAL_SPEED; vz += forward.z * HORIZONTAL_SPEED; }
        if (client.options.backKey.isPressed())    { vx -= forward.x * HORIZONTAL_SPEED; vz -= forward.z * HORIZONTAL_SPEED; }
        if (client.options.rightKey.isPressed())   { vx += right.x * HORIZONTAL_SPEED;   vz += right.z * HORIZONTAL_SPEED; }
        if (client.options.leftKey.isPressed())    { vx -= right.x * HORIZONTAL_SPEED;   vz -= right.z * HORIZONTAL_SPEED; }

        vehicle.setVelocity(vx, vy, vz);
        vehicle.velocityModified = true;
        vehicle.fallDistance = 0;
    }
}
