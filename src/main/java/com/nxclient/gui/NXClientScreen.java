package com.nxclient.gui;

import com.nxclient.modules.Module;
import com.nxclient.modules.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

public class NXClientScreen extends Screen {

    private final Screen parent;
    private Module.Category selectedCategory = Module.Category.MOVEMENT;

    private static final int PANEL_X = 10;
    private static final int PANEL_Y = 30;
    private static final int BTN_W = 90;
    private static final int BTN_H = 16;
    private static final int CAT_BTN_W = 80;
    private static final int CAT_BTN_H = 18;
    private static final int SPACING = 3;

    public NXClientScreen(Screen parent) {
        super(Text.literal("NX Client"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        rebuildButtons();
    }

    private void rebuildButtons() {
        this.clearChildren();

        Module.Category[] cats = Module.Category.values();
        int catX = PANEL_X;
        for (Module.Category cat : cats) {
            final Module.Category c = cat;
            String label = (cat == selectedCategory ? "§f" : "§7") + cat.name();
            this.addDrawableChild(
                    ButtonWidget.builder(Text.literal(label), btn -> {
                        selectedCategory = c;
                        rebuildButtons();
                    }).dimensions(catX + (cat.ordinal() * (CAT_BTN_W + SPACING)), PANEL_Y - CAT_BTN_H - 2, CAT_BTN_W, CAT_BTN_H).build()
            );
        }

        List<Module> modules = ModuleManager.getByCategory(selectedCategory);
        int y = PANEL_Y;
        for (Module module : modules) {
            final Module m = module;
            String label = (m.isEnabled() ? "§a" : "§c") + m.getName();
            this.addDrawableChild(
                    ButtonWidget.builder(Text.literal(label), btn -> {
                        m.toggle();
                        btn.setMessage(Text.literal((m.isEnabled() ? "§a" : "§c") + m.getName()));
                    }).dimensions(PANEL_X, y, BTN_W, BTN_H).build()
            );
            y += BTN_H + SPACING;
        }

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("§7Back"), btn -> this.client.setScreen(parent))
                        .dimensions(PANEL_X, this.height - 24, 60, 16).build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        context.fill(PANEL_X - 2, PANEL_Y - 2,
                PANEL_X + BTN_W + 2, PANEL_Y + (ModuleManager.getByCategory(selectedCategory).size() * (BTN_H + SPACING)) + 2,
                0xAA000000);

        context.drawTextWithShadow(this.textRenderer, Text.literal("§fNX Client"), PANEL_X, 8, 0xFFFFFF);

        List<Module> modules = ModuleManager.getByCategory(selectedCategory);
        int y = PANEL_Y;
        for (Module m : modules) {
            int descX = PANEL_X + BTN_W + 8;
            if (mouseX >= PANEL_X && mouseX <= PANEL_X + BTN_W && mouseY >= y && mouseY <= y + BTN_H) {
                context.drawTextWithShadow(this.textRenderer,
                        Text.literal("§7" + m.getDescription()), descX, y + 4, 0xAAAAAA);
            }
            y += BTN_H + SPACING;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void close() { this.client.setScreen(parent); }
}
