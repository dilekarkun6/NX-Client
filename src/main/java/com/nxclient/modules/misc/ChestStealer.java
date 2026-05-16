package com.nxclient.modules.misc;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.HashSet;
import java.util.Set;

public class ChestStealer extends Module {

    public static boolean active = false;
    public static final Set<String> ignoredKeywords = new HashSet<>();
    static {
        ignoredKeywords.add("apple");
        ignoredKeywords.add("elma");
    }

    private int delay = 0;

    public ChestStealer() {
        super("ChestStealer", "Auto-pulls all chest items except those matching the ignore list (default: apple/elma).", Category.MISC);
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
        if (delay > 0) { delay--; return; }

        int chestSize = -1;
        ScreenHandler handler = null;

        if (client.currentScreen instanceof GenericContainerScreen gcs) {
            GenericContainerScreenHandler gch = gcs.getScreenHandler();
            chestSize = gch.getRows() * 9;
            handler = gch;
        } else if (client.currentScreen instanceof ShulkerBoxScreen sbs) {
            ShulkerBoxScreenHandler sbh = sbs.getScreenHandler();
            chestSize = 27;
            handler = sbh;
        }
        if (handler == null || chestSize == -1) return;

        for (int i = 0; i < chestSize; i++) {
            Slot slot = handler.slots.get(i);
            if (!slot.hasStack()) continue;
            ItemStack stack = slot.getStack();
            if (shouldIgnore(stack)) continue;
            client.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.QUICK_MOVE, client.player);
            delay = 1;
            return;
        }
    }

    private boolean shouldIgnore(ItemStack stack) {
        if (stack.isEmpty()) return true;
        String displayName = stack.getName().getString().toLowerCase();
        String regName = stack.getItem().toString().toLowerCase();
        for (String keyword : ignoredKeywords) {
            String k = keyword.toLowerCase();
            if (displayName.contains(k) || regName.contains(k)) return true;
        }
        return false;
    }
}
