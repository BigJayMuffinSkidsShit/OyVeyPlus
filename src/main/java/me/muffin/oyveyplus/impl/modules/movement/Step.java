package me.muffin.oyveyplus.impl.modules.movement;

import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;

public class Step extends Module {
    public static Step instance;
    public final Setting<Double> height = register("Height", 2, 0, 2, 0);
    private Setting setting;

    public Step() {
        super("Step", "Steps", Category.Movement);
    }

    public void onUpdate() {
        mc.player.stepHeight = height.getValue().floatValue();
    }

    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }
}
