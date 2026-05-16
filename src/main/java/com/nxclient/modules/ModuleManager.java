package com.nxclient.modules;

import com.nxclient.modules.combat.AntiKnockback;
import com.nxclient.modules.combat.Criticals;
import com.nxclient.modules.combat.KillAura;
import com.nxclient.modules.movement.Blink;
import com.nxclient.modules.movement.BoatFly;
import com.nxclient.modules.movement.Fly;
import com.nxclient.modules.movement.NoFall;
import com.nxclient.modules.movement.Speed;
import com.nxclient.modules.movement.Spider;
import com.nxclient.modules.movement.Sprint;
import com.nxclient.modules.movement.Step;
import com.nxclient.modules.player.AutoArmor;
import com.nxclient.modules.player.AutoEat;
import com.nxclient.modules.player.AutoTotem;
import com.nxclient.modules.player.Freecam;
import com.nxclient.modules.player.InvWalk;
import com.nxclient.modules.player.NoHunger;
import com.nxclient.modules.render.ESP;
import com.nxclient.modules.render.FullBright;
import com.nxclient.modules.render.HUD;
import com.nxclient.modules.render.XRay;
import com.nxclient.modules.misc.AutoFish;
import com.nxclient.modules.misc.ChestStealer;
import com.nxclient.modules.misc.FastBreak;
import com.nxclient.modules.misc.FastPlace;
import com.nxclient.modules.misc.Reach;
import com.nxclient.modules.misc.Scaffold;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static final List<Module> modules = new ArrayList<>();

    public static void init() {
        register(new KillAura());
        register(new AntiKnockback());
        register(new Criticals());

        register(new Fly());
        register(new BoatFly());
        register(new Speed());
        register(new NoFall());
        register(new Sprint());
        register(new Step());
        register(new Spider());
        register(new Blink());

        register(new AutoEat());
        register(new NoHunger());
        register(new AutoTotem());
        register(new AutoArmor());
        register(new InvWalk());
        register(new Freecam());

        register(new ESP());
        register(new FullBright());
        register(new XRay());
        register(new HUD());

        register(new AutoFish());
        register(new Reach());
        register(new FastPlace());
        register(new FastBreak());
        register(new Scaffold());
        register(new ChestStealer());
    }

    private static void register(Module module) {
        modules.add(module);
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static List<Module> getByCategory(Module.Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) result.add(m);
        }
        return result;
    }

    public static Module getByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}
