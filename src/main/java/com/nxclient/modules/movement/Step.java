package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;

public class Step extends Module {

    public static boolean active = false;
    private static final double STEP_HEIGHT = 1.0;
    private static final double DEFAULT_STEP_HEIGHT = 0.6;

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
            EntityAttributeInstance attr = client.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT);
            if (attr != null) attr.setBaseValue(DEFAULT_STEP_HEIGHT);
        }
    }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;
        EntityAttributeInstance attr = client.player.getAttributeInstance(EntityAttributes.STEP_HEIGHT);
        if (attr != null) attr.setBaseValue(STEP_HEIGHT);
    }
}
