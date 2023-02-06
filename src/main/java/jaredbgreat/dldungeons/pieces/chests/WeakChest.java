package jaredbgreat.dldungeons.pieces.chests;

import java.util.Random;

import jaredbgreat.dldungeons.builder.AbstractBlock;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;

/**
 * Represents a weak / junk chest found in rooms without spawners and containing
 * small amounts of starter materials.
 * 
 * @author Jared Blackburn
 *
 */
public class WeakChest extends BasicChest {
	
	
	public WeakChest(int x, int y, int z, LootCategory category) {
		super(x, y, z, 0, category);		
	}
	
	@Override
	public void place(ISeedReader world, int x, int y, int z, Random random) {
		BlockPos pos = new BlockPos(x, y, z);
		ChestTileEntity contents = (ChestTileEntity)world.getBlockEntity(pos);
		if(world.getBlockState(pos).getBlock() != AbstractBlock.chest) {
			System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at " 
									+ x + ", " + y + ", " + z + " (basic chest).");
			return;
		}
		if(random.nextBoolean()) {
			if(random.nextBoolean()) fillChest(contents, LootType.GEAR, random);
			else fillChest(contents, LootType.HEAL, random);
		} else {
			fillChest(contents, LootType.GEAR, random);
			fillChest(contents, LootType.HEAL, random);
		}
	}

}
