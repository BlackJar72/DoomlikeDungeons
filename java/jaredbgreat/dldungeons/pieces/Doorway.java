package jaredbgreat.dldungeons.pieces;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.Tile;


/**
 * This class represents a doorway for use with classes in the astar 
 * package, notable DoorChecker and AStar.
 * 
 * Note that this represents the door in relation to a specific room;
 * the same room will have a separate listing for each room it connects
 * within that rooms list of doors.
 * 
 * @author Jared Blackburn
 *
 */
public class Doorway extends Tile implements Comparable<Doorway> {
	/**
	 * True if aligned along the x axis, false if aligned along the z axis.
	 */
	public boolean xOriented;
	/**
	 * How desirable this door is as the primary connection between rooms; this
	 * is effected by its position in relation to pool and its previous uses in
	 * connecting rooms. 
	 */
	public int priority;
	/**
	 * The id (index) of the room on the other side.
	 */
	public int otherside;
	
	
	public Doorway(int x, int z, boolean xOriented) {
		super(x, z);
		this.xOriented = xOriented;
		priority = 0;
	}
	
	
	public Doorway(Doorway door) {
		super(door.x, door.z);
		xOriented = door.xOriented;
		priority = door.priority;
	}
	
	
	public Doorway(Doorway door, int otherside) {
		super(door.x, door.z);
		xOriented = door.xOriented;
		priority = door.priority;
		this.otherside = otherside;
	}
	
	
	/**
	 * This will find the number of sides bordering a "liguid"
	 * pool and add it to the priority value; since the Java
	 * priority queue sorts to lowest value first increasing this
	 * effectively decreases the priority so that pools with no
	 * or fewer pools beside them will be chosen first as doors
	 * for AStar to connect.
	 * 
	 * @param dungeon
	 * @param start
	 */
	public void prioritize(Dungeon dungeon, int start) {
		if(xOriented) {
			if(dungeon.map.hasLiquid[x+1][z]) priority++;
			if(dungeon.map.hasLiquid[x-1][z]) priority++;
			if(dungeon.map.room[x+1][z] == start) 
				otherside = dungeon.map.room[x-1][z];
			else otherside = dungeon.map.room[x+1][z];
		} else {
			if(dungeon.map.hasLiquid[x][z+1]) priority++;
			if(dungeon.map.hasLiquid[x][z-1]) priority++;
			if(dungeon.map.room[x][z+1] == start) 
				otherside = dungeon.map.room[x][z-1];
			else otherside = dungeon.map.room[x][z+1];			
		}
	}


	@Override
	public int compareTo(Doorway o) {
		return priority - o.priority;
	}
	
	
	/**
	 * Returns the same location represented as a Tile.
	 * 
	 * @return basic tile at the same location
	 */
	public Tile getTile() {
		return new Tile(x, z);
	}
	
	
	
}
