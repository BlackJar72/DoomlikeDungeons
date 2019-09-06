package jaredbgreat.dldungeons.planner.mapping;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * A two dimensional map of the dungeon, including heights, blocks, and 
 * certain features such as fall and doorways, and pathfinding data.
 * 
 * This map also includes the method for building itself into the actual 
 * world, converting the 2 1/2 d mapping into blocks.
 * 
 * @author Jared Blackburn
 *
 */
public class MapMatrix {
	private static final Block lapis = Blocks.LAPIS_BLOCK;
	private static final Block slab  = Blocks.END_STONE;
	private static final Block gold  = Blocks.GOLD_BLOCK;
	private static final Block glass = Blocks.GLASS;
	
	private static boolean drawFlyingMap = false;
	
	private final int width;
	
	public final World world;
	public final int   chunkX, chunkZ, origenX, origenZ;
	
	//That Chunks
	public ChunkMap[] chunks;
	
	//The A* scratch pad
	public Step    nodedge[][];
	
	
	public MapMatrix(int width, World world, int chunkX, int chunkZ) {
		this.width = width;
		this.world = world;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		origenX   = (chunkX * 16) - (width / 2) + 8;
		origenZ   = (chunkZ * 16) - (width / 2) + 8;
		chunks    = new ChunkMap[(width * width) / ChunkMap.SIZE]; 
		nodedge   = new Step[width][width];
	}
	
	
	/**
	 * Sets whether the flying debug map should be drawn.
	 * 
	 * @param value
	 */
	public static void setDrawFlyingMap(boolean value) {
		drawFlyingMap = value;
	}
	
	/*------------------------------------------------------------*/
	/*                        SETTERS                             */
	/*------------------------------------------------------------*/
	
	// map of heights to build at
	public void setCeilY(byte val, int x, int z) {
		chunks[(x % width) + z].setCeilY(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}	
	
	public void setFloorY(byte val, int x, int z) {
		chunks[(x % width) + z].setCeilY(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setNCeilY(byte val, int x, int z) {
		chunks[(x % width) + z].setCeilY(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setNFloorY(byte val, int x, int z) {
		chunks[(x % width) + z].setCeilY(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setCeiling(int val, int x, int z) {
		chunks[(x % width) + z].setCeiling(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setWall(int val, int x, int z) {
		chunks[(x % width) + z].setWall(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setFloor(int val, int x, int z) {
		chunks[(x % width) + z].setRoom(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setRoom(int val, int x, int z) {
		chunks[(x % width) + z].setRoom(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setIsWall(boolean val, int x, int z) {
		chunks[(x % width) + z].setIsWall(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setIsFence(boolean val, int x, int z) {
		chunks[(x % width) + z].setIsFence(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setHasLiquid(boolean val, int x, int z) {
		chunks[(x % width) + z].setHasLiquid(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setIsDoor(boolean val, int x, int z) {
		chunks[(x % width) + z].setIsDoor(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public void setAStarted(boolean val, int x, int z) {
		chunks[(x % width) + z].setAStarted(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	
	
}
