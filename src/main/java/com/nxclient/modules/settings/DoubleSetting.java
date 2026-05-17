package com.nxclient.modules.settings;

public class DoubleSetting extends Setting<Double> {

    public final double min;
    public final double max;
    public final double step;

    public DoubleSetting(String name, double defaultValue, double min, double max, double step) {
        super(name, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public String display() {
        return name + ": " + String.format("%.2f", value);
    }

    @Override
    public void onIncrement() {
        value = Math.min(max, value + step);
    }

    @Override
    public void onDecrement() {
        value = Math.max(min, value - step);
    }
}
