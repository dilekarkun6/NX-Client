package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Fly extends Module {

    public static boolean active = false;
    private static final double SPEED = 0.6;

    public Fly() {
        super("Fly", "Lets you fly freely in survival mode.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        active = true;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.getAbilities().flying = false;
        }
    }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;

        ClientPlayerEntity player = client.player;
        player.getAbilities().flying = false;
        player.getAbilities().allowFlying = false;

        double motionX = 0;
        double motionY = 0;
        double motionZ = 0;

        Vec3d forward = Vec3d.fromPolar(0, player.getYaw());
        Vec3d right = Vec3d.fromPolar(0, player.getYaw() + 90);

        if (client.options.forwardKey.isPressed()) {
            motionX += forward.x * SPEED;
            motionZ += forward.z * SPEED;
        }
        if (client.options.backKey.isPressed()) {
            motionX -= forward.x * SPEED;
            motionZ -= forward.z * SPEED;
        }
        if (client.options.rightKey.isPressed()) {
            motionX += right.x * SPEED;
            motionZ += right.z * SPEED;
        }
        if (client.options.leftKey.isPressed()) {
            motionX -= right.x * SPEED;
            motionZ -= right.z * SPEED;
        }
        if (client.options.jumpKey.isPressed()) {
            motionY = SPEED;
        } else if (client.options.sneakKey.isPressed()) {
            motionY = -SPEED;
        } else {
            motionY = -0.0008;
        }

        player.setVelocity(motionX, motionY, motionZ);
        player.fallDistance = 0;
    }
}
