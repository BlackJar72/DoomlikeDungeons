package jaredbgreat.dldungeons.rooms;


/*
 * I need logic to do the following:
 * 
 * 1. Assign / store rotation and inversion data that fits with any whole room patterns.
 * 2. Assign a connection pattern fitting the rooms symmetry or full room pattern.
 * 3. Determine node-to-node targets (two closest nodes to each?) and try to grow toward them.
 * 4. Generate non-nodal intermediate and sub-rooms.
 * 5. Vector toward targets by closest currently available connections (doors); make paths between nodes this way?
 * 6. Fill in non-target rooms (sub-rooms here), eliminated excess doors if needed.
 * 7. Solve the problem if meeting at the correct Y value.
 * 8. Work on stairs / ladders, spawners, chests, and entrances / exits
 * 
 * Also:
 * 		A. Depressions
 * 		B. Island subrooms
 * 		C. Pillars
 * 		D. Implement consistency and dungeon versus room block selctions
 * 
 * Also, other things not related to room creation and connections, such as theme reading from files, 
 * but that goes elsewhere. 
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
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.PlaceSeed;
import jaredbgreat.dldungeons.planner.Route;
import jaredbgreat.dldungeons.planner.Symmetry;
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


public class Room extends AbstractRoom {
	
	public int id;	// Should be equal to the index in Dungeon.rooms ArrayList.
	public static final Room roomNull = new Room(); // Areas outside the dungeon (index 0)
	public int beginX, endX, beginZ, endZ, floorY, nFloorY, ceilY, nCeilY, y, level;
	public float realX, realZ;
	public boolean hasWholePattern;
	public Symmetry sym;
	public int orientation;
	public boolean XFlip, ZFlip;
	public Shapes shape = Shapes.X;
	public ArrayList<PlaceSeed> childSeeds;
	public boolean isNode;
	public boolean hasEntrance;
	public boolean hasSpawners;
	public ArrayList<Spawner> spawners;
	public ArrayList<BasicChest> chests;
	public ArrayList<Doorway> doors;
	public ArrayList<DoorQueue> connections;
	
	private Room() {id = 0;}
	
	@Override
	public void finalize() throws Throwable {
		childSeeds.clear();
		childSeeds = null;
		spawners.clear();
		chests.clear();
		spawners = null;
		chests = null;
		doors = null;
		super.finalize();
	}
	
	
	public Room(int beginX, int endX, int beginZ, int endZ, int floorY, int ceilY, 
			Dungeon dungeon, Room parent, Room previous) {
		super(dungeon, previous);		
		//DoomlikeDungeons.profiler.startTask("Creating a Room");
		dungeon.rooms.add(this);
		id = dungeon.rooms.size();
		childSeeds = new ArrayList<PlaceSeed>();
		spawners = new ArrayList<Spawner>();
		chests   = new ArrayList<BasicChest>();
		doors    = new ArrayList<Doorway>();
		connections = new ArrayList<DoorQueue>();
		dungeon.planter.add(this);
		isNode = (previous == null);
		hasEntrance = (isNode && dungeon.entrances.use(dungeon.random));
		if(hasEntrance) dungeon.numEntrances++;
		if(isNode) degenerateFloors = false;
		hasSpawners = false;		
		
		sym = Symmetry.getSymmetry(dungeon);		
		orientation = dungeon.random.nextInt(4);
		XFlip = dungeon.random.nextBoolean();
		ZFlip = dungeon.random.nextBoolean();
		if((sym == Symmetry.TR1) && (XFlip != ZFlip)) sym = Symmetry.TR2;
		
		this.beginX = beginX;
		this.endX = endX;
		this.beginZ = beginZ;
		this.endZ = endZ;
		this.floorY = floorY;
		this.ceilY = ceilY;
		level = 0;
		
		if(parent != null) {
			sky = (sky && !dungeon.outside.use(dungeon.random));
//			floorY = parent.floorY;  // There is much more to this than that, but not yet needed
		}
		
		realX = (((float)(endX - beginX)) / 2.0f) + (float)beginX + 1.0f;
		realZ = (((float)(endZ - beginZ)) / 2.0f) + (float)beginZ + 1.0f;
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

		//wallPlatform(dungeon);
		// TODO Add room feature -- probably using helper methods.
		if(!dungeon.complexity.use(dungeon.random) && !isNode) {
			hasWholePattern = true;
			if(dungeon.liquids.use(dungeon.random)) {				
				walkway(dungeon);
			} else if(!sky && (dungeon.complexity.use(dungeon.random) 
					|| dungeon.symmetry.use(dungeon.random))) {
				cutin(dungeon);
			}			
		} else addFeatures(dungeon);
		doorways(dungeon);
		if(parent == null) {
			addSpawners(dungeon);
			addChests(dungeon);
		} 
//			else if(!parent.hasSpawners && dungeon.random.nextBoolean()) {
//			addSpawners(dungeon);
//			addChests(dungeon);
//		}
		if(hasEntrance) {
			for(int i = (int)realX -2; i < ((int)realX + 2); i++)
					for(int j = (int)realZ - 2; j < ((int)realZ + 2); j++) {
						dungeon.map.floorY[i][j] = (byte)floorY;
						dungeon.map.hasLiquid[i][j] = false;
						dungeon.map.isWall[i][j] = false;
					}
		}
		//DoomlikeDungeons.profiler.endTask("Creating a Room");
	}
	
	
	private void assignEdge(Dungeon dungeon, int x, int z) {
		if(dungeon.map.room[x][z] == 0) dungeon.map.room[x][z] = id;
		if(dungeon.map.ceilY[x][z] < (byte)ceilY)  dungeon.map.ceilY[x][z] = (byte)ceilY;
		if(dungeon.map.nCeilY[x][z] < (byte)ceilY) dungeon.map.nCeilY[x][z] = (byte)ceilY;
		if(dungeon.map.floorY[x][z] < (byte)floorY) dungeon.map.floorY[x][z] = (byte)floorY;
		if((dungeon.map.nFloorY[x][z] > (byte)nFloorY) || (dungeon.map.nFloorY[x][z] == 0)) 
			dungeon.map.nFloorY[x][z] = (byte)floorY;
		if(!sky) dungeon.map.ceiling[x][z] = cielingBlock;
		dungeon.map.floor[x][z] = floorBlock;
		dungeon.map.wall[x][z] = wallBlock1;	
		dungeon.map.hasLiquid[x][z] = false;
		dungeon.map.isWall[x][z] = !sky;
		dungeon.map.isFence[x][z] = fenced;				
	}
	
	
	public void addFeatures(Dungeon dungeon) {
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
	
	
	public void addSpawners(Dungeon dungeon) {
		if(ConfigHandler.difficulty == Difficulty.NONE ||
				(!isNode && !ConfigHandler.difficulty.addmob(dungeon.random)) 
				|| hasEntrance) return;
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
					spawners.add(new Spawner(x, y, z, mob));					
					if(level < lev) level = lev;
					multibonus = hasSpawners;
					hasSpawners = true;
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
				spawners.add(new Spawner(x, y, z, mob));
				hasSpawners = true;
				level = lev;
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
				spawners.add(new Spawner(x, y, z, mob));
				if(level < lev) level = lev;
				multibonus = hasSpawners;
				hasSpawners = true;
				level++;                 // Add destination node bonus
			}
		}
		if(multibonus) level++;
		if(dungeon.theme.flags.contains(ThemeFlags.HARD)) level++;
		if(dungeon.theme.flags.contains(ThemeFlags.EASY)) level--;
		if(level >= LootCategory.LEVELS) level = LootCategory.LEVELS - 1;
	}
	
	
	private int levAdjust(int lev, Dungeon dungeon) {
		while(dungeon.theme.allMobs[lev].isEmpty()) {
			lev--;
			if(lev < 0) return -1;
		}
		return lev;
	}
	
	
	public void addChests(Dungeon dungeon) {
		if((ConfigHandler.difficulty == Difficulty.NONE) || 
				hasEntrance) return;
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
			if(isNode)num = dungeon.random.nextInt(1 + (spawners.size())) + 1;
			else      num = dungeon.random.nextInt(2 + (spawners.size()) / 2) + 1;
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
	
	
	private void addPlatform(Dungeon dungeon) {
		float platX, platZ;
		if(dungeon.random.nextBoolean()) {
			// Wall bordering
			
		} else {
			
		}
	}
	
	
	public boolean islandSubroom(Dungeon dungeon) {
		int dimX = (int)((endX - beginX) * (0.2f + (0.3f * dungeon.random.nextFloat()))); 
		int dimZ = (int)((endZ - beginZ) * (0.2f + (0.3f * dungeon.random.nextFloat())));
		float centerX, centerZ, oppX, oppZ;
		centerX = dungeon.random.nextInt(endX - beginX) + beginX;
		centerZ = dungeon.random.nextInt(endZ - beginZ) + beginZ;
		oppX = endX - (centerX - beginX); 
		oppZ = endZ - (centerZ - beginZ); 
		if(sym.halfX) {
			dimX *= 2;
			dimX /= 3;
			oppX = endX - ((centerX - beginX) / 2);
			centerX = ((centerX - beginX) / 2) + beginX;
		}
		if(sym.halfZ) {
			dimZ *= 2;
			dimZ /= 3;
			oppZ = endZ -((centerZ - beginZ) / 2);
			centerZ = ((centerZ - beginZ) / 2) + beginZ;
		}
		if(sym.doubler) {
			dimX *= 0.75;
			dimZ *= 0.75;
		}
		if((dimX < 5) || (dimZ < 5)) return false;
		int ymod = (dimX <= dimZ) ? (int) Math.sqrt(dimX) : (int) Math.sqrt(dimZ);
		int height = dungeon.random.nextInt((dungeon.verticle.value / 2) + ymod + 1) + 2;
		Room created = 
				new PlaceSeed((int)centerX, floorY, 
						(int)centerZ).growRoom(dimX, dimZ, height, dungeon, this, this);
		if(created == null) return false;
		// Apply Symmetries
		switch (sym) {
			case NONE: break;
			case TR1: {
				oppX = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				oppZ = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ); 
				created = 
						new PlaceSeed((int)oppX, floorY, 
								(int)oppZ).growRoom(dimZ, dimX, height, dungeon, this, this);
			} break;
			case TR2: {
				oppX = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				oppZ = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ);  
				oppZ = endZ - (oppZ - beginZ);  
				created = 
						new PlaceSeed((int)oppX, floorY, 
								(int)oppZ).growRoom(dimZ, dimX, height, dungeon, this, this);
			} break;
			case X: {
				created = 
						new PlaceSeed((int)oppX, floorY, 
								(int)centerZ).growRoom(dimX, dimZ, height, dungeon, this, this);
			} break;
			case Y: {
				created = 
						new PlaceSeed((int)centerX, floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, this, this);
			} break;
			case XY: {
				created = 
						new PlaceSeed((int)oppX, floorY, 
								(int)centerZ).growRoom(dimX, dimZ, height, dungeon, this, this);
				created = 
						new PlaceSeed((int)centerX, floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, this, this);
				created = 
						new PlaceSeed((int)oppX, floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, this, this);
			} break;
			case R: { 
				created = 
						new PlaceSeed((int)oppX, floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, this, this);
			} break;
			case SW: {
				float swX1 = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				float swZ1 = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ);
				float swX2 = realX + ((oppZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				float swZ2 = realZ + ((oppX - realX) / (endX - beginX)) * (endZ - beginZ);
				created = 
						new PlaceSeed((int)swX2, floorY, 
								(int)swZ1).growRoom(dimZ, dimX, height, dungeon, this, this);
				created = 
						new PlaceSeed((int)swX1, floorY, 
								(int)swZ2).growRoom(dimZ, dimX, height, dungeon, this, this);
				created = 
						new PlaceSeed((int)oppX, floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, this, this);
			}
		}
		return true;
	}
	
	
	public void doorways(Dungeon dungeon) {
		int num = dungeon.random.nextInt(2 + ((endX - beginX + endZ - beginZ) / ((sym.level * 8) + 8)) 
				+ (dungeon.subrooms.value / (2 + sym.level))) + 1;
		for(int i = 0; i < num; i++) doorway(dungeon);
	}
	
	
	public void addDoor(Dungeon dungeon, int x, int z, boolean xOriented) {
		doors.add(new Doorway(x, z, xOriented));
		dungeon.map.isDoor[x][z] = true;
	}
	
	
	public void doorway(Dungeon dungeon) {
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
		childSeeds.add(new PlaceSeed(x + xSeedDir, floorY, z + zSeedDir));
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
				childSeeds.add(new PlaceSeed(oppX + zSeedDir, floorY, oppZ + xSeedDir));
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
				childSeeds.add(new PlaceSeed(oppX + zSeedDir, floorY, oppZ - xSeedDir));			
			} break;
			case X: {
				addDoor(dungeon, oppX, z, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, z + zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(oppX - xSeedDir, floorY, z + zSeedDir));
			} break;
			case Y: {
				addDoor(dungeon, x, oppZ, (xSeedDir != 0));
				addDoor(dungeon, x + xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(x + xSeedDir, floorY, oppZ - zSeedDir));
			} break;
			case XY: {
				addDoor(dungeon, oppX,z, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, z + zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(oppX - xSeedDir, floorY, z + zSeedDir));
				addDoor(dungeon, x, oppZ, (xSeedDir != 0));
				addDoor(dungeon, x + xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(x + xSeedDir, floorY, oppZ - zSeedDir));
				addDoor(dungeon, oppX, oppZ, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(oppX - xSeedDir, floorY, oppZ - zSeedDir));
			} break;
			case R: {
				addDoor(dungeon, oppX, oppZ, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(oppX - xSeedDir, floorY, oppZ - zSeedDir));
			} break;
			case SW: {
				addDoor(dungeon, oppX, z, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, z + zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(oppX - xSeedDir, floorY, z + zSeedDir));
				addDoor(dungeon, x, oppZ, (xSeedDir != 0));
				addDoor(dungeon, x + xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(x + xSeedDir, floorY, oppZ - zSeedDir));
				addDoor(dungeon, oppX, oppZ, (xSeedDir != 0));
				addDoor(dungeon, oppX - xExtend, oppZ - zExtend, (xSeedDir != 0));
				childSeeds.add(new PlaceSeed(oppX - xSeedDir, floorY, oppZ - zSeedDir));
			}
		}		
	}
	
	
	public Room connector(Dungeon dungeon, int dir, int xdim, int zdim, int height, Route source) {
		//System.out.println("Running connector(Dungeon dungeon, int dir, int xdim, int zdim, int height)");
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
		else if((dir % 2) == 0) return new PlaceSeed(x + xSeedDir, floorY, z + zSeedDir)
				.growRoomZ(xdim, zdim, height, dungeon, null, this);
		else return new PlaceSeed(x + xSeedDir, floorY, z + zSeedDir)
				.growRoomX(xdim, zdim, height, dungeon, null, this);
	}
	
	
	public void islandPlatform(Dungeon dungeon, boolean isDepression) {
		int available = ceilY - floorY;
		if(available < 4) return;
		float dimX, dimZ, centerX, centerZ, oppX, oppZ;
		byte platY;
		int rotation = dungeon.random.nextInt(4);
		Shape[] which;
		dimX = ((endX - beginX) * ((dungeon.random.nextFloat() * 0.25f) + 0.15f));
		dimZ = ((endX - beginX) * ((dungeon.random.nextFloat() * 0.25f) + 0.15f));
		centerX = dungeon.random.nextInt(endX - beginX - 1) + beginX + 1;
		centerZ = dungeon.random.nextInt(endZ - beginZ - 1) + beginZ + 1;
		oppX = endX - (centerX - beginX); 
		oppZ = endZ - (centerZ - beginZ); 
		if(sym.halfX) {
			dimX *= 2;
			dimX /= 3;
			oppX = endX - ((centerX - beginX) / 2);
			centerX = ((centerX - beginX) / 2) + beginX;
		}
		if(sym.halfZ) {
			dimZ *= 2;
			dimZ /= 3;
			oppZ = endZ -((centerZ - beginZ) / 2);
			centerZ = ((centerZ - beginZ) / 2) + beginZ;
		}
		if(sym.doubler) {
			dimX *= 0.75;
			dimZ *= 0.75;
		}
		centerX++;
		centerZ++;
		oppX++;
		oppZ++;
		if(isDepression) {
			available -= 2;
			if(available > ((dungeon.verticle.value / 2) + 1)) available = ((dungeon.verticle.value / 2) + 1);
			platY = (byte) (floorY - dungeon.random.nextInt((dungeon.verticle.value / 2) + 1) -1);
		}
		else {
			platY = (byte) (floorY + 1 + (dungeon.random.nextInt(2)));
			if(available > 4) platY += (byte)(dungeon.random.nextInt(available - 3));
		}
		if(dungeon.random.nextBoolean() || !dungeon.complexity.use(dungeon.random)) {
			which = Shape.xgroup;
		} else {
			which = Shape.allSolids[dungeon.random.nextInt(Shape.allSolids.length)];
		}
		if(platY > nFloorY) nFloorY = platY; 
		which[rotation].drawPlatform(dungeon, this, platY, centerX, centerZ, dimX, dimZ, false, false);
		// Apply Symmetries
		switch (sym) {
			case NONE: break;
			case TR1: {
				oppX = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				oppZ = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ); 
				which[(rotation + 3) % 4].drawPlatform(dungeon, this, platY, oppX, 
						oppZ, dimX, dimZ, false, false);
			} break;
			case TR2: {
				oppX = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				oppZ = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ);  
				oppZ = endZ - (oppZ - beginZ); 
				which[(rotation + 3) % 4].drawPlatform(dungeon, this, platY, oppX, 
						oppZ, dimX, dimZ, false, true);				
			} break;
			case X: {
				which[rotation].drawPlatform(dungeon, this, platY, oppX, 
						centerZ, dimX, dimZ, true, false);
			} break;
			case Y: {
				which[rotation].drawPlatform(dungeon, this, platY, centerX, 
						oppZ, dimX, dimZ, false, true);
			} break;
			case XY: {
				which[rotation].drawPlatform(dungeon, this, platY, oppX, 
						centerZ, dimX, dimZ, true, false);
				which[rotation].drawPlatform(dungeon, this, platY, centerX, 
						oppZ, dimX, dimZ, false, true);
				which[rotation].drawPlatform(dungeon, this, platY, oppX, 
						oppZ, dimX, dimZ, true, true);			
			} break;
			case R: {
				which[(rotation + 2) % 4].drawPlatform(dungeon, this, platY, oppX, 
						oppZ, dimX, dimZ, false, false);			
			} break;
			case SW: {
				float swX1 = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				float swZ1 = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ);
				float swX2 = realX + ((oppZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				float swZ2 = realZ + ((oppX - realX) / (endX - beginX)) * (endZ - beginZ);
				which[(rotation + 1) % 4].drawPlatform(dungeon, this, platY, swX2, swZ1, 
						dimX, dimZ, false, false);
				which[(rotation + 3) % 4].drawPlatform(dungeon, this, platY, swX1, swZ2, 
						dimX, dimZ, false, false);
				which[(rotation + 2) % 4].drawPlatform(dungeon, this, platY, oppX, oppZ, 
						dimX, dimZ, false, false);			
			}
		}
	}
	
	
	public void pillar(Dungeon dungeon) {
		int pillarx1 = dungeon.random.nextInt(endX - beginX - 2) + 1;
		int pillarz1 = dungeon.random.nextInt(endZ - beginZ - 2) + 1;
		if(sym.halfX) pillarx1 = ((pillarx1 - 1) / 2) + 1;
		if(sym.halfZ) pillarz1 = ((pillarz1 - 1) / 2) + 1;
		int pillarx2 = endX - pillarx1;
		int pillarz2 = endZ - pillarz1;
		pillarx1 += beginX;
		pillarz1 += beginZ;
		switch (sym) {
		case NONE: break;
		case TR1:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarz1][pillarx1] = true;
			dungeon.map.wall[pillarx1][pillarz1] = pillarBlock;
			dungeon.map.wall[pillarz1][pillarx1] = pillarBlock;
			break;
		case TR2:
			dungeon.map.isWall[pillarx1][pillarz1]  = true;
			dungeon.map.isWall[pillarz1][pillarx1] = true;
			dungeon.map.wall[pillarx2][pillarz1]  = pillarBlock;
			dungeon.map.wall[pillarz2][pillarx1] = pillarBlock;
			break;
		case X:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx2][pillarz1] = true;
			dungeon.map.wall[pillarx1][pillarz1]  = pillarBlock;
			dungeon.map.wall[pillarx2][pillarz1]  = pillarBlock;
			break;
		case Y:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx1][pillarz2] = true;
			dungeon.map.wall[pillarx1][pillarz1]  = pillarBlock;
			dungeon.map.wall[pillarx1][pillarz2]  = pillarBlock;
			break;
		case XY:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx1][pillarz2] = true;
			dungeon.map.isWall[pillarx2][pillarz1] = true;
			dungeon.map.isWall[pillarx2][pillarz2] = true;
			dungeon.map.wall[pillarx1][pillarz1]  = pillarBlock;
			dungeon.map.wall[pillarx1][pillarz2]  = pillarBlock;
			dungeon.map.wall[pillarx2][pillarz1]  = pillarBlock;
			dungeon.map.wall[pillarx2][pillarz2]  = pillarBlock;
			break;
		case R:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx2][pillarz2] = true;
			dungeon.map.wall[pillarx1][pillarz1]  = pillarBlock;
			dungeon.map.wall[pillarx2][pillarz2]  = pillarBlock;
			break;
		case SW:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx1][pillarz2] = true;
			dungeon.map.isWall[pillarx2][pillarz1] = true;
			dungeon.map.isWall[pillarx2][pillarz2] = true;
			dungeon.map.wall[pillarx1][pillarz1]  = pillarBlock;
			dungeon.map.wall[pillarx1][pillarz2]  = pillarBlock;
			dungeon.map.wall[pillarx2][pillarz1]  = pillarBlock;
			dungeon.map.wall[pillarx2][pillarz2]  = pillarBlock;
		}
	}
	
	
	public void pool(Dungeon dungeon) {
		float centerX, centerZ, oppX, oppZ;
		float dimX, dimZ;
		int rotation = dungeon.random.nextInt(4);
		Shape[] which;
		dimX = ((endX - beginX) * ((dungeon.random.nextFloat() * 0.25f) + 0.15f));
		dimZ = ((endX - beginX) * ((dungeon.random.nextFloat() * 0.25f) + 0.15f));
		centerX = dungeon.random.nextInt(endX - beginX -1) + beginX + 1;
		centerZ = dungeon.random.nextInt(endZ - beginZ -1) + beginZ + 1;
		oppX = endX - (centerX - beginX); 
		oppZ = endZ - (centerZ - beginZ); 
		if(sym.halfX) {
			dimX *= 2;
			dimX /= 3;
			oppX = endX - ((centerX - beginX) / 2);
			centerX = ((centerX - beginX) / 2) + beginX;
		}
		if(sym.halfZ) {
			dimZ *= 2;
			dimZ /= 3;
			oppZ = endZ -((centerZ - beginZ) / 2);
			centerZ = ((centerZ - beginZ) / 2) + beginZ;
		}
		if(sym.doubler) {
			dimX *= 0.75;
			dimZ *= 0.75;
		}
		centerX++;
		centerZ++;
		oppX++;
		oppZ++;
		if(dungeon.random.nextBoolean() || !dungeon.complexity.use(dungeon.random)) {
			which = Shape.xgroup;
		} else {
			which = Shape.allSolids[dungeon.random.nextInt(Shape.allSolids.length)];
		}
		which[rotation].drawLiquid(dungeon, this, centerX, centerZ, dimX, dimZ, false, false);
		// Apply Symmetries
		switch (sym) {
			case NONE: break;
			case TR1: {
				oppX = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				oppZ = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ); 
				which[(rotation + 1) % 4].drawLiquid(dungeon, this, oppX, 
						oppZ, dimX, dimZ, false, false);
			} break;
			case TR2: {
				oppX = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				oppZ = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ); 
				oppZ = endZ - (oppZ - beginZ); 
				which[(rotation + 1) % 4].drawLiquid(dungeon, this, oppX, 
						oppZ, dimX, dimZ, false, true);				
			} break;
			case X: {
				which[rotation].drawLiquid(dungeon, this, oppX, 
						centerZ, dimX, dimZ, true, false);
			} break;
			case Y: {
				which[rotation].drawLiquid(dungeon, this, centerX, 
						oppZ, dimX, dimZ, false, true);
			} break;
			case XY: {
				which[rotation].drawLiquid(dungeon, this, oppX, 
						centerZ, dimX, dimZ, true, false);
				which[rotation].drawLiquid(dungeon, this, centerX, 
						oppZ, dimX, dimZ, false, true);
				which[rotation].drawLiquid(dungeon, this, oppX, 
						oppZ, dimX, dimZ, true, true);			
			} break;
			case R: {
				which[(rotation + 2) % 4].drawLiquid(dungeon, this, oppX, 
						oppZ, dimX, dimZ, false, false);			
			} break;
			case SW: {
				float swX1 = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				float swZ1 = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ);
				float swX2 = realX + ((oppZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				float swZ2 = realZ + ((oppX - realX) / (endX - beginX)) * (endZ - beginZ);
				which[(rotation + 1) % 4].drawLiquid(dungeon, this, swX2, 
						swZ1, dimX, dimZ, false, false);
				which[(rotation + 3) % 4].drawLiquid(dungeon, this, swX1, 
						swZ2, dimX, dimZ, false, false);
				which[(rotation + 2) % 4].drawLiquid(dungeon, this, oppX, 
						oppZ, dimX, dimZ, false, false);			
			}
		}
	}
	
	
	public void cutout(Dungeon dungeon) {
		float centerX, centerZ, oppX, oppZ;
		float dimX, dimZ;
		int rotation = dungeon.random.nextInt(4);
		Shape[] which;
		dimX = ((endX - beginX) * ((dungeon.random.nextFloat() * 0.20f) + 0.10f));
		dimZ = ((endX - beginX) * ((dungeon.random.nextFloat() * 0.20f) + 0.10f));
		centerX = dungeon.random.nextInt(endX - beginX -1) + beginX + 1;
		centerZ = dungeon.random.nextInt(endZ - beginZ -1) + beginZ + 1;
		oppX = endX - (centerX - beginX); 
		oppZ = endZ - (centerZ - beginZ); 
		if(sym.halfX) {
			dimX *= 2;
			dimX /= 3;
			oppX = endX - ((centerX - beginX) / 2);
			centerX = ((centerX - beginX) / 2) + beginX;
		}
		if(sym.halfZ) {
			dimZ *= 2;
			dimZ /= 3;
			oppZ = endZ -((centerZ - beginZ) / 2);
			centerZ = ((centerZ - beginZ) / 2) + beginZ;
		}
		if(sym.doubler) {
			dimX *= 0.7;
			dimZ *= 0.7;
		}
		centerX++;
		centerZ++;
		oppX++;
		oppZ++;
		if(!dungeon.complexity.use(dungeon.random)) {
			which = Shape.xgroup;
		} else {
			which = Shape.allSolids[dungeon.random.nextInt(Shape.allSolids.length)];
		}
		which[rotation].drawCutout(dungeon, this, centerX, centerZ, dimX, dimZ, false, false);
		// Apply Symmetries
		switch (sym) {
			case NONE: break;
			case TR1: {
				oppX = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				oppZ = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ); 
				which[(rotation + 1) % 4].drawCutout(dungeon, this, oppX, 
						oppZ, dimX, dimZ, false, false);
			} break;
			case TR2: {
				oppX = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				oppZ = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ); 
				oppZ = endZ - (oppZ - beginZ); 
				which[(rotation + 1) % 4].drawCutout(dungeon, this, oppX, 
						oppZ, dimX, dimZ, false, true);				
			} break;
			case X: {
				which[rotation].drawCutout(dungeon, this, oppX, 
						centerZ, dimX, dimZ, true, false);
			} break;
			case Y: {
				which[rotation].drawCutout(dungeon, this, centerX, 
						oppZ, dimX, dimZ, false, true);
			} break;
			case XY: {
				which[rotation].drawCutout(dungeon, this, oppX, 
						centerZ, dimX, dimZ, true, false);
				which[rotation].drawCutout(dungeon, this, centerX, 
						oppZ, dimX, dimZ, false, true);
				which[rotation].drawCutout(dungeon, this, oppX, 
						oppZ, dimX, dimZ, true, true);			
			} break;
			case R: {
				which[(rotation + 2) % 4].drawCutout(dungeon, this, oppX, 
						oppZ, dimX, dimZ, false, false);			
			} break;
			case SW: {
				float swX1 = realX + ((centerZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				float swZ1 = realZ + ((centerX - realX) / (endX - beginX)) * (endZ - beginZ);
				float swX2 = realX + ((oppZ - realZ) / (endZ - beginZ)) * (endX - beginX);
				float swZ2 = realZ + ((oppX - realX) / (endX - beginX)) * (endZ - beginZ);
				which[(rotation + 1) % 4].drawCutout(dungeon, this, swX2, 
						swZ1, dimX, dimZ, false, false);
				which[(rotation + 3) % 4].drawCutout(dungeon, this, swX1, 
						swZ2, dimX, dimZ, false, false);
				which[(rotation + 2) % 4].drawCutout(dungeon, this, oppX, 
						oppZ, dimX, dimZ, false, false);			
			}
		}
	}
	
	
	private void walkway(Dungeon dungeon) {
		int drop;
		if(dungeon.theme.type.contains(ThemeType.SWAMP)) drop = 1;
		else drop = 2;
		shape = Shapes.wholeShape(sym, dungeon.random);
		//System.out.println("About to flood room for walkway.");
		for(int i = beginX; i <= endX; i++) 
			for(int j = beginZ; j <= endZ; j++) {
				dungeon.map.floorY[i][j] -= drop;
				dungeon.map.hasLiquid[i][j] = true;
			}
		//System.out.println("Room is now flooded; ready to place walkway.");
		shape.family[orientation].drawWalkway(dungeon, this, realX, realZ, 
				(byte)(endX - beginX + 1), (byte)(endZ - beginZ + 1), XFlip, ZFlip);
	}
	
	
	private void cutin(Dungeon dungeon) {
		shape = Shapes.wholeShape(sym, dungeon.random);
		for(int i = beginX; i <= endX; i++) 
			for(int j = beginZ; j <= endZ; j++) {
				dungeon.map.isWall[i][j] = true;
			}
		shape.family[orientation].drawCutin(dungeon, this, realX, realZ, 
				(byte)(endX - beginX - 1), (byte)(endZ - beginZ - 1), XFlip, ZFlip);
	}
	
	
	public boolean plantChildren(Dungeon dungeon) {
		boolean result = false;
		for(PlaceSeed planted : childSeeds) {
			if(dungeon.rooms.size() >= dungeon.size.maxRooms) return false;
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
	
	
	public void addToConnections(Doorway door) {
		if(id < 1) {
			//System.err.println("[DLDUNGEONS] Error! Trying to add a " 
			//			+ "connection to room #" + id + " (nullRoom)!");
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
