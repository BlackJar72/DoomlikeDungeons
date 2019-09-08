package jaredbgreat.dldungeons.planner.mapping;

import java.util.Random;

import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.structure.DungeonStructure;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class ChunkMap extends StructurePiece {
	public static final int WIDTH = 16;
	public static final int SIZE = WIDTH * WIDTH;
	
	private int wx,  wz;  // X and Z coords of chunk (lower corner)
	private int cdx, cdz; // Chunk coords of the dungeon center	
	
	// map of heights to build at
	private byte[] ceilY;		// Ceiling height
	private byte[] floorY;		// Floor Height	
	private byte[] nCeilY;		// Height of Neighboring Ceiling	
	private byte[] nFloorY;	    // Height of Neighboring Floor
	
	// Blocks referenced against the DBlock.registry	
	private int[] ceiling;
	private int[] wall;
	private int[] floor;
	
	// The room id (index of the room in the dungeons main RoomList)
	private int[] room;
	
	// Is it a wall?
	private boolean[] isWall;	    // Is this coordinate occupied by a wall?
	private boolean[] isFence;	    // Is this coordinate occupied by a wall?
	private boolean[] hasLiquid;	// Is floor covered by a liquid block?
	private boolean[] isDoor;		// Is there a door here?
	
	//The A* scratch pad
	private boolean[] astared;
	
	
	public ChunkMap(TemplateManager tm, CompoundNBT nbt) {
		super(DungeonStructure.CHUNK_MAP, nbt);
		wx = nbt.getInt("x");
		wz = nbt.getInt("z");
		cdx = nbt.getInt("cdx");
		cdz = nbt.getInt("cdz");
		ceilY     = getByteArray(nbt, "ceily");
		floorY    = getByteArray(nbt, "floory");	
		nCeilY    = getByteArray(nbt, "nceily");	
		nFloorY   = getByteArray(nbt, "nfloory");	
		ceiling   = getIntArray(nbt, "ceiling");
		wall      = getIntArray(nbt, "wall");
		floor     = getIntArray(nbt, "floor");
		room      = getIntArray(nbt, "room");
		isWall    = getBooleanArray(nbt, "iswall");
		isFence   = getBooleanArray(nbt, "isfence");
		hasLiquid = getBooleanArray(nbt, "hasliquid");
		isDoor    = getBooleanArray(nbt, "isdoor");
		astared   = getBooleanArray(nbt, "astared");
	}
	


	@Override
	protected void readAdditional(CompoundNBT nbt) {
	    nbt.putInt("x", wx);
	    nbt.putInt("y", 64);
	    nbt.putInt("z", wz);
	    nbt.putInt("cdx", cdx);
	    nbt.putInt("cdz", cdz);
		nbt.putByteArray("ceily", ceilY);
		nbt.putByteArray("floory", floorY);	
		nbt.putByteArray("nceily", nCeilY);	
		nbt.putByteArray("nfloory", nFloorY);	
		nbt.putIntArray("ceiling", ceiling);
		nbt.putIntArray("wall", wall);
		nbt.putIntArray("floor", floor);
		nbt.putIntArray("room", room);
		putBooleanArray(nbt, "iswall", isWall);
		putBooleanArray(nbt, "isfence", isFence);
		putBooleanArray(nbt, "hasliquid", hasLiquid);
		putBooleanArray(nbt, "isdoor", isDoor);
		putBooleanArray(nbt, "astared", astared);
	}
	
	
	private byte[] getByteArray(CompoundNBT nbt, String key) {
		byte[] out = nbt.getByteArray(key);
		if(out.length < SIZE) {
			out = new byte[SIZE];
		}
		return out;
	}
	
	
	private int[] getIntArray(CompoundNBT nbt, String key) {
		int[] out = nbt.getIntArray(key);
		if(out.length < SIZE) {
			out = new int[SIZE];
		}
		return out;
	}
	
	
	private boolean[] getBooleanArray(CompoundNBT nbt, String key) {
		boolean[] out = new boolean[SIZE];
		byte[] data = nbt.getByteArray(key);
		if(data.length == SIZE) {
			for(int i = 0; i < SIZE; i++) {
				out[i] = data[i] != 0;
			}
		}
		return out;
	}
	
	
	private void putBooleanArray(CompoundNBT nbt, String key, boolean[] value) {
		byte[] out = new byte[value.length];
		for(int i = 0; i < value.length; i++) {
			// C or C++ would do this so much better!
			if(value[i]) out[i] = 1;
		}
		nbt.putByteArray(key, out);
	}
	
	
	public void doBB() {
		boundingBox = new MutableBoundingBox(wx, 0, wz, wx + 15, 256, wz + 15);
	}
	
	
	/*------------------------------------------------------------*/
	/*                        SETTERS                             */
	/*------------------------------------------------------------*/
	
	
	public void setLocationData(int chunkX, int chunkZ, int dungeonX, int dungeonZ) {
		wx  = chunkX * 16;
		wz  = chunkZ * 16;
		cdx = dungeonX;
		cdz = dungeonZ;
	}
	
	
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
	
	public void setAStared(boolean val, int x, int z) {
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
	
	public boolean getAStared(int x, int z) {
		return astared[(x % WIDTH) + z];
	}
	
	/*------------------------------------------------------------*/
	/*                        BUILDING                            */
	/*------------------------------------------------------------*/
		
	
	@Override
	public boolean addComponentParts(IWorld world, Random random, MutableBoundingBox sbb,
			ChunkPos cpos) {
		// TODO: Auto-generated method stub
		// TODO: I need a way to catalogue and store dungeons for retrieval
		// TODO: Once I can get the dungeon this belongs to I need to do this:
		//       build(dungeon, world);
		return false;
	}
	
	
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
							 if(noLowDegenerate(theRoom, world, wx + i, k, wz + j, i, j))
								 DBlock.place(world, wx + i, k, wz + j, wall[(i * WIDTH) + j]);
					 if(nFloorY[(i * WIDTH) + j] > floorY[(i * WIDTH) + j])
						 for(int k = floorY[(i * WIDTH) + j]; k < nFloorY[(i * WIDTH) + j]; k++) 
							 if(noLowDegenerate(theRoom, world, wx + i, k, wz + j, i, j))
								 DBlock.place(world, wx + i, k, wz + j, wall[(i * WIDTH) + j]);
					 
					 if(noLowDegenerate(theRoom, world, wx + i, floorY[(i * WIDTH) + j] - 1, wz + j, i, j)) {
						 DBlock.place(world, wx + i, floorY[(i * WIDTH) + j] - 1, wz + j, floor[(i * WIDTH) + j]);
						 if(dungeon.theme.buildFoundation) {
							 below = nFloorY[(i * WIDTH) + j] < floorY[(i * WIDTH) + j] ? nFloorY[(i * WIDTH) + j] - 1 : floorY[(i * WIDTH) + j] - 2;
							 while(!DBlock.isGroundBlock(world, wx + i, below, wz + j)) {
								 DBlock.place(world, wx + i, below, wz + j, dungeon.floorBlock);
						 		below--;
						 		if(below < 0) break;						 		
						 	 }
						}
					 }
					 
					 // Upper parts of the room
					 if(!theRoom.sky 
							 && noHighDegenerate(theRoom, world, wx + i, ceilY[(i * WIDTH) + j] + 1, wz + j))
						 DBlock.place(world, wx + i, ceilY[(i * WIDTH) + j] + 1, wz + j, ceiling[(i * WIDTH) + j]);
					
					 for(int k = roomBottom(i, j); k <= ceilY[(i * WIDTH) + j]; k++)
						 if(!isWall[(i * WIDTH) + j])DBlock.deleteBlock(world, wx +i, k, wz + j, flooded);
						 else if(noHighDegenerate(theRoom, world, wx + i, k, wz + j))
							 DBlock.place(world, wx + i, k, wz + j, wall[(i * WIDTH) + j]);
					 for(int k = nCeilY[(i * WIDTH) + j]; k < ceilY[(i * WIDTH) + j]; k++) 
						 if(noHighDegenerate(theRoom, world, wx + i, k, wz + j))
							 DBlock.place(world, wx + i, k, wz + j, wall[(i * WIDTH) + j]);
					 if(isFence[(i * WIDTH) + j]) 
						 DBlock.place(world, wx + i, floorY[(i * WIDTH) + j], wz + j, dungeon.fenceBlock);
					 
					 if(isDoor[(i * WIDTH) + j]) {
						 DBlock.deleteBlock(world, wx + i, floorY[(i * WIDTH) + j],     wz + j, flooded);
						 DBlock.deleteBlock(world, wx + i, floorY[(i * WIDTH) + j] + 1, wz + j, flooded);
						 DBlock.deleteBlock(world, wx + i, floorY[(i * WIDTH) + j] + 2, wz + j, flooded);
					 }
					 
					 // Liquids
					 if(hasLiquid[(i * WIDTH) + j] && (!isWall[(i * WIDTH) + j] && !isDoor[(i * WIDTH) + j])
							 && !world.isAirBlock(new BlockPos(wx + i, floorY[(i * WIDTH) + j] - 1, wz + j))) 
						 DBlock.place(world, wx + i, floorY[(i * WIDTH) + j], wz + j, theRoom.liquidBlock);					 
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
