package jaredbgreat.dldungeons.pieces.entrances;

/*
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */


import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * The base class for all that build entrances.
 */

public abstract class AbstractEntrance {

    protected static final Block LADDER = Blocks.LADDER;
    protected static final Block STAIR_SLAB = Blocks.STONE_SLAB;

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
    public abstract void build(Dungeon dungeon, WorldGenLevel world);
}
