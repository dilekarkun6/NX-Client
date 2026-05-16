package com.nxclient.modules.misc;

import com.nxclient.modules.Module;
import com.nxclient.modules.settings.DoubleSetting;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class Reach extends Module {

    public static boolean active = false;
    public final DoubleSetting maxReach = new DoubleSetting("MaxReach", 60.0, 6.0, 200.0, 5.0);
    private boolean wasAttackDown = false;

    public Reach() {
        super("Reach", "Attacks entities from extreme distances via 2-packet teleport.", Category.MISC);
        settings.add(maxReach);
    }

    @Override
    public void onEnable() {
        active = true;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() { active = false; }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null || client.world == null) return;

        long window = client.getWindow().getHandle();
        boolean isAttackDown = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean justPressed = isAttackDown && !wasAttackDown;
        wasAttackDown = isAttackDown;
        if (!justPressed) return;

        Entity target = getTarget(client);
        if (target == null) return;

        Vec3d origin = client.player.getPos();
        Vec3d targetPos = target.getPos();
        double distance = origin.distanceTo(targetPos);

        boolean isOnGround = client.player.isOnGround();

        client.getNetworkHandler().sendPacket(
                new PlayerMoveC2SPacket.PositionAndOnGround(targetPos.x, targetPos.y, targetPos.z, isOnGround, false)
        );
        client.getNetworkHandler().sendPacket(
                PlayerInteractEntityC2SPacket.attack(target, client.player.isSneaking())
        );
        client.getNetworkHandler().sendPacket(
                new PlayerMoveC2SPacket.PositionAndOnGround(origin.x, origin.y, origin.z, isOnGround, false)
        );

        client.player.swingHand(Hand.MAIN_HAND);
        client.player.playSound(net.minecraft.sound.SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1f, 1f);

        client.player.sendMessage(
                Text.literal("§c" + target.getName().getString() + " §7| §e" + String.format("%.1f", distance) + "m"),
                true
        );
    }

    private Entity getTarget(MinecraftClient client) {
        double maxR = maxReach.value;
        Vec3d camera = client.player.getCameraPosVec(1.0F);
        Vec3d rotation = client.player.getRotationVec(1.0F);
        Vec3d end = camera.add(rotation.multiply(maxR));

        Entity closest = null;
        double closestDist = maxR;

        for (Entity entity : client.world.getEntities()) {
            if (entity == client.player || !entity.isAlive()) continue;
            Optional<Vec3d> hit = entity.getBoundingBox().expand(0.3).raycast(camera, end);
            if (hit.isEmpty()) continue;
            double dist = camera.distanceTo(hit.get());
            if (dist < closestDist) { closestDist = dist; closest = entity; }
        }
        return closest;
    }
}
