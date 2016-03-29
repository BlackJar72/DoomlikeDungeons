package jaredbgreat.dldungeons.planner.mapping;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// More data intensive but perhaps simpler

public class MapMatrix {
	private static Block lapis = Block.getBlockFromName("lapis_block");
	private static Block slab  = Block.getBlockFromName("double_stone_slab");
	private static Block glow  = Block.getBlockFromName("gold_block");
	private static Block glass = Block.getBlockFromName("glass");
	
	public World world;
	public int chunkX, chunkZ, origenX, origenZ;
	
	// map of heights to build at
	public byte[][] ceilY;		// Ceiling height
	public byte[][] floorY;		// Floor Height	
	public byte[][] nCeilY;		// Height of Neighboring Ceiling	
	public byte[][] nFloorY;	// Height of Neighboring Floor
	
	// Blocks referenced against theme data	
	public int[][] ceiling;
	public int[][] wall;
	public int[][] floor;
	public int[][] room;		// The index of the room in an array or ArrayList
	
	// Is it a wall?
	public boolean[][] isWall;	    // Is this coordinate occupied by a wall?
	public boolean[][] isFence;	    // Is this coordinate occupied by a wall?
	public boolean[][] hasLiquid;	// Is floor covered by a liquid block?
	public boolean[][] isDoor;		// Is there a door here?
	
	//The A* scratch pad
	public Step nodedge[][];
	public boolean astared[][];
	
	@Override
	public void finalize() throws Throwable {
		world = null;
		super.finalize();
	}
	
	
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
	
	
	public void build(Dungeon dungeon) {		
		DoomlikeDungeons.profiler.startTask("Building Dungeon in World");	
		DoomlikeDungeons.profiler.startTask("Building Dungeon architecture");
		//System.out.println("Running map.build(Dungeon dungeon); generating dungeon");
		int shiftX = (chunkX * 16) - (room.length / 2) + 8;
		int shiftZ = (chunkZ * 16) - (room.length / 2) + 8;
		int below;
		boolean flooded = dungeon.theme.flags.contains(ThemeFlags.WATER);
		for(int i = 0; i < room.length; i++)
			for(int j = 0; j < room.length; j++) {
				//DoomlikeDungeons.profiler.startTask("Looking at Column " + i +  ", " + j);
				//System.out.println("Checking X = " + (shiftX + i) + ", Y = " + (shiftZ + j) + " for dungeon; " 
				//		+ " room #" + room[i][j]);
				if(room[i][j] != 0) {
					 //DoomlikeDungeons.profiler.startTask("Building Room " + room[i][j] + " column " + i + ", " +j);
					 Room theRoom = dungeon.rooms.get(room[i][j]);
					 
//					 if(astared[i][j]) {
//						 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, lapis);
//					 } else if(isDoor[i][j]) {
//						 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, slab);
//					 } else if(isWall[i][j]) {
//						 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, glass);
//					 } else {
//						 DBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, glow);
//					 }
					 
					 // Lower parts of the room

					 //DoomlikeDungeons.profiler.startTask("Building Height Variant");
					 if(nFloorY[i][j] < floorY[i][j])
						 for(int k = nFloorY[i][j]; k < floorY[i][j]; k++) 
							 if(noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j))
								 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 if(nFloorY[i][j] > floorY[i][j])
						 for(int k = floorY[i][j]; k < nFloorY[i][j]; k++) 
							 if(noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j))
								 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 //DoomlikeDungeons.profiler.endTask("Building Height Variant");
					 
					 if(noLowDegenerate(theRoom, shiftX + i, floorY[i][j] - 1, shiftZ + j, i, j)) {
						 //DoomlikeDungeons.profiler.startTask("Building Floor");
						 DBlock.place(world, shiftX + i, floorY[i][j] - 1, shiftZ + j, floor[i][j]);
						 if(dungeon.theme.buildFoundation) {
							 //DoomlikeDungeons.profiler.startTask("Building Foundation");
							 below = nFloorY[i][j] < floorY[i][j] ? nFloorY[i][j] - 1 : floorY[i][j] - 2;
							 while(!DBlock.isGroundBlock(world, shiftX + i, below, shiftZ + j)) {
								 DBlock.place(world, shiftX + i, below, shiftZ + j, dungeon.floorBlock);
						 		below--;
						 		if(below < 0) break;						 		
						 	 }
							 //DoomlikeDungeons.profiler.endTask("Building Foundation");
						}
						//DoomlikeDungeons.profiler.endTask("Building Floor");
					 }
					 
					 // Upper parts of the room
					 //DoomlikeDungeons.profiler.startTask("Building Cieling");
					 if(!theRoom.sky 
							 && noHighDegenerate(theRoom, shiftX + i, ceilY[i][j] + 1, shiftZ + j))
						 DBlock.place(world, shiftX + i, ceilY[i][j] + 1, shiftZ + j, ceiling[i][j]);
					 //DoomlikeDungeons.profiler.endTask("Building Cieling");
					 
					 //DoomlikeDungeons.profiler.startTask("Building Wall");
					 for(int k = roomBottom(i, j); k <= ceilY[i][j]; k++)
						 if(!isWall[i][j])DBlock.deleteBlock(world, shiftX +i, k, shiftZ + j, flooded);
						 else if(noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j))
							 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 for(int k = nCeilY[i][j]; k < ceilY[i][j]; k++) 
						 if(noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j))
							 DBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 if(isFence[i][j]) 
						 DBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, dungeon.fenceBlock);
					 //DoomlikeDungeons.profiler.endTask("Building Wall");
					 
					 //DoomlikeDungeons.profiler.startTask("Building Doorway");
					 if(isDoor[i][j]) {
						 DBlock.deleteBlock(world, shiftX + i, floorY[i][j],     shiftZ + j, flooded);
						 DBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 1, shiftZ + j, flooded);
						 DBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 2, shiftZ + j, flooded);
					 }
					 //DoomlikeDungeons.profiler.endTask("Building Doorway");
					 
					 // Liquids
					 //DoomlikeDungeons.profiler.startTask("Building Pool");
					 if(hasLiquid[i][j] && (!isWall[i][j] && !isDoor[i][j])
							 && !isAirBlock(world, shiftX + i, floorY[i][j] - 1, shiftZ + j)) 
						 DBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, theRoom.liquidBlock);
					 //DoomlikeDungeons.profiler.endTask("Building Pool");
					 //System.out.println("One column of the dungeon should be built!");
					 //DoomlikeDungeons.profiler.endTask("Building Room " + room[i][j] + " column " + i + ", " + j);
				}
				//DoomlikeDungeons.profiler.endTask("Looking at Column " + i +  ", " + j);
			}	
		DoomlikeDungeons.profiler.endTask("Building Dungeon architecture");
		dungeon.addTileEntities();	
		dungeon.addEntrances();
		DoomlikeDungeons.profiler.endTask("Building Dungeon in World");
	}
	
	
	private boolean noHighDegenerate(Room theRoom, int x, int y, int z) {
		return !(theRoom.degenerate && isAirBlock(world, x, y, z));
	}
	
	
	private boolean noLowDegenerate(Room theRoom, int x, int y, int z, int i, int j) {
		return !(theRoom.degenerateFloors 
				&& isAirBlock(world, x, y, z)
				&& !astared[i][j]);
	}
	
	
	private int roomBottom(int i, int j) {
		int b = floorY[i][j];
		if(isWall[i][j] && !isDoor[i][j]) b--;
		return b;		
	}
	
	
	private boolean isAirBlock(World world, int x, int y, int z) {
		// Trying stuff, may not work!
		return world.isAirBlock(new BlockPos(x, y, z));
	}
	
	
	
	

	
/*	
//	public void build(Dungeon dungeon) {
////		//System.out.println("Running map.build(Dungeon dungeon); generating dungeon");
////		int shiftX = (chunkX * 16) + 8;
////		int shiftZ = (chunkZ * 16) + 8;
////		int below;
////		for(int i = 0; i < room.length; i++)
////			for(int j = 0; j < room.length; j++) {
////				//System.out.println("Checking X = " + (shiftX + i) + ", Y = " + (shiftZ + j) + " for dungeon; " 
////				//		+ " room #" + room[i][j]);
////				if(room[i][j] != 0) {
////					 Room theRoom = dungeon.rooms.get(room[i][j]);
////					 
////					 Builder.placeBlock(world, shiftX, 96, shiftZ, Block.glowStone.blockID);
////					 
////					 // Lower parts of the room
////					 if(nFloorY[i][j] < floorY[i][j])
////						 for(int k = nFloorY[i][j]; k < floorY[i][j]; k++) 
////							 if(noLowDegenerate(theRoom, shiftX, 128, shiftZ))
////								 Builder.placeBlock(world, shiftX, 128, shiftZ, wall[i][j]);
////					 if(nFloorY[i][j] > floorY[i][j])
////						 for(int k = floorY[i][j]; k < nFloorY[i][j]; k++) 
////							 if(noLowDegenerate(theRoom, shiftX, 128, shiftZ))
////								 Builder.placeBlock(world, shiftX, 128, shiftZ, wall[i][j]);
////					 if(noLowDegenerate(theRoom, shiftX, 128, shiftZ)) { 
////						 Builder.placeBlock(world, shiftX, 128, shiftZ, floor[i][j]);
////						 if(dungeon.theme.buildFoundation) {
////						 below = nFloorY[i][j] < floorY[i][j] ? nFloorY[i][j] - 1 : floorY[i][j] - 2;
////						 while(!Builder.isGroundBlock(world, shiftX, 128, shiftZ)) {
////						 		Builder.placeBlock(world, shiftX, 128, shiftZ, dungeon.floorBlock);
////						 		below--;
////						 	} 
////						}
////					 }
////					 
////					 // Upper parts of the room
////					 if(!theRoom.sky 
////							 && noHighDegenerate(theRoom, shiftX, ceilY[i][j] + 1, shiftZ))
////						 Builder.placeBlock(world, shiftX, 128, shiftZ, ceiling[i][j]);
////					 for(int k = floorY[i][j]; k <= ceilY[i][j]; k++)
////						 if(!isWall[i][j])Builder.deleteBlock(world, shiftX, 128, shiftZ);
////						 else if(noHighDegenerate(theRoom, shiftX, 128, shiftZ))
////							 Builder.placeBlock(world, shiftX, 128, shiftZ, wall[i][j]);
////					 for(int k = nCeilY[i][j]; k < ceilY[i][j]; k++) 
////						 if(noHighDegenerate(theRoom, shiftX, 128, shiftZ))
////							 Builder.placeBlock(world, shiftX, 128, shiftZ, wall[i][j]);
////					 if(isFence[i][j]) 
////						 Builder.placeBlock(world, shiftX, 128, shiftZ, dungeon.fenceBlock);					 
////					 if(isDoor[i][j]) {
////						 Builder.deleteBlock(world, shiftX, 128,     shiftZ);
////						 Builder.deleteBlock(world, shiftX, 128, shiftZ);
////					 }
////					 
////					 // Liquids
////					 if(hasLiquid[i][j] && !isWall[i][j]
////							 && !world.isAirBlock(shiftX, 128, shiftZ)) 
////						 Builder.placeBlock(world, shiftX, 128, shiftZ, theRoom.liquidBlock);
////					 //System.out.println("One column of the dungeon should be built!");
////				}
////			}
////		dungeon.addSpawners();
//	}
*/
	
}
