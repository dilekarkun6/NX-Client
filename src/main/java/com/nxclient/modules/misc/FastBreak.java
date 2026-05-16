package com.nxclient.modules.misc;

import com.nxclient.modules.Module;

public class FastBreak extends Module {

    public static boolean active = false;

    public FastBreak() {
        super("FastBreak", "Breaks blocks without the normal between-block cooldown.", Category.MISC);
    }

    @Override public void onEnable()  { active = true; }
    @Override public void onDisable() { active = false; }
}
