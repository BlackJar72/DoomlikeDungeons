package jaredbgreat.dldungeons.builder;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;

public abstract class AbstractBlock implements IBlockPlacer {	
	
	public static final Block spawner = (Block)Blocks.SPAWNER;
	public static final Block chest   = (Block)Blocks.CHEST;
	public static final Block portal1 = (Block)Blocks.END_PORTAL_FRAME;
	public static final Block portal2 = (Block)Blocks.END_PORTAL;
	public static final Block bedrock = Blocks.BEDROCK;
	public static final int chestid   = Block.getId(chest.defaultBlockState());
	public static final int spawnerid = Block.getId(spawner.defaultBlockState());
	public static final int portal1id = Block.getId(portal1.defaultBlockState());
	public static final int portal2id = Block.getId(portal2.defaultBlockState());
	public static final int bedrockId = Block.getId(bedrock.defaultBlockState());

	/**
	 * True if the block is one that should not be built over; specifically a chest,
	 * spawner, or any part of the End portal.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static boolean isProtectedBlock(ISeedReader world, int x, int y, int z) {
		int block = Block.getId(world.getBlockState(new BlockPos(x, y, z)));
		return (block == chestid || block == spawnerid  
				|| block == portal1id || block == portal2id || block == bedrockId);
	}
	
	
	

}