package com.nxclient.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;

import java.io.File;
import java.nio.file.Path;

public class BotCallScreen extends Screen {

    private final Screen parent;
    private TextFieldWidget nameField;
    private TextFieldWidget authPassField;
    private String status = "";

    public BotCallScreen(Screen parent) {
        super(Text.literal("Call Bot"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        nameField = new TextFieldWidget(this.textRenderer, cx - 100, cy - 30, 200, 18, Text.literal("Bot name"));
        nameField.setPlaceholder(Text.literal("Bot name (e.g. Helper42)"));
        nameField.setMaxLength(16);
        this.addDrawableChild(nameField);

        authPassField = new TextFieldWidget(this.textRenderer, cx - 100, cy - 4, 200, 18, Text.literal("Auth password"));
        authPassField.setPlaceholder(Text.literal("/register password (optional)"));
        authPassField.setMaxLength(64);
        this.addDrawableChild(authPassField);

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("§aSpawn Bot"), b -> spawn())
                        .dimensions(cx - 100, cy + 24, 95, 20).build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("§7Cancel"), b -> this.client.setScreen(parent))
                        .dimensions(cx + 5, cy + 24, 95, 20).build()
        );

        this.setInitialFocus(nameField);
    }

    private void spawn() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { status = "§cName cannot be empty"; return; }

        ServerInfo server = MinecraftClient.getInstance().getCurrentServerEntry();
        if (server == null) { status = "§cYou must be on a server to call bots"; return; }

        String address = server.address;
        String host;
        int port = 25565;
        int colon = address.lastIndexOf(':');
        if (colon > 0) {
            host = address.substring(0, colon);
            try { port = Integer.parseInt(address.substring(colon + 1)); } catch (NumberFormatException ignored) {}
        } else {
            host = address;
        }

        Path runDir = MinecraftClient.getInstance().runDirectory.toPath();
        Path jar = runDir.resolve("nxbots-all.jar");
        if (!jar.toFile().exists()) {
            File alt = new File("bots/build/libs/nxbots-all.jar");
            if (alt.exists()) jar = alt.toPath();
            else {
                status = "§cnxbots-all.jar not found in run dir. Build it with `gradle :bots:shadowJar` and place it beside Minecraft.";
                return;
            }
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", jar.toString(), host, String.valueOf(port), "1", "--names", name);
            if (!authPassField.getText().isEmpty()) {
                pb.command().add("--auth-pass");
                pb.command().add(authPassField.getText());
            }
            pb.redirectErrorStream(true);
            pb.inheritIO();
            pb.start();
            status = "§aSpawned bot '" + name + "' → " + host + ":" + port;
        } catch (Exception e) {
            status = "§cFailed to launch bot: " + e.getMessage();
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC000000);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        int cx = this.width / 2;
        int cy = this.height / 2;
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("§fCall a Bot to this Server"), cx, cy - 56, 0xFFFFFF);
        if (!status.isEmpty()) {
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(status), cx, cy + 52, 0xFFFFFF);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() { this.client.setScreen(parent); }

    @Override
    public boolean shouldPause() { return false; }
}
