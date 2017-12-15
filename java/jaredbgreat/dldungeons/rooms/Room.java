package jaredbgreat.dldungeons.rooms;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.Difficulty;
import jaredbgreat.dldungeons.pieces.Doorway;
import jaredbgreat.dldungeons.pieces.Shape;
import jaredbgreat.dldungeons.pieces.Shapes;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.pieces.chests.LootCategory;
import jaredbgreat.dldungeons.pieces.chests.TreasureChest;
import jaredbgreat.dldungeons.pieces.chests.WeakChest;
import jaredbgreat.dldungeons.planner.Level;
import jaredbgreat.dldungeons.planner.RoomSeed;
import jaredbgreat.dldungeons.planner.Route;
import jaredbgreat.dldungeons.planner.Symmetry;
import jaredbgreat.dldungeons.planner.astar.AStar;
import jaredbgreat.dldungeons.planner.astar.DoorQueue;
import jaredbgreat.dldungeons.planner.features.Cutout;
import jaredbgreat.dldungeons.planner.features.Depression;
import jaredbgreat.dldungeons.planner.features.FeatureAdder;
import jaredbgreat.dldungeons.planner.features.IslandPlatform;
import jaredbgreat.dldungeons.planner.features.IslandRoom;
import jaredbgreat.dldungeons.planner.features.Pillar;
import jaredbgreat.dldungeons.planner.features.Pool;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import jaredbgreat.dldungeons.themes.ThemeType;

import java.util.ArrayList;
import java.util.Collections;


/**
 * This is the basic room type, built from shape primitive and most 
 * common in most dungeon themes.  Most of the methods are used for
 * adding features to the room so that it builds itself, though most 
 * of these features are actually store in the dungeon map (a MapMatrix)
 * rather than the Room instance.  Exception are chests, spawners, 
 * and doors, which are kept in lists to be further processes and/or 
 * added in ways that accommodate their special features (TileEntities).
 * 
 * Note that the poor encapsulation of this class is largely due to 
 * excessive concerns about efficiency and exaggerated assumptions 
 * about the overhead of method calls (getters).  While this could, and
 * perhaps should be changed in principle, the number of required 
 * code changes throughout the mod is now a major reason for leaving 
 * them as public. 
 * 
 * @author Jared Blackburn
 *
 */
public class Room extends AbstractRoom {
	
	public int id;	// Should be equal to the index in Dungeon.rooms ArrayList.
	public static final Room roomNull = new Room(); // Areas outside the dungeon (index 0)
	public int beginX, midX, endX, beginZ, midZ, endZ, floorY, nFloorY, ceilY, nCeilY, y, level;
	public float realX, realZ;
	public boolean hasWholePattern;
	public Symmetry sym;
	public int orientation;
	public boolean XFlip, ZFlip;
	public Shapes shape = Shapes.X;
	public ArrayList<RoomSeed> childSeeds;
	public boolean isNode;
	public boolean isSubroom;
	public boolean hasEntrance;
	public boolean hasSpawners;
	public ArrayList<Spawner> spawners;
	public ArrayList<BasicChest> chests;
	public ArrayList<Doorway> doors;
	public ArrayList<DoorQueue> connections;
	public ArrayList<Doorway> topDoors;
	public Doorway midpoint; // not really a door but used as one at times
	
	
	private Room() {id = 0;}	
	
	
	/**
	 * A safety method to ensure there are no circular references 
	 * to create a memory leak.  Note that none should exist, this 
	 * is included as a safety measure do to the general complexity 
	 * of the relationship between dungeons, rooms, and related rooms.
	 */
	public void preFinalize() {
		childSeeds.clear();
		childSeeds = null;
		spawners.clear();
		chests.clear();
		spawners = null;
		chests = null;
		doors = null;
	}
	
	
	public Room(int beginX, int endX, int beginZ, int endZ, int floorY, int ceilY, 
			Level dungeon, Room parent, Room previous) {
		super(dungeon, previous);		
		//DoomlikeDungeons.profiler.startTask("Creating a Room");
		dungeon.rooms.add(this);
		id = dungeon.rooms.realSize();
		childSeeds = new ArrayList<RoomSeed>();
		spawners = new ArrayList<Spawner>();
		chests   = new ArrayList<BasicChest>();
		doors    = new ArrayList<Doorway>();
		connections = new ArrayList<DoorQueue>();
		dungeon.planter.add(this);
		isNode = (previous == null);
		isSubroom = (parent != null);
		hasEntrance = (isNode && dungeon.entrances.use(dungeon.random));
		if(hasEntrance) dungeon.numEntrances++;
		hasSpawners = false;		
		
		sym = Symmetry.getSymmetry(dungeon);		
		orientation = dungeon.random.nextInt(4);
		XFlip = dungeon.random.nextBoolean();
		ZFlip = dungeon.random.nextBoolean();
		
		this.beginX = beginX;
		this.endX = endX;
		this.beginZ = beginZ;
		this.endZ = endZ;
		this.floorY = floorY;
		this.ceilY = ceilY;
		level = 0;		

		midX = beginX + ((endX - beginX) / 2);
		midZ = beginZ + ((endZ - beginZ) / 2);
		
		realX = (((float)(endX - beginX)) / 2.0f) + (float)beginX + 1.0f;
		realZ = (((float)(endZ - beginZ)) / 2.0f) + (float)beginZ + 1.0f;
		
		dungeon.spawners.addRoom((endX - beginX) * (endZ - beginZ));
		
		if(isNode) {
			degenerateFloors = false;
			doors.add(new Doorway(midX, midZ, dungeon.random.nextBoolean()));
		}
		
		if(isSubroom && parent.sky) {
			sky = (sky && !dungeon.outside.use(dungeon.random));
		}
		for(int i = beginX + 1; i < endX; i++)
			for(int j = beginZ + 1; j < endZ; j++) {
				if(dungeon.map.room[i][j] == 0) dungeon.map.room[i][j] = id;
				dungeon.map.ceilY[i][j] = dungeon.map.nCeilY[i][j] = (byte)ceilY;
				dungeon.map.floorY[i][j] = dungeon.map.nFloorY[i][j] = (byte)floorY;
				if(!sky) dungeon.map.ceiling[i][j] = cielingBlock;
				dungeon.map.floor[i][j] = floorBlock;
				dungeon.map.wall[i][j] = wallBlock1;	
				dungeon.map.hasLiquid[i][j] = false;	
				dungeon.map.isWall[i][j] = false;					
			}
		for(int i = beginX; i <= endX; i++) {
			assignEdge(dungeon, i, beginZ);
			assignEdge(dungeon, i, endZ);
		}
		for(int i = beginZ; i <= endZ; i++) {
			assignEdge(dungeon, beginX, i);
			assignEdge(dungeon, endX, i);
		}
		doorways(dungeon);
		midpoint = new Doorway(midX, midZ, dungeon.random.nextBoolean());
	}
	
	
	/**
	 * Adds the features, including chests and spawners, to the room.
	 * 
	 * @param dungeon
	 * @param parent
	 * @return
	 */
	public Room plan(Level dungeon, Room parent) {
		if(!dungeon.complexity.use(dungeon.random) && !isNode) {
			hasWholePattern = true;
			if(dungeon.liquids.use(dungeon.random)) {				
				walkway(dungeon);
			} else if(!sky && (dungeon.complexity.use(dungeon.random) 
					|| dungeon.symmetry.use(dungeon.random))) {
				cutin(dungeon);
			}			
		} else addFeatures(dungeon); 
		if(hasEntrance) {
			for(int i = (int)realX -2; i < ((int)realX + 2); i++)
					for(int j = (int)realZ - 2; j < ((int)realZ + 2); j++) {
						dungeon.map.floorY[i][j] = (byte)floorY;
						dungeon.map.hasLiquid[i][j] = false;
						dungeon.map.isWall[i][j] = false;
					}
		}
		if(parent == null) {
			addSpawners(dungeon);
		}
		return this;
	}
	
	
	/**
	 * Assigns a wall section between this room and another to the room with 
	 * the higher ceiling, so as too avoid overly short walls between rooms
	 * of differing heights.
	 * 
	 * @param dungeon
	 * @param x
	 * @param z
	 */
	private void assignEdge(Level dungeon, int x, int z) {
		if((dungeon.map.room[x][z] == 0) 
				|| (dungeon.rooms.get(dungeon.map.room[x][z]).sky && !sky)
				|| (isSubroom)) {
			dungeon.map.room[x][z] = id;
			if(!sky) dungeon.map.ceiling[x][z] = cielingBlock;
			dungeon.map.floor[x][z] = floorBlock;
			dungeon.map.wall[x][z] = wallBlock1;	
			dungeon.map.hasLiquid[x][z] = false;
			dungeon.map.isWall[x][z] = !sky;
			dungeon.map.isFence[x][z] = fenced;
		}
		if(dungeon.map.ceilY[x][z] < (byte)ceilY)  dungeon.map.ceilY[x][z] = (byte)ceilY;
		if(dungeon.map.nCeilY[x][z] < (byte)ceilY) dungeon.map.nCeilY[x][z] = (byte)ceilY;
		if(dungeon.map.floorY[x][z] < (byte)floorY) dungeon.map.floorY[x][z] = (byte)floorY;
		if((dungeon.map.nFloorY[x][z] > (byte)nFloorY) || (dungeon.map.nFloorY[x][z] == 0)) 
			dungeon.map.nFloorY[x][z] = (byte)floorY;				
	}
	
	
	/**
	 * Adds features other than chests and spawns to the room to rooms that
	 * lack a whole room pattern.  This is called by plan room.
	 * 
	 * It will try to at a number of features based on dungeon complexity. 
	 * On each attempt it will check each possible feature once; if a feature
	 * is selected to add it the attempt ends and the next begins.  To avoid 
	 * a universal bias based on checking order, all features types are added 
	 * to a list and shuffled once, giving each room its own set of biases 
	 * that can be thought of as the room character or a room equivalent to 
	 * the dungeons wide theme-based probabilities.
	 * 
	 * @param dungeon
	 */
	public void addFeatures(Level dungeon) {
		ArrayList<FeatureAdder> features = new ArrayList<FeatureAdder>();
		features.add(new IslandPlatform(dungeon.verticle));
		features.add(new Depression(dungeon.verticle));
		features.add(new Pool(dungeon.liquids));
		if(!sky) {
			features.add(new Cutout(dungeon.complexity));
			features.add(new Pillar(dungeon.pillars));
		}
		features.add(new IslandRoom(dungeon.islands));
		features.trimToSize();
		Collections.shuffle(features, dungeon.random);
		if(dungeon.complexity.value <= 0) return;
		int tries = ((endX - beginX + endZ - beginZ) / (4 + sym.level));
		for(int i = 0; i <= tries; i++) {
			for(FeatureAdder feat: features) {
				if(feat.addFeature(dungeon, this)) break;
			}
		}
	}
	
	
	/**
	 * Adds spawners, taking into consideration the mods difficulty setting 
	 * and whether the room is an entrance, a destination, or an ordinary room.
	 * 
	 * @param dungeon
	 */
	protected void addSpawners(Level dungeon) {
		if(ConfigHandler.difficulty == Difficulty.NONE ||
				(!isNode && !ConfigHandler.difficulty.addmob(dungeon.random)) 
				|| (hasEntrance && !ConfigHandler.difficulty.entrancemobs)) return;
		boolean multibonus = false;
		int x, y, z, tmp, num;
		String mob;		
		if(ConfigHandler.difficulty.multimob(dungeon.random) || isNode) {
			tmp = (endX - beginX) > (endZ - beginZ) ? (endX - beginX) : (endZ - beginZ);  
			num = dungeon.random.nextInt(2 + (tmp / 8)) + 1;
			for(int i = 0; i < num; i++) {
				tmp = (endX - beginX - 3);
				x = dungeon.random.nextInt(tmp) + beginX + 2;
				tmp = (endZ - beginZ - 3);
				z = dungeon.random.nextInt(tmp) + beginZ + 2;
				if(dungeon.random.nextInt(4) == 0) y = dungeon.map.ceilY[x][z];
				else y = dungeon.map.floorY[x][z];
				int lev = levAdjust(ConfigHandler.difficulty.moblevel(dungeon.random), dungeon);
				if(lev >= 0) {
					mob = dungeon.theme.allMobs[lev].get(dungeon.random.nextInt(dungeon.theme.allMobs[lev].size()));
					Spawner s = new Spawner(x, y, z, id, lev, mob);
					spawners.add(s);
					dungeon.spawners.addSpawner(s);
				}
			}
		} else  {
			tmp = (endX - beginX) / 2;
			x = dungeon.random.nextInt(tmp) + beginX + (tmp /2);
			tmp = (endZ - beginZ) / 2;
			z = dungeon.random.nextInt(tmp) + beginZ + (tmp /2);
			if(dungeon.random.nextInt(4) == 0) y = dungeon.map.floorY[x][z];
			else y = dungeon.map.ceilY[x][z];
			int lev = levAdjust(ConfigHandler.difficulty.moblevel(dungeon.random), dungeon);
			if(lev >= 0) {
				mob = dungeon.theme.allMobs[lev].get(dungeon.random.nextInt(dungeon.theme.allMobs[lev].size()));
				Spawner s = new Spawner(x, y, z, id, lev, mob);
				spawners.add(s);
				dungeon.spawners.addSpawner(s);
			}
		}
		if(isNode) {
			x = (int)realX;
			z = (int)realZ;
			if(dungeon.map.hasLiquid[x][z]) {y = dungeon.map.ceilY[x][z];}
			else {y = dungeon.map.floorY[x][z] - 1;} 
			int lev = levAdjust(ConfigHandler.difficulty.nodelevel(dungeon.random), dungeon);
			if(lev >= 0) {
				mob = dungeon.theme.allMobs[lev].get(dungeon.random.nextInt(dungeon.theme.allMobs[lev].size()));
				spawners.add(new Spawner(x, y, z, id, lev, mob));
			}
		}
		if(multibonus) level++;
		if(dungeon.theme.flags.contains(ThemeFlags.HARD)) level++;
		if(dungeon.theme.flags.contains(ThemeFlags.EASY)) level--;
		if(level >= LootCategory.LEVELS) level = LootCategory.LEVELS - 1;
	}
	
	
	/**
	 * Checks to see if there are mobs available at the level determined,
	 * and if not adjusts down.
	 * 
	 * @param lev
	 * @param dungeon
	 * @return
	 */
	private int levAdjust(int lev, Level dungeon) {
		while(dungeon.theme.allMobs[lev].isEmpty()) {
			lev--;
			if(lev < 0) return -1;
		}
		return lev;
	}
	
	
	/**
	 * Adds chests, taking into consideration the rooms difficulty and 
	 * whether its a destination or ordinary room.  No chests will be
	 * added to entrance rooms.
	 * 
	 * @param dungeon
	 */
	public void addChests(Level dungeon) {
		if((ConfigHandler.difficulty == Difficulty.NONE) || 
				hasEntrance) return;
		hasSpawners = spawners.size() > 0;
		int lev = 0;
		int n = spawners.size();
		for(int i = 0; i < n; i++) {
			lev = Math.max(lev, spawners.get(i).getLevel());
		}
		if(n > 1) {
			lev++;			
		}
		if(isNode && !hasEntrance) {
			lev++;
		}
		if((!hasSpawners && (dungeon.random.nextInt(5) > 0))) return;
		int x, y, z, tmp, num;
		if(!hasSpawners) {
			tmp = (endX - beginX - 3);
			x = dungeon.random.nextInt(tmp) + beginX + 2;
			tmp = (endZ - beginZ - 3);
			z = dungeon.random.nextInt(tmp) + beginZ + 2;
			y = dungeon.map.floorY[x][z];
			chests.add(new WeakChest(x, y, z));
		} else if(dungeon.random.nextBoolean() && !isNode) {
			tmp = (endX - beginX - 3);
			x = dungeon.random.nextInt(tmp) + beginX + 2;
			tmp = (endZ - beginZ - 3);
			z = dungeon.random.nextInt(tmp) + beginZ + 2;
			y = dungeon.map.floorY[x][z];
			chests.add(new BasicChest(x, y, z, level));
		} else {
			int ms = Math.max(n, 2);
			if(isNode)num = Math.min(ms, dungeon.random.nextInt(2 + (ms / 2)) + 2);
			else      num = dungeon.random.nextInt(1 + (ms / 2)) + 1;
			for(int i = 0; i < num; i++) {
				tmp = (endX - beginX - 3);
				x = dungeon.random.nextInt(tmp) + beginX + 2;
				tmp = (endZ - beginZ - 3);
				z = dungeon.random.nextInt(tmp) + beginZ + 2;
				y = dungeon.map.floorY[x][z];
				chests.add(new BasicChest(x, y, z, level));
			}
		} if(isNode) {
			x = (int)realX;
			z = (int)realZ;
			y = dungeon.map.floorY[x][z]; 
			chests.add(new TreasureChest(x, y, z, level)); // Will be a special chest later
		}
	}
	
	
	/**
	 * Determine the number of doorways and add them.
	 * 
	 * @param dungeon
	 */
	protected void doorways(Level dungeon) {
		int num = dungeon.random.nextInt(2 + ((endX - beginX + endZ - beginZ) / ((sym.level * 8) + 8)) 
				+ (dungeon.subrooms.value / (2 + sym.level))) + 1;
		for(int i = 0; i < num; i++) doorway(dungeon);
	}
	
	
	/**
	 * Adds a doorway.
	 * 
	 * @param dungeon
	 * @param x
	 * @param z
	 * @param xOriented
	 */
	protected void addDoor(Level dungeon, int x, int z, boolean xOriented) {
		doors.add(new Doorway(x, z, xOriented));
		dungeon.map.isDoor[x][z] = true;
	}
	
	
	/**
	 * Creates a doorway, or more than one based on room symmetry.
	 * 
	 * @param dungeon
	 */
	protected void doorway(Level dungeon) {
		int xExtend = 0;
		int zExtend = 0;
		int xSeedDir = 0;
		int zSeedDir = 0;
		int wall = dungeon.random.nextInt(4);
		int x, z, oppX, oppZ;
		switch (wall) {
			case 0:
				x = beginX;
				oppX = endX;
				z = dungeon.random.nextInt(endZ - beginZ - 3) + 2;
				oppZ = endZ - z;
				z += beginZ;
				xSeedDir = -1;
				zSeedDir =  0;
				zExtend = dungeon.random.nextInt(3) - 1;
				break;
			case 1:
				z = beginZ;
				oppZ = endZ;
				x = dungeon.random.nextInt(endX - beginX - 3) + 2;
				oppX = endX - x;
				x += beginX;
				xSeedDir =  0;
				zSeedDir = -1;
				xExtend = dungeon.random.nextInt(3) - 1;
				break;
			case 2:
				x = endX;
				oppX = beginX;
				z = dungeon.random.nextInt(endZ - beginZ - 3) + 2;
				oppZ = endZ - z;
				z += beginZ;
				xSeedDir =  1;
				zSeedDir =  0;
				zExtend = dungeon.random.nextInt(3) - 1;
				break;
			case 3:
				z = endZ;
				oppZ = beginZ;
				x = dungeon.random.nextInt(endX - beginX - 3) + 2;
				oppX = endX - x;
				x += beginX;
				xSeedDir =  0;
				zSeedDir =  1;
				xExtend = dungeon.random.nextInt(3) - 1;
				break;
			default: // Removes the "...may not be initialized" warning.
				x = beginX;
				oppX = endX;
				z = dungeon.random.nextInt(endZ - beginZ - 3) + 2;
				oppZ = endZ - z;
				z += beginZ;
				xSeedDir = -1;
				zSeedDir =  0;
				zExtend = dungeon.random.nextInt(3) - 1;
				break;
		}		
		addDoor(dungeon, x, z, (xSeedDir != 0));
		addDoor(dungeon, x + xExtend, z + zExtend, (xSeedDir != 0));
		childSeeds.add(new RoomSeed(x + xSeedDir, floorY, z + zSeedDir));
		// Apply Symmetries
		switch (sym) {
			case NONE: break;
			case TR1: {
				oppX = (int)(realX + ((z - realZ) / (float)(endZ - beginZ)) * (endX - beginX));
				if(oppX < 1) oppX = 1;
				if(oppX > (dungeon.size.width - 2)) oppX = (dungeon.size.width - 2);
				oppZ = (int)(realZ + ((x - realX) / (float)(endX - beginX)) * (endZ - beginZ));
				if(oppZ < 1) oppZ = 1;
				if(oppZ > (dungeon.size.width - 2)) oppZ = (dungeon.size.width - 2); 
				addDoor(dungeon, oppX, oppZ, (zSeedDir != 0));
				addDoor(dungeon, oppX + xExtend, oppZ + zExtend, (zSeedDir != 0));
				childSeeds.add(new RoomSeed(oppX + zSeedDir, floorY, oppZ + xSeedDir));
			} break;
			case TR2: {
				oppX = (int)(realX + ((z - realZ) / (float)(endZ - beginZ)) * (endX - beginX));
				if(oppX < 1) oppX = 1;
				if(oppX > (dungeon.size.width - 2)) oppX = (dungeon.size.width - 2);
				oppZ = (int)(realZ + ((x - realX) / (float)(endX - beginX)) * (endZ - beginZ)); 
				oppZ = endZ - (oppZ - beginZ); 
				if(oppZ < 1) oppZ = 1;
				if(oppZ > (dungeon.size.width - 2)) oppZ = (dungeon.size.width - 2); 
				addDoor(dungeon, oppX, oppZ, (zSeedDir != 0));
				addDoor(dungeon, oppX + xExtend, oppZ + zExtend, (zSeedDir != 0));	
				childSeeds.add(new RoomSeed(oppX + zSeedDir, floorY, oppZ - xSeedDir));			
			} break;
			case X: {
				addDoor(dungeon, oppX, z, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, z + zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(oppX - xSeedDir, floorY, z + zSeedDir));
			} break;
			case Z: {
				addDoor(dungeon, x, oppZ, (xSeedDir != 0));
				addDoor(dungeon, x + xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(x + xSeedDir, floorY, oppZ - zSeedDir));
			} break;
			case XZ: {
				addDoor(dungeon, oppX,z, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, z + zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(oppX - xSeedDir, floorY, z + zSeedDir));
				addDoor(dungeon, x, oppZ, (xSeedDir != 0));
				addDoor(dungeon, x + xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(x + xSeedDir, floorY, oppZ - zSeedDir));
				addDoor(dungeon, oppX, oppZ, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(oppX - xSeedDir, floorY, oppZ - zSeedDir));
			} break;
			case R: {
				addDoor(dungeon, oppX, oppZ, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(oppX - xSeedDir, floorY, oppZ - zSeedDir));
			} break;
			case SW: {
				addDoor(dungeon, oppX, z, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, z + zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(oppX - xSeedDir, floorY, z + zSeedDir));
				addDoor(dungeon, x, oppZ, (xSeedDir != 0));
				addDoor(dungeon, x + xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(x + xSeedDir, floorY, oppZ - zSeedDir));
				addDoor(dungeon, oppX, oppZ, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new RoomSeed(oppX - xSeedDir, floorY, oppZ - zSeedDir));
			}
		}		
	}
	
	
	/**
	 * Adds a room new room branching from this one that is part of a sequence of 
	 * rooms between two dungeon nods.
	 * 
	 * @param dungeon
	 * @param dir
	 * @param xdim
	 * @param zdim
	 * @param height
	 * @param source
	 * @return
	 */
	public Room connector(Level dungeon, int dir, int xdim, int zdim, int height, Route source) {
		int xExtend = 0;
		int zExtend = 0;
		int xSeedDir = 0;
		int zSeedDir = 0;
		int x, z, oppX, oppZ;
		switch (dir) {
			case 2:
				x = beginX;
				oppX = endX;
				z = (int)realZ;
				oppZ = z;
				xSeedDir = -1;
				zSeedDir =  0;
				zExtend = dungeon.random.nextInt(3) - 1;
				break;
			case 3:
				z = beginZ;
				oppZ = endZ;
				x = (int)realX;
				oppX = x;
				xSeedDir =  0;
				zSeedDir = -1;
				xExtend = dungeon.random.nextInt(3) - 1;
				break;
			case 0:
				x = endX;
				oppX = beginX;
				z = (int)realZ;
				oppZ = z;
				xSeedDir =  1;
				zSeedDir =  0;
				zExtend = dungeon.random.nextInt(3) - 1;
				break;
			case 1:
				z = endZ;
				oppZ = beginZ;
				x = (int)realX;
				oppX = x;
				xSeedDir =  0;
				zSeedDir =  1;
				xExtend = dungeon.random.nextInt(3) - 1;
				break;
			default: // Removes the "...may not be initialized" warning.
				x = beginX;
				oppX = endX;
				z = (int)realZ;
				oppZ = z;
				z += beginZ;
				xSeedDir = -1;
				zSeedDir =  0;
				zExtend = dungeon.random.nextInt(3) - 1;
				break;
		}
		addDoor(dungeon, x, z, (xSeedDir != 0));
		addDoor(dungeon, x + xExtend, z + zExtend, (xSeedDir != 0));
		if(((x + xSeedDir) >= dungeon.size.width) ||
				((x + xSeedDir) < 0) ||
				((z + zSeedDir) >= dungeon.size.width) ||
				((z + zSeedDir) < 0)) return null;
		if(dungeon.map.room[x + xSeedDir][z + zSeedDir] != 0) {
				return dungeon.rooms.get(dungeon.map.room[x + xSeedDir][z + zSeedDir]);
			}
		else if((dir % 2) == 0) return new RoomSeed(x + xSeedDir, floorY, z + zSeedDir)
				.growRoomZ(xdim, zdim, height, dungeon, null, this);
		else return new RoomSeed(x + xSeedDir, floorY, z + zSeedDir)
				.growRoomX(xdim, zdim, height, dungeon, null, this);
	}
	
	
	/**
	 * Fills a room with a "liquid" and then adds a walkway through it; 
	 * this is used for rooms with a whole room pattern. 
	 * 
	 * @param dungeon
	 */
	private void walkway(Level dungeon) {
		int drop;
		if(dungeon.theme.type.contains(ThemeType.SWAMP)) drop = 1;
		else drop = 2;
		shape = Shapes.wholeShape(sym, dungeon.random);
		for(int i = beginX; i <= endX; i++) 
			for(int j = beginZ; j <= endZ; j++) {
				dungeon.map.floorY[i][j] -= drop;
				dungeon.map.hasLiquid[i][j] = true;
			}
		shape.family[orientation].drawWalkway(dungeon, this, realX, realZ, 
				(byte)(endX - beginX + 1), (byte)(endZ - beginZ + 1), XFlip, ZFlip);
	}
	
	
	/**
	 * Fills the room with walls, then opens up a passage; used for rooms 
	 * with a whole room pattern.
	 * 
	 * @param dungeon
	 */
	private void cutin(Level dungeon) {
		shape = Shapes.wholeShape(sym, dungeon.random);
		for(int i = beginX; i <= endX; i++) 
			for(int j = beginZ; j <= endZ; j++) {
				dungeon.map.isWall[i][j] = true;
			}
		shape.family[orientation].drawCutin(dungeon, this, realX, realZ, 
				(byte)(endX - beginX - 1), (byte)(endZ - beginZ - 1), XFlip, ZFlip);
	}
	
	
	/**
	 * Generate a side room; this is called by Dungeon.growCycle to expand 
	 * the dungeon.
	 * 
	 * @param dungeon
	 * @return
	 */
	public boolean plantChildren(Level dungeon) {
		boolean result = false;
		for(RoomSeed planted : childSeeds) {
			if(dungeon.rooms.realSize() >= dungeon.size.maxRooms) return false;
				int height = dungeon.baseHeight;
				int x = dungeon.random.nextInt(dungeon.size.width);
				int z = dungeon.random.nextInt(dungeon.size.width);
				if(height > dungeon.theme.maxY) height = dungeon.baseHeight;
				if(height < dungeon.theme.minY) height = dungeon.baseHeight;
				int xdim = dungeon.random.nextInt(dungeon.size.maxRoomSize - 5) + 6;
				int zdim = dungeon.random.nextInt(dungeon.size.maxRoomSize - 5) + 6;
				int ymod = (xdim <= zdim) ? (int) Math.sqrt(xdim) : (int) Math.sqrt(zdim);
				int roomHeight = dungeon.random.nextInt((dungeon.verticle.value / 2) + ymod + 1) + 2;
				if(planted.growRoom(xdim, zdim, roomHeight, dungeon, null, this) != null) 
					result = true;
			}
		return result;
	}
	
	
	/**
	 * Determines the other rooms to which a door leads; this is 
	 * used in processing door corrections and room passibility.
	 * 
	 * @param door
	 */
	public void addToConnections(Doorway door) {
		if(id < 1) {
			return;
		}
		if(connections.isEmpty()) {
			DoorQueue pq = new DoorQueue(door.otherside);
			pq.add(door);
			connections.add(pq);			
		} else {
			boolean added = false;
			for(DoorQueue pq : connections) {
				if(pq.isRoom(door.otherside)) {
					pq.add(door);
					added = true;
					break;
				}
			} 
			if(!added) {
				DoorQueue pq = new DoorQueue(door.otherside);
				pq.add(door);
				connections.add(pq);			
			}
		}
	}
	
	
}
