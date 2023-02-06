package jaredbgreat.dldungeons.pieces.entrances;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


/**
 * The base class for all that build entrances.
 */
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.state.properties.SlabType;
import net.minecraft.world.ISeedReader;

public abstract class AbstractEntrance {
	
	protected static final Block LADDER 
			= (Block)Blocks.LADDER;
	protected static final BlockState STAIR_SLAB1 
		= Blocks.SMOOTH_STONE_SLAB.defaultBlockState();
	protected static final BlockState STAIR_SLAB2 
		= Blocks.SMOOTH_STONE_SLAB.defaultBlockState()
			.setValue(SlabBlock.TYPE, SlabType.TOP);
	
	public final int x, z;
	
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
	public abstract void build(Dungeon dungeon, ISeedReader sreader);
}
