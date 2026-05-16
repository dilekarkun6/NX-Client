package com.nxclient.modules.player;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class AutoArmor extends Module {

    public static boolean active = false;
    private int delay = 0;

    public AutoArmor() {
        super("AutoArmor", "Auto-equips the best armor piece from your inventory.", Category.PLAYER);
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

        for (EquipmentSlot slot : new EquipmentSlot[]{ EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET }) {
            ItemStack equipped = client.player.getEquippedStack(slot);
            int currentValue = getMaterialValue(equipped);
            int bestSlot = findBestArmorSlot(client, slot, currentValue);
            if (bestSlot != -1) {
                int containerSlot;
                if (bestSlot < 9) containerSlot = bestSlot + 36;
                else containerSlot = bestSlot;

                int armorSlotIndex = switch (slot) {
                    case HEAD -> 5;
                    case CHEST -> 6;
                    case LEGS -> 7;
                    case FEET -> 8;
                    default -> -1;
                };
                if (armorSlotIndex == -1) continue;

                int syncId = client.player.playerScreenHandler.syncId;
                client.interactionManager.clickSlot(syncId, containerSlot, 0, SlotActionType.PICKUP, client.player);
                client.interactionManager.clickSlot(syncId, armorSlotIndex, 0, SlotActionType.PICKUP, client.player);
                if (!client.player.currentScreenHandler.getCursorStack().isEmpty()) {
                    client.interactionManager.clickSlot(syncId, containerSlot, 0, SlotActionType.PICKUP, client.player);
                }
                delay = 5;
                return;
            }
        }
    }

    private int findBestArmorSlot(MinecraftClient client, EquipmentSlot targetSlot, int currentValue) {
        int bestSlot = -1;
        int bestValue = currentValue;
        for (int i = 0; i < client.player.getInventory().main.size(); i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            if (!matchesSlot(stack, targetSlot)) continue;
            int value = getMaterialValue(stack);
            if (value > bestValue) {
                bestValue = value;
                bestSlot = i;
            }
        }
        return bestSlot;
    }

    private boolean matchesSlot(ItemStack stack, EquipmentSlot slot) {
        EquippableComponent equippable = stack.get(DataComponentTypes.EQUIPPABLE);
        return equippable != null && equippable.slot() == slot;
    }

    private int getMaterialValue(ItemStack stack) {
        if (stack.isEmpty()) return -1;
        String id = stack.getItem().toString().toLowerCase();
        if (id.contains("netherite")) return 5;
        if (id.contains("diamond")) return 4;
        if (id.contains("turtle")) return 3;
        if (id.contains("iron")) return 3;
        if (id.contains("chainmail")) return 2;
        if (id.contains("gold")) return 1;
        if (id.contains("leather")) return 0;
        return -1;
    }
}
