package me.muffin.oyveyplus.impl.gui.click.hud.components;

import me.muffin.oyveyplus.impl.gui.click.hud.HudComponent;

public class WaterMark extends HudComponent
{
    public WaterMark(String name)
    {
        super(name);

        setW(mc.fontRenderer.getStringWidth("OyVeyPlus"));
        setH(mc.fontRenderer.FONT_HEIGHT);
    }

    @Override
    public void render()
    {
        mc.fontRenderer.drawStringWithShadow("OyVeyPlus", getX(), getY(), -1);
    }
}
