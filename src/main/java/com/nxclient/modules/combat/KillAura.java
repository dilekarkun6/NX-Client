package com.nxclient.modules.combat;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

import java.util.List;

public class KillAura extends Module {

    private static final double RANGE = 4.5;
    private int cooldown = 0;
    private static final int COOLDOWN_TICKS = 10;

    public KillAura() {
        super("KillAura", "Attacks ALL nearby living entities at once.", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void tick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;

        if (cooldown > 0) {
            cooldown--;
            return;
        }

        List<LivingEntity> targets = client.world.getEntitiesByClass(
                LivingEntity.class,
                client.player.getBoundingBox().expand(RANGE),
                e -> e != client.player
                        && e.isAlive()
                        && !(e instanceof PlayerEntity && ((PlayerEntity) e).isCreative())
                        && client.player.distanceTo(e) <= RANGE
        );

        if (targets.isEmpty()) return;

        for (LivingEntity target : targets) {
            client.getNetworkHandler().sendPacket(
                    PlayerInteractEntityC2SPacket.attack(target, client.player.isSneaking())
            );
        }
        client.player.swingHand(Hand.MAIN_HAND);
        cooldown = COOLDOWN_TICKS;
    }
}
