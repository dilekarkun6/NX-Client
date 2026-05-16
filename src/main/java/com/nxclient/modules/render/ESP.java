package com.nxclient.modules.render;

import com.nxclient.modules.Module;

public class ESP extends Module {

    public static boolean active = false;

    public ESP() {
        super("ESP", "Highlights all living entities through walls.", Category.RENDER);
    }

    @Override
    public void onEnable() { active = true; }

    @Override
    public void onDisable() { active = false; }
}
