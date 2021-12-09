package me.muffin.oyveyplus.api.manager;

import me.muffin.oyveyplus.api.utils.Timer;
import me.muffin.oyveyplus.api.wrapper.Wrapper;

public class TextManager implements Wrapper {
    private final Timer idleTimer = new Timer();
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private boolean idling;
    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public float drawString(String text, float x, float y, int color, boolean shadow) {
        TextManager.mc.fontRenderer.drawString(text, x, y, color, shadow);
        return x;
    }

    public int getStringWidth(String text) {
        return TextManager.mc.fontRenderer.getStringWidth(text);
    }

    public int getFontHeight() {
        return TextManager.mc.fontRenderer.FONT_HEIGHT;
    }


}
