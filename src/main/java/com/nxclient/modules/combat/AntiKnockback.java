package com.nxclient.modules.combat;

import com.nxclient.modules.Module;

public class AntiKnockback extends Module {

    public static boolean active = false;

    public AntiKnockback() {
        super("AntiKnockback", "Cancels knockback velocity packets from the server.", Category.COMBAT);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
