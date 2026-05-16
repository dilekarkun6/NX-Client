package com.nxclient.modules;

public abstract class Module {

    private final String name;
    private final String description;
    private final Category category;
    protected boolean enabled = false;

    public enum Category {
        COMBAT, MOVEMENT, RENDER, PLAYER, MISC
    }

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}

    public void toggle() {
        enabled = !enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public boolean isEnabled() { return enabled; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
}
