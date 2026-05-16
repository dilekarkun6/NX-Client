package com.nxclient.modules.combat;

import com.nxclient.modules.Module;

public class AntiKnockback extends Module {

    public static boolean active = false;

    public AntiKnockback() {
        super("AntiKnockback", "Reduces knockback taken from hits.", Category.COMBAT);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
