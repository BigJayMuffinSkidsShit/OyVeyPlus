package me.muffin.oyveyplus.impl.modules.combat;

import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.settings.Setting;
import me.muffin.oyveyplus.api.utils.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Surround extends Module {
    public Surround() {
        super ("Surround", "surround", Category.Combat);
    }
	public Setting<Boolean> packet = this.register("Packet", true);
	public Setting<Boolean> rotate = this.register("Rotate",  true);
	public Setting<Boolean> center = this.register("Center",  true);
	
    public static boolean isPlacing = false;
    
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet <>();
    private final Map<BlockPos, Integer> retries = new HashMap <>();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private int obbySlot = -1;
    private boolean offHand = false;

    @Override
    public void onEnable() {
        if (this == null) {
            disable();
        }
        if(center.getValue())
        	PlayerUtil.centerMotion();
        lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        startPos = EntityUtil.getRoundedBlockPos(Surround.mc.player);
        retries.clear();
        retryTimer.reset();
    }

    @Override
    public void onUpdate() {
    	
        feetPlace();
       
    }

    @Override
    public void onDisable() {
        if (this == null) {
            return;
        }
        isPlacing = false;
        isSneaking = EntityUtil.stopSneaking(isSneaking);
    }

    private void feetPlace()
    {
    	
    	BlockPos floored_pos = new BlockPos(Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5);
    	
    	for(BlockPos b : BlockUtil.getFriends(floored_pos))
    	{
    		if(BlockUtil.canPlaceBlock(b))
    			BlockUtil.placeBlock(b, EnumHand.MAIN_HAND, rotate.getValue(), rotate.getValue(), true);
    		//	BlockUtil.placeBlock(b, EnumHand.MAIN_HAND, true, true, true);
    	}
    }


    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtil.getUnsafeBlockArray(Surround.mc.player, 0, true)) {
                if (!vec3d.equals(pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return Surround.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }

 

}
