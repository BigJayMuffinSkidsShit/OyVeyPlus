package me.muffin.oyveyplus.impl.modules.client;

import me.muffin.oyveyplus.api.module.Module;

public class HUDEditor extends Module
{
    public HUDEditor()
    {
        super("HUDEditor", Category.Client);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new me.muffin.oyveyplus.impl.gui.click.hud.HUDEditor());
        disable();
    }
}
