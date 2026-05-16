package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class BotFly extends Module {

    public static boolean active = false;
    private static final double SPEED = 0.18;
    private int oscillationTick = 0;

    public BotFly() {
        super("BotFly", "Slow, bot-like fly with vertical oscillation (harder to detect).", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        active = true;
        oscillationTick = 0;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() { active = false; }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;

        ClientPlayerEntity player = client.player;

        double motionX = 0;
        double motionY;
        double motionZ = 0;

        Vec3d forward = Vec3d.fromPolar(0, player.getYaw());
        Vec3d right = Vec3d.fromPolar(0, player.getYaw() + 90);

        if (client.options.forwardKey.isPressed()) { motionX += forward.x * SPEED; motionZ += forward.z * SPEED; }
        if (client.options.backKey.isPressed())    { motionX -= forward.x * SPEED; motionZ -= forward.z * SPEED; }
        if (client.options.rightKey.isPressed())   { motionX += right.x * SPEED;   motionZ += right.z * SPEED; }
        if (client.options.leftKey.isPressed())    { motionX -= right.x * SPEED;   motionZ -= right.z * SPEED; }

        if (client.options.jumpKey.isPressed()) {
            motionY = SPEED;
        } else if (client.options.sneakKey.isPressed()) {
            motionY = -SPEED;
        } else {
            oscillationTick = (oscillationTick + 1) % 20;
            motionY = (oscillationTick < 10) ? 0.02 : -0.02;
        }

        player.setVelocity(motionX, motionY, motionZ);
        player.fallDistance = 0;
    }
}
