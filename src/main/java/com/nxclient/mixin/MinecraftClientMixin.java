package com.nxclient.mixin;

import com.nxclient.modules.combat.Criticals;
import com.nxclient.modules.misc.FastPlace;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow public int itemUseCooldown;
    @Shadow public ClientPlayerEntity player;
    @Shadow public HitResult crosshairTarget;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (FastPlace.active) {
            itemUseCooldown = 0;
        }
    }

    @Inject(method = "doAttack", at = @At("HEAD"))
    private void onDoAttackHead(CallbackInfoReturnable<Boolean> cir) {
        if (!Criticals.active) return;
        if (player == null) return;
        if (crosshairTarget == null || !(crosshairTarget instanceof EntityHitResult)) return;
        if (!player.isOnGround()) return;
        if (player.fallDistance > 0.0f) return;
        if (player.isInsideWaterOrBubbleColumn()) return;
        if (player.isClimbing()) return;
        if (player.hasVehicle()) return;
        if (player.getAbilities().flying) return;

        ClientPlayNetworkHandler nh = player.networkHandler;
        if (nh == null) return;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.1, z, false, false));
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.05, z, false, false));
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.01, z, false, false));
    }
}
