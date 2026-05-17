package com.nxclient.modules.misc;

import com.nxclient.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class AnarchyBypass extends Module {

    public static boolean active = false;

    public AnarchyBypass() {
        super("AnarchyBypass", "Makes other modules more anticheat-friendly (slower attacks, vanilla-like motion).", Category.MISC);
    }

    @Override
    public void onEnable() {
        active = true;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§6[AnarchyBypass] §fOn — modules now use slower, vanilla-like behavior."), true);
        }
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§6[AnarchyBypass] §fOff — modules back to full power."), true);
        }
    }
}
