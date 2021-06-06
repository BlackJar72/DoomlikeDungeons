package jaredbgreat.dldungeons.planner.mapping;

import java.util.ArrayList;
import java.util.List;

import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.builder.BlockFamily;
import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChunkMatrix {	private static final Block lapis = Block.getBlockFromName("lapis_block");
	private static final Block slab  = Block.getBlockFromName("double_stone_slab");
	private static final Block gold  = Block.getBlockFromName("gold_block");
	private static final Block glass = Block.getBlockFromName("glass");
	
	private static boolean drawFlyingMap = false;
	
	final World world;
	final int   xoff, zoff;
	boolean     hasEntrance;
	
	final List<Spawner> spawners;
	final List<BasicChest> chests;
	
	// map of heights to build at
	byte[][] ceilY;		// Ceiling height
	byte[][] floorY;		// Floor Height	
	byte[][] nCeilY;		// Height of Neighboring Ceiling	
	byte[][] nFloorY;	// Height of Neighboring Floor
	
	// Blocks referenced against the DBlock.registry	
	int[][] ceiling;
	int[][] wall;
	int[][] floor;
	
	// The room id (index of the room in the dungeons main RoomList)
	int[][] room;
	
	// Is it a wall?
	boolean[][] isWall;	    // Is this coordinate occupied by a wall?
	boolean[][] isFence;	// Is this coordinate occupied by a wall?
	boolean[][] hasLiquid;	// Is floor covered by a liquid block?
	boolean[][] isDoor;		// Is there a door here?
	
	//The A* scratch pad
	Step    nodedge[][];
	boolean astared[][];
	
	
	public ChunkMatrix(World world, int chunkX, int chunkZ) {
		this.world = world;
		xoff = chunkX * 16;
		zoff = chunkZ * 16;
		ceilY  	  = new byte[16][16];
		floorY    = new byte[16][16];
		nCeilY 	  = new byte[16][16];
		nFloorY   = new byte[16][16];
		room      = new int[16][16];
		ceiling   = new int[16][16];
		wall	  = new int[16][16];
		floor	  = new int[16][16];
		isWall	  = new boolean[16][16];
		isFence	  = new boolean[16][16];
		hasLiquid = new boolean[16][16];	
		isDoor    = new boolean[16][16];
		nodedge   = new Step[16][16];
		astared   = new boolean[16][16];
		spawners  = new ArrayList<>();
		chests    = new ArrayList<>(); 
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
		BlockFamily.setRadnom(dungeon.random);
		int below;
		boolean flooded = dungeon.theme.flags.contains(ThemeFlags.WATER);
		
		for(int i = 0; i < room.length; i++)
			for(int j = 0; j < room.length; j++) {
				if(room[i][j] != 0) {
					 Room theRoom = dungeon.rooms.get(room[i][j]);
					 
					 // Debugging code; should not normally run
					 if(drawFlyingMap) {
						 if(astared[i][j]) {
							 RegisteredBlock.placeBlock(world, xoff + i, 96, zoff +j, lapis);
						 } else if(isDoor[i][j]) {
							 RegisteredBlock.placeBlock(world, xoff + i, 96, zoff +j, slab);
						 } else if(isWall[i][j]) {
							 RegisteredBlock.placeBlock(world, xoff + i, 96, zoff +j, gold);
						 } else {
							 RegisteredBlock.placeBlock(world, xoff + i, 96, zoff +j, glass);
						 }
					 }
					 

					 // Fix bad heights
					 if(nFloorY[i][j] < 1) {
						 nFloorY[i][j] = (byte) dungeon.baseHeight;
					 }
					 if(floorY[i][j] < 1) {
						 floorY[i][j] = (byte) dungeon.baseHeight;
					 }
					 
					 // Lower parts of the room
					 if((nFloorY[i][j] < floorY[i][j]) && (nFloorY[i][j] > 0))
						 for(int k = nFloorY[i][j]; k < floorY[i][j]; k++) 
							 if(noLowDegenerate(theRoom, xoff + i, k, zoff + j, i, j))
								 RegisteredBlock.place(world, xoff + i, k, zoff + j, wall[i][j]);
					 if((nFloorY[i][j] > floorY[i][j]) && (floorY[i][j] > 0))
						 for(int k = floorY[i][j]; k < nFloorY[i][j]; k++) 
							 if(noLowDegenerate(theRoom, xoff + i, k, zoff + j, i, j))
								 RegisteredBlock.place(world, xoff + i, k, zoff + j, wall[i][j]);
					 
					 if(noLowDegenerate(theRoom, xoff + i, floorY[i][j] - 1, zoff + j, i, j)) {
						 RegisteredBlock.place(world, xoff + i, floorY[i][j] - 1, zoff + j, floor[i][j]);
						 if(dungeon.theme.buildFoundation) {
							 below = nFloorY[i][j] < floorY[i][j] ? nFloorY[i][j] - 1 : floorY[i][j] - 2;
							 while(!RegisteredBlock.isGroundBlock(world, xoff + i, below, zoff + j)) {
								 RegisteredBlock.place(world, xoff + i, below, zoff + j, dungeon.floorBlock);
						 		below--;
						 		if(below < 1) break;						 		
						 	 }
						}
					 }
					 
					 // Upper parts of the room
					 if(!theRoom.sky 
							 && noHighDegenerate(theRoom, xoff + i, ceilY[i][j] + 1, zoff + j))
						 RegisteredBlock.place(world, xoff + i, ceilY[i][j] + 1, zoff + j, ceiling[i][j]);
					
					 for(int k = roomBottom(i, j); k <= ceilY[i][j]; k++)
						 if(!isWall[i][j]) {RegisteredBlock.deleteBlock(world, xoff +i, k, zoff + j, 
								 theRoom.airBlock);
						 }
						 else if(noHighDegenerate(theRoom, xoff + i, k, zoff + j))
							 RegisteredBlock.place(world, xoff + i, k, zoff + j, wall[i][j]);
					 for(int k = nCeilY[i][j]; k < ceilY[i][j]; k++) 
						 if(noHighDegenerate(theRoom, xoff + i, k, zoff + j))
							 RegisteredBlock.place(world, xoff + i, k, zoff + j, wall[i][j]);
					 if(isFence[i][j]) 
						 RegisteredBlock.place(world, xoff + i, floorY[i][j], zoff + j, theRoom.fenceBlock);
					 
					 if(isDoor[i][j]) {
						 RegisteredBlock.deleteBlock(world, xoff + i, floorY[i][j],     zoff + j, flooded);
						 RegisteredBlock.deleteBlock(world, xoff + i, floorY[i][j] + 1, zoff + j, flooded);
						 RegisteredBlock.deleteBlock(world, xoff + i, floorY[i][j] + 2, zoff + j, flooded);
					 }
					 
					 // Liquids
					 if(hasLiquid[i][j] && (!isWall[i][j] && !isDoor[i][j])
							 && !world.isAirBlock(new BlockPos(xoff + i, floorY[i][j] - 1, zoff + j))) 
						 RegisteredBlock.place(world, xoff + i, floorY[i][j], zoff + j, theRoom.liquidBlock);					 
				}
			}	
		
		DoomlikeDungeons.profiler.endTask("Building Dungeon architecture");
		// TODO: Build chests, spawners, and entrances for chunk
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
	private boolean noLowDegenerate(Room theRoom, int x, int y, int z, int i, int j) {
		return !(theRoom.degenerateFloors 
				&& world.isAirBlock(new BlockPos(x, y, z))
				&& !astared[i][j]);
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
		int b = floorY[i][j];
		if(isWall[i][j] && !isDoor[i][j]) b--;
		return b;		
	}
	
	
	/**
	 * Sets whether the flying debug map should be drawn.
	 * 
	 * @param value
	 */
	public static void setDrawFlyingMap(boolean value) {
		drawFlyingMap = value;
	}

}
