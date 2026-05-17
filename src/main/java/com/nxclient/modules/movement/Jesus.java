package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Jesus extends Module {

    public static boolean active = false;

    public Jesus() {
        super("Jesus", "Walk on water. Hold sneak to dive.", Category.MOVEMENT);
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
        if (!p.isTouchingWater()) return;
        if (client.options.sneakKey.isPressed()) return;
        if (p.getAbilities().flying) return;

        Vec3d vel = p.getVelocity();
        if (vel.y < 0) {
            p.setVelocity(vel.x, 0.08, vel.z);
        } else {
            p.setVelocity(vel.x, Math.max(vel.y, 0.08), vel.z);
        }
        p.fallDistance = 0;
    }
}
