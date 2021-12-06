package me.muffin.oyveyplus.api.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import java.util.ArrayList;

public class PlayerUtil{
	private static final PlayerUtil playerUtil = new PlayerUtil();
	static Minecraft mc = Minecraft.getMinecraft();
	
	
	public static boolean centerMotion() {
		if (isCentered()) {
			return true;
		}
		
		double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
		mc.player.motionX = (centerPos[0] - mc.player.posX) / 2;
		mc.player.motionZ = (centerPos[2] - mc.player.posZ) / 2;
		return false;
	}
	
	/**
	 * Gets all the players the client knows except yourself.
	 */
	public static ArrayList<EntityPlayer> getAll() {
		try {
			ArrayList<EntityPlayer> players = new ArrayList <>();
			
			for (EntityPlayer player : mc.world.playerEntities) {
				if (!player.isEntityEqual(mc.player)) {
					players.add(player);
				}
			}
			
			return players;
		} catch (NullPointerException ignored) {
			return new ArrayList <>();
		}
	}
	
	/**
	 * Checks if the player is centered on the block
	 */
	public static boolean isCentered() {
		double[] centerPos = {Math.floor(mc.player.posX) + 0.5, Math.floor(mc.player.posY), Math.floor(mc.player.posZ) + 0.5};
		return Math.abs(centerPos[0] - mc.player.posX) <= 0.1 && Math.abs(centerPos[2] - mc.player.posZ) <= 0.1;
	}
	
	/**
	 * Gets a player with the given name
	 */
	public static EntityPlayer getPlayer(String name) {
		for (EntityPlayer player : getAll()) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the closest player
	 */
	public static EntityPlayer getClosest(float mindistance, float maxdistance) {
		double lowestDistance = Integer.MAX_VALUE;
		EntityPlayer closest = null;
		
		for (EntityPlayer player : getAll()) {
			if(mindistance != 0 && maxdistance!=0)
			{
				if (player.getDistance(mc.player) <= maxdistance && player.getDistance(mc.player) >= mindistance) {
					lowestDistance = player.getDistance(mc.player);
					closest = player;
				}
			}else {
				lowestDistance = player.getDistance(mc.player);
				closest = player;
			}

		}
		
		return closest;
	}
	
	
	public static BlockPos getClosestPos(ArrayList<BlockPos> b) {
		double lowestDistance = Integer.MAX_VALUE;
		BlockPos closest = null;
		
		for (BlockPos pos : b) {
			if(closest!=null) 
			{
				if(BlockUtil.distance(pos, mc.player.getPosition())<BlockUtil.distance(pos, closest))
				{
					lowestDistance = BlockUtil.distance(pos, mc.player.getPosition());
					closest = pos;
				}	
			}else {
				closest = pos;
			}
			}

		
		
		return closest;
	}
    public static BlockPos getPosition()
    {
        return new BlockPos(mc.player.getPosition().getX(), mc.player.getPosition().getY(), mc.player.getPosition().getZ());
    }
	
	/**
	 * Checks if these 2 players are in the same position
	 * @y how much the y difference can be
	 */
	public static boolean isInSameBlock(EntityPlayer player, EntityPlayer other, int y) {
		BlockPos first = new BlockPos((int)player.posX, (int)player.posY, (int)player.posZ);
		BlockPos second = new BlockPos((int)other.posX, (int)other.posY, (int)other.posZ);
		
		return first.getX() == second.getX() && Math.abs(first.getY() - second.getY()) <= y && first.getZ() == second.getZ();
	}
	
	public static boolean isTheSameBlock(BlockPos one, BlockPos two) {
		return Math.abs(one.getX()) == Math.abs(one.getX()) && Math.abs(one.getZ()) == Math.abs(one.getZ());
	}
	
	/**
	 * Makes the player do a right click
	 * Its run on the client thread because otherwise it yeets all kinds of exceptions
	 */
	public static void rightClick() {
		MinecraftForge.EVENT_BUS.register(playerUtil);
	}
	
	/**
	 * Gets the speed the given entity is moving at
	 * Like the difference between current and last tick position
	 */
	public static double getSpeed(Entity entity) {
		return new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ).distanceTo(new Vec3d(mc.player.lastTickPosX, mc.player.lastTickPosY, mc.player.lastTickPosZ));
	}
	
	public static boolean isMoving(Entity entity) {
		return getSpeed(entity) == 0;
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
		MinecraftForge.EVENT_BUS.unregister(playerUtil);
	}
}
