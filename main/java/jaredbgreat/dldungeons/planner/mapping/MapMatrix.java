package jaredbgreat.dldungeons.planner.mapping;


import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.BlockFamily;
import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.pieces.entrances.AbstractEntrance;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.Sizes;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import jaredbgreat.dldungeons.util.cache.Coords;
import jaredbgreat.dldungeons.util.cache.IHaveCoords;
import jaredbgreat.dldungeons.util.debug.DebugOut;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
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
public class MapMatrix implements IHaveCoords {
	private static final Block lapis = Blocks.LAPIS_BLOCK;
	private static final Block slab  = Blocks.SMOOTH_STONE;
	private static final Block gold  = Blocks.GOLD_BLOCK;
	private static final Block glass = Blocks.GLASS;
	
	private static boolean drawFlyingMap = false;
	
	public final ISeedReader world;
	public final Coords coords;
	public final int   chunkX, chunkZ, lowCX, lowCZ, shiftX, shiftZ;
	
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
		
	public final ChunkFeatures[][] features;
	
	
	public MapMatrix(Sizes size, ISeedReader world, Coords coords) {
		this.world = world;
		this.coords = coords;
		chunkX = coords.getX();
		chunkZ = coords.getZ();
		lowCX = chunkX - size.chunkRadius;
		lowCZ = chunkZ - size.chunkRadius;
		ceilY  	  = new byte[size.width][size.width];
		floorY    = new byte[size.width][size.width];
		nCeilY 	  = new byte[size.width][size.width];
		nFloorY   = new byte[size.width][size.width];
		room      = new int[size.width][size.width];
		ceiling   = new int[size.width][size.width];
		wall	  = new int[size.width][size.width];
		floor	  = new int[size.width][size.width];
		isWall	  = new boolean[size.width][size.width];
		isFence	  = new boolean[size.width][size.width];
		hasLiquid = new boolean[size.width][size.width];	
		isDoor    = new boolean[size.width][size.width];
		nodedge   = new Step[size.width][size.width];
		astared   = new boolean[size.width][size.width];
		features  = new ChunkFeatures[size.chunkWidth][size.chunkWidth];
		for(int i = 0; i < size.chunkWidth; i++)
			for(int j = 0; j < size.chunkWidth; j++) {
				features[i][j] = new ChunkFeatures();
			}
		ChunkPos origen = new ChunkPos(lowCX, lowCZ);
		shiftX = origen.getMinBlockX();
		shiftZ = origen.getMinBlockZ();
	}
	
	
	public void addSpawner(Spawner spawner) {
		features[spawner.getX()/16][spawner.getZ()/16].addSpawner(spawner);
	}
	
	
	public void addChest(BasicChest chest) {
		features[chest.mx/16][chest.mz/16].addChest(chest);
	}
	
	
	public void addEntrance(AbstractEntrance entrance) {
		features[entrance.x/16][entrance.z/16].addEntrance(entrance);
	}
	
	
	/**
	 * This will build the dungeon into the world, transforming the information 
	 * mapped here in 2D arrays into the finished 3D structure in the Minecraft 
	 * world.
	 * 
	 * @param dungeon
	 * @throws Exception 
	 */
	public void buildInChunk(Dungeon dungeon, ChunkPos cp) throws Exception {
		int cx1 = cp.x - lowCX, cz1 = cp.z - lowCZ;
		if((cx1 < 0) || (cx1 >= dungeon.size.chunkWidth) || (cz1 < 0) || (cz1 >= dungeon.size.chunkWidth)) {
			return;
		}
		
		//DoomlikeDungeons.profiler.startTask("Building Dungeon in Chunk");	
		//DoomlikeDungeons.profiler.startTask("Building Dungeon architecture");
		BlockFamily.setRadnom(dungeon.random);
		int below;
		boolean flooded = dungeon.theme.flags.contains(ThemeFlags.WATER);
		MinecraftForge.EVENT_BUS.post(new DLDEvent.BeforeBuild(this, shiftX, shiftZ, flooded));
		
		int sx = cx1 * 16, ex = sx + 16;
		int sz = cz1 * 16, ez = sz + 16;
		
		//DebugOut.bigSysout("Building dungeon for " + coords + " in chunk " + cp.x + ", " + cp.z 
		//		+ "\n        Should be at: " + cp.getMinBlockX() + ", " + cp.getMinBlockZ() + " to " + cp.getMaxBlockX() + ", " + cp.getMaxBlockZ()
		//	    + "\nActually building at: " + (shiftX + sx) + ", " + (shiftZ + sz) + " to " + (shiftX + ex - 1) + ", " + (shiftZ + ez - 1));
		try {	
		for(int i = sx; i < ex; i++)
			for(int j = sz; j < ez; j++) {
				if(room[i][j] != 0) {
					 Room theRoom = dungeon.rooms.get(room[i][j]);
					 
					 // Debugging code; should not normally run
					 if(drawFlyingMap) {
						 if(astared[i][j]) {
							 RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, lapis);
						 } else if(isDoor[i][j]) {
							 RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, slab);
						 } else if(isWall[i][j]) {
							 RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, gold);
						 } else {
							 RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ +j, glass);
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
							 if(noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j))
								 RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 if((nFloorY[i][j] > floorY[i][j]) && (floorY[i][j] > 0))
						 for(int k = floorY[i][j]; k < nFloorY[i][j]; k++) 
							 if(noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j))
								 RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 
					 if(noLowDegenerate(theRoom, shiftX + i, floorY[i][j] - 1, shiftZ + j, i, j)) {
						 RegisteredBlock.place(world, shiftX + i, floorY[i][j] - 1, shiftZ + j, floor[i][j]);
						 if(dungeon.theme.buildFoundation) {
							 below = nFloorY[i][j] < floorY[i][j] ? nFloorY[i][j] - 1 : floorY[i][j] - 2;
							 while(!RegisteredBlock.isGroundBlock(world, shiftX + i, below, shiftZ + j)) {
								 RegisteredBlock.place(world, shiftX + i, below, shiftZ + j, dungeon.floorBlock);
						 		below--;
						 		if(below < 1) break;						 		
						 	 }
						}
					 }
					 
					 // Upper parts of the room
					 if(!theRoom.sky 
							 && noHighDegenerate(theRoom, shiftX + i, ceilY[i][j] + 1, shiftZ + j))
						 RegisteredBlock.place(world, shiftX + i, ceilY[i][j] + 1, shiftZ + j, ceiling[i][j]);
					
					 for(int k = roomBottom(i, j); k <= ceilY[i][j]; k++)
						 if(!isWall[i][j]) {RegisteredBlock.deleteBlock(world, shiftX +i, k, shiftZ + j, 
								 theRoom.airBlock);
						 }
						 else if(noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j))
							 RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 for(int k = nCeilY[i][j]; k < ceilY[i][j]; k++) 
						 if(noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j))
							 RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
					 if(isFence[i][j]) 
						 RegisteredBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, theRoom.fenceBlock);
					 
					 if(isDoor[i][j]) {
						 RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j],     shiftZ + j, theRoom.airBlock);
						 RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 1, shiftZ + j, theRoom.airBlock);
						 RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 2, shiftZ + j, theRoom.airBlock);
					 }
					 
					 // Liquids

					 if(hasLiquid[i][j] && (!isWall[i][j] && !isDoor[i][j])
							 && (!world.getBlockState(new BlockPos(shiftX + i, floorY[i][j] - 1, shiftZ + j))
									 .equals(Blocks.AIR.defaultBlockState()))) 
						 RegisteredBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, theRoom.liquidBlock);					 
				}
			}
		} catch(Exception e) {
			StringBuilder b = new StringBuilder(e.getLocalizedMessage());
			b.append(System.lineSeparator());
			b.append("MapMatrix.buildInChunkbuildInChunk(): Building range ");
			b.append((sx + shiftX) + "," + (sz + shiftZ));
			b.append(" to ");
			b.append((ex + shiftX - 1) + "," + (ez + shiftZ - 1));
			b.append(" in chunk at " + cp);
			b.append(" (blocks  ");
			b.append(cp.getMinBlockX() + "," + cp.getMinBlockZ());
			b.append(" to ");
			b.append(cp.getMaxBlockX() + "," + cp.getMaxBlockZ() + ")");
			b.append(System.lineSeparator());
			throw new Exception(b.toString());
		}
		
		//MinecraftForge.EVENT_BUS.post(new DLDEvent.AfterBuild(this, shiftX, shiftZ, flooded));
		//DoomlikeDungeons.profiler.endTask("Building Dungeon architecture");
		features[cx1][cz1].buildFeatures(dungeon, this, shiftX, shiftZ, world);
		//DoomlikeDungeons.profiler.endTask("Building Dungeon in Chunk");
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
		return !(theRoom.degenerate && isAirBlock(world, new BlockPos(x, y, z)));
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
				&& isAirBlock(world, new BlockPos(x, y, z))
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


	@Override
	public Coords getCoords() {
		return coords;
	}
	
	
	public static boolean isAirBlock(ISeedReader s, int x, int y, int z) {
		return (s.getBlockState(new BlockPos(x, y, z))
				 .equals(Blocks.AIR.defaultBlockState()));
	}
	
	
	public static boolean isAirBlock(ISeedReader s, BlockPos pos) {
		return (s.getBlockState(pos).equals(Blocks.AIR.defaultBlockState()));
	}
	
}
