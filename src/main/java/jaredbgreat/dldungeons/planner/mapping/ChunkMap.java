package jaredbgreat.dldungeons.planner.mapping;

import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ChunkMap {
	public static final int WIDTH = 16;
	public static final int SIZE = WIDTH * WIDTH;
	
	private int chunkX, chunkZ; // offsets, world coords for 0,0 in the chunk	
	
	// map of heights to build at
	private byte[] ceilY   = new byte[SIZE];		// Ceiling height
	private byte[] floorY  = new byte[SIZE];		// Floor Height	
	private byte[] nCeilY  = new byte[SIZE];		// Height of Neighboring Ceiling	
	private byte[] nFloorY = new byte[SIZE];	    // Height of Neighboring Floor
	
	// Blocks referenced against the DBlock.registry	
	private int[] ceiling = new int[SIZE];
	private int[] wall    = new int[SIZE];
	private int[] floor   = new int[SIZE];
	
	// The room id (index of the room in the dungeons main RoomList)
	private int[] room;
	
	// Is it a wall?
	private boolean[] isWall    = new boolean[SIZE];	    // Is this coordinate occupied by a wall?
	private boolean[] isFence   = new boolean[SIZE];	    // Is this coordinate occupied by a wall?
	private boolean[] hasLiquid = new boolean[SIZE];	// Is floor covered by a liquid block?
	private boolean[] isDoor    = new boolean[SIZE];		// Is there a door here?
	
	//The A* scratch pad
	private boolean astared[] = new boolean[SIZE];
	
	
	public ChunkMap() {/*TODO*/}
	
	/*------------------------------------------------------------*/
	/*                        SETTERS                             */
	/*------------------------------------------------------------*/
	
	// map of heights to build at
	public void setCeilY(byte val, int x, int z) {
		ceilY[(x % WIDTH) + z] = val;
	}	
	
	public void setFloorY(byte val, int x, int z) {
		floorY[(x % WIDTH) + z] = val;
	}
	
	public void setNCeilY(byte val, int x, int z) {
		nCeilY[(x % WIDTH) + z] = val;
	}
	
	public void setNFloorY(byte val, int x, int z) {
		nFloorY[(x % WIDTH) + z] = val;
	}
	
	public void setCeiling(int val, int x, int z) {
		ceiling[(x % WIDTH) + z] = val;
	}
	
	public void setWall(int val, int x, int z) {
		wall[(x % WIDTH) + z] = val;
	}
	
	public void setFloor(int val, int x, int z) {
		floor[(x % WIDTH) + z] = val;
	}
	
	public void setRoom(int val, int x, int z) {
		room[(x % WIDTH) + z] = val;
	}
	
	public void setIsWall(boolean val, int x, int z) {
		isWall[(x % WIDTH) + z] = val;
	}
	
	public void setIsFence(boolean val, int x, int z) {
		isFence[(x % WIDTH) + z] = val;
	}
	
	public void setHasLiquid(boolean val, int x, int z) {
		hasLiquid[(x % WIDTH) + z] = val;
	}
	
	public void setIsDoor(boolean val, int x, int z) {
		isDoor[(x % WIDTH) + z] = val;
	}
	
	public void setAStarted(boolean val, int x, int z) {
		astared[(x % WIDTH) + z] = val;
	}
	
	/*------------------------------------------------------------*/
	/*                        GETTERS                             */
	/*------------------------------------------------------------*/
	
	// map of heights to build at
	public byte getCeilY(int x, int z) {
		return ceilY[(x % WIDTH) + z];
	}	
	
	public byte getFloorY(int x, int z) {
		return floorY[(x % WIDTH) + z];
	}
	
	public byte getNCeilY(int x, int z) {
		return nCeilY[(x % WIDTH) + z];
	}
	
	public byte getNFloorY(int x, int z) {
		return nFloorY[(x % WIDTH) + z];
	}
	
	public int getCeiling(int x, int z) {
		return ceiling[(x % WIDTH) + z];
	}
	
	public int getWall(int x, int z) {
		return wall[(x % WIDTH) + z];
	}
	
	public int getFloor(int x, int z) {
		return floor[(x % WIDTH) + z];
	}
	
	public int getRoom(int x, int z) {
		return room[(x % WIDTH) + z];
	}
	
	public boolean getIsWall( int x, int z) {
		return isWall[(x % WIDTH) + z];
	}
	
	public boolean getIsFence(int x, int z) {
		return isFence[(x % WIDTH) + z];
	}
	
	public boolean getHasLiquid(int x, int z) {
		return hasLiquid[(x % WIDTH) + z];
	}
	
	public boolean getIsDoor(int x, int z) {
		return isDoor[(x % WIDTH) + z];
	}
	
	public boolean getAStarted(int x, int z) {
		return astared[(x % WIDTH) + z];
	}
	
	/*------------------------------------------------------------*/
	/*                        BUILDING                            */
	/*------------------------------------------------------------*/
	
	
	/**
	 * This will build the dungeon into the world, transforming the information 
	 * mapped here in 2D arrays into the finished 3D structure in the Minecraft 
	 * world.
	 * 
	 * @param dungeon
	 */
	public void build(Dungeon dungeon, World world) {		
		DoomlikeDungeons.profiler.startTask("Building Dungeon in World");	
		DoomlikeDungeons.profiler.startTask("Building Dungeon architecture");
		int shiftX = (chunkX * 16) - (room.length / 2) + 8;
		int shiftZ = (chunkZ * 16) - (room.length / 2) + 8;
		int below;
		boolean flooded = dungeon.theme.flags.contains(ThemeFlags.WATER);
		//MinecraftForge.EVENT_BUS.post(new DLDEvent.BeforeBuild(this, shiftX, shiftZ, flooded));
		
		for(int i = 0; i < room.length; i++)
			for(int j = 0; j < room.length; j++) {
				if(room[(i * WIDTH) + j] != 0) {
					 Room theRoom = dungeon.rooms.get(room[(i * WIDTH) + j]);
					 
					 // Debugging code; should not normally run
					 /*if(drawFlyingMap) {
						 if(astared[(i * WIDTH) + j]) {
							 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, lapis);
						 } else if(isDoor[(i * WIDTH) + j]) {
							 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, slab);
						 } else if(isWall[(i * WIDTH) + j]) {
							 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, gold);
						 } else {
							 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, glass);
						 }
					 }*/
					 
					 // Lower parts of the room
					 if(nFloorY[(i * WIDTH) + j] < floorY[(i * WIDTH) + j])
						 for(int k = nFloorY[(i * WIDTH) + j]; k < floorY[(i * WIDTH) + j]; k++) 
							 if(noLowDegenerate(theRoom, world, shiftX + i, k, shiftZ + j, i, j))
								 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[(i * WIDTH) + j]);
					 if(nFloorY[(i * WIDTH) + j] > floorY[(i * WIDTH) + j])
						 for(int k = floorY[(i * WIDTH) + j]; k < nFloorY[(i * WIDTH) + j]; k++) 
							 if(noLowDegenerate(theRoom, world, shiftX + i, k, shiftZ + j, i, j))
								 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[(i * WIDTH) + j]);
					 
					 if(noLowDegenerate(theRoom, world, shiftX + i, floorY[(i * WIDTH) + j] - 1, shiftZ + j, i, j)) {
						 DBlock.place(world, shiftX + i, floorY[(i * WIDTH) + j] - 1, shiftZ + j, floor[(i * WIDTH) + j]);
						 if(dungeon.theme.buildFoundation) {
							 below = nFloorY[(i * WIDTH) + j] < floorY[(i * WIDTH) + j] ? nFloorY[(i * WIDTH) + j] - 1 : floorY[(i * WIDTH) + j] - 2;
							 while(!DBlock.isGroundBlock(world, shiftX + i, below, shiftZ + j)) {
								 DBlock.place(world, shiftX + i, below, shiftZ + j, dungeon.floorBlock);
						 		below--;
						 		if(below < 0) break;						 		
						 	 }
						}
					 }
					 
					 // Upper parts of the room
					 if(!theRoom.sky 
							 && noHighDegenerate(theRoom, world, shiftX + i, ceilY[(i * WIDTH) + j] + 1, shiftZ + j))
						 DBlock.place(world, shiftX + i, ceilY[(i * WIDTH) + j] + 1, shiftZ + j, ceiling[(i * WIDTH) + j]);
					
					 for(int k = roomBottom(i, j); k <= ceilY[(i * WIDTH) + j]; k++)
						 if(!isWall[(i * WIDTH) + j])DBlock.deleteBlock(world, shiftX +i, k, shiftZ + j, flooded);
						 else if(noHighDegenerate(theRoom, world, shiftX + i, k, shiftZ + j))
							 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[(i * WIDTH) + j]);
					 for(int k = nCeilY[(i * WIDTH) + j]; k < ceilY[(i * WIDTH) + j]; k++) 
						 if(noHighDegenerate(theRoom, world, shiftX + i, k, shiftZ + j))
							 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[(i * WIDTH) + j]);
					 if(isFence[(i * WIDTH) + j]) 
						 DBlock.place(world, shiftX + i, floorY[(i * WIDTH) + j], shiftZ + j, dungeon.fenceBlock);
					 
					 if(isDoor[(i * WIDTH) + j]) {
						 DBlock.deleteBlock(world, shiftX + i, floorY[(i * WIDTH) + j],     shiftZ + j, flooded);
						 DBlock.deleteBlock(world, shiftX + i, floorY[(i * WIDTH) + j] + 1, shiftZ + j, flooded);
						 DBlock.deleteBlock(world, shiftX + i, floorY[(i * WIDTH) + j] + 2, shiftZ + j, flooded);
					 }
					 
					 // Liquids
					 if(hasLiquid[(i * WIDTH) + j] && (!isWall[(i * WIDTH) + j] && !isDoor[(i * WIDTH) + j])
							 && !world.isAirBlock(new BlockPos(shiftX + i, floorY[(i * WIDTH) + j] - 1, shiftZ + j))) 
						 DBlock.place(world, shiftX + i, floorY[(i * WIDTH) + j], shiftZ + j, theRoom.liquidBlock);					 
				}
			}	
		
		//MinecraftForge.EVENT_BUS.post(new DLDEvent.AfterBuild(this, shiftX, shiftZ, flooded));
		DoomlikeDungeons.profiler.endTask("Building Dungeon architecture");
		dungeon.addTileEntities();	
		dungeon.addEntrances();
		DoomlikeDungeons.profiler.endTask("Building Dungeon in World");
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
	private boolean noHighDegenerate(Room theRoom,World world, int x, int y, int z) {
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
	private boolean noLowDegenerate(Room theRoom, World world, int x, int y, int z, int i, int j) {
		return !(theRoom.degenerateFloors 
				&& world.isAirBlock(new BlockPos(x, y, z))
				&& !astared[(i * WIDTH) + j]);
	}
	
	
	/**
	 * The lowest height to place air or wall; walls may 
	 * go one block lower.
	 * 
	 * @param i dungeon x coordinate
	 * @param j dungeon z coordinate
	 * @return lowest height to place a wall or air/water block.
	 */
	private int roomBottom(int i, int j) {
		int b = floorY[(i * WIDTH) + j];
		if(isWall[(i * WIDTH) + j] && !isDoor[(i * WIDTH) + j]) b--;
		return b;		
	}
	

}
