package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class NoClip extends Module {

    public static boolean active = false;
    private Vec3d savedPos = null;
    private boolean previousFlying = false;
    private boolean previousAllowFlying = false;

    public NoClip() {
        super("NoClip", "Walk through blocks. Returns you to start position on disable.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        active = true;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null) {
            savedPos = player.getPos();
            previousFlying = player.getAbilities().flying;
            previousAllowFlying = player.getAbilities().allowFlying;
            player.getAbilities().allowFlying = true;
            player.getAbilities().flying = true;
            player.sendMessage(Text.literal("§b[NoClip] §fOn — walk through anything"), true);
        }
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null) {
            if (savedPos != null) {
                player.refreshPositionAndAngles(savedPos.x, savedPos.y, savedPos.z, player.getYaw(), player.getPitch());
                player.setVelocity(Vec3d.ZERO);
                player.fallDistance = 0;
            }
            player.getAbilities().flying = previousFlying;
            player.getAbilities().allowFlying = previousAllowFlying;
            player.sendMessage(Text.literal("§b[NoClip] §fOff — returned to start"), true);
        }
        savedPos = null;
    }
}
