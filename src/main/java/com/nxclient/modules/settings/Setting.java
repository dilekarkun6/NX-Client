package com.nxclient.modules.settings;

public abstract class Setting<T> {

    public final String name;
    public T value;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public abstract String display();
    public abstract void onIncrement();
    public abstract void onDecrement();
}
