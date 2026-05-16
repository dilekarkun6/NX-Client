package com.nxclient.mixin;

import com.nxclient.modules.ModuleManager;
import com.nxclient.modules.Module;
import com.nxclient.modules.render.HUD;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(RenderTickCounter tickCounter, boolean tick, CallbackInfo ci) {
        if (!HUD.active) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getOverlay() != null || client.currentScreen != null) return;

        DrawContext context = new DrawContext(client, client.getRenderManager().getBufferBuilders().getEntityVertexConsumers());

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
    }
}
