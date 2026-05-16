package com.nxclient.modules.player;

import com.nxclient.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module {

    public static boolean active = false;
    public static Vec3d savedPos;
    public static float savedYaw;
    public static float savedPitch;

    public Freecam() {
        super("Freecam", "Detaches the camera; server thinks you're standing still.", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        active = true;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            savedPos = client.player.getPos();
            savedYaw = client.player.getYaw();
            savedPitch = client.player.getPitch();
            client.player.getAbilities().allowFlying = true;
            client.player.getAbilities().flying = true;
            client.player.sendMessage(Text.literal("§b[Freecam] §fOn — server position frozen."), true);
        }
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            if (savedPos != null) {
                client.player.setPosition(savedPos);
                client.player.setYaw(savedYaw);
                client.player.setPitch(savedPitch);
            }
            client.player.getAbilities().flying = false;
            client.player.getAbilities().allowFlying = false;
            client.player.sendMessage(Text.literal("§b[Freecam] §fOff — restored position."), true);
        }
    }
}
