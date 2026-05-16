package com.nxclient.mixin;

import com.nxclient.modules.combat.Criticals;
import com.nxclient.modules.misc.FastBreak;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow private int blockBreakingCooldown;

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntityHead(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (!Criticals.active) return;
        if (!(player instanceof ClientPlayerEntity)) return;
        if (!player.isOnGround()) return;
        if (player.isInsideWaterOrBubbleColumn()) return;
        if (player.hasVehicle()) return;
        if (player.isClimbing()) return;
        if (player.getAbilities().flying) return;

        ClientPlayNetworkHandler nh = MinecraftClient.getInstance().getNetworkHandler();
        if (nh == null) return;

        Vec3d pos = player.getPos();
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y + 0.1, pos.z, false, false));
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y + 0.0625, pos.z, false, false));
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, false, false));
    }

    @Inject(method = "attackEntity", at = @At("TAIL"))
    private void onAttackEntityTail(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (!Criticals.active) return;
        if (!(player instanceof ClientPlayerEntity)) return;

        ClientPlayNetworkHandler nh = MinecraftClient.getInstance().getNetworkHandler();
        if (nh == null) return;

        Vec3d pos = player.getPos();
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, true, false));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (FastBreak.active) {
            blockBreakingCooldown = 0;
        }
    }
}
