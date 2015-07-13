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
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.rooms.RoomList;
import jaredbgreat.dldungeons.rooms.naturals.DldCave;
import jaredbgreat.dldungeons.themes.BiomeLists;
import jaredbgreat.dldungeons.themes.Degrees;
import jaredbgreat.dldungeons.themes.Sizes;
import jaredbgreat.dldungeons.themes.Theme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;


public class Dungeon {
	
	public Theme theme;
	public Random random;
	public BiomeGenBase biome;
	
	public MapMatrix map;
	public Node[] nodes;
	public int numNodes;
	public int roomCount;
	public int entrancePref;
	
	public int baseHeight;
	public int numEntrances = 0;
	
	public RoomList rooms;
	public RoomList nodeRooms;
	public ArrayList<Room> planter;
	public ArrayList<Room> grower;
	//TODO: Other Components
	
	// Planning parameters
	public Sizes   size;
	// Not sure if I'll use all of them...	
	public Degrees outside;		// Roofless rooms (also wall-less, but may have fences)
	public Degrees liquids;		// Quantity of water / lava pools
	public Degrees subrooms;	// Rooms budding of from the main one
	public Degrees islands;		// Rooms inside rooms	
	public Degrees pillars;		// Uh, pillars / columns, duh!
	public Degrees fences;		// Uh, fences, duh!
	public Degrees symmetry;	// How symmetrical rooms are (technically, chance of axis mirroring)
	public Degrees variability;	// Inconsistency, that chance of using a different style in some place
	public Degrees degeneracy;	// Chance of walls / ceilings not spawning over airblocks (idea taken from Greymerk)
	public Degrees complexity;	// Basically how many shape primitives to add; depth of added place seeds
	public Degrees verticle;	// How many height change and how much height change
	public Degrees entrances;	// Ways in and outS
	public Degrees bigRooms;
	public Degrees naturals;
	
	// Default blocks
	public int wallBlock1;
	public int floorBlock;
	public int cielingBlock;
	public int stairBlock;
	public int stairSlab;
	public int fenceBlock;
	public int cornerBlock;
	public int liquidBlock;
	
	
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

	
	public Dungeon(Random random, BiomeGenBase biome, World world, int chunkX, int chunkZ) throws Throwable {
		DoomlikeDungeons.profiler.startTask("Planning Dungeon");
		DoomlikeDungeons.profiler.startTask("Layout dungeon (rough draft)");
		//this.random = random;
		this.random = new Random(random.nextLong());
		this.biome = biome;
		theme = BiomeLists.getTheme(biome, random);
		if(theme == null) return;
		
		applyTheme();
		entrancePref = random.nextInt(3);		
		
		wallBlock1 = theme.walls[random.nextInt(theme.walls.length)];
		floorBlock = theme.floors[random.nextInt(theme.floors.length)];
		cielingBlock = theme.ceilings[random.nextInt(theme.ceilings.length)];
		fenceBlock = theme.fencing[random.nextInt(theme.fencing.length)];
		cornerBlock = theme.pillarBlock[random.nextInt(theme.pillarBlock.length)];
		liquidBlock = theme.liquid[random.nextInt(theme.liquid.length)];
		
		rooms = new RoomList(size.maxRooms + 1);
		planter = new ArrayList<Room>();
		map = new MapMatrix(size.width, world, chunkX, chunkZ);
		numNodes = random.nextInt(size.maxNodes - size.minNodes + 1) + size.minNodes + 1;
		nodes = new Node[numNodes];
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
	
	
	void makeNodes() {	
		//DoomlikeDungeons.profiler.startTask("Creating Node Rooms");
		int height = baseHeight;
		nodeRooms = new RoomList(numNodes);
		for(int i = 0; i < numNodes; i++) {
			nodes[i] = new Node(random.nextInt(size.width), height, random.nextInt(size.width), random, this);
		}
		//System.out.println("[DLDUNGEONS] " + numNodes + " nodes");	
		//DoomlikeDungeons.profiler.endTask("Creating Node Rooms");
	}
	
	
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
	
	
	void makeMoreRooms() {		
		//DoomlikeDungeons.profiler.startTask("Adding Extra Rooms (old)");
		while(rooms.realSize() < size.maxRooms) {
				Room made;
				int height = baseHeight;
				int x = random.nextInt(size.width);
				int z = random.nextInt(size.width);
				int xdim = random.nextInt(size.maxRoomSize - 5) + 6;
				int zdim = random.nextInt(size.maxRoomSize - 5) + 6;
				if(bigRooms.use(random)) {
					xdim += random.nextInt((size.maxRoomSize / 2)) + (size.maxRoomSize / 2);
					zdim += random.nextInt((size.maxRoomSize / 2)) + (size.maxRoomSize / 2);
				}
				int ymod = (xdim <= zdim) ? (int) Math.sqrt(xdim) : (int) Math.sqrt(zdim);
				int roomHeight = random.nextInt((verticle.value / 2) + ymod + 1) + 2;
				made = new PlaceSeed(x, height, z).growRoom(xdim, zdim, roomHeight, this, null, null);
			}		
		//DoomlikeDungeons.profiler.endTask("Adding Extra Rooms (old)");
		}
	
	
	public void growthCycle() {		
		//DoomlikeDungeons.profiler.startTask("Adding Rooms (growthCycle)");
		//System.out.println("Running growthCycle.");
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
					//System.out.println("Added side room.");
					doMore = true;
				}
			} 
		} while(doMore);		
		//DoomlikeDungeons.profiler.endTask("Adding Rooms (growthCycle)");
	}
	
	
	public void fixRoomContents() {
		for(Room room : rooms) {	
			DoorChecker.processDoors1(this, room);
		}
		for(Room room : rooms) {	
			DoorChecker.processDoors2(this, room);	
		}
		for(Room room : rooms) {
			DoorChecker.processDoors3(this, room);
			if(room instanceof DldCave) DoorChecker.caveConnector(this, room);
			addSpawners(room);	
		}
		DoorChecker.checkConnectivity(this);
	}
	
	
	public void addSpawners(Room room) {
		// TODO: Separate methods for chests and entrances
		int shiftX = (map.chunkX * 16) - (map.room.length / 2) + 8;
		int shiftZ = (map.chunkZ * 16) - (map.room.length / 2) + 8;
		//for(Room room : rooms) {			
			//DoomlikeDungeons.profiler.startTask("Adding to room " + room.id);
			for(Spawner  spawner : room.spawners) {
					DBlock.placeSpawner(map.world, shiftX + spawner.x, spawner.y, shiftZ + spawner.z, spawner.mob);
			}
			for(BasicChest  chest : room.chests) {
				chest.place(map.world, shiftX + chest.mx, chest.my, shiftZ + chest.mz, random);
			}
			//DoomlikeDungeons.profiler.endTask("Adding to room " + room.id);
		//}	
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
			new SpiralStair((int)room.realX, room.floorY, (int)room.realZ).build(this, map.world);
			//DoomlikeDungeons.profiler.endTask("Adding Sriral Stair");
			break;
		case 1:
			//DoomlikeDungeons.profiler.startTask("Adding Top Room");
			new TopRoom((int)room.realX, room.floorY, (int)room.realZ).build(this, map.world);
			//DoomlikeDungeons.profiler.endTask("Adding Top Room");
			break;
		case 2:
		default:
			//DoomlikeDungeons.profiler.startTask("Adding Simple Entrance");
			new SimpleEntrance((int)room.realX, room.floorY, (int)room.realZ).build(this, map.world);
			//DoomlikeDungeons.profiler.endTask("Adding Simple Entrance");
			break;
		}		
		//DoomlikeDungeons.profiler.endTask("Adding Entrances");
	}
	
	
	public void addAnEntrance() {
		if(theme.entrances.never()) return;
		int which = random.nextInt(numNodes);
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


