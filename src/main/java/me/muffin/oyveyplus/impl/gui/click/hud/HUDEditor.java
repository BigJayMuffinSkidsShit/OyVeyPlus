package me.muffin.oyveyplus.impl.gui.click.hud;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.module.ModuleManager;
import me.muffin.oyveyplus.api.utils.ColorUtil;
import me.muffin.oyveyplus.impl.gui.click.ClickGui;
import me.muffin.oyveyplus.impl.modules.client.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class HUDEditor extends GuiScreen
{
    private boolean dragging;
    private int dragX;
    private int dragY;
    private HudComponent dragComponent;
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (HudComponent component : HudComponentManager.INSTANCE.getComponents())
        {
            if (dragging && dragComponent.equals(component)) {
                component.setX(mouseX - dragX);
                component.setY(mouseY - dragY);
            }

            if (!OyVeyPlus.hudComponentManager.getComponents().equals(component.getName()))
                continue;
            new HudComponent("OyVeyPlus HudEditor");
            //drawRect(component.getX(), component.getY(), component.getX(), component.getY(), 0xFFFF);
            ClickGui.drawRect(10, 200, component.getX() + component.getW() + 2, component.getY() + component.getH() + 2, isHover(component.getX(), component.getY(), component.getW(), component.getH(), mouseX, mouseY) ? ColorUtil.toRGBA(Gui.instance.red.getValue.intValue(), Gui.instance.green.getValue.intValue(), Gui.instance.blue.getValue.intValue()) : ColorUtil.toRGBA(Gui.instance.red.getValue.intValue(), Gui.instance.green.getValue.intValue(), Gui.instance.blue.getValue.intValue(), 255));
            component.render();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        for (HudComponent component : HudComponentManager.INSTANCE.getComponents())
        {
            if (OyVeyPlus.hudComponentManager.getHudComponent(component.getName()).isEnabled() && isHover(component.getX() - 2, component.getY() - 2, component.getW() + 2, component.getH() + 2, mouseX, mouseY))
            {
                dragComponent = component;
                dragging = true;

                dragX = mouseX - component.getX();
                dragY = mouseY - component.getY();
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        dragging = false;
        dragComponent = null;
    }

    @Override
    public void onGuiClosed()
    {
        dragComponent = null;
        dragging = false;

        OyVeyPlus.moduleManager.getModule("HUDEditor").disable();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private boolean isHover(int X, int Y, int W, int H, int mX, int mY)
    {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
