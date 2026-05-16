package com.nxclient.mixin;

import com.nxclient.modules.movement.NoFall;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void onFallDamage(float fallDistance, float damageMultiplier,
                              net.minecraft.entity.damage.DamageSource source,
                              CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        MinecraftClientCheck check = new MinecraftClientCheck();
        if (NoFall.active && self instanceof net.minecraft.client.network.ClientPlayerEntity) {
            cir.setReturnValue(false);
        }
    }

    private static class MinecraftClientCheck {}
}
