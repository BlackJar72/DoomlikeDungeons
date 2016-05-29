package jaredbgreat.dldungeons.planner;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.pieces.entrances.SimpleEntrance;
import jaredbgreat.dldungeons.pieces.entrances.SpiralStair;
import jaredbgreat.dldungeons.pieces.entrances.TopRoom;
import jaredbgreat.dldungeons.planner.astar.DoorChecker;
import jaredbgreat.dldungeons.planner.mapping.MapMatrix;
import jaredbgreat.dldungeons.rooms.Cave;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.rooms.RoomList;
import jaredbgreat.dldungeons.themes.BiomeLists;
import jaredbgreat.dldungeons.themes.Degree;
import jaredbgreat.dldungeons.themes.Sizes;
import jaredbgreat.dldungeons.themes.Theme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;


/**
 * A representation of a dungeon level; as multi-level dungeons are not generated this is,
 * by extension, a dungeon.  This class holds all the dungeon wide information as well
 * as the 2D map (MapMatrix) of the dungeon and its list of rooms.
 * 
 * Methods of this class are responsible for determining the dungeon wide info it holds and 
 * and for layout the rooms and other features.
 * 
 * @author Jared Blackburn
 *
 */
public class Dungeon {
	
	public Theme theme;
	public Random random;
	public BiomeGenBase biome;
	
	public MapMatrix map;   // 2D layout of the dungeon
	public Node[] nodes;    // Main rooms (entrances and destination) which other rooms connect
	public int numNodes;    
	public int roomCount;
	public int entrancePref;
	
	public int baseHeight;  // Default floor height for the dungeon
	public int numEntrances = 0;
	
	public RoomList rooms;
	public RoomList nodeRooms;
	public ArrayList<Room> planter;
	public ArrayList<Room> grower;
	
	// Planning parameters
	public Sizes   size;
	
	// Not sure if I'll use all of them...	
	public Degree outside;		// Roofless rooms (also wall-less, but may have fences)
	public Degree liquids;		// Quantity of water / lava pools
	public Degree subrooms;	// Rooms budding of from the main one
	public Degree islands;		// Rooms inside rooms	
	public Degree pillars;		// Uh, pillars / columns, duh!
	public Degree fences;		// Uh, fences, duh!
	public Degree symmetry;	// How symmetrical rooms are (technically, chance of axis mirroring)
	public Degree variability;	// Inconsistency, that chance of using a different style in some place
	public Degree degeneracy;	// Chance of walls / ceilings not spawning over airblocks (idea taken from Greymerk)
	public Degree complexity;	// Basically how many shape primitives to add; depth of added place seeds
	public Degree verticle;	// How many height change and how much height change
	public Degree entrances;	// Ways in and outS
	public Degree bigRooms;    // Not currently used, but for oversided rooms between 1 and 2 time the normal max
	public Degree naturals;    // Cave like areas created with celluar automata
	
	// Default blocks
	public int wallBlock1;
	public int floorBlock;
	public int cielingBlock;
	public int stairBlock;
	public int stairSlab;
	public int fenceBlock;
	public int cornerBlock;
	public int liquidBlock;
	public int caveBlock;
	
	int shiftX;
	int shiftZ;
	
	
	/**
	 * De-links all referenced objects as a safety measure against memory leaks, 
	 * which the complexity creates a risk for.  This might not be needed, as 
	 * circular have been checked for.  
	 */
	public void preFinalize() {
		if(theme != null) {
			for(int i = 0; i < nodes.length; i++) nodes[i] = null;
			for(Room room: rooms) {
				room.preFinalize();
				room = null;
			}
			rooms.clear();
		}
		rooms = null;
		planter = grower = null;
		nodes = null;
		theme =  null;
		random = null;
		biome = null;
		map = null;		
		size = null;	
		outside = null;
		liquids = null;
		subrooms = null;
		islands = null;	
		pillars = null;
		symmetry = null;
		variability = null;
		degeneracy = null;
		complexity = null;
		verticle = null;
		entrances = null;
		bigRooms = null;
		naturals = null;
	}

	
	public Dungeon(Random rnd, BiomeGenBase biome, World world, int chunkX, int chunkZ) throws Throwable {
		DoomlikeDungeons.profiler.startTask("Planning Dungeon");
		DoomlikeDungeons.profiler.startTask("Layout dungeon (rough draft)");
		random = rnd;
		this.biome = biome;
		theme = BiomeLists.getTheme(biome, random);
		if(theme == null) return;
		
		applyTheme();
		entrancePref = random.nextInt(3);		
		
		wallBlock1   = theme.walls[random.nextInt(theme.walls.length)];
		floorBlock   = theme.floors[random.nextInt(theme.floors.length)];
		cielingBlock = theme.ceilings[random.nextInt(theme.ceilings.length)];
		fenceBlock   = theme.fencing[random.nextInt(theme.fencing.length)];
		cornerBlock  = theme.pillarBlock[random.nextInt(theme.pillarBlock.length)];
		liquidBlock  = theme.liquid[random.nextInt(theme.liquid.length)];
		caveBlock    = theme.caveWalls[random.nextInt(theme.caveWalls.length)];
		
		rooms = new RoomList(size.maxRooms + 1);
		planter = new ArrayList<Room>();
		map = new MapMatrix(size.width, world, chunkX, chunkZ);
		numNodes = random.nextInt(size.maxNodes - size.minNodes + 1) + size.minNodes + 1;
		nodes = new Node[numNodes];
		
		shiftX = (map.chunkX * 16) - (map.room.length / 2) + 8;
		shiftZ = (map.chunkZ * 16) - (map.room.length / 2) + 8;
		
		makeNodes();
		if((numEntrances < 1) && ConfigHandler.easyFind) addAnEntrance();
		connectNodes();
		growthCycle();	
		DoomlikeDungeons.profiler.endTask("Layout dungeon (rough draft)");
		DoomlikeDungeons.profiler.startTask("Fixing room contents");
		fixRoomContents();
		DoomlikeDungeons.profiler.endTask("Fixing room contents");
		DoomlikeDungeons.profiler.endTask("Planning Dungeon");
	}
	
	
	/**
	 * Set all the dungeon wide theme derived variables that are
	 * of type Degree.
	 */
	private void applyTheme() {
		size  	 	= theme.sizes.select(random);
		outside 	= theme.outside.select(random);
		liquids	 	= theme.liquids.select(random);
		subrooms 	= theme.subrooms.select(random);
		islands	 	= theme.islands.select(random);
		pillars	 	= theme.pillars.select(random);
		symmetry 	= theme.symmetry.select(random);
		variability = theme.variability.select(random);
		degeneracy	= theme.degeneracy.select(random);
		complexity	= theme.complexity.select(random);
		verticle	= theme.verticle.select(random);
		entrances	= theme.entrances.select(random);
		fences		= theme.fences.select(random);
		naturals    = theme.naturals.select(random);
		baseHeight  = random.nextInt(theme.maxY - theme.minY) + theme.minY;
	}
	
	
	/**
	 * Creates all the nodes and store along with a list of node rooms.
	 */
	void makeNodes() {	
		//DoomlikeDungeons.profiler.startTask("Creating Node Rooms");
		nodeRooms = new RoomList(numNodes);
		for(int i = 0; i < numNodes;) {
			nodes[i] = new Node(random.nextInt(size.width), baseHeight, random.nextInt(size.width), random, this);
			assert(nodes[i].hubRoom == nodeRooms.get(i));
			if(nodeRooms.get(i) != null) ++i;
		}
		//DoomlikeDungeons.profiler.endTask("Creating Node Rooms");
	}
	
	
	/**
	 * This will connect all the nodes with series of intermediate rooms. 
	 * 
	 * Attempts are made to connect all nodes to all others, so as to 
	 * insure that all are connected.  This may change in a future update.
	 * 
	 * @throws Throwable
	 */
	void connectNodes() throws Throwable {		
		//DoomlikeDungeons.profiler.startTask("Connecting Nodes");
		Node first, other;
		for(int i = 0; i < nodes.length; i++) {
			first = nodes[i];
			if(first == null) continue;
			if(first.hubRoom == null) continue;
			for(int j = i + 1; j < nodes.length; j++) {
				other = nodes[j];
				if(other == null) continue;
				if(other.hubRoom == null) continue;
				if(rooms.realSize() >= size.maxRooms) {
					//DoomlikeDungeons.profiler.endTask("Connecting Nodes");
					return;
				}				
				if(other != first) {
					new Route(first, other).drawConnections(this);
				}
			}			
		}		
		//DoomlikeDungeons.profiler.endTask("Connecting Nodes");
	}
	
	
	public void growthCycle() {		
		//DoomlikeDungeons.profiler.startTask("Adding Rooms (growthCycle)");
		boolean doMore = true;
		do {
			doMore = false;
			grower = planter;
			Collections.shuffle(grower, random);
			planter = new ArrayList<Room>();
			for(Room room : grower) {
				if(rooms.realSize() >= size.maxRooms) {
					return;
				}
				if(room.plantChildren(this)) {
					doMore = true;
				}
			} 
		} while(doMore);		
		//DoomlikeDungeons.profiler.endTask("Adding Rooms (growthCycle)");
	}
	
	
	public void fixRoomContents() {
		for(Room room : rooms) {
			addChestBlocks(room);
			DoorChecker.processDoors1(this, room);
		}
		for(Room room : rooms) {	
			DoorChecker.processDoors2(this, room);	
		}
		for(Room room : rooms) {
			DoorChecker.processDoors3(this, room);
			if(room instanceof Cave) DoorChecker.caveConnector(this, room);
		}
		DoorChecker.checkConnectivity(this);
	}
	
	
	public void addTileEntities() {
		for(Room room : rooms) {
			addTileEntitesToRoom(room);
		}
	}
	
	
	public void addTileEntitesToRoom(Room room) {
			for(Spawner  spawner : room.spawners) {
					DBlock.placeSpawner(map.world, 
										shiftX + spawner.getX(), 
										spawner.getY(), 
										shiftZ + spawner.getZ(), 
										spawner.getMob());
			}
			for(BasicChest  chest : room.chests) {
				chest.place(map.world, shiftX + chest.mx, chest.my, shiftZ + chest.mz, random);
			}
	}
	
	
	public void addChestBlocks(Room room) {
		for(BasicChest  chest : room.chests) {
			DBlock.placeChest(map.world, shiftX + chest.mx, chest.my, shiftZ + chest.mz);
		}		
	}
	
	
	public void addEntrances() {
		for(Room room : nodeRooms) {
			if(room != null) addEntrance(room);
		}
	}
	
	
	public void addEntrance(Room room) {		
		if(!room.hasEntrance) return;
		//DoomlikeDungeons.profiler.startTask("Adding Entrances");
		int entrance;
		if(variability.use(random)) entrance = random.nextInt(3);
		else entrance = entrancePref; 
		if(ConfigHandler.easyFind) entrance = 1;
				
		switch (entrance) {
		case 0:
			//DoomlikeDungeons.profiler.startTask("Adding Sriral Stair");
			new SpiralStair((int)room.realX, (int)room.realZ).build(this, map.world);
			//DoomlikeDungeons.profiler.endTask("Adding Sriral Stair");
			break;
		case 1:
			//DoomlikeDungeons.profiler.startTask("Adding Top Room");
			new TopRoom((int)room.realX, (int)room.realZ).build(this, map.world);
			//DoomlikeDungeons.profiler.endTask("Adding Top Room");
			break;
		case 2:
		default:
			//DoomlikeDungeons.profiler.startTask("Adding Simple Entrance");
			new SimpleEntrance((int)room.realX, (int)room.realZ).build(this, map.world);
			//DoomlikeDungeons.profiler.endTask("Adding Simple Entrance");
			break;
		}		
		//DoomlikeDungeons.profiler.endTask("Adding Entrances");
	}
	
	
	public void addAnEntrance() {
		if(theme.entrances.never()) return;
		int which = random.nextInt(nodes.length);
		Room it   = nodes[which].hubRoom;
		it.chests.clear();
		it.spawners.clear();
		it.hasEntrance = true;
		it.hasSpawners = false;
		numEntrances = 1; // Probably never really needed :-/
		for(int i = (int)it.realX -2; i < ((int)it.realX + 2); i++)
			for(int j = (int)it.realZ - 2; j < ((int)it.realZ + 2); j++) {
				map.floorY[i][j] = (byte)it.floorY;
				map.hasLiquid[i][j] = false;
				map.isWall[i][j] = false;
		}	
	}
	
}


