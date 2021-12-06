package me.muffin.oyveyplus.impl.modules.render;

import me.muffin.oyveyplus.api.event.events.Render3DEvent;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.BlockUtil;
import me.muffin.oyveyplus.api.utils.ColorUtil;
import me.muffin.oyveyplus.api.utils.advanced.RenderBlock.BlockColor;
import me.muffin.oyveyplus.api.utils.advanced.Renderer;
import me.muffin.oyveyplus.impl.modules.client.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;


public class HoleESP extends Module {
    public Setting<String> type = this.register("Type", "Solid","Outline","Solid","Gradient");
    public Setting<Double> range = this.register("Range",10,1,15,0);
    public Setting<Boolean> rainbow = this.register("Rainbow",false);
    public Setting<Boolean> fov = this.register("Fov",true);
    public Setting<Boolean> renderown = this.register("Render Own",true);
    public Setting<Double> safeRed = this.register("Safe Red",0,0,255,0);
    public Setting<Double> safeGreen = this.register("Safe Green",255,0,255,0);
    public Setting<Double> safeBlue = this.register("Safe Blue",0,0,255,0);
    public Setting<Double> safeAlpha = this.register("Safe Alpha",180,0,255,0);
    public Setting<Double> red = this.register("Red",255,0,255,0);
    public Setting<Double> green = this.register("Green",0,0,255,0);
    public Setting<Double> blue = this.register("Blue",0,0,255,0);
    public Setting<Double> alpha = this.register("Aplha",180,0,255,0);
    private static HoleESP INSTANCE = new HoleESP();
    private final int currentAlpha = 0;
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
	                    if (!HoleESP.mc.world.getBlockState(b).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(b.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(b.add(0, 2, 0)).getBlock().equals(Blocks.AIR) || b.equals(new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) && !renderown.getValue() || !BlockUtil.isPosInFov(b) && fov.getValue())
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
