package jaredbgreat.dldungeons.pieces.entrances;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


/**
 * The base class for all that build entrances.
 */
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class AbstractEntrance {
	
	protected static final Block ladder 
			= (Block)Block.getBlockFromName("ladder");
	protected static final Block stairSlab 
			= (Block)Block.getBlockFromName("stone_slab");	
	
	int x, z;
	
	/**
	 * Set an entrance to be built at the given coordinates.
	 * 
	 * @param x
	 * @param z
	 */
	public AbstractEntrance(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	
	/**
	 * Build the entrance to the given dungeon in the given world.
	 * 
	 * @param dungeon
	 * @param world
	 */
	public abstract void build(Dungeon dungeon, World world);
}
