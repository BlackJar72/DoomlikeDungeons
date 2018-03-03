package jaredbgreat.dldungeons.planner.mapping;

import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import static jaredbgreat.dldungeons.planner.mapping.MapMatrix.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ChunkMap {	
	static final int CSIZE = 16;
	static final int ASIZE = CSIZE * CSIZE;
	
	private final World world;
	private final int   origenX, origenZ;
	
	// map of heights to build at
	private byte[] ceilY;		// Ceiling height
	private byte[] floorY;		// Floor Height	
	private byte[] nCeilY;		// Height of Neighboring Ceiling	
	private byte[] nFloorY;	// Height of Neighboring Floor
	
	// Blocks referenced against the DBlock.registry	
	private int[] ceiling;
	private int[] wall;
	private int[] floor;
	
	// The room id (index of the room in the dungeons main RoomList)
	private int[] room;
	
	// Is it a wall?
	private boolean[] bWall;	    // Is this coordinate occupied by a wall?
	private boolean[] bFence;	    // Is this coordinate occupied by a wall?
	private boolean[] bLiquid;	// Is floor covered by a liquid block?
	private boolean[] bDoor;		// Is there a door here?
	
	//The A* scratch pad
	private boolean[] astared;
	
	
	public ChunkMap(World world, int x, int z) {
		this.world = world;
		origenX = x;
		origenZ = z;
		ceilY   = new byte[ASIZE];
		floorY  = new byte[ASIZE];
		nCeilY  = new byte[ASIZE];
		nFloorY = new byte[ASIZE];
		wall    = new int[ASIZE];
		floor   = new int[ASIZE];
		ceiling = new int[ASIZE];
		room    = new int[ASIZE];
		bWall   = new boolean[ASIZE];
		bFence  = new boolean[ASIZE];
		bLiquid = new boolean[ASIZE];
		bDoor   = new boolean[ASIZE];
		astared = new boolean[ASIZE];
	}

	/*
	 * Setters for heights
	 */
	public void setCeilY(int x, int z, byte val) {
		ceilY[x + (z * CSIZE)] = val;
	}
	
	public void setFloorY(int x, int z, byte val) {
		floorY[x + (z * CSIZE)] = val;
	}
	
	public void setNCeilY(int x, int z, byte val) {
		nCeilY[x + (z * CSIZE)] = val;
	}
	
	public void setNFloorY(int x, int z, byte val) {
		nFloorY[x + (z * CSIZE)] = val;
	}

	/*
	 * Setters for heights
	 */
	public byte getCeilY(int x, int z) {
		return ceilY[x + (z * CSIZE)];
	}
	
	public byte getFloorY(int x, int z) {
		return floorY[x + (z * CSIZE)];
	}
	
	public byte getNCeilY(int x, int z) {
		return nCeilY[x + (z * CSIZE)];
	}
	
	public byte getNFloorY(int x, int z) {
		return nFloorY[x + (z * CSIZE)];
	}
	
	/*
	 * Setters for blocks
	 */
	
	public void setWall(int x, int z, int val) {
		wall[x + (z * CSIZE)] = val;
	}
	
	public void setFloor(int x, int z, int val) {
		floor[x + (z * CSIZE)] = val;
	}
	
	public void setCeiling(int x, int z, int val) {
		ceiling[x + (z * CSIZE)] = val;
	}
	
	/*
	 * Getters for blocks
	 */
	
	public int getWall(int x, int z) {
		return wall[x + (z * CSIZE)];
	}
	
	public int getFloor(int x, int z) {
		return floor[x + (z * CSIZE)];
	}
	
	public int getCeiling(int x, int z) {
		return ceiling[x + (z * CSIZE)];
	}
	
	/**
	 * Setters and getters for room id
	 */
	public void setRoom(int x, int z, int id) {
		room[x + (z * CSIZE)] = id;
	}
	
	public int getRoom(int x, int z) {
		return room[x + (z * CSIZE)];
	}
	
	public int getWidthRoom() {
		return room.length;
	}
	
	/*
	 * Set booleans
	 */
	public void setToWall(int x, int z) {
		bWall[x + (z * CSIZE)] = true;
	}
	
	public void unsetWall(int x, int z) {
		bWall[x + (z * CSIZE)] = false;
	}
	
	public void setIsWall(int x, int z, boolean val) {
		bWall[x + (z * CSIZE)] = val;
	}
	
	public void setToFence(int x, int z) {
		bFence[x + (z * CSIZE)] = true;
	}
	
	public void unsetFence(int x, int z) {
		bFence[x + (z * CSIZE)] = false;
	}
	
	public void setIsFence(int x, int z, boolean val) {
		bFence[x + (z * CSIZE)] = val;
	}
	
	public void setToLiquid(int x, int z) {
		bLiquid[x + (z * CSIZE)] = true;
	}
	
	public void unsetLiquid(int x, int z) {
		bLiquid[x + (z * CSIZE)] = false;
	}
	
	public void setIsLiquid(int x, int z, boolean val) {
		bLiquid[x + (z * CSIZE)] = val;
	}
	
	public void setToDoor(int x, int z) {
		bDoor[x + (z * CSIZE)] = true;
	}
	
	public void unsetDoor(int x, int z) {
		bDoor[x + (z * CSIZE)] = false;
	}
	
	public void setIsDoor(int x, int z, boolean val) {
		bDoor[x + (z * CSIZE)] = val;
	}
	
	public void setAStared(int x, int z) {
		astared[x + (z * CSIZE)] = true;
	}
	
	public void unsetAStared(int x, int z) {
		astared[x + (z * CSIZE)] = false;
	}
	
	public void setIsAStared(int x, int z, boolean val) {
		astared[x + (z * CSIZE)] = val;
	}
	
	/*
	 * Get Booleans
	 */
	
	public boolean isWall(int x, int z) {
		return bWall[x + (z * CSIZE)];
	}
	
	public boolean isFence(int x, int z) {
		return bFence[x + (z * CSIZE)];
	}
	
	public boolean isLiquid(int x, int z) {
		return bLiquid[x + (z * CSIZE)];
	}
	
	public boolean isDoor(int x, int z) {
		return bDoor[x + (z * CSIZE)];
	}
	
	public boolean isAStared(int x, int z) {
		return astared[x + (z * CSIZE)];
	}
	
	
	/**
	 * Returns true if a block should be placed in those coordinates; that is 
	 * the block is not air or the room is not degenerate.
	 * 
	 * This is for use with wall and ceiling blocks; for floor blocks use 
	 * noLowDegenerate.
	 * 
	 * @param theRoom
	 * @param x world x coordinate
	 * @param y world y coordinate
	 * @param z world z coordinate
	 * @return if the block should be placed here.
	 */
	private boolean noHighDegenerate(Room theRoom, int x, int y, int z) {
		return !(theRoom.degenerate && world.isAirBlock(new BlockPos(x, y, z)));
	}
	
	
	/**
	 * Returns true if a floor block should be placed here.  This will be true
	 * if the block is not air, if the room does not have degenerate floors, or 
	 * is part of a main path through the room.
	 * 
	 * @param theRoom
	 * @param x world x coordinate
	 * @param y world y coordinate
	 * @param z world z coordinate
	 * @param i dungeon x coordinate
	 * @param j dungeon z coordinate
	 * @return
	 * @return if the block should be placed here.
	 */
	private boolean noLowDegenerate(Room theRoom, int x, int y, int z, int l) {
		return !(theRoom.degenerateFloors 
				&& world.isAirBlock(new BlockPos(x, y, z))
				&& !astared[l]);
	}
	
	
	/**
	 * The lowest height to place air or wall; walls may 
	 * go one block lower.
	 * 
	 * @param i dungeon x coordinate
	 * @param j dungeon z coordinate
	 * @return lowest height to place a wall or air/water block.
	 */
	private int roomBottom(int l) {
		int b = floorY[l];
		if(bWall[l] && !bDoor[l]) b--;
		return b;		
	}
	
	
	/**
	 * This will build the dungeon into the world, transforming the information 
	 * mapped here in 2D arrays into the finished 3D structure in the Minecraft 
	 * world.
	 * 
	 * @param dungeon
	 */
	public void build(Dungeon dungeon) {
		int below;
		boolean flooded = dungeon.theme.flags.contains(ThemeFlags.WATER);
		MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.BeforeBuild(this, origenX, origenZ, flooded));
		
		for(int l = 0; l < ASIZE; l++) {
				int i = l % CSIZE;
				int j = l / CSIZE;
				if(room[l] != 0) {
					 Room theRoom = dungeon.rooms.get(room[l]);
					 
					 // Debugging code; should not normally run
					 if(drawFlyingMap) {
						 if(astared[l]) {
							 DBlock.placeBlock(world, origenX + i, 96, origenZ +j, lapis);
						 } else if(bDoor[l]) {
							 DBlock.placeBlock(world, origenX + i, 96, origenZ +j, slab);
						 } else if(bWall[l]) {
							 DBlock.placeBlock(world, origenX + i, 96, origenZ +j, gold);
						 } else {
							 DBlock.placeBlock(world, origenX + i, 96, origenZ +j, glass);
						 }
					 }
					 
					 // Lower parts of the room
					 if(nFloorY[l] < floorY[l])
						 for(int k = nFloorY[l]; k < floorY[l]; k++) 
							 if(noLowDegenerate(theRoom, origenX + i, k, origenZ + j, l))
								 DBlock.place(world, origenX + i, k, origenZ + j, wall[l]);
					 if(nFloorY[l] > floorY[l])
						 for(int k = floorY[l]; k < nFloorY[l]; k++) 
							 if(noLowDegenerate(theRoom, origenX + i, k, origenZ + j, l))
								 DBlock.place(world, origenX + i, k, origenZ + j, wall[l]);
					 
					 if(noLowDegenerate(theRoom, origenX + i, floorY[l] - 1, origenZ + j, l)) {
						 DBlock.place(world, origenX + i, floorY[l] - 1, origenZ + j, floor[l]);
						 if(dungeon.theme.buildFoundation) {
							 below = nFloorY[l] < floorY[l] ? nFloorY[l] - 1 : floorY[l] - 2;
							 while(!DBlock.isGroundBlock(world, origenX + i, below, origenZ + j)) {
								 DBlock.place(world, origenX + i, below, origenZ + j, dungeon.floorBlock);
						 		below--;
						 		if(below < 0) break;						 		
						 	 }
						}
					 }
					 
					 // Upper parts of the room
					 if(!theRoom.sky 
							 && noHighDegenerate(theRoom, origenX + i, ceilY[l] + 1, origenZ + j))
						 DBlock.place(world, origenX + i, ceilY[l] + 1, origenZ + j, ceiling[l]);
					
					 for(int k = roomBottom(l); k <= ceilY[l]; k++)
						 if(!bWall[l])DBlock.deleteBlock(world, origenX +i, k, origenZ + j, flooded);
						 else if(noHighDegenerate(theRoom, origenX + i, k, origenZ + j))
							 DBlock.place(world, origenX + i, k, origenZ + j, wall[l]);
					 for(int k = nCeilY[l]; k < ceilY[l]; k++) 
						 if(noHighDegenerate(theRoom, origenX + i, k, origenZ + j))
							 DBlock.place(world, origenX + i, k, origenZ + j, wall[l]);
					 if(bFence[l]) 
						 DBlock.place(world, origenX + i, floorY[l], origenZ + j, dungeon.fenceBlock);
					 
					 if(bDoor[l]) {
						 DBlock.deleteBlock(world, origenX + i, floorY[l],     origenZ + j, flooded);
						 DBlock.deleteBlock(world, origenX + i, floorY[l] + 1, origenZ + j, flooded);
						 DBlock.deleteBlock(world, origenX + i, floorY[l] + 2, origenZ + j, flooded);
					 }
					 
					 // Liquids
					 if(bLiquid[l] && (!bWall[l] && !bDoor[l])
							 && !world.isAirBlock(new BlockPos(origenX + i, floorY[l] - 1, origenZ + j))) 
						 DBlock.place(world, origenX + i, floorY[l], origenZ + j, theRoom.liquidBlock);					 
				}
			}
		MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.AfterBuild(this, origenX, origenZ, flooded));
	}

}
