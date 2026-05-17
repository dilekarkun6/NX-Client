package com.nxclient.modules.movement;

import com.nxclient.modules.Module;

public class NoFall extends Module {

    public static boolean active = false;

    public NoFall() {
        super("NoFall", "Modifies every outgoing move packet to set onGround=true when falling.", Category.MOVEMENT);
    }

    @Override public void onEnable()  { active = true; }
    @Override public void onDisable() { active = false; }
}
