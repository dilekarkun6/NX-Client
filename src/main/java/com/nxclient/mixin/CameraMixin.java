package com.nxclient.mixin;

import com.nxclient.modules.player.Freecam;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    private void onGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (Freecam.active) {
            cir.setReturnValue(CameraSubmersionType.NONE);
        }
    }
}
