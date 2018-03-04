package jaredbgreat.dldungeons.planner.mapping;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * Forge event code by Charles Howard, 2016.
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import static jaredbgreat.dldungeons.planner.mapping.ChunkMap.*;
import static jaredbgreat.dldungeons.planner.mapping.MapMatrix.drawFlyingMap;
import net.minecraft.block.Block;
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
	static final Block lapis = Block.getBlockFromName("lapis_block");
	static final Block slab  = Block.getBlockFromName("double_stone_slab");
	static final Block gold  = Block.getBlockFromName("gold_block");
	static final Block glass = Block.getBlockFromName("glass");
	
	static boolean drawFlyingMap = false;
	
	private final World world;
	private final int   chunkX, chunkZ, origenX, origenZ;
	private final int   cwidth;
	
	// New "chunky" way to hold data
	private ChunkMap[] chunks;
	
	//The A* scratch pad
	private Step[][]    nodedge;
	
	
	public MapMatrix(int width, World world, int chunkX, int chunkZ) {
		this.world  = world;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		cwidth = width / CSIZE;
		chunks = new ChunkMap[cwidth * cwidth];
		origenX   = (chunkX * 16) - (width / 2) + 8;
		origenZ   = (chunkZ * 16) - (width / 2) + 8;
		for(int i = 0; i < cwidth; i++)
			for(int j = 0; j < cwidth; j++) {
				chunks[i + (j * cwidth)] 
						= new ChunkMap(world, 
								       origenX + (i * CSIZE), 
								       origenZ + (j * CSIZE),
								       i, j);
			}
		nodedge   = new Step[width][width];
	}
	
	
	/*
	 * Chunk related getters and setters
	 */
	public int getChunkX() {
		return chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}

	public int getOrigenX() {
		return origenX;
	}

	public int getOrigenZ() {
		return origenZ;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Step getStep(int x, int z) {
		return nodedge[x][z];
	}
	
	public Step[][] getSteps() {
		return nodedge;
	}
	
	public int getNumChunks() {
		return chunks.length;
	}
	
	public int getChunkMidX(int chunk) {
		return ((chunk % cwidth) * CSIZE) + 8;
	}
	
	public int getChunkMidZ(int chunk) {
		return ((chunk / cwidth) * CSIZE) + 8;
	}
	
	public ChunkMap getChunkMap(int index) {
		return chunks[index];
	}
	
	public void addEntrance(int index) {
		chunks[index].makeEntrance();
	}

	/*
	 * Setters for heights
	 */
	public void setCeilY(int x, int z, byte val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setCeilY(bx, bz, val);
	}
	
	public void setFloorY(int x, int z, byte val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setFloorY(bx, bz, val);
	}
	
	public void setNCeilY(int x, int z, byte val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setNCeilY(bx, bz, val);
	}
	
	public void setNFloorY(int x, int z, byte val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setNFloorY(bx, bz, val);
	}

	/*
	 * Setters for heights
	 */
	public byte getCeilY(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].getCeilY(bx, bz);
	}
	
	public byte getFloorY(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].getFloorY(bx, bz);
	}
	
	public byte getNCeilY(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].getNCeilY(bx, bz);
	}
	
	public byte getNFloorY(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].getNFloorY(bx, bz);
	}
	
	/*
	 * Setters for blocks
	 */
	
	public void setWall(int x, int z, int val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setWall(bx, bz, val);
	}
	
	public void setFloor(int x, int z, int val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setFloor(bx, bz, val);
	}
	
	public void setCeiling(int x, int z, int val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setCeiling(bx, bz, val);
	}
	
	/*
	 * Getters for blocks
	 */
	
	public int getWall(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].getWall(bx, bz);
	}
	
	public int getFloor(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].getFloor(bx, bz);
	}
	
	public int getCeiling(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].getCeiling(bx, bz);
	}
	
	/**
	 * Setters and getters for room id
	 */
	public void setRoom(int x, int z, int id) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setRoom(bx, bz, id);
	}
	
	public int getRoom(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].getRoom(bx, bz);
	}
	
	public int getWidth() {
		return cwidth * CSIZE;
	}
	
	/*
	 * Set booleans
	 */
	public void setToWall(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setToWall(bx, bz);
	}
	
	public void unsetWall(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].unsetWall(bx, bz);
	}
	
	public void setIsWall(int x, int z, boolean val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setIsWall(bx, bz, val);
	}
	
	public void setToFence(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setToFence(bx, bz);
	}
	
	public void unsetFence(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].unsetFence(bx, bz);
	}
	
	public void setIsFence(int x, int z, boolean val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setIsFence(bx, bz, val);
	}
	
	public void setToLiquid(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setToLiquid(bx, bz);
	}
	
	public void unsetLiquid(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].unsetLiquid(bx, bz);
	}
	
	public void setIsLiquid(int x, int z, boolean val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setIsLiquid(bx, bz, val);
	}
	
	public void setToDoor(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setToDoor(bx, bz);
	}
	
	public void unsetDoor(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].unsetDoor(bx, bz);
	}
	
	public void setIsDoor(int x, int z, boolean val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setIsDoor(bx, bz, val);
	}
	
	public void setAStared(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setAStared(bx, bz);
	}
	
	public void unsetAStared(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].unsetAStared(bx, bz);
	}
	
	public void setIsAStared(int x, int z, boolean val) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].setIsAStared(bx, bz, val);
	}
	
	/*
	 * Get Booleans
	 */
	
	public boolean isWall(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].isWall(bx, bz);
	}
	
	public boolean isFence(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].isFence(bx, bz);
	}
	
	public boolean isLiquid(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].isLiquid(bx, bz);
	}
	
	public boolean isDoor(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].isDoor(bx, bz);
	}
	
	public boolean isAStared(int x, int z) {
		int cx = x / CSIZE;
		int cz = z / CSIZE;
		int bx = x - (cx * CSIZE);
		int bz = z - (cz * CSIZE);
		return chunks[cx + (cz * cwidth)].isAStared(bx, bz);
	}
	
	
	/*
	 * Adding tile entities 
	 */
	
	
	public void addChest(BasicChest chest) {
		int cx = chest.mx / CSIZE;
		int cz = chest.mz / CSIZE;
		int bx = chest.mx - (cx * CSIZE);
		int bz = chest.mz - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].addChest(chest, bx, bz);
	}
	
	
	public void addSpawner(Spawner spawner) {
		int cx = spawner.getX() / CSIZE;
		int cz = spawner.getZ() / CSIZE;
		int bx = spawner.getX() - (cx * CSIZE);
		int bz = spawner.getZ() - (cz * CSIZE);
		chunks[cx + (cz * cwidth)].addSpawner(spawner, bx, bz);
	}
	
	/*
	 * General stuff
	 */
	
	
	/**
	 * Sets whether the flying debug map should be drawn.
	 * 
	 * @param value
	 */
	public static void setDrawFlyingMap(boolean value) {
		drawFlyingMap = value;
	}
	
	
	/**
	 * This will build the dungeon into the world, transforming the information 
	 * mapped here in 2D arrays into the finished 3D structure in the Minecraft 
	 * world.
	 * 
	 * @param dungeon
	 */
	public void build(Dungeon dungeon) {		
		DoomlikeDungeons.profiler.startTask("Building Dungeon in World");	
		DoomlikeDungeons.profiler.startTask("Building Dungeon architecture");
		for(int i = 0; i < (cwidth * cwidth); i++) {
			chunks[i].build(dungeon);
		}
		DoomlikeDungeons.profiler.endTask("Building Dungeon architecture");
		DoomlikeDungeons.profiler.endTask("Building Dungeon in World");
	}
	
}
