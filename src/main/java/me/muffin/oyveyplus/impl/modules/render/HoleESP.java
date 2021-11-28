package me.muffin.oyveyplus.impl.modules.render;

import me.muffin.oyveyplus.api.event.events.Render3DEvent;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.BlockUtil;
import me.muffin.oyveyplus.api.utils.ColorUtil;
import me.muffin.oyveyplus.api.utils.RenderUtil;
import me.muffin.oyveyplus.impl.modules.client.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class HoleESP extends Module {
    public Setting<Boolean> renderOwn = register("RenderOwn", true);
    public Setting<Boolean> fov = register("InFov", true);
    public Setting<Boolean> rainbow = register("Rainbow", false);
    private final Setting<Double> range = register("RangeX", 0, 0, 10,0);
    private final Setting<Double> rangeY = register("RangeY", 0, 0, 10,0);
    public Setting<Boolean> box = register("Box", true);
    public Setting<Boolean> gradientBox = register("Gradient", Boolean.valueOf(false));
    public Setting<Boolean> invertGradientBox = register("ReverseGradient", Boolean.valueOf(false));
    public Setting<Boolean> outline = register("Outline", true);
    public Setting<Boolean> gradientOutline = register("GradientOutline", Boolean.valueOf(false));
    public Setting<Boolean> invertGradientOutline = register("ReverseOutline", Boolean.valueOf(false));
    public Setting<Double> height = register("Height", 0.0, -2.0, 2.0,1);
    private Setting<Double> red = register("Red", 0, 0, 255,0);
    private Setting<Double> green = register("Green", 255, 0, 255,0);
    private Setting<Double> blue = register("Blue", 0, 0, 255,0);
    private Setting<Double> alpha = register("Alpha", 255, 0, 255,0);
    private Setting<Double> boxAlpha = register("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> lineWidth = register("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f),1);
    public Setting<Boolean> safeColor = register("BedrockColor", false);
    private Setting<Double> safeRed = register("BedrockRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> safeGreen = register("BedrockGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> safeBlue = register("BedrockBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> safeAlpha = register("BedrockAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255),0);
    public Setting<Boolean> customOutline = register("CustomLine", Boolean.valueOf(false));
    private Setting<Double> cRed = register("OL-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> cGreen = register("OL-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> cBlue = register("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> cAlpha = register("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> safecRed = register("OL-SafeRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> safecGreen = register("OL-SafeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> safecBlue = register("OL-SafeBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255),0);
    private Setting<Double> safecAlpha = register("OL-SafeAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255),0);
    private static HoleESP INSTANCE = new HoleESP();
    private int currentAlpha = 0;

    public HoleESP() {
        super("HoleESP", "Shows safe spots.", Category.Render);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static HoleESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        assert (HoleESP.mc.getRenderViewEntity() != null);
        Vec3i playerPos = new Vec3i(HoleESP.mc.getRenderViewEntity().posX, HoleESP.mc.getRenderViewEntity().posY, HoleESP.mc.getRenderViewEntity().posZ);
        for (int x = playerPos.getX() - range.getValue().intValue(); x < playerPos.getX() + range.getValue(); ++x) {
            for (int z = playerPos.getZ() - range.getValue().intValue(); z < playerPos.getZ() + range.getValue(); ++z) {
                for (int y = playerPos.getY() + rangeY.getValue().intValue(); y > playerPos.getY() - rangeY.getValue(); --y) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) || pos.equals(new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) && !renderOwn.getValue().booleanValue() || !BlockUtil.isPosInFov(pos).booleanValue() && fov.getValue().booleanValue())
                        continue;
                    if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                        RenderUtil.drawBoxESP(pos, rainbow.getValue() ? ColorUtil.rainbow(Gui.instance.rainbowHue.getValue().intValue()) : new Color(safeRed.getValue().intValue(), safeGreen.getValue().intValue(), safeBlue.getValue().intValue(), safeAlpha.getValue().intValue()), customOutline.getValue(), new Color(safecRed.getValue().intValue(), safecGreen.getValue().intValue(), safecBlue.getValue().intValue(), safecAlpha.getValue().intValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue().intValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), currentAlpha);
                        continue;
                    }
                    if (!BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock()))
                        continue;
                    RenderUtil.drawBoxESP(pos, rainbow.getValue() ? ColorUtil.rainbow(Gui.instance.rainbowHue.getValue().intValue()) : new Color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue(), alpha.getValue().intValue()), customOutline.getValue(), new Color(cRed.getValue().intValue(), cGreen.getValue().intValue(), cBlue.getValue().intValue(), cAlpha.getValue().intValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue().intValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), currentAlpha);
                }
            }
        }
    }
}
