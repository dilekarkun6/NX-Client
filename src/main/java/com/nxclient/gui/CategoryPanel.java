package com.nxclient.gui;

import com.nxclient.modules.Module;
import com.nxclient.modules.ModuleManager;
import com.nxclient.modules.settings.Setting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryPanel {

    public final Module.Category category;
    public int x;
    public int y;
    public final int width = 130;

    public static final int HEADER_H = 18;
    public static final int MODULE_H = 14;
    public static final int SETTING_H = 14;
    public static final int BTN_W = 20;

    public final Set<Module> expanded = new HashSet<>();

    public CategoryPanel(Module.Category category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
    }

    public int getHeight() {
        int h = HEADER_H;
        for (Module m : ModuleManager.getByCategory(category)) {
            h += MODULE_H;
            if (expanded.contains(m)) {
                h += m.settings.size() * SETTING_H;
            }
        }
        return h;
    }

    public void render(DrawContext context, int mouseX, int mouseY, TextRenderer textRenderer) {
        int totalH = getHeight();

        context.fill(x - 1, y - 1, x + width + 1, y + totalH + 1, 0xFFFFFFFF);
        context.fill(x, y, x + width, y + HEADER_H, 0xFF1A1A1A);
        context.drawTextWithShadow(textRenderer, Text.literal("§f" + cap(category.name())), x + 6, y + 5, 0xFFFFFF);

        int my = y + HEADER_H;
        for (Module m : ModuleManager.getByCategory(category)) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= my && mouseY < my + MODULE_H;
            int bg = m.isEnabled() ? (hovered ? 0xFF2D7A2D : 0xFF1F5A1F) : (hovered ? 0xFF2B2B2B : 0xFF1F1F1F);
            context.fill(x, my, x + width, my + MODULE_H, bg);

            String prefix = m.isEnabled() ? "§a" : "§7";
            String arrow = !m.settings.isEmpty() ? (expanded.contains(m) ? " §7▼" : " §7▶") : "";
            context.drawTextWithShadow(textRenderer, Text.literal(prefix + m.getName() + arrow), x + 6, my + 3, 0xFFFFFF);
            my += MODULE_H;

            if (expanded.contains(m)) {
                for (Setting<?> s : m.settings) {
                    context.fill(x, my, x + BTN_W, my + SETTING_H, 0xFF7A2D2D);
                    context.drawTextWithShadow(textRenderer, Text.literal("§f-"), x + 8, my + 3, 0xFFFFFF);
                    context.fill(x + BTN_W, my, x + width - BTN_W, my + SETTING_H, 0xFF1A2A4A);
                    context.drawTextWithShadow(textRenderer, Text.literal("§f" + s.display()), x + BTN_W + 4, my + 3, 0xFFFFFF);
                    context.fill(x + width - BTN_W, my, x + width, my + SETTING_H, 0xFF2D7A2D);
                    context.drawTextWithShadow(textRenderer, Text.literal("§f+"), x + width - BTN_W + 7, my + 3, 0xFFFFFF);
                    my += SETTING_H;
                }
            }
        }
    }

    private String cap(String s) {
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    public boolean isOnHeader(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + HEADER_H;
    }

    public ClickResult handleClick(double mouseX, double mouseY, int button) {
        if (mouseX < x || mouseX > x + width) return ClickResult.MISS;

        int my = y + HEADER_H;
        for (Module m : ModuleManager.getByCategory(category)) {
            if (mouseY >= my && mouseY < my + MODULE_H) {
                if (button == 0) {
                    m.toggle();
                    return ClickResult.HANDLED;
                } else if (button == 1) {
                    if (!m.settings.isEmpty()) {
                        if (expanded.contains(m)) expanded.remove(m);
                        else expanded.add(m);
                        return ClickResult.HANDLED;
                    }
                }
                return ClickResult.HANDLED;
            }
            my += MODULE_H;

            if (expanded.contains(m)) {
                for (Setting<?> s : m.settings) {
                    if (mouseY >= my && mouseY < my + SETTING_H) {
                        if (button == 0) {
                            if (mouseX < x + BTN_W) s.onDecrement();
                            else if (mouseX > x + width - BTN_W) s.onIncrement();
                        } else if (button == 1) {
                            s.reset();
                        }
                        return ClickResult.HANDLED;
                    }
                    my += SETTING_H;
                }
            }
        }
        return ClickResult.MISS;
    }

    public enum ClickResult { MISS, HANDLED }
}
