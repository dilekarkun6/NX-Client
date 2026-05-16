package com.nxclient.mixin;

import com.nxclient.modules.movement.Blink;
import com.nxclient.modules.player.Freecam;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void onSend(Packet<?> packet, CallbackInfo ci) {
        if (Blink.blinking && packet instanceof PlayerMoveC2SPacket) {
            Blink.queuedPackets.add(packet);
            ci.cancel();
            return;
        }
        if (Freecam.active && packet instanceof PlayerMoveC2SPacket) {
            ci.cancel();
        }
    }
}
