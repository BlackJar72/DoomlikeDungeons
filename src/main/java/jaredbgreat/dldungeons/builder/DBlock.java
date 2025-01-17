package jaredbgreat.dldungeons.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class DBlock extends AbstractBlock {
	protected BlockState block;
	
	
	DBlock(BlockState block) {
		this.block = block;
	}
	
	
	@Override
	@Deprecated
	public void placeNoMeta(WorldGenLevel world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		BlockPos pos = new BlockPos(x, y, z);
		world.setBlock(pos, block, 2);
	}

	
	@Override
	public void place(WorldGenLevel world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		BlockPos pos = new BlockPos(x, y, z);
		world.setBlock(pos, block, 2);
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
			int meta;
			StringTokenizer nums = new StringTokenizer(id, ":({[]})");
			String modid = nums.nextToken();
			ResourceLocation name = new ResourceLocation(modid
					+ ":" + nums.nextToken());
			theBlock = BuiltInRegistries.BLOCK.getOptional(name).orElse(null);
			if(theBlock == null) {
				String error = "[DLDUNGEONS] ERROR! Block read as \"" + id 
						+ "\" was was not in registry (returned null).";
				throw new NoSuchElementException(error);
			}
			if(nums.hasMoreElements()) {
				meta = Integer.parseInt(nums.nextToken()); 
			} else {
				meta = 0;
			}
			// Hack: ignore meta
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