package com.nxclient.modules.combat;

import com.nxclient.modules.Module;

public class Criticals extends Module {

    public static boolean active = false;

    public Criticals() {
        super("Criticals", "Injects mini-jump packets right before every attack packet (Meteor pattern).", Category.COMBAT);
    }

    @Override public void onEnable()  { active = true; }
    @Override public void onDisable() { active = false; }
}
