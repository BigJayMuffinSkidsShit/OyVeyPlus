package me.muffin.oyveyplus.api.settings;

import me.muffin.oyveyplus.api.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fuckyouthinkimboogieman
 */

public class SettingManager {
    private final List<Setting> settings;

    public SettingManager() {
        settings = new ArrayList<>();
    }

    public void register(Setting setting) {
        settings.add(setting);
    }

    public List<Setting> getSettings() { return settings; }

    public List<Setting> getSettings(Module module) { return settings.stream().filter(s -> s.getModule() == module).collect(Collectors.toList()); }

    public Setting getSetting(String name, Module module) { return settings.stream().filter(s -> s.getModule() == module).filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(null); }
}
