package com.nxclient.modules.misc;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class Reach extends Module {

    public static boolean active = false;
    private static final double MAX_REACH = 100.0;
    private static final double STEP_SIZE = 8.0;
    private boolean wasAttackDown = false;

    public Reach() {
        super("Reach", "Attacks entities from extreme distances via packet teleport.", Category.MISC);
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
        Vec3d dest = target.getPos();
        double distance = origin.distanceTo(dest);

        client.player.sendMessage(
                Text.literal("§c" + target.getName().getString() + " §7| §e" + String.format("%.1f", distance) + "m"),
                true
        );

        int steps = Math.max(1, (int) Math.ceil(distance / STEP_SIZE));
        double dx = dest.x - origin.x;
        double dy = dest.y - origin.y;
        double dz = dest.z - origin.z;

        for (int i = 1; i <= steps; i++) {
            double t = (double) i / steps;
            sendPos(client, origin.x + dx * t, origin.y + dy * t, origin.z + dz * t);
        }

        client.getNetworkHandler().sendPacket(
                PlayerInteractEntityC2SPacket.attack(target, client.player.isSneaking())
        );
        client.player.playSound(net.minecraft.sound.SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, 1f, 1f);

        for (int i = steps - 1; i >= 0; i--) {
            double t = (double) i / steps;
            sendPos(client, origin.x + dx * t, origin.y + dy * t, origin.z + dz * t);
        }
    }

    private Entity getTarget(MinecraftClient client) {
        Vec3d camera = client.player.getCameraPosVec(1.0F);
        Vec3d rotation = client.player.getRotationVec(1.0F);
        Vec3d end = camera.add(rotation.multiply(MAX_REACH));

        Entity closest = null;
        double closestDist = MAX_REACH;

        for (Entity entity : client.world.getEntities()) {
            if (entity == client.player || !entity.isAlive()) continue;
            Optional<Vec3d> hit = entity.getBoundingBox().expand(0.3).raycast(camera, end);
            if (hit.isEmpty()) continue;
            double dist = camera.distanceTo(hit.get());
            if (dist < closestDist) { closestDist = dist; closest = entity; }
        }
        return closest;
    }

    private void sendPos(MinecraftClient client, double x, double y, double z) {
        client.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, true, false));
    }
}
