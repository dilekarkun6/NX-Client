package com.nxclient.modules.render;

import com.nxclient.mixin.SimpleOptionAccessor;
import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

public class FullBright extends Module {

    public static boolean active = false;
    private double previousGamma = 1.0;

    public FullBright() {
        super("FullBright", "Removes darkness — full visibility everywhere.", Category.RENDER);
    }

    @Override
    public void onEnable() {
        active = true;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        SimpleOption<Double> gammaOption = client.options.getGamma();
        previousGamma = gammaOption.getValue();
        ((SimpleOptionAccessor) (Object) gammaOption).setForcedValue(16.0);
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        SimpleOption<Double> gammaOption = client.options.getGamma();
        ((SimpleOptionAccessor) (Object) gammaOption).setForcedValue(previousGamma);
    }

    private void tick(MinecraftClient client) {
        if (!active || client == null) return;
        SimpleOption<Double> gammaOption = client.options.getGamma();
        if (gammaOption.getValue() < 16.0) {
            ((SimpleOptionAccessor) (Object) gammaOption).setForcedValue(16.0);
        }
    }
}
