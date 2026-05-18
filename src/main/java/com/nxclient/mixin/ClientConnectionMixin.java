package com.nxclient.mixin;

import com.nxclient.modules.combat.Criticals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {

    @Unique
    private static boolean nx_recursionGuard = false;

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    private void onSend(Packet<?> packet, CallbackInfo ci) {
        if (nx_recursionGuard) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity p = mc.player;

        if (Criticals.active && packet instanceof PlayerInteractEntityC2SPacket
                && p != null
                && p.isOnGround()
                && p.fallDistance == 0
                && !p.hasVehicle()
                && !p.isClimbing()
                && !p.isInsideWaterOrBubbleColumn()
                && !p.getAbilities().flying) {
            nx_recursionGuard = true;
            try {
                ClientConnection self = (ClientConnection) (Object) this;
                double x = p.getX();
                double y = p.getY();
                double z = p.getZ();
                self.send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0625, z, false, false));
                self.send(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false, false));
            } finally {
                nx_recursionGuard = false;
            }
        }
    }
}
