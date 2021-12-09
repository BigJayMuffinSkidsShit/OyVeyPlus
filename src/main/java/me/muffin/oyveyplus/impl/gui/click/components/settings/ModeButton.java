package me.muffin.oyveyplus.impl.gui.click.components.settings;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.impl.gui.click.Component;
import me.muffin.oyveyplus.impl.gui.click.components.ModuleButton;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class ModeButton extends Component {

    private final Setting<String> setting;
    private final ModuleButton button;
    private boolean isHovered;
    private int offset;
    private int x;
    private int y;
    private int modeIndex;
    private Minecraft mc;

    public ModeButton(final Setting<String> setting, final ModuleButton button, final int offset) {
        this.setting = setting;
        this.button = button;
        this.x = button.frame.getX() + button.frame.getWidth();
        this.y = button.frame.getY() + button.offset;
        this.offset = offset;
        this.modeIndex = 0;
    }

    @Override
    public void setOffset(final int offset) {
        this.offset = offset;
    }

    @Override
    public void mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (isHovered(mouseX, mouseY) && this.button.open) {
            final int maxIndex = setting.getModes().size() - 1;
            if(button == 0) {
                ++modeIndex;
                if (modeIndex > maxIndex) {
                    modeIndex = 0;
                }
                setting.setValue(setting.getModes().get(modeIndex));
            }
            if (button == 1) {
                --modeIndex;
                if (modeIndex < 0) {
                    modeIndex = maxIndex;
                }
                setting.setValue(setting.getModes().get(modeIndex));
            }
        }
    }

    @Override
    public void updateComponent(final double mouseX, final double mouseY) {
        isHovered = isHovered(mouseX, mouseY);
        y = button.frame.getY() + this.offset;
        x = button.frame.getX();
    }

    @Override
    public void render() {
        OyVeyPlus.gui.drawGradient(button.frame.getX(), button.frame.getY() + offset, button.frame.getX() + button.frame.getWidth(), button.frame.getY() + offset + 16, isHovered ? new Color(0, 0, 0, 75).getRGB() : new Color(0, 0, 0, 60).getRGB(), isHovered ? new Color(0, 0, 0, 75).getRGB() : new Color(0, 0, 0, 60).getRGB());
        OyVeyPlus.textManager.drawStringWithShadow(setting.getName(), button.frame.getX() + 5, button.frame.getY() + offset + 1, -1);
        OyVeyPlus.textManager.drawStringWithShadow(setting.getValue(),  button.frame.getX() + button.frame.getWidth() - 5 - OyVeyPlus.textManager.getStringWidth(setting.getValue()), button.frame.getY() + offset + 1, -1);
    }

    public boolean isHovered(final double x, final double y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16;
    }

}
