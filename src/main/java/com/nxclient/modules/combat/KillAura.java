package com.nxclient.modules.combat;

import com.nxclient.modules.Module;
import com.nxclient.modules.misc.AnarchyBypass;
import com.nxclient.modules.settings.DoubleSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {

    public final DoubleSetting range = new DoubleSetting("Range", 4.5, 3.0, 6.0, 0.1);
    public final DoubleSetting cooldownTicks = new DoubleSetting("Cooldown", 10.0, 1.0, 20.0, 1.0);
    private int cooldown = 0;

    public KillAura() {
        super("KillAura", "Attacks ALL nearby living entities at once (single target in AnarchyBypass mode).", Category.COMBAT);
        settings.add(range);
        settings.add(cooldownTicks);
    }

    @Override
    public void onEnable() {
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void tick(MinecraftClient client) {
        if (!isEnabled() || client.player == null || client.world == null) return;

        if (cooldown > 0) { cooldown--; return; }

        double r = range.value;
        List<LivingEntity> targets = client.world.getEntitiesByClass(
                LivingEntity.class,
                client.player.getBoundingBox().expand(r),
                e -> e != client.player
                        && e.isAlive()
                        && !(e instanceof PlayerEntity && ((PlayerEntity) e).isCreative())
                        && client.player.distanceTo(e) <= r
        );

        if (targets.isEmpty()) return;

        if (AnarchyBypass.active) {
            targets.sort(Comparator.comparingDouble(client.player::distanceTo));
            LivingEntity target = targets.get(0);
            client.player.lookAt(
                    net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor.EYES,
                    target.getPos().add(0, target.getHeight() / 2.0, 0)
            );
            client.getNetworkHandler().sendPacket(
                    PlayerInteractEntityC2SPacket.attack(target, client.player.isSneaking())
            );
            client.player.swingHand(Hand.MAIN_HAND);
            cooldown = Math.max(cooldownTicks.value.intValue(), 12);
            return;
        }

        for (LivingEntity target : targets) {
            client.getNetworkHandler().sendPacket(
                    PlayerInteractEntityC2SPacket.attack(target, client.player.isSneaking())
            );
        }
        client.player.swingHand(Hand.MAIN_HAND);
        cooldown = cooldownTicks.value.intValue();
    }
}
