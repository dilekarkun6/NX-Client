package com.nxclient.mixin;

import com.nxclient.modules.combat.AntiKnockback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "applyMovementInput", at = @At("HEAD"))
    private void onApplyMovement(Vec3d movementInput, boolean sneaking, CallbackInfo ci) {
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo ci) {
        if (!AntiKnockback.active) return;
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        Vec3d vel = player.getVelocity();
        if (Math.abs(vel.x) > 0.3 || Math.abs(vel.z) > 0.3) {
            player.setVelocity(vel.multiply(0.1, 1.0, 0.1));
        }
    }
}
