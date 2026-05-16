package com.nxclient.modules.player;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module {

    public static boolean active = false;
    public static Vec3d savedPos;
    public static float savedYaw;
    public static float savedPitch;
    private boolean previousAllowFlying = false;
    private boolean previousFlying = false;

    public Freecam() {
        super("Freecam", "Detached camera. Pass through blocks freely; returns to start on disable.", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        active = true;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        savedPos = client.player.getPos();
        savedYaw = client.player.getYaw();
        savedPitch = client.player.getPitch();
        previousAllowFlying = client.player.getAbilities().allowFlying;
        previousFlying = client.player.getAbilities().flying;

        client.player.getAbilities().allowFlying = true;
        client.player.getAbilities().flying = true;
        client.player.noClip = true;

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        client.player.sendMessage(Text.literal("§b[Freecam] §fOn — pass through anything"), true);
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        client.player.noClip = false;
        client.player.getAbilities().flying = previousFlying;
        client.player.getAbilities().allowFlying = previousAllowFlying;

        if (savedPos != null) {
            client.player.refreshPositionAndAngles(savedPos.x, savedPos.y, savedPos.z, savedYaw, savedPitch);
            client.player.setVelocity(Vec3d.ZERO);
            client.player.fallDistance = 0;
        }
        client.player.sendMessage(Text.literal("§b[Freecam] §fOff — returned to start"), true);
        savedPos = null;
    }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;
        client.player.noClip = true;
        client.player.fallDistance = 0;
        client.player.getAbilities().flying = true;
    }
}
