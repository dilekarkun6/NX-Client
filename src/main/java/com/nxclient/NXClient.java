package com.nxclient;

import com.nxclient.modules.ModuleManager;
import com.nxclient.modules.render.HUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NXClient implements ClientModInitializer {

    public static final String MOD_ID = "nxclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("NX Client loaded.");

        ModuleManager.init();
        HUD.registerRenderer();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nxclient.opengui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "NX Client"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null && client.player != null) {
                    client.setScreen(new com.nxclient.gui.NXClientScreen(null));
                }
            }
        });
    }
}
