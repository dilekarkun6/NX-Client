package com.nxclient.modules.settings;

public abstract class Setting<T> {

    public final String name;
    public T value;
    public final T defaultValue;

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public void reset() { this.value = defaultValue; }

    public abstract String display();
    public abstract void onIncrement();
    public abstract void onDecrement();
}
