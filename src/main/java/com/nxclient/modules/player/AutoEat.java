package com.nxclient.modules.player;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class AutoEat extends Module {

    public static boolean active = false;
    private static final int HUNGER_THRESHOLD = 16;

    public AutoEat() {
        super("AutoEat", "Automatically eats food when hunger is low.", Category.PLAYER);
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

        int foodLevel = client.player.getHungerManager().getFoodLevel();
        if (foodLevel >= HUNGER_THRESHOLD) return;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            FoodComponent food = stack.get(DataComponentTypes.FOOD);
            if (food == null) continue;

            client.player.getInventory().selectedSlot = i;

            if (!client.player.isUsingItem()) {
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
            }
            break;
        }
    }
}
