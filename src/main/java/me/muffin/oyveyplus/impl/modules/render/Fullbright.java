package me.muffin.oyveyplus.impl.modules.render;

import me.muffin.oyveyplus.api.module.Module;
//susssy owo
public class Fullbright extends Module {
    public Fullbright() {
        super("Fullbright","Gamma go brrrr",Category.Render);
    }

    public void onUpdate() {
        mc.gameSettings.gammaSetting = 1000;
    }

    public void onDisable() {
        mc.gameSettings.gammaSetting = 1;
    }
}
