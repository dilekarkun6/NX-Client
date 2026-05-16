package com.nxclient.modules.render;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class FullBright extends Module {

    public static boolean active = false;
    private double previousGamma = 1.0;

    public FullBright() {
        super("FullBright", "Removes darkness and gives full visibility.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        active = true;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            previousGamma = client.options.getGamma().getValue();
            client.options.getGamma().setValue(100.0);
        }
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.options.getGamma().setValue(previousGamma);
        }
    }

    private void tick(MinecraftClient client) {
        if (!active || client == null) return;
        if (client.options.getGamma().getValue() < 100.0) {
            client.options.getGamma().setValue(100.0);
        }
    }
}
