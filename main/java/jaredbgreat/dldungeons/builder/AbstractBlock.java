package jaredbgreat.dldungeons.builder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;

public abstract class AbstractBlock implements IBlockPlacer {	
	
	public static final Block spawner = (Block)Blocks.SPAWNER;
	public static final Block chest   = (Block)Blocks.CHEST;
	public static final Block portal1 = (Block)Blocks.END_PORTAL_FRAME;
	public static final Block portal2 = (Block)Blocks.END_PORTAL;
	public static final Block bedrock = Blocks.BEDROCK;	
	public static final BlockState spawnerst = spawner.defaultBlockState();
	public static final BlockState chestst   = chest.defaultBlockState();
	public static final BlockState portal1st = portal1.defaultBlockState();
	public static final BlockState portal2st = portal2.defaultBlockState();
	public static final BlockState bedrockst = bedrock.defaultBlockState();
	public static final int chestid   = Block.getId(chestst);
	public static final int spawnerid = Block.getId(spawnerst);
	public static final int portal1id = Block.getId(portal1st);
	public static final int portal2id = Block.getId(portal2st);
	public static final int bedrockId = Block.getId(bedrockst);

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
		Block block = (world.getBlockState(new BlockPos(x, y, z))).getBlock();
		return (block == chest || block == spawner  
				|| block == portal1 || block == portal2 || block == bedrock);
	}
	
	
	

}