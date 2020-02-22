package jaredbgreat.dldungeons.builder;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.debug.Logging;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DBlock extends AbstractBlock {
	protected IBlockState block;
	
	
	DBlock(IBlockState block) {
		this.block = block;
	}
	
	
	@Override
	@Deprecated
	public void placeNoMeta(World world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		BlockPos pos = new BlockPos(x, y, z);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDBlock(world, pos, this))) return;
		world.setBlockState(new BlockPos(x, y, z), block);
	}

	
	@Override
	public void place(World world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		BlockPos pos = new BlockPos(x, y, z);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDBlock(world, pos, this))) return;
		world.setBlockState(pos, block);
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
		Block theBlock;
		int meta;
		StringTokenizer nums = new StringTokenizer(id, ":({[]})");
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
		if(nums.hasMoreElements()) {
			meta = Integer.parseInt(nums.nextToken()); 
		} else {
			meta = 0;
		}
		return new DBlock(theBlock.getStateFromMeta(meta));
	}


	@Override
	public Object getContents() {
		return block;
	}

}