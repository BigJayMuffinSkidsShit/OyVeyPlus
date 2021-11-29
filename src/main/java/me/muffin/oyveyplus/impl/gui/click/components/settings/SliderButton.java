package me.muffin.oyveyplus.impl.gui.click.components.settings;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.impl.gui.click.ClickGui;
import me.muffin.oyveyplus.impl.gui.click.Component;
import me.muffin.oyveyplus.impl.gui.click.components.ModuleButton;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderButton extends Component {

    private final Setting<Double> setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;
    private boolean dragging;
    private double renderWidth;
    private Minecraft mc;

    public SliderButton(final Setting<Double> setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.frame.getX() + button.frame.getWidth();
        this.y = button.frame.getY() + button.offset;
        this.offset = offset;
    }

    @Override
    public void setOffset(final int offset) {
        this.offset = offset;
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY) && button == 0 && this.button.open) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(final double mouseX, final double mouseY, final int mouseButton) {
        dragging = false;
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        y = button.frame.getY() + offset;
        x = button.frame.getX();
        final double diff = Math.min(88, Math.max(0, mouseX - x));
        final double min = setting.getMin();
        final double max = setting.getMax();
        renderWidth = 88 * (setting.getValue() - min) / (max - min);
        if (dragging) {
            if (diff == 0) setting.setValue(setting.getMin());
            else {
                final double newValue = round(diff / 88 * (max - min) + min, setting.getDecimal());
                setting.setValue(newValue);
            }
        }
    }

    @Override
    public void render() {
        OyVeyPlus.gui.drawGradient(button.frame.getX(), button.frame.getY() + offset, button.frame.getX() + button.frame.getWidth(), button.frame.getY() + offset + 16, isHovered ? new Color(0, 0, 0, 75).getRGB() : new Color(0, 0, 0, 60).getRGB(), isHovered ? new Color(0, 0, 0, 75).getRGB() : new Color(0, 0, 0, 60).getRGB());
        OyVeyPlus.gui.drawGradient(button.frame.getX(), button.frame.getY() + offset, (int) (button.frame.getX() + renderWidth), button.frame.getY() + offset + 16, isHovered ? ClickGui.color.darker().getRGB() : ClickGui.color.getRGB(), isHovered ? ClickGui.color.darker().getRGB() : ClickGui.color.getRGB());
        OyVeyPlus.textManager.drawStringWithShadow(setting.getName(),  button.frame.getX() + 5, button.frame.getY() + offset + 1, isHovered ? new Color(170, 170, 170).getRGB() : -1);
        OyVeyPlus.textManager.drawStringWithShadow(String.valueOf(round(setting.getValue(), setting.getDecimal())), button.frame.getX() + button.frame.getWidth() - 2 - OyVeyPlus.textManager.getStringWidth(String.valueOf(round(setting.getValue(), setting.getDecimal()))), button.frame.getY() + offset + 1, isHovered ? new Color(170, 170, 170).getRGB() : -1);
    }

    private static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16;
    }

}
