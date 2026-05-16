package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class Step extends Module {

    public static boolean active = false;
    private static final float STEP_HEIGHT = 1.0f;
    private static final float DEFAULT_STEP_HEIGHT = 0.6f;

    public Step() {
        super("Step", "Step up full blocks without jumping.", Category.MOVEMENT);
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
            client.player.setStepHeight(DEFAULT_STEP_HEIGHT);
        }
    }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;
        client.player.setStepHeight(STEP_HEIGHT);
    }
}
