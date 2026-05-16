package com.nxclient.mixin;

import com.nxclient.modules.movement.NoClip;
import com.nxclient.modules.render.ESP;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (ESP.active) {
            Entity self = (Entity) (Object) this;
            if (self instanceof LivingEntity) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    private void onAdjustMovementForCollisions(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
        if (!NoClip.active) return;
        Entity self = (Entity) (Object) this;
        if (self instanceof ClientPlayerEntity) {
            cir.setReturnValue(movement);
        }
    }
}
