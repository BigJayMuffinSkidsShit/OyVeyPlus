package me.muffin.oyveyplus.impl.gui.click.hud;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.utils.ColorUtil;
import me.muffin.oyveyplus.impl.gui.click.components.ModuleButton;
import me.muffin.oyveyplus.impl.gui.click.components.settings.BooleanButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class HudComponent {
    protected final Minecraft mc = Minecraft.getMinecraft();
    private final String name;
    private int x;
    private int y;
    private int w;
    private int h;
    private boolean showing;
    private boolean enabled;

    public HudComponent(String name)
    {
        this.name = name;
    }

    public void render() {
        this.w = 88;
        this.x = 5;
        this.y = 5;
        this.h = 16;
        this.showing = true;
        this.enabled = true;
        Gui.drawRect(x,y,x + w,y + h, ColorUtil.toRGBA(me.muffin.oyveyplus.impl.modules.client.Gui.instance.red.getValue.intValue(),me.muffin.oyveyplus.impl.modules.client.Gui.instance.green.getValue.intValue(),me.muffin.oyveyplus.impl.modules.client.Gui.instance.blue.getValue.intValue()));
    }

    public String getName()
    {
        return name;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getW()
    {
        return w;
    }

    public void setW(int w)
    {
        this.w = w;
    }

    public int getH()
    {
        return h;
    }

    public void setH(int h)
    {
        this.h = h;
    }

    public boolean isShowing()
    {
        return showing;
    }

    public void setShowing(boolean showing)
    {
        this.showing = showing;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
