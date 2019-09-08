package jaredbgreat.dldungeons.planner.mapping;


import java.util.List;

import jaredbgreat.dldungeons.planner.astar.Step;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

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
	
	public final int width;
	
	public final int   chunkX, chunkZ, origenX, origenZ;
	
	//That Chunks
	public ChunkMap[] chunks;
	
	//The A* scratch pad
	public Step    nodedge[][];
	
	
	public MapMatrix(TemplateManager tm, int width, int chunkX, int chunkZ) {
		this.width = width;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		origenX   = (chunkX * 16) - (width / 2) + 8;
		origenZ   = (chunkZ * 16) - (width / 2) + 8;
		chunks    = new ChunkMap[(width * width) / ChunkMap.SIZE]; 
		nodedge   = new Step[width][width];
		int r = width / 2;
		for(int i = 0; i < width; i++) 
			for(int j = 0; j < width; j++) {
				CompoundNBT nbt = new CompoundNBT();
				nbt.putInt("x", chunkX - r + i);
				nbt.putInt("z", chunkZ - r + j);
				nbt.putInt("cdz", chunkZ);
				nbt.putInt("cdz", chunkZ);
				chunks[(i * width) + j] = new ChunkMap(tm, nbt);
			}
	}
	
	
	/**
	 * Sets whether the flying debug map should be drawn.
	 * 
	 * @param value
	 */
	public static void setDrawFlyingMap(boolean value) {
		drawFlyingMap = value;
	}
	
	
	public void addChunkMaps(List<StructurePiece> components) {
		for(int i = 0; i < chunks.length; i++) {
			components.add(chunks[i]);
		}
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
	
	public void setAStared(boolean val, int x, int z) {
		chunks[(x % width) + z].setAStared(val, x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	/*------------------------------------------------------------*/
	/*                        GETTERS                             */
	/*------------------------------------------------------------*/
	
	// map of heights to build at
	public byte getCeilY(int x, int z) {
		return chunks[(x % width) + z].getCeilY(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}	
	
	public byte getFloorY(int x, int z) {
		return chunks[(x % width) + z].getFloorY(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public byte getNCeilY(int x, int z) {
		return chunks[(x % width) + z].getNCeilY(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public byte getNFloorY(int x, int z) {
		return chunks[(x % width) + z].getNFloorY(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public int getCeiling(int x, int z) {
		return chunks[(x % width) + z].getCeiling(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public int getWall(int x, int z) {
		return chunks[(x % width) + z].getWall(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public int getFloor(int x, int z) {
		return chunks[(x % width) + z].getFloor(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public int getRoom(int x, int z) {
		return chunks[(x % width) + z].getRoom(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public boolean getIsWall( int x, int z) {
		return chunks[(x % width) + z].getIsWall(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public boolean getIsFence(int x, int z) {
		return chunks[(x % width) + z].getIsFence(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public boolean getHasLiquid(int x, int z) {
		return chunks[(x % width) + z].getHasLiquid(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public boolean getIsDoor(int x, int z) {
		return chunks[(x % width) + z].getIsDoor(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	public boolean getAStared(int x, int z) {
		return chunks[(x % width) + z].getAStared(x % ChunkMap.WIDTH, z % ChunkMap.WIDTH);
	}
	
	
	
}
