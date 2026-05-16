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
    public final int width = 110;

    public static final int HEADER_H = 18;
    public static final int MODULE_H = 14;
    public static final int SETTING_H = 12;

    private final Set<Module> expanded = new HashSet<>();

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
        context.drawTextWithShadow(textRenderer, Text.literal("§f" + getDisplayName()), x + 6, y + 5, 0xFFFFFF);

        int my = y + HEADER_H;
        for (Module m : ModuleManager.getByCategory(category)) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= my && mouseY < my + MODULE_H;
            int bg;
            if (m.isEnabled()) bg = hovered ? 0xFF2D7A2D : 0xFF1F5A1F;
            else                bg = hovered ? 0xFF2B2B2B : 0xFF1F1F1F;
            context.fill(x, my, x + width, my + MODULE_H, bg);

            String prefix = m.isEnabled() ? "§a" : "§7";
            String marker = expanded.contains(m) ? " §8>" : "";
            context.drawTextWithShadow(textRenderer, Text.literal(prefix + m.getName() + marker), x + 6, my + 3, 0xFFFFFF);

            my += MODULE_H;

            if (expanded.contains(m)) {
                for (Setting<?> s : m.settings) {
                    boolean sHovered = mouseX >= x && mouseX <= x + width && mouseY >= my && mouseY < my + SETTING_H;
                    int sBg = sHovered ? 0xFF333366 : 0xFF222244;
                    context.fill(x, my, x + width, my + SETTING_H, sBg);
                    context.drawTextWithShadow(textRenderer, Text.literal("§b- §f" + s.display()), x + 6, my + 2, 0xFFFFFF);
                    my += SETTING_H;
                }
            }
        }
    }

    private String getDisplayName() {
        String name = category.name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    public boolean isOnHeader(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + HEADER_H;
    }

    public ClickResult handleClick(double mouseX, double mouseY, int button) {
        if (mouseX < x || mouseX > x + width) return ClickResult.NONE;
        int my = y + HEADER_H;
        for (Module m : ModuleManager.getByCategory(category)) {
            if (mouseY >= my && mouseY < my + MODULE_H) {
                if (button == 0) {
                    m.toggle();
                } else if (button == 1) {
                    if (m.settings.isEmpty()) return ClickResult.HANDLED;
                    if (expanded.contains(m)) expanded.remove(m);
                    else expanded.add(m);
                }
                return ClickResult.HANDLED;
            }
            my += MODULE_H;

            if (expanded.contains(m)) {
                for (Setting<?> s : m.settings) {
                    if (mouseY >= my && mouseY < my + SETTING_H) {
                        boolean leftHalf = mouseX < x + width / 2.0;
                        if (button == 0) {
                            if (leftHalf) s.onDecrement();
                            else s.onIncrement();
                        } else if (button == 1) {
                            s.onDecrement();
                        }
                        return ClickResult.HANDLED;
                    }
                    my += SETTING_H;
                }
            }
        }
        return ClickResult.NONE;
    }

    public enum ClickResult { NONE, HANDLED }
}
