package jaredbgreat.dldungeons.planner.mapping;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.ThemeFlags;
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
	private static final Block lapis = Block.getBlockFromName("lapis_block");
	private static final Block slab  = Block.getBlockFromName("double_stone_slab");
	private static final Block gold  = Block.getBlockFromName("gold_block");
	private static final Block glass = Block.getBlockFromName("glass");
	
	private static boolean drawFlyingMap = false;
	
	public final World world;
	public final int   chunkX, chunkZ, origenX, origenZ;
	
	// map of heights to build at
	public byte[][] ceilY;		// Ceiling height
	public byte[][] floorY;		// Floor Height	
	public byte[][] nCeilY;		// Height of Neighboring Ceiling	
	public byte[][] nFloorY;	// Height of Neighboring Floor
	
	// Blocks referenced against the DBlock.registry	
	public int[][] ceiling;
	public int[][] wall;
	public int[][] floor;
	
	// The room id (index of the room in the dungeons main RoomList)
	public int[][] room;
	
	// Is it a wall?
	public boolean[][] isWall;	    // Is this coordinate occupied by a wall?
	public boolean[][] isFence;	    // Is this coordinate occupied by a wall?
	public boolean[][] hasLiquid;	// Is floor covered by a liquid block?
	public boolean[][] isDoor;		// Is there a door here?
	
	//The A* scratch pad
	public Step    nodedge[][];
	public boolean astared[][];
	
	
	public MapMatrix(int width, World world, int chunkX, int chunkZ) {
		this.world = world;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		origenX   = (chunkX * 16) - (width / 2) + 8;
		origenZ   = (chunkZ * 16) - (width / 2) + 8;
		ceilY  	  = new byte[width][width];
		floorY    = new byte[width][width];
		nCeilY 	  = new byte[width][width];
		nFloorY   = new byte[width][width];
		room      = new int[width][width];
		ceiling   = new int[width][width];
		wall	  = new int[width][width];
		floor	  = new int[width][width];
		isWall	  = new boolean[width][width];
		isFence	  = new boolean[width][width];
		hasLiquid = new boolean[width][width];	
		isDoor    = new boolean[width][width];
		nodedge   = new Step[width][width];
		astared   = new boolean[width][width];
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
		int shiftX = (chunkX * 16) - (room.length / 2) + 8;
		int shiftZ = (chunkZ * 16) - (room.length / 2) + 8;
		int below;
		boolean flooded = dungeon.theme.flags.contains(ThemeFlags.WATER);
		MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.BeforeBuild(this, shiftX, shiftZ, flooded));
		
		for(int i = 0; i < room.length; i++)
			for(int j = 0; j < room.length; j++) {
				if(room[i][j] != 0) {
					 Room theRoom = dungeon.rooms.get(room[i][j]);
					 
					 // Debugging code; should not normally run
					 if(drawFlyingMap) {
						 if(astared[i][j]) {
							 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, lapis);
						 } else if(isDoor[i][j]) {
							 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, slab);
						 } else if(isWall[i][j]) {
							 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, gold);
						 } else {
							 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, glass);
						 }
					 }
					 
					 // Lower parts of the room
					 if(nFloorY[i][j] < floorY[i][j])
						 for(int k = nFloorY[i][j]; k < floorY[i][j]; k++) 
							 if(noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j))
								 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 if(nFloorY[i][j] > floorY[i][j])
						 for(int k = floorY[i][j]; k < nFloorY[i][j]; k++) 
							 if(noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j))
								 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 
					 if(noLowDegenerate(theRoom, shiftX + i, floorY[i][j] - 1, shiftZ + j, i, j)) {
						 DBlock.place(world, shiftX + i, floorY[i][j] - 1, shiftZ + j, floor[i][j]);
						 if(dungeon.theme.buildFoundation) {
							 below = nFloorY[i][j] < floorY[i][j] ? nFloorY[i][j] - 1 : floorY[i][j] - 2;
							 while(!DBlock.isGroundBlock(world, shiftX + i, below, shiftZ + j)) {
								 DBlock.place(world, shiftX + i, below, shiftZ + j, dungeon.floorBlock);
						 		below--;
						 		if(below < 0) break;						 		
						 	 }
						}
					 }
					 
					 // Upper parts of the room
					 if(!theRoom.sky 
							 && noHighDegenerate(theRoom, shiftX + i, ceilY[i][j] + 1, shiftZ + j))
						 DBlock.place(world, shiftX + i, ceilY[i][j] + 1, shiftZ + j, ceiling[i][j]);
					
					 for(int k = roomBottom(i, j); k <= ceilY[i][j]; k++)
						 if(!isWall[i][j])DBlock.deleteBlock(world, shiftX +i, k, shiftZ + j, flooded);
						 else if(noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j))
							 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 for(int k = nCeilY[i][j]; k < ceilY[i][j]; k++) 
						 if(noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j))
							 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 if(isFence[i][j]) 
						 DBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, dungeon.fenceBlock);
					 
					 if(isDoor[i][j]) {
						 DBlock.deleteBlock(world, shiftX + i, floorY[i][j],     shiftZ + j, flooded);
						 DBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 1, shiftZ + j, flooded);
						 DBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 2, shiftZ + j, flooded);
					 }
					 
					 // Liquids
					 if(hasLiquid[i][j] && (!isWall[i][j] && !isDoor[i][j])
							 && !world.isAirBlock(new BlockPos(shiftX + i, floorY[i][j] - 1, shiftZ + j))) 
						 DBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, theRoom.liquidBlock);					 
				}
			}	
		
		MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.AfterBuild(this, shiftX, shiftZ, flooded));
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
