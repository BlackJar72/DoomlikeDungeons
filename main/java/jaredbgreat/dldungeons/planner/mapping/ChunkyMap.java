package jaredbgreat.dldungeons.planner.mapping;

import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.themes.Sizes;
import jaredbgreat.dldungeons.util.cache.Coords;
import jaredbgreat.dldungeons.util.cache.IHaveCoords;
import net.minecraft.world.World;


/**
 * THIS WILL PROBABLY NOT BE USED!!! THE ChunkFeatures CLASS IS A FAR BETTER IDEA!!!S
 * 
 * @author jared
 *
 */
public class ChunkyMap implements IHaveCoords {	
	private final ChunkMatrix[][] chunks;
	private final World  world;
	private final Coords coords;
	private final Sizes  size;
	private final int    lowX, lowZ;
	
	
	public ChunkyMap(final Sizes size, final World world, final Coords coords) {
		this.size   = size;
		this.world  = world;
		this.coords = coords;
		this.chunks = new ChunkMatrix[size.chunkWidth][size.chunkWidth];
		lowX = coords.getX() - size.chunkRadius;
		lowZ = coords.getZ() - size.chunkRadius;
		for(int i = 0; i < size.chunkWidth; i++)
			for(int j = 0; j < size.chunkWidth; j++) {
				chunks[i][j] = new ChunkMatrix(world, lowX + i, lowZ + j);
			}
	}
	

	/**
	 * Return the Coords for this ChunkyMap; these are the chunk coordinates and 
	 * dimension ID for the central chunk.
	 */
	@Override
	public Coords getCoords() {
		return coords;
	}
	
	// map of heights to build at
	public byte setCeilY(int x, int z, byte val) {
		chunks[x/16][z/16].ceilY[x%16][z%16] = val;
		return val;
	}
	
	
	public byte setFloorY(int x, int z, byte val) {
		chunks[x/16][z/16].floorY[x%16][z%16] = val;
		return val;
	}
	
		
	public byte setNCeilY(int x, int z, byte val) {
		chunks[x/16][z/16].nCeilY[x%16][z%16] = val;
		return val;
	}
	
		
	public byte setNFloorY(int x, int z, byte val) {
		chunks[x/16][z/16].nFloorY[x%16][z%16] = val;
		return val;
	}
	
	
	// Blocks referenced against the DBlock.registry	
	public int setCeiling(int x, int z, int val) {
		chunks[x/16][z/16].ceiling[x%16][z%16] = val;
		return val;
	}
	
	
	public int setWall(int x, int z, int val) {
		chunks[x/16][z/16].wall[x%16][z%16] = val;
		return val;
	}
	
	
	public int setFloor(int x, int z, int val) {
		chunks[x/16][z/16].floor[x%16][z%16] = val;
		return val;
	}
	
	
	// The room id (index of the room in the dungeons main RoomList)
	public int setRoom(int x, int z, int val) {
		chunks[x/16][z/16].room[x%16][z%16] = val;
		return val;
	}
		
	
	// Is it a wall?
	public void setWall(int x, int z, boolean val) {
		chunks[x/16][z/16].isWall[x%16][z%16] = val;
	}
	
	
	public void setFence(int x, int z, boolean val) {
		chunks[x/16][z/16].isFence[x%16][z%16] = val;
	}
	
	
	public void setLiquid(int x, int z, boolean val) {
		chunks[x/16][z/16].hasLiquid[x%16][z%16] = val;
	}
	
	
	public void setDoor(int x, int z, boolean val) {
		chunks[x/16][z/16].isDoor[x%16][z%16] = val;
	}
	
	
	
	//The A* scratch pad
	Step    nodedge[][];
	boolean astared[][];
	

}
