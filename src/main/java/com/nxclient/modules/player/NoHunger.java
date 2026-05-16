package com.nxclient.modules.player;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class NoHunger extends Module {

    public static boolean active = false;

    public NoHunger() {
        super("NoHunger", "Keeps your food level full (client-side).", Category.PLAYER);
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
        client.player.getHungerManager().setFoodLevel(20);
        client.player.getHungerManager().setSaturationLevel(20.0f);
    }
}
