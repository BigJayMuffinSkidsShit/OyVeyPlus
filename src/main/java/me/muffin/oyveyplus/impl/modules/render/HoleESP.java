package me.muffin.oyveyplus.impl.modules.render;

import me.muffin.oyveyplus.api.event.events.Render3DEvent;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.BlockUtil;
import me.muffin.oyveyplus.api.utils.ColorUtil;
import me.muffin.oyveyplus.api.utils.RenderUtil;
import me.muffin.oyveyplus.api.utils.advanced.RenderBlock.BlockColor;
import me.muffin.oyveyplus.api.utils.advanced.Renderer;
import me.muffin.oyveyplus.impl.modules.client.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;



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
    static int delay;
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

    	super.onRender3D(event);
    }
    

    
    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        
    }


    @Override
    public void onEnable() {

    	
    	super.onEnable();
    }

    

@Override
public void Update() {
	// TODO Auto-generated method stub
	for(BlockPos b : BlockUtil.getAll(20))
	{	
	                    if (!HoleESP.mc.world.getBlockState(b).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(b.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(b.add(0, 2, 0)).getBlock().equals(Blocks.AIR) || b.equals(new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) && !renderOwn.getValue().booleanValue() || !BlockUtil.isPosInFov(b).booleanValue() && fov.getValue().booleanValue())
	                        continue;
	                    if (HoleESP.mc.world.getBlockState(b.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(b.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(b.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(b.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(b.down()).getBlock() == Blocks.BEDROCK) {
	                    	
	                    	Renderer.filledBoxes.add(new BlockColor(b, rainbow.getValue() ? ColorUtil.rainbow(Gui.instance.rainbowHue.getValue().intValue()) : new Color(safeRed.getValue().intValue(), safeGreen.getValue().intValue(), safeBlue.getValue().intValue(), safeAlpha.getValue().intValue()), 3));		
	                    	continue;
	                    }
	                    if (!BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(b.down()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(b.east()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(b.west()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(b.south()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(b.north()).getBlock()))
	                        continue;
	       
	                    Renderer.filledBoxes.add(new BlockColor(b, rainbow.getValue() ? ColorUtil.rainbow(Gui.instance.rainbowHue.getValue().intValue()) : new Color(red.getValue().intValue(), green.getValue().intValue(), blue.getValue().intValue(), alpha.getValue().intValue()), 3));		
	                }
	super.Update();
}
}
