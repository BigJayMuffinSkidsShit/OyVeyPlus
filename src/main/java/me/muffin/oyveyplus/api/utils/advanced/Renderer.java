package me.muffin.oyveyplus.api.utils.advanced;

import java.awt.Color;
import java.util.ArrayList;

import me.muffin.oyveyplus.api.utils.RenderUtill;
import me.muffin.oyveyplus.api.utils.advanced.RenderBlock.BlockColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Renderer{
	static Minecraft mc = Minecraft.getMinecraft();
	static int delay=0;
	public static String[] status;
	public static ArrayList<BlockColor> rectangles = new ArrayList<BlockColor>();
	public static ArrayList<BlockColor> filledBoxes = new ArrayList<BlockColor>();
	public static ArrayList<BlockColor> customBoxes = new ArrayList<BlockColor>();
	
	@SubscribeEvent
	public void renderText(RenderGameOverlayEvent.Text e) {
		//Draw status
		if (status != null) {
			for (int i = 0; i < status.length; i++) {
				if (status[i] == null) {
					continue;
				}
				
				GuiScreen.drawRect(((mc.displayWidth / 4) - (mc.fontRenderer.getStringWidth(status[i]) / 2)) - 3, (i * 10) - 1, ((mc.displayWidth / 4) + (mc.fontRenderer.getStringWidth(status[i]) / 2)) + 3, (i + 1) * 10, 0xFF000000);
				GuiScreen.drawRect(((mc.displayWidth / 4) - (mc.fontRenderer.getStringWidth(status[i]) / 2)) - 3, (i * 10) + 9, ((mc.displayWidth / 4) + (mc.fontRenderer.getStringWidth(status[i]) / 2)) + 3, (i + 1) * 10, 0xFF27f5be);
				GuiScreen.drawRect(((mc.displayWidth / 4) - (mc.fontRenderer.getStringWidth(status[i]) / 2)) - 3, (i * 10) - 1, ((mc.displayWidth / 4) - (mc.fontRenderer.getStringWidth(status[i]) / 2)) - 2, (i + 1) * 10, 0xFF27f5be);
				GuiScreen.drawRect(((mc.displayWidth / 4) + (mc.fontRenderer.getStringWidth(status[i]) / 2)) + 2, (i * 10) - 1, ((mc.displayWidth / 4) + (mc.fontRenderer.getStringWidth(status[i]) / 2)) + 3, (i + 1) * 10, 0xFF27f5be);
				mc.fontRenderer.drawString(status[i], (mc.displayWidth / 4) - (mc.fontRenderer.getStringWidth(status[i]) / 2), i * 10, 0xFF000000);
			}
		}
	}
	
	@SubscribeEvent
	public void renderWorld(RenderWorldLastEvent event) {
		try {
			//Render path
			BlockPos last = null;
			for (BlockPos pos : RenderPath.path) {
				if (last != null) {
					RenderUtil.drawLine(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), new Vec3d(last.getX() + 0.5, last.getY() + 0.5, last.getZ() + 0.5), 2, RenderPath.color);
				}
				
				last = pos;
			}

				for (BlockColor blockColor : RenderBlock.list1) {
					Color c = blockColor.color;
					RenderUtil.drawBoundingBox(RenderUtil.getBB(blockColor.pos), blockColor.width, c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, 1f);
				}

			//Render block bounding box

			
			for (BlockColor blockColor : RenderBlock.list2) {
				Color c = blockColor.color;
				RenderUtil.drawBoundingBox(RenderUtil.getBB(blockColor.pos), blockColor.width, c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, 1f);
			}
			//Render 2d rectangles
			for (BlockColor rectangle : rectangles) {
				Color c = rectangle.color;
				RenderUtil.draw2DRec(RenderUtil.getBB(rectangle.pos), rectangle.width, c.getRed() / 255, c.getGreen() / 255, c.getBlue() / 255, 1f);
			}
			
			//Render FilledBoxes
			for (BlockColor FilledBox : filledBoxes) {
				Color c = FilledBox.color;
			RenderUtil.drawBoxESP(FilledBox.pos, FilledBox.color, 100, false, true, 100);
				
			}
			
			//Render CustomBoxes
			for (BlockColor CustomBox : customBoxes) {
				Color c = CustomBox.color;
			RenderUtill.drawBox(CustomBox.pos, CustomBox.color, CustomBox.h, false, false, 100);
				
			}
			
		} catch (Exception ignored) {
			
		}
	}
}
