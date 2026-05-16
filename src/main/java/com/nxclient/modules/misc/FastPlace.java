package com.nxclient.modules.misc;

import com.nxclient.modules.Module;

public class FastPlace extends Module {

    public static boolean active = false;

    public FastPlace() {
        super("FastPlace", "Places items with no cooldown between clicks.", Category.MISC);
    }

    @Override public void onEnable()  { active = true; }
    @Override public void onDisable() { active = false; }
}
