package com.nxclient.modules.settings;

public class BoolSetting extends Setting<Boolean> {

    public BoolSetting(String name, boolean value) {
        super(name, value);
    }

    @Override
    public String display() {
        return name + ": " + (value ? "§aON" : "§cOFF");
    }

    @Override
    public void onIncrement() {
        value = !value;
    }

    @Override
    public void onDecrement() {
        value = !value;
    }
}
