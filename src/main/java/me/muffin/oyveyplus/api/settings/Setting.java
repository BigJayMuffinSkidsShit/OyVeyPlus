package me.muffin.oyveyplus.api.settings;

import me.muffin.oyveyplus.api.module.Module;

import java.util.List;

/**
 * @author cattyngmd
 * @author fuckyouthinkimboogieman
 */

public class Setting<T> {

    private final String name;
    public T getValue;
    private String description = null;
    private final Module module;
    private final Module.Category category;
    private final Type type;

    private T value;
    private double max;
    private double min;
    private int decimal;
    private List<String> modes;

    public Setting(final String name, final Module module, final Module.Category category, T value) {
        this.name = name;
        this.module = module;
        this.category = category;
        this.value = value;

        this.type = Type.BOOLEAN;
    }

    public Setting(final String name, final Module module, final Module.Category category, T value, double min, double max, int decimal) {
        this.name = name;
        this.module = module;
        this.category = category;
        this.value = value;
        this.min = min;
        this.max = max;
        this.decimal = decimal;

        this.type = Type.NUMBER;
    }

    public Setting(final String name, final Module module, final Module.Category category, List<String> modes, T value) {
        this.name = name;
        this.module = module;
        this.category = category;
        this.value = value;
        this.modes = modes;

        this.type = Type.MODE;
    }

    public String getName() {
        return this.name;
    }

    public Module getModule() {
        return this.module;
    }

    public Type getType() {
        return this.type;
    }

    public Module.Category getCategory() {
        return this.category;
    }

    public T getValue() { return value; }

    public void setValue(T value) { this.value = value; }

    public double getMin() { return min; }

    public double getMax() { return max; }

    public int getDecimal() { return decimal; }

    public List<String> getModes() { return modes; }

    public Setting withDesc(String desc) { this.description = desc; return this; }

    public String getDescription() { return description; }
    
    public T getValueAsString() {
        return value;
    }

    public enum Type {
        NUMBER, BOOLEAN, MODE
    }
}
