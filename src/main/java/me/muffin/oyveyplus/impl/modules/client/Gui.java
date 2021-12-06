package me.muffin.oyveyplus.impl.modules.client;

import me.muffin.oyveyplus.OyVeyPlus;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class Gui extends Module {
    public static Gui instance;

    public Gui() {
        super("Gui", "mod menu + management i guess", Category.Client);
        setKey(Keyboard.KEY_RCONTROL);
        instance = this;
    }
    public static double scrollspeed=10;
    public Setting<Boolean> particles = register("Particles",true);
    public Setting<Boolean> blur = this.register("Blur",true);
    public Setting<Double> scrollSpeed = this.register("ScrollSpeed",10,0,50,0);
    public Setting<Double> red = register("Red", 255, 0, 255, 0);
    public Setting<Double> green = register("Green", 0, 0, 255, 0);
    public Setting<Double> blue = register("Blue", 255, 0, 255, 0);
    public Setting<Double> rainbowHue = this.register("Rainbow H",255,0,255, 0);
    public Setting<Double> rainbowBrightness = this.register("Rainbow B",255,0,255,0);
    public Setting<Double> rainbowSaturation = this.register("Rainbow S",255,0,255,0);
   
    @Override
    public void onEnable() {
        mc.displayGuiScreen(OyVeyPlus.gui);
        if (this.blur.getValue) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));

        }
        disable();
    }

    @Override
    public void onTick() {
            if (mc.entityRenderer.getShaderGroup() != null) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
}