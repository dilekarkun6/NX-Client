package com.nxclient.gui;

import com.nxclient.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class NXClientScreen extends Screen {

    private static final Map<Module.Category, int[]> savedPositions = new EnumMap<>(Module.Category.class);
    private final List<CategoryPanel> panels = new ArrayList<>();

    private CategoryPanel draggedPanel = null;
    private int dragOffsetX;
    private int dragOffsetY;

    public NXClientScreen() {
        super(Text.literal("NX Client"));
    }

    @Override
    protected void init() {
        panels.clear();
        int defaultX = 12;
        int defaultY = 12;
        for (Module.Category category : Module.Category.values()) {
            int[] saved = savedPositions.get(category);
            int x = saved != null ? saved[0] : defaultX;
            int y = saved != null ? saved[1] : defaultY;
            panels.add(new CategoryPanel(category, x, y));
            defaultX += 120;
            if (defaultX > this.width - 120) {
                defaultX = 12;
                defaultY += 160;
            }
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x88000000);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, Text.literal("§fNX Client §7by Novatex"), 6, this.height - 14, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("§7L-click = toggle  §8|  §7R-click = settings  §8|  §7Drag header = move"), 6, this.height - 26, 0xAAAAAA);

        for (CategoryPanel panel : panels) {
            panel.render(context, mouseX, mouseY, this.textRenderer);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (CategoryPanel panel : panels) {
                if (panel.isOnHeader(mouseX, mouseY)) {
                    draggedPanel = panel;
                    dragOffsetX = (int) mouseX - panel.x;
                    dragOffsetY = (int) mouseY - panel.y;
                    return true;
                }
            }
        }
        for (CategoryPanel panel : panels) {
            if (panel.handleClick(mouseX, mouseY, button) == CategoryPanel.ClickResult.HANDLED) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggedPanel != null && button == 0) {
            int newX = (int) mouseX - dragOffsetX;
            int newY = (int) mouseY - dragOffsetY;
            newX = Math.max(0, Math.min(this.width - draggedPanel.width, newX));
            newY = Math.max(0, Math.min(this.height - draggedPanel.getHeight(), newY));
            draggedPanel.x = newX;
            draggedPanel.y = newY;
            savedPositions.put(draggedPanel.category, new int[]{newX, newY});
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggedPanel != null) {
            draggedPanel = null;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() { return false; }
}
