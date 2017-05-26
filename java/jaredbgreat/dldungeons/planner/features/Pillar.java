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


import jaredbgreat.dldungeons.planner.Level;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.Degree;

/**
 * A chance to add a pillar / column (1x1 wall with a different block) 
 * into a room.  
 * 
 * @author Jared Blackburn
 *
 */
public class Pillar extends FeatureAdder {

	public Pillar(Degree chance) {
		super(chance);
	}
	

	@Override
	public void buildFeature(Level dungeon, Room room) {
		int pillarx1 = dungeon.random.nextInt(room.endX - room.beginX - 2) + 1;
		int pillarz1 = dungeon.random.nextInt(room.endZ - room.beginZ - 2) + 1;
		if(room.sym.halfX) pillarx1 = ((pillarx1 - 1) / 2) + 1;
		if(room.sym.halfZ) pillarz1 = ((pillarz1 - 1) / 2) + 1;
		int pillarx2 = room.endX - pillarx1;
		int pillarz2 = room.endZ - pillarz1;
		pillarx1 += room.beginX;
		pillarz1 += room.beginZ;
		switch (room.sym) {
		case NONE: break;
		case TR1:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarz1][pillarx1] = true;
			dungeon.map.wall[pillarx1][pillarz1]   = room.pillarBlock;
			dungeon.map.wall[pillarz1][pillarx1]   = room.pillarBlock;
			break;
		case TR2:
			dungeon.map.isWall[pillarx1][pillarz1]  = true;
			dungeon.map.isWall[pillarz1][pillarx1]  = true;
			dungeon.map.wall[pillarx2][pillarz1]    = room.pillarBlock;
			dungeon.map.wall[pillarz2][pillarx1]    = room.pillarBlock;
			break;
		case X:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx2][pillarz1] = true;
			dungeon.map.wall[pillarx1][pillarz1]   = room.pillarBlock;
			dungeon.map.wall[pillarx2][pillarz1]   = room.pillarBlock;
			break;
		case Z:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx1][pillarz2] = true;
			dungeon.map.wall[pillarx1][pillarz1]   = room.pillarBlock;
			dungeon.map.wall[pillarx1][pillarz2]   = room.pillarBlock;
			break;
		case XZ:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx1][pillarz2] = true;
			dungeon.map.isWall[pillarx2][pillarz1] = true;
			dungeon.map.isWall[pillarx2][pillarz2] = true;
			dungeon.map.wall[pillarx1][pillarz1]   = room.pillarBlock;
			dungeon.map.wall[pillarx1][pillarz2]   = room.pillarBlock;
			dungeon.map.wall[pillarx2][pillarz1]   = room.pillarBlock;
			dungeon.map.wall[pillarx2][pillarz2]   = room.pillarBlock;
			break;
		case R:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx2][pillarz2] = true;
			dungeon.map.wall[pillarx1][pillarz1]   = room.pillarBlock;
			dungeon.map.wall[pillarx2][pillarz2]   = room.pillarBlock;
			break;
		case SW:
			dungeon.map.isWall[pillarx1][pillarz1] = true;
			dungeon.map.isWall[pillarx1][pillarz2] = true;
			dungeon.map.isWall[pillarx2][pillarz1] = true;
			dungeon.map.isWall[pillarx2][pillarz2] = true;
			dungeon.map.wall[pillarx1][pillarz1]   = room.pillarBlock;
			dungeon.map.wall[pillarx1][pillarz2]   = room.pillarBlock;
			dungeon.map.wall[pillarx2][pillarz1]   = room.pillarBlock;
			dungeon.map.wall[pillarx2][pillarz2]   = room.pillarBlock;
		}
	}
}
