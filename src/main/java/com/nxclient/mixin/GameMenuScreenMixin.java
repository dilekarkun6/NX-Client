package com.nxclient.mixin;

import com.nxclient.gui.NXClientScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void addNXClientButton(CallbackInfo ci) {
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("§0NX Client"), button ->
                        MinecraftClient.getInstance().setScreen(new NXClientScreen(this))
                ).dimensions(10, 10, 100, 20).build()
        );
    }
}
