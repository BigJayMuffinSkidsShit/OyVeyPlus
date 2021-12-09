package me.muffin.oyveyplus.api.utils.advanced;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;

public class RenderBlock {
	public static ArrayList<BlockColor> list1 = new ArrayList<BlockColor>();
	public static ArrayList<BlockColor> list2 = new ArrayList<BlockColor>();
	
	public static void add(BlockPos pos, Color color, float width, int a) {
		if(a==1)
		{
			list1.add(new BlockColor(pos, color, width));
		}
		if(a==2)
		{
			list2.add(new BlockColor(pos, color, width));
		}
	}
	
	public static void remove(BlockPos pos) {
		ArrayList<BlockColor> remove = new ArrayList<BlockColor>();
		for (BlockColor blockColor : list1) {
			if (blockColor.pos.equals(pos)) {
				remove.add(blockColor);
			}
		}
		
		list1.removeAll(remove);
	}
	
	public static void clear(int a) {
		if(a==1)
		{
			list1.clear();
		}
		if(a==2)
		{
			list2.clear();
		}

	}
	
	public static class BlockColor {
		public BlockPos pos;
		public Color color;
		public float width;
		public double r = 0;
		public double g = 0;
		public double b = 0;
		public double a = 0;
		public BlockColor(BlockPos pos, Color color, float width) {
			this.pos = pos;
			this.color = color;
			this.width = width;
		}
		
		public BlockColor(BlockPos pos, float width,double r,double g,double b,double a) {
			this.pos = pos;
			this.color = color;
			this.width = width;
			this.r=r;
			this.g=g;
			this.b=b;
			this.a=a;
		}
	}
}
