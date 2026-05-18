package com.nxclient.mixin;

import com.nxclient.modules.movement.NoFall;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void onFallDamage(float fallDistance, float damageMultiplier, DamageSource source,
                              CallbackInfoReturnable<Boolean> cir) {
        if (!NoFall.active) return;
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof PlayerEntity) {
            cir.setReturnValue(false);
        }
    }
}
