package jaredbgreat.dldungeons.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

public interface IBlockPlacer {

	/**
	 * Places the block in the world, including its correct meta-data.  This wrapping allow
	 * for changes in block / state representation and method signatures to be easily
	 * adapted and for meta-blocks to easily be used in dungeons. 
	 * 
	 * @param world
	 * @param pos
	 */
	public abstract void place(WorldGenLevel world, BlockPos pos);

	public abstract String getName();


}