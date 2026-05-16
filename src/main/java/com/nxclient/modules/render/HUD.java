package com.nxclient.modules.render;

import com.nxclient.modules.Module;

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
}
