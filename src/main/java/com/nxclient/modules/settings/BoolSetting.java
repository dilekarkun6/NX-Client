package com.nxclient.modules.settings;

public class BoolSetting extends Setting<Boolean> {

    public BoolSetting(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public String display() {
        return name + ": " + (value ? "ON" : "OFF");
    }

    @Override
    public void onIncrement() { value = !value; }

    @Override
    public void onDecrement() { value = !value; }
}
