package com.nxclient.modules.player;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
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
            int bestSlot = findBestArmorSlot(client, slot, getMaterialValue(equipped));
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

                client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId, containerSlot, 0, SlotActionType.PICKUP, client.player);
                client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId, armorSlotIndex, 0, SlotActionType.PICKUP, client.player);
                if (!client.player.currentScreenHandler.getCursorStack().isEmpty()) {
                    client.interactionManager.clickSlot(client.player.playerScreenHandler.syncId, containerSlot, 0, SlotActionType.PICKUP, client.player);
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
            if (!(stack.getItem() instanceof ArmorItem armor)) continue;
            if (!matchesSlot(armor, targetSlot)) continue;
            int value = getMaterialValue(stack);
            if (value > bestValue) {
                bestValue = value;
                bestSlot = i;
            }
        }
        return bestSlot;
    }

    private boolean matchesSlot(ArmorItem armor, EquipmentSlot slot) {
        return armor.getSlotType() == slot;
    }

    private int getMaterialValue(ItemStack stack) {
        if (stack.isEmpty()) return -1;
        if (!(stack.getItem() instanceof ArmorItem armor)) return -1;
        return armor.getProtection();
    }
}
