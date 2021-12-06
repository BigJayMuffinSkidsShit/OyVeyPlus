package me.muffin.oyveyplus.api.utils.advanced;

import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;

public class RenderPath {
	public static ArrayList<BlockPos> path = new ArrayList <>();
	public static Color color;
	
	public static void setPath(ArrayList<BlockPos> path, Color color) {
		RenderPath.path = path;
		RenderPath.color = color;
	}
	
	public static void clearPath() {
		path.clear();
	}
}
