package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import com.nxclient.modules.settings.DoubleSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class BotFly extends Module {

    public static boolean active = false;
    public final DoubleSetting upSpeed = new DoubleSetting("UpSpeed", 1.0, 0.3, 3.0, 0.1);
    public final DoubleSetting horizontalSpeed = new DoubleSetting("HSpeed", 1.5, 0.3, 4.0, 0.1);
    public final DoubleSetting fallSpeed = new DoubleSetting("FallSpeed", 0.04, 0.0, 0.5, 0.01);

    public BotFly() {
        super("BotFly", "Ride a boat and fly with it. Jump = up, release = slow descent.", Category.MOVEMENT);
        settings.add(upSpeed);
        settings.add(horizontalSpeed);
        settings.add(fallSpeed);
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

        double up = upSpeed.value;
        double h = horizontalSpeed.value;

        double vx = 0;
        double vy;
        double vz = 0;

        if (client.options.jumpKey.isPressed()) {
            vy = up;
        } else if (client.options.sneakKey.isPressed()) {
            vy = -up;
        } else {
            vy = -fallSpeed.value;
        }

        float yaw = client.player.getYaw();
        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);

        if (client.options.forwardKey.isPressed()) { vx += forward.x * h; vz += forward.z * h; }
        if (client.options.backKey.isPressed())    { vx -= forward.x * h; vz -= forward.z * h; }
        if (client.options.rightKey.isPressed())   { vx += right.x * h;   vz += right.z * h; }
        if (client.options.leftKey.isPressed())    { vx -= right.x * h;   vz -= right.z * h; }

        vehicle.setVelocity(vx, vy, vz);
        vehicle.velocityModified = true;
        vehicle.fallDistance = 0;
    }
}
