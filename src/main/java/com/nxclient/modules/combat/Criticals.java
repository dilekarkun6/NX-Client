package com.nxclient.modules.combat;

import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Criticals extends Module {

    public static boolean active = false;
    private boolean wasAttackDown = false;

    public Criticals() {
        super("Criticals", "Makes every attack a critical hit.", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        active = true;
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    @Override
    public void onDisable() { active = false; }

    private void tick(MinecraftClient client) {
        if (!active || client.player == null) return;

        long window = client.getWindow().getHandle();
        boolean isAttackDown = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean justPressed = isAttackDown && !wasAttackDown;
        wasAttackDown = isAttackDown;
        if (!justPressed) return;

        if (!client.player.isOnGround()) return;
        if (client.player.isInsideWaterOrBubbleColumn()) return;
        if (client.player.hasVehicle()) return;
        if (client.player.isClimbing()) return;

        if (!hasAttackTarget(client)) return;

        ClientPlayNetworkHandler nh = client.getNetworkHandler();
        if (nh == null) return;

        Vec3d pos = client.player.getPos();
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y + 0.0625, pos.z, false, false));
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, false, false));
        nh.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, true, false));
    }

    private boolean hasAttackTarget(MinecraftClient client) {
        Entity targeted = client.targetedEntity;
        return targeted instanceof LivingEntity && targeted != client.player;
    }
}
