package com.nxclient.modules.player;

import com.nxclient.mixin.KeyBindingAccessor;
import com.nxclient.modules.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class InvWalk extends Module {

    public static boolean active = false;

    public InvWalk() {
        super("InvWalk", "Walk and look around while an inventory screen is open.", Category.PLAYER);
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
        if (!(client.currentScreen instanceof HandledScreen<?>)) return;

        long window = client.getWindow().getHandle();
        forceKey(client.options.forwardKey, window);
        forceKey(client.options.backKey, window);
        forceKey(client.options.leftKey, window);
        forceKey(client.options.rightKey, window);
        forceKey(client.options.jumpKey, window);
        forceKey(client.options.sprintKey, window);
    }

    private void forceKey(KeyBinding kb, long window) {
        InputUtil.Key boundKey = ((KeyBindingAccessor) (Object) kb).getBoundKeyNX();
        if (boundKey == null) return;
        int code = boundKey.getCode();
        if (code == -1) return;
        boolean pressed = false;
        if (boundKey.getCategory() == InputUtil.Type.KEYSYM) {
            pressed = InputUtil.isKeyPressed(window, code);
        } else if (boundKey.getCategory() == InputUtil.Type.MOUSE) {
            pressed = org.lwjgl.glfw.GLFW.glfwGetMouseButton(window, code) == org.lwjgl.glfw.GLFW.GLFW_PRESS;
        }
        kb.setPressed(pressed);
    }
}
