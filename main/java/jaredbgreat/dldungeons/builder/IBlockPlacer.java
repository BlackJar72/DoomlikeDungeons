package jaredbgreat.dldungeons.builder;

import net.minecraft.world.World;

public interface IBlockPlacer {

	/**
	 * This is the same as just place(), and will use the full block state.  It 
	 * is maintained for compatibility, but no longer has a real purpose.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public abstract void placeNoMeta(World world, int x, int y, int z);

	/**
	 * Places the block in the world, including its correct meta-data.  This wrapping allow
	 * for changes in block / state representation and method signatures to be easily
	 * adapted and for meta-blocks to easily be used in dungeons. 
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public abstract void place(World world, int x, int y, int z);

}