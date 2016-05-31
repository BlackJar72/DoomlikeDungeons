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
 * A base class for all room features.  This allows a chance for
 * a type of feature occurring to be store in a collection and sorted
 * to randomize the order of evaluation. 
 * 
 * @author Jared Blackburn
 *
 */
public abstract class FeatureAdder {
	
	/**
	 * The chance of placing the feature on a given use.
	 */
	Degree chance;

	public FeatureAdder(Degree chance) {
		this.chance = chance;
	}
	
	
	/**
	 * This will try to add the feature to the room, based on the features
	 * pre-defined degree of chance.  The actual feature is built by calling 
	 * a method back on the room.  It will return true if the room was 
	 * instructed to add the feature, and false otherwise; whether or not the 
	 * feature was actually added successfully is another matter.
	 * 
	 * @param dungeon
	 * @param room
	 * @return the result of chance.use()
	 */
	public boolean addFeature(Dungeon dungeon, Room room) {
		boolean built = chance.use(dungeon.random);
		if(built) buildFeature(dungeon, room);
		return built;	
	}
	
	
	/**
	 * This will actually build the feature into the room.
	 * 
	 * @param dungeon
	 * @param room
	 */
	public abstract void buildFeature(Dungeon dungeon, Room room);

}
