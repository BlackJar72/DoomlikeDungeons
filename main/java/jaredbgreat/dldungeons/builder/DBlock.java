package jaredbgreat.dldungeons.builder;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import jaredbgreat.dldungeons.util.debug.Logging;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DBlock extends AbstractBlock {
	protected BlockState block;
	
	
	DBlock(BlockState block) {
		this.block = block;
	}
	
	
	@Override
	@Deprecated
	public void placeNoMeta(ISeedReader world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		BlockPos pos = new BlockPos(x, y, z);
		//if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDBlock(world, pos, this))) return;
		world.setBlock(pos, block, 3);
	}

	
	@Override
	public void place(ISeedReader world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		BlockPos pos = new BlockPos(x, y, z);
		//if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDBlock(world, pos, this))) return;
		world.setBlock(pos, block, 3);
	}
	
	
	/**
	 * Returns true if the other object is a DBlock the holds the same block with 
	 * the same meta-data. 
	 */
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof DBlock)) return false; 
		return (block.equals(((DBlock)other).block));
	}
	
	
	/**
	 * Returns a hash code derived from the block id and meta data; it will only produce 
	 * the same hash code if these are both equal, that is, equal hash codes implies equals() 
	 * is true. 
	 */
	@Override
	public int hashCode() {
		return block.hashCode();
	}
	
	
	public static DBlock makeDBlock(String id) {
		try {
			Block theBlock;
			StringTokenizer nums = new StringTokenizer(id, ":");
			String modid = nums.nextToken();
			ResourceLocation name = new ResourceLocation(modid 
					+ ":" + nums.nextToken());
			theBlock = GameRegistry.findRegistry(Block.class).getValue(name);
			if(theBlock == null) {
				String error = "[DLDUNGEONS] ERROR! Block read as \"" + id 
						+ "\" was was not in registry (returned null).";
				Logging.logError(error);
				throw new NoSuchElementException(error);
			}
			return new DBlock(theBlock.defaultBlockState());
		} catch (NoSuchElementException ex) {
			throw new NoSuchElementException("Something was wrong with " + id 
					+ "; could not find all elements.");
		}
	}


	@Override
	public Object getContents() {
		return block;
	}

}