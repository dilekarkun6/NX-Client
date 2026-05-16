package com.nxclient.modules.player;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {

    public static boolean active = false;
    private static final int OFFHAND_SLOT = 45;
    private int delay = 0;

    public AutoTotem() {
        super("AutoTotem", "Auto-swaps a Totem of Undying into your offhand.", Category.PLAYER);
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
        if (client.currentScreen != null) return;
        if (delay > 0) { delay--; return; }

        ItemStack offhand = client.player.getOffHandStack();
        if (offhand.isOf(Items.TOTEM_OF_UNDYING)) return;

        int totemSlot = -1;
        for (int i = 0; i < client.player.getInventory().size(); i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                totemSlot = i;
                break;
            }
        }
        if (totemSlot == -1) return;

        int containerSlot;
        if (totemSlot < 9) {
            containerSlot = totemSlot + 36;
        } else {
            containerSlot = totemSlot;
        }

        client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId, containerSlot, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId, OFFHAND_SLOT, 0, SlotActionType.PICKUP, client.player);
        if (!client.player.currentScreenHandler.getCursorStack().isEmpty()) {
            client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId, containerSlot, 0, SlotActionType.PICKUP, client.player);
        }
        delay = 5;
    }
}
