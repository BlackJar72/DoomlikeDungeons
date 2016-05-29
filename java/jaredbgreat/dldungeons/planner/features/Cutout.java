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
		if(built) room.cutout(dungeon);
		return built;
	}	
}
