package com.nxclient.modules.combat;

import com.nxclient.modules.Module;

public class Criticals extends Module {

    public static boolean active = false;

    public Criticals() {
        super("Criticals", "Every melee hit becomes a critical via attackEntity mixin.", Category.COMBAT);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
