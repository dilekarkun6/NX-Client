package com.nxclient.modules.player;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;

public class AutoEat extends Module {

    public static boolean active = false;
    private static final int HUNGER_THRESHOLD = 17;
    private static final int FULL_HUNGER = 20;

    private int originalSlot = -1;
    private boolean eating = false;

    public AutoEat() {
        super("AutoEat", "Automatically eats food when hunger is low and stops when full.", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        active = true;
        eating = false;
        originalSlot = -1;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() {
        active = false;
        stopEating(MinecraftClient.getInstance());
    }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;

        int foodLevel = client.player.getHungerManager().getFoodLevel();

        if (eating) {
            if (foodLevel >= FULL_HUNGER) {
                stopEating(client);
                return;
            }
            ItemStack stack = client.player.getMainHandStack();
            if (stack.isEmpty() || stack.get(DataComponentTypes.FOOD) == null) {
                stopEating(client);
                return;
            }
            client.options.useKey.setPressed(true);
            return;
        }

        if (foodLevel < HUNGER_THRESHOLD) {
            int foodSlot = findFoodSlot(client);
            if (foodSlot == -1) return;

            originalSlot = client.player.getInventory().selectedSlot;
            client.player.getInventory().selectedSlot = foodSlot;
            client.options.useKey.setPressed(true);
            eating = true;
        }
    }

    private void stopEating(MinecraftClient client) {
        if (client != null && client.options != null) {
            client.options.useKey.setPressed(false);
        }
        if (client != null && client.player != null && originalSlot != -1) {
            client.player.getInventory().selectedSlot = originalSlot;
        }
        originalSlot = -1;
        eating = false;
    }

    private int findFoodSlot(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            FoodComponent food = stack.get(DataComponentTypes.FOOD);
            if (food != null) return i;
        }
        return -1;
    }
}
