package me.muffin.oyveyplus.impl.gui.click.hud;

import me.muffin.oyveyplus.impl.gui.click.hud.components.WaterMark;

import java.util.ArrayList;

public class HudComponentManager {
    public static HudComponentManager INSTANCE = new HudComponentManager();

    private final ArrayList<HudComponent> components = new ArrayList<>();

    public HudComponentManager() {
        components.add(new WaterMark("Watermark"));
    }

    public ArrayList<HudComponent> getComponents()
    {
        return components;
    }

    public HudComponent getHudComponent(String name) {
        return components.stream().filter(components -> components.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
