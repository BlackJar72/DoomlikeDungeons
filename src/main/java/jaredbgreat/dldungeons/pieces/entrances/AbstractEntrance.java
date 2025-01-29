package jaredbgreat.dldungeons.pieces.entrances;

/*
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */


import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.util.math.ModMath;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * The base class for all that build entrances.
 */

public abstract class AbstractEntrance {

    protected static final Block LADDER = Blocks.LADDER;
    protected static final Block STAIR_SLAB = Blocks.SMOOTH_STONE_SLAB;

    protected int x, z;

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
    public abstract void build(Dungeon dungeon, WorldGenLevel world, int shiftX, int shiftZ);


    /**
     * This will move the entrance one block closer to the center of the chunk on any axis where is on the very
     * edge.  This is to prevent parts of the entrance that is built (.e.g., steps or ladders) from overlapping
     * into the next chunk where they could be overwritten (deleted) in the process of building the neighboring
     * chunk.
     *
     * @param shiftX
     * @param shiftZ
     */
    public void avoidChunkEdges(int shiftX, int shiftZ) {
        int chunkX = ModMath.modRight(x + shiftX, 16);
        int chunkZ = ModMath.modRight(z + shiftZ, 16);
        if(chunkX == 0) x++;
        else if(chunkX == 15) x--;
        if(chunkZ == 0) z++;
        else if(chunkZ == 15) z--;
    }
}
