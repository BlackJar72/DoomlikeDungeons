package jaredbgreat.dldungeons.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

public abstract class AbstractBlock implements IBlockPlacer {

	/**
	 * True if the block is one that should not be built over; specifically a chest,
	 * spawner, or any part of the End portal.
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	public static boolean isProtectedBlock(WorldGenLevel world, BlockPos pos) {
		return world.getBlockState(pos).getDestroySpeed(world, pos) < 0; // Unbreakable block
	}
	
	
	

}