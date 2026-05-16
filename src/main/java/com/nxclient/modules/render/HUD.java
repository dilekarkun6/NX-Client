package com.nxclient.modules.render;

import com.nxclient.modules.Module;
import com.nxclient.modules.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class HUD extends Module {

    public static boolean active = true;

    public HUD() {
        super("HUD", "Displays enabled modules on screen.", Category.RENDER);
        this.enabled = true;
        active = true;
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }

    public static void registerRenderer() {
        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            if (!active) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.options.hudHidden) return;

            List<Module> enabled = ModuleManager.getModules().stream()
                    .filter(Module::isEnabled)
                    .toList();

            int y = 2;
            for (Module m : enabled) {
                String tag = switch (m.getCategory()) {
                    case COMBAT -> "§c";
                    case MOVEMENT -> "§a";
                    case RENDER -> "§b";
                    case PLAYER -> "§e";
                    case MISC -> "§7";
                };
                context.drawTextWithShadow(
                        client.textRenderer,
                        tag + m.getName(),
                        2, y, 0xFFFFFF
                );
                y += 10;
            }
        });
    }
}
