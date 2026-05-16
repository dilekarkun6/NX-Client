package com.nxclient.modules.misc;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;

public class AutoFish extends Module {

    public static boolean active = false;
    private int checkCooldown = 0;
    private int recastDelay = 0;

    public AutoFish() {
        super("AutoFish", "Automatically casts and recasts the fishing rod.", Category.MISC);
    }

    @Override
    public void onEnable() {
        active = true;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() { active = false; }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null || client.interactionManager == null) return;

        if (recastDelay > 0) {
            recastDelay--;
            return;
        }

        FishingBobberEntity bobber = client.player.fishHook;

        if (bobber == null) {
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            checkCooldown = 0;
            return;
        }

        checkCooldown++;
        if (checkCooldown < 5) return;

        if (bobber.getVelocity().length() < 0.01 && bobber.isInsideWaterOrBubbleColumn()) {
            client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            recastDelay = 10;
            checkCooldown = 0;
        }
    }
}
