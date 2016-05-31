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
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.Degree;

/**
 * A chance to add a pool of liquid (or a liquid stand-in).
 * 
 * @author Jared Blackburn
 *
 */
public class Pool extends FeatureAdder {

	public Pool(Degree chance) {
		super(chance);
	}
	

	@Override
	public void buildFeature(Dungeon dungeon, Room room) {
		float centerX, centerZ, oppX, oppZ;
		float dimX, dimZ;
		int rotation = dungeon.random.nextInt(4);
		Shape[] which;
		dimX = ((room.endX - room.beginX) 
				* ((dungeon.random.nextFloat() * 0.25f) + 0.15f));
		dimZ = ((room.endX - room.beginX) 
				* ((dungeon.random.nextFloat() * 0.25f) + 0.15f));
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
			which = Shape.allshapes[dungeon.random.nextInt(Shape.allshapes.length)];
		}
		which[rotation].drawLiquid(dungeon, room, centerX, centerZ, dimX, dimZ, false, false);
		// Apply Symmetries
		switch (room.sym) {
			case NONE: break;
			case TR1: {
				oppX = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				oppZ = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ); 
				which[(rotation + 1) % 4].drawLiquid(dungeon, room, oppX, 
						oppZ, dimX, dimZ, false, false);
			} break;
			case TR2: {
				oppX = room.realX + ((centerZ - room.realZ) 
						/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				oppZ = room.realZ + ((centerX - room.realX) 
						/ (room.endX - room.beginX)) * (room.endZ - room.beginZ); 
				oppZ = room.endZ - (oppZ - room.beginZ); 
				which[(rotation + 1) % 4].drawLiquid(dungeon, room, oppX, 
						oppZ, dimX, dimZ, false, true);				
			} break;
			case X: {
				which[rotation].drawLiquid(dungeon, room, oppX, 
						centerZ, dimX, dimZ, true, false);
			} break;
			case Z: {
				which[rotation].drawLiquid(dungeon, room, centerX, 
						oppZ, dimX, dimZ, false, true);
			} break;
			case XZ: {
				which[rotation].drawLiquid(dungeon, room, oppX, 
						centerZ, dimX, dimZ, true, false);
				which[rotation].drawLiquid(dungeon, room, centerX, 
						oppZ, dimX, dimZ, false, true);
				which[rotation].drawLiquid(dungeon, room, oppX, 
						oppZ, dimX, dimZ, true, true);			
			} break;
			case R: {
				which[(rotation + 2) % 4].drawLiquid(dungeon, room, oppX, 
						oppZ, dimX, dimZ, false, false);			
			} break;
			case SW: {
				float swX1 = room.realX 
						+ ((centerZ - room.realZ) 
								/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				float swZ1 = room.realZ 
						+ ((centerX - room.realX) 
								/ (room.endX - room.beginX)) * (room.endZ - room.beginZ);
				float swX2 = room.realX 
						+ ((oppZ - room.realZ) 
								/ (room.endZ - room.beginZ)) * (room.endX - room.beginX);
				float swZ2 = room.realZ 
						+ ((oppX - room.realX) 
								/ (room.endX - room.beginX)) * (room.endZ - room.beginZ);
				which[(rotation + 1) % 4].drawLiquid(dungeon, room, swX2, 
						swZ1, dimX, dimZ, false, false);
				which[(rotation + 3) % 4].drawLiquid(dungeon, room, swX1, 
						swZ2, dimX, dimZ, false, false);
				which[(rotation + 2) % 4].drawLiquid(dungeon, room, oppX, 
						oppZ, dimX, dimZ, false, false);			
			}
		}
	}

}
