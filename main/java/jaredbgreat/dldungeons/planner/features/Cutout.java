package jaredbgreat.dldungeons.planner.features;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.pieces.Shape;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.Degree;

/**
 * A chance for to add a wall segment to the room. 
 * 
 * @author Jared Blackburn
 *
 */
public class Cutout extends FeatureAdder {

	public Cutout(Degree chance) {
		super(chance);
	}

	@Override
	public boolean addFeature(Dungeon dungeon, Room room) {
		boolean built = chance.use(dungeon.random);
		if(built) buildFeature(dungeon, room);
		return built;
	}

	@Override
	public void buildFeature(Dungeon dungeon, Room room) {
		float centerX, centerZ, oppX, oppZ;
		float dimX, dimZ;
		int rotation = dungeon.random.nextInt(4);
		Shape[] which;
		dimX = ((room.endX - room.beginX) * ((dungeon.random.nextFloat() * 0.20f) + 0.10f));
		dimZ = ((room.endX - room.beginX) * ((dungeon.random.nextFloat() * 0.20f) + 0.10f));
		centerX = dungeon.random.nextInt(room.endX - room.beginX -1) + room.beginX + 1;
		centerZ = dungeon.random.nextInt(room.endZ - room.beginZ -1) + room.beginZ + 1;
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
		which[rotation].drawCutout(dungeon, room, centerX, centerZ, dimX, dimZ, false, false);
		// Apply Symmetries
		switch (room.sym) {
			case NONE: break;
			case TR1: {
				oppX = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				oppZ = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ); 
				which[(rotation + 1) % 4].drawCutout(dungeon, room, oppX, 
						oppZ, dimX, dimZ, false, false);
			} break;
			case TR2: {
				oppX = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				oppZ = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ); 
				oppZ = room.endZ - (oppZ - room.beginZ); 
				which[(rotation + 1) % 4].drawCutout(dungeon, room, oppX, 
						oppZ, dimX, dimZ, false, true);				
			} break;
			case X: {
				which[rotation].drawCutout(dungeon, room, oppX, 
						centerZ, dimX, dimZ, true, false);
			} break;
			case Z: {
				which[rotation].drawCutout(dungeon, room, centerX, 
						oppZ, dimX, dimZ, false, true);
			} break;
			case XZ: {
				which[rotation].drawCutout(dungeon, room, oppX, 
						centerZ, dimX, dimZ, true, false);
				which[rotation].drawCutout(dungeon, room, centerX, 
						oppZ, dimX, dimZ, false, true);
				which[rotation].drawCutout(dungeon, room, oppX, 
						oppZ, dimX, dimZ, true, true);			
			} break;
			case R: {
				which[(rotation + 2) % 4].drawCutout(dungeon, room, oppX, 
						oppZ, dimX, dimZ, false, false);			
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
				which[(rotation + 1) % 4].drawCutout(dungeon, room, swX2, 
						swZ1, dimX, dimZ, false, false);
				which[(rotation + 3) % 4].drawCutout(dungeon, room, swX1, 
						swZ2, dimX, dimZ, false, false);
				which[(rotation + 2) % 4].drawCutout(dungeon, room, oppX, 
						oppZ, dimX, dimZ, false, false);			
			}
		}
	}	
}
