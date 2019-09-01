package jaredbgreat.dldungeons.planner.features;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.RoomSeed;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.Degree;

/**
 * A chance to add a sub-room inside the main room.
 * 
 * @author Jared Blackburn
 *
 */
public class IslandRoom extends FeatureAdder {
	private boolean built;
	
	public IslandRoom(Degree chance) {
		super(chance);
	}
	
	
	
	public boolean addFeature(Dungeon dungeon, Room room) {
		built = chance.use(dungeon.random);
		if(built) buildFeature(dungeon, room);
		return built;	
	}
	

	@Override
	public void buildFeature(Dungeon dungeon, Room room) {
		built = buildSubroom(dungeon, room);
	}
	
	
	private boolean buildSubroom(Dungeon dungeon, Room room) {
		int dimX = (int)((room.endX - room.beginX) 
				* (0.2f + (0.3f * dungeon.random.nextFloat()))); 
		int dimZ = (int)((room.endZ - room.beginZ) 
				* (0.2f + (0.3f * dungeon.random.nextFloat())));
		float centerX, centerZ, oppX, oppZ;
		centerX = dungeon.random.nextInt(room.endX - room.beginX) + room.beginX;
		centerZ = dungeon.random.nextInt(room.endZ - room.beginZ) + room.beginZ;
		oppX = room.endX - (centerX - room.beginX); 
		oppZ = room.endZ - (centerZ - room.beginZ); 
		if(room.sym.halfX) {
			dimX *= 2;
			dimX /= 3;
			oppX = room.endX - ((centerX - room.beginX) / 2);
			centerX = ((centerX - room.beginX) / 2) + room.beginX;
		}
		if(room.sym.halfZ) {
			dimZ *= 2;
			dimZ /= 3;
			oppZ = room.endZ -((centerZ - room.beginZ) / 2);
			centerZ = ((centerZ - room.beginZ) / 2) + room.beginZ;
		}
		if(room.sym.doubler) {
			dimX *= 0.75;
			dimZ *= 0.75;
		}
		if((dimX < 5) || (dimZ < 5)) return false;
		int ymod = (dimX <= dimZ) ? (int) Math.sqrt(dimX) : (int) Math.sqrt(dimZ);
		int height = dungeon.random.nextInt((dungeon.verticle.value / 2) + ymod + 1) + 2;
		Room created = 
				new RoomSeed((int)centerX, room.floorY, 
						(int)centerZ).growRoom(dimX, dimZ, height, dungeon, room, room);
		if(created == null) return false;
		// Apply Symmetries
		switch (room.sym) {
			case NONE: break;
			case TR1: {
				oppX = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				oppZ = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ); 
				created = 
						new RoomSeed((int)oppX, room.floorY, 
								(int)oppZ).growRoom(dimZ, dimX, height, dungeon, room, room);
			} break;
			case TR2: {
				oppX = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				oppZ = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ);  
				oppZ = room.endZ - (oppZ - room.beginZ);  
				created = 
						new RoomSeed((int)oppX, room.floorY, 
								(int)oppZ).growRoom(dimZ, dimX, height, dungeon, room, room);
			} break;
			case X: {
				created = 
						new RoomSeed((int)oppX, room.floorY, 
								(int)centerZ).growRoom(dimX, dimZ, height, dungeon, room, room);
			} break;
			case Z: {
				created = 
						new RoomSeed((int)centerX, room.floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, room, room);
			} break;
			case XZ: {
				created = 
						new RoomSeed((int)oppX, room.floorY, 
								(int)centerZ).growRoom(dimX, dimZ, height, dungeon, room, room);
				created = 
						new RoomSeed((int)centerX, room.floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, room, room);
				created = 
						new RoomSeed((int)oppX, room.floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, room, room);
			} break;
			case R: { 
				created = 
						new RoomSeed((int)oppX, room.floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, room, room);
			} break;
			case SW: {
				float swX1 = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				float swZ1 = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ);
				float swX2 = room.realX + ((oppZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				float swZ2 = room.realZ + ((oppX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ);
				created = 
						new RoomSeed((int)swX2, room.floorY, 
								(int)swZ1).growRoom(dimZ, dimX, height, dungeon, room, room);
				created = 
						new RoomSeed((int)swX1, room.floorY, 
								(int)swZ2).growRoom(dimZ, dimX, height, dungeon, room, room);
				created = 
						new RoomSeed((int)oppX, room.floorY, 
								(int)oppZ).growRoom(dimX, dimZ, height, dungeon, room, room);
			}
		}
		return true;
	}
}