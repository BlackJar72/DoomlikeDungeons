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
	protected final String name;
	
	
	DBlock(BlockState block, String name) {
		this.block = block;
		this.name = name;
	}

	
	@Override
	public void place(WorldGenLevel world, BlockPos pos) {
		if(isProtectedBlock(world, pos)) return;
		world.setBlock(pos, block, 2);
	}

	@Override
	public String getName() {
		return name;
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
			StringTokenizer nums = new StringTokenizer(id, ":({[]})");
			String modid = nums.nextToken();
			ResourceLocation name = new ResourceLocation(modid
					+ ":" + nums.nextToken());
			theBlock = BuiltInRegistries.BLOCK.get(name);
			if(theBlock == null) {
				String error = "[DLDUNGEONS] ERROR! Block read as \"" + id 
						+ "\" was was not in registry (returned null).";
				throw new NoSuchElementException(error);
			}
			return new DBlock(theBlock.defaultBlockState(), name.toString());
		} catch (NoSuchElementException ex) {
			throw new NoSuchElementException("Something was wrong with " + id 
					+ "; could not find all elements.");
		}
	}


}