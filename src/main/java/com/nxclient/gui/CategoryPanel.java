package com.nxclient.gui;

import com.nxclient.modules.Module;
import com.nxclient.modules.ModuleManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public class CategoryPanel {

    public final Module.Category category;
    public int x;
    public int y;
    public final int width = 110;

    public static final int HEADER_H = 18;
    public static final int MODULE_H = 14;

    public CategoryPanel(Module.Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
    }

    public int getHeight() {
        return HEADER_H + ModuleManager.getByCategory(category).size() * MODULE_H;
    }

    public void render(DrawContext context, int mouseX, int mouseY, TextRenderer textRenderer) {
        List<Module> modules = ModuleManager.getByCategory(category);
        int totalH = getHeight();

        context.fill(x - 1, y - 1, x + width + 1, y + totalH + 1, 0xFFFFFFFF);
        context.fill(x, y, x + width, y + HEADER_H, 0xFF1A1A1A);
        context.drawTextWithShadow(textRenderer, Text.literal("§f" + getDisplayName()), x + 6, y + 5, 0xFFFFFF);

        int my = y + HEADER_H;
        for (Module m : modules) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= my && mouseY < my + MODULE_H;
            int bg;
            if (m.isEnabled()) {
                bg = hovered ? 0xFF2D7A2D : 0xFF1F5A1F;
            } else {
                bg = hovered ? 0xFF2B2B2B : 0xFF1F1F1F;
            }
            context.fill(x, my, x + width, my + MODULE_H, bg);

            String prefix = m.isEnabled() ? "§a" : "§7";
            context.drawTextWithShadow(textRenderer, Text.literal(prefix + m.getName()), x + 6, my + 3, 0xFFFFFF);

            my += MODULE_H;
        }
    }

    private String getDisplayName() {
        String name = category.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    public boolean isOnHeader(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + HEADER_H;
    }

    public int getModuleIndexAt(double mouseX, double mouseY) {
        if (mouseX < x || mouseX > x + width) return -1;
        List<Module> modules = ModuleManager.getByCategory(category);
        int my = y + HEADER_H;
        for (int i = 0; i < modules.size(); i++) {
            if (mouseY >= my && mouseY < my + MODULE_H) return i;
            my += MODULE_H;
        }
        return -1;
    }

    public void toggleModuleAt(int index) {
        List<Module> modules = ModuleManager.getByCategory(category);
        if (index >= 0 && index < modules.size()) {
            modules.get(index).toggle();
        }
    }
}
