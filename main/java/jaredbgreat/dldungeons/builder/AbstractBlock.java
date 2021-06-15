package jaredbgreat.dldungeons.builder;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractBlock implements IBlockPlacer {	
	
	public static final Block spawner = (Block)Block.getBlockFromName("mob_spawner");
	public static final Block chest   = (Block)Block.getBlockFromName("chest");
	public static final Block portal1 = (Block)Block.getBlockFromName("end_portal_frame");
	public static final Block portal2 = (Block)Block.getBlockFromName("end_portal");
	public static final Block bedrock = Blocks.BEDROCK;
	public static final int chestid   = Block.getIdFromBlock(chest);
	public static final int spawnerid = Block.getIdFromBlock(spawner);
	public static final int portal1id = Block.getIdFromBlock(portal1);
	public static final int portal2id = Block.getIdFromBlock(portal2);
	public static final int bedrockId = Block.getIdFromBlock(bedrock);

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
	public static boolean isProtectedBlock(World world, int x, int y, int z) {
		int block = Block.getIdFromBlock(world.getBlockState(new BlockPos(x, y, z)).getBlock());
		return (block == chestid || block == spawnerid  
				|| block == portal1id || block == portal2id || block == bedrockId);
	}
	
	
	

}