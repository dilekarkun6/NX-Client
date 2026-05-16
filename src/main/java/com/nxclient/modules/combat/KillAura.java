package com.nxclient.modules.combat;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {

    private static final double RANGE = 4.5;
    private int cooldown = 0;

    public KillAura() {
        super("KillAura", "Auto attacks nearby living entities.", Category.COMBAT);
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
                e -> e != client.player && e.isAlive() && !(e instanceof PlayerEntity && e == client.player)
        );

        if (targets.isEmpty()) return;

        targets.sort(Comparator.comparingDouble(e -> client.player.distanceTo(e)));

        LivingEntity target = targets.get(0);

        if (client.player.distanceTo(target) > RANGE) return;

        client.player.lookAt(
                net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES,
                target.getPos().add(0, target.getHeight() / 2.0, 0)
        );

        client.getNetworkHandler().sendPacket(
                PlayerInteractEntityC2SPacket.attack(target, client.player.isSneaking())
        );

        client.player.swingHand(Hand.MAIN_HAND);
        cooldown = 10;
    }
}
