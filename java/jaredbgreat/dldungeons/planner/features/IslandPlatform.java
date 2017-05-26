package jaredbgreat.dldungeons.planner.features;


/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	


import jaredbgreat.dldungeons.pieces.Shape;
import jaredbgreat.dldungeons.planner.Level;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.Degree;

/**
 * A chance to add a platform.
 * 
 * @author Jared Blackurn
 *
 */
public class IslandPlatform extends FeatureAdder {
	protected boolean isDepression;

	public IslandPlatform(Degree chance) {
		super(chance);
		isDepression = this instanceof Depression;
	}
	

	@Override
	public void buildFeature(Level dungeon, Room room) {
		int available = room.ceilY - room.floorY;
		if(available < 4) return;
		float dimX, dimZ, centerX, centerZ, oppX, oppZ;
		byte platY;
		int rotation = dungeon.random.nextInt(4);
		Shape[] which;
		dimX = ((room.endX - room.beginX) * ((dungeon.random.nextFloat() * 0.25f) + 0.15f));
		dimZ = ((room.endX - room.beginX) * ((dungeon.random.nextFloat() * 0.25f) + 0.15f));
		centerX = dungeon.random.nextInt(room.endX - room.beginX - 1) + room.beginX + 1;
		centerZ = dungeon.random.nextInt(room.endZ - room.beginZ - 1) + room.beginZ + 1;
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
		centerX++;
		centerZ++;
		oppX++;
		oppZ++;
		if(isDepression) {
			available -= 2;
			if(available > ((dungeon.verticle.value / 2) + 1)) available = ((dungeon.verticle.value / 2) + 1);
			platY = (byte) (room.floorY - dungeon.random.nextInt((dungeon.verticle.value / 2) + 1) -1);
		}
		else {
			platY = (byte) (room.floorY + 1 + (dungeon.random.nextInt(2)));
			if(available > 4) platY += (byte)(dungeon.random.nextInt(available - 3));
		}
		if(dungeon.random.nextBoolean() || !dungeon.complexity.use(dungeon.random)) {
			which = Shape.xgroup;
		} else {
			which = Shape.allSolids[dungeon.random.nextInt(Shape.allSolids.length)];
		}
		if(platY > room.nFloorY) room.nFloorY = platY; 
		which[rotation].drawPlatform(dungeon, room, platY, centerX, 
				centerZ, dimX, dimZ, false, false);
		// Apply Symmetries
		switch (room.sym) {
			case NONE: break;
			case TR1: {
				oppX = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				oppZ = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ); 
				which[(rotation + 3) % 4].drawPlatform(dungeon, room, platY, oppX, 
						oppZ, dimX, dimZ, false, false);
			} break;
			case TR2: {
				oppX = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				oppZ = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ);  
				oppZ = room.endZ - (oppZ - room.beginZ); 
				which[(rotation + 3) % 4].drawPlatform(dungeon, room, platY, oppX, 
						oppZ, dimX, dimZ, false, true);				
			} break;
			case X: {
				which[rotation].drawPlatform(dungeon, room, platY, oppX, 
						centerZ, dimX, dimZ, true, false);
			} break;
			case Z: {
				which[rotation].drawPlatform(dungeon, room, platY, centerX, 
						oppZ, dimX, dimZ, false, true);
			} break;
			case XZ: {
				which[rotation].drawPlatform(dungeon, room, platY, oppX, 
						centerZ, dimX, dimZ, true, false);
				which[rotation].drawPlatform(dungeon, room, platY, centerX, 
						oppZ, dimX, dimZ, false, true);
				which[rotation].drawPlatform(dungeon, room, platY, oppX, 
						oppZ, dimX, dimZ, true, true);			
			} break;
			case R: {
				which[(rotation + 2) % 4].drawPlatform(dungeon, room, platY, oppX, 
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
				which[(rotation + 1) % 4].drawPlatform(dungeon, room, platY, swX2, swZ1, 
						dimX, dimZ, false, false);
				which[(rotation + 3) % 4].drawPlatform(dungeon, room, platY, swX1, swZ2, 
						dimX, dimZ, false, false);
				which[(rotation + 2) % 4].drawPlatform(dungeon, room, platY, oppX, oppZ, 
						dimX, dimZ, false, false);			
			}
		}
	}

}
