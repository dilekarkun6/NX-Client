package com.nxclient.modules.movement;

import com.nxclient.modules.Module;

public class NoFall extends Module {

    public static boolean active = false;

    public NoFall() {
        super("NoFall", "Spoofs onGround=true in movement packets so you take no fall damage.", Category.MOVEMENT);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
