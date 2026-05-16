package com.nxclient.modules.misc;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Scaffold extends Module {

    public static boolean active = false;
    private int previousSlot = -1;

    public Scaffold() {
        super("Scaffold", "Auto-places blocks under your feet while walking.", Category.MISC);
    }

    @Override
    public void onEnable() {
        active = true;
        previousSlot = -1;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && previousSlot != -1) {
            client.player.getInventory().selectedSlot = previousSlot;
            previousSlot = -1;
        }
    }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null || client.world == null) return;
        if (client.player.isOnGround()) return;
        if (client.player.getAbilities().flying) return;

        BlockPos below = client.player.getBlockPos().down();
        if (!client.world.getBlockState(below).isAir()) return;

        int slot = findBlockSlot(client);
        if (slot == -1) return;

        if (previousSlot == -1) previousSlot = client.player.getInventory().selectedSlot;
        client.player.getInventory().selectedSlot = slot;

        BlockPos placeAgainst = below.down();
        if (client.world.getBlockState(placeAgainst).isAir()) return;

        Vec3d hitVec = Vec3d.of(below).add(0.5, 0.0, 0.5);
        BlockHitResult hit = new BlockHitResult(hitVec, Direction.UP, placeAgainst, false);

        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        client.player.swingHand(Hand.MAIN_HAND);
    }

    private int findBlockSlot(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) return i;
        }
        return -1;
    }
}
