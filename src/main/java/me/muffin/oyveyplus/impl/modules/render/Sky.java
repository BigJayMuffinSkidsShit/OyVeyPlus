package me.muffin.oyveyplus.impl.modules.render;

import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Sky extends Module {
    private Setting<Double> red = register("Red", 255, 0, 255,0);
    private Setting<Double> green = register("Green", 255, 0, 255,0);
    private Setting<Double> blue = register("Blue", 255, 0, 255,0);
    private Setting<Boolean> rainbow = register("Rainbow", true);

    private static Sky INSTANCE = new Sky();

    public Sky() {
        super("SkyColor", "Changes the color of the fog", Category.Render);
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Sky getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Sky();
        return INSTANCE;
    }

    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        event.setRed(red.getValue().intValue() / 255f);
        event.setGreen(green.getValue().intValue() / 255f);
        event.setBlue(blue.getValue().intValue() / 255f);
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.0f);
        event.setCanceled(true);
    }

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void onUpdate() {
        if (rainbow.getValue()) {
            doRainbow();
        }
    }

    public void doRainbow() {

        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        red.setValue((double) ((color_rgb_o >> 16) & 0xFF));
        green.setValue((double) ((color_rgb_o >> 8) & 0xFF));
        blue.setValue((double) (color_rgb_o & 0xFF));
    }

}
