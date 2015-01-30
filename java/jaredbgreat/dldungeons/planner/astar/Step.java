package jaredbgreat.dldungeons.planner.astar;

import java.util.EnumMap;

import jaredbgreat.dldungeons.pieces.Doorway;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.Tile;
import jaredbgreat.dldungeons.themes.ThemeFlags;

/**This meant to represent a Tile (block column / xz coordinate) that acts
 * as a both a node and an implied edge (from the previous node) in an
 * implied nav-graph consisting of the rooms floor surface.
 * 
 * The purpose of this is for storing steps on the route from door to 
 * door, both in an abstract PriorityQueue and in a tree representing 
 * valid routes.
 * 
 * @author JaredBGreat (Jared Blackburn)
 *
 */
public class Step extends Tile implements Comparable<Step> {
// FIXME: This class has a lot data that turn out to be extraneous
	
	int changes;   // Changes made to create a player-passable route
	int distance;  // Tiles traversed from start to get here
	int heuristic; // Manhattan distance to destination
	int value;     // (distance + 16*changes) + heuristic
	
	Step parent;
	
	private static enum Relation {
		P,  // Parent		
		C,  // Child
		N;  // None 
	}
	
	private static enum Move {
		A ( 0, -1),
		B (-1,  0), 
		C ( 0,  1),
		D ( 1,  0);
		public final int dx, dz;
		Move(int dx, int dz) {
			this.dx = dx;
			this.dz = dz;
		}
	}
	
	EnumMap<Move, Relation> neighbors;
	
	
	public Step(int x, int z, int traversed, int changes, Tile destination) {
		super(x, z);
		distance = traversed;
		this.changes = changes;
		heuristic = Math.abs(x - destination.x) + Math.abs(z - destination.z);
		value = (changes * 16) + distance + heuristic; 
		neighbors = new EnumMap(Move.class);
		neighbors.put(Move.A, Relation.N);
		neighbors.put(Move.B, Relation.N);
		neighbors.put(Move.C, Relation.N);
		neighbors.put(Move.D, Relation.N);
		parent = null;
	}
	
	
	// The preferred way to make one of these
	public Step(int x, int z, Step previous, Tile destination, Dungeon dungeon) {
		super(x, z);
		distance = previous.distance + 1;
		heuristic = Math.abs(x - destination.x) + Math.abs(z - destination.z);
		changes = previous.changes;
		if(dungeon.map.isWall[x][z]) changes++;
		if(dungeon.map.isFence[x][z]) changes++;
		if(dungeon.map.hasLiquid[x][z]) changes++;
		if(Math.abs(dungeon.map.floorY[x][z] 
				- dungeon.map.floorY[previous.x][previous.z]) > 1) changes++;
		value = (changes * 16) + distance + heuristic;
		neighbors = new EnumMap(Move.class);
		int pdx = previous.x - x;
		int pdz = previous.z - z;
		switch(pdx) {
			case -1:
				neighbors.put(Move.A, Relation.N);
				neighbors.put(Move.B, Relation.P);
				neighbors.put(Move.C, Relation.N);
				neighbors.put(Move.D, Relation.N);				
				break;
			case  1:
				neighbors.put(Move.A, Relation.N);
				neighbors.put(Move.B, Relation.N);
				neighbors.put(Move.C, Relation.N);
				neighbors.put(Move.D, Relation.P);
				break;
			default:
				switch(pdz){
					case -1:
						neighbors.put(Move.A, Relation.P);
						neighbors.put(Move.B, Relation.N);
						neighbors.put(Move.C, Relation.N);
						neighbors.put(Move.D, Relation.N);
						break;
					case  1: 
						neighbors.put(Move.A, Relation.N);
						neighbors.put(Move.B, Relation.N);
						neighbors.put(Move.C, Relation.P);
						neighbors.put(Move.D, Relation.N);
						break;
					default:
						System.err.println("[DLDUNGEONS] Error! " 
								+ "AStar.Step given invalid parent " 
								+ "in constructor!");
				}
		}
		parent = previous;		
	}
	
	
	public static Step firstFromDoorway(Tile door, Tile destination) {
		return new Step(door.x, door.z, 0, 0, destination);		
	}
	
	
	public Tile getTile() {
		return new Tile(x, z);
	}


	@Override
	public int compareTo(Step o) {
		return value - o.value;
	}

}
