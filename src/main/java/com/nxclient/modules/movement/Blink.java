package com.nxclient.modules.movement;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Blink extends Module {

    public static boolean active = false;
    public static boolean blinking = false;
    private int blinkTimer = 0;
    private static final int MAX_BLINK_TICKS = 40;

    public Blink() {
        super("Blink", "Freezes your position for a moment then teleports you.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
        active = true;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() {
        active = false;
        blinking = false;
        blinkTimer = 0;
    }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;

        if (!blinking) {
            blinking = true;
            blinkTimer = 0;
            client.player.sendMessage(Text.literal("§b[Blink] §fStarted"), true);
        } else {
            blinkTimer++;
            if (blinkTimer >= MAX_BLINK_TICKS) {
                blinking = false;
                blinkTimer = 0;
                client.player.sendMessage(Text.literal("§b[Blink] §fReleased"), true);
            }
        }
    }
}
