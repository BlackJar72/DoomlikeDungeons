package jaredbgreat.dldungeons.planner.astar;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

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
	
	
	Step(int x, int z, int traversed, int changes, Tile destination) {
		super(x, z);
		distance = traversed;
		this.changes = changes;
		heuristic = Math.abs(x - destination.x) + Math.abs(z - destination.z);
		value = (changes * 16) + distance + heuristic; 
		parent = null;
	}
	
	
	/**
	 * The preferred way to constructor for creating a Step.
	 * 
	 * @param x
	 * @param z
	 * @param previous
	 * @param destination
	 * @param dungeon
	 */
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
		parent = previous;		
	}
	
	
	/**
	 * Returns the first step from a Tile ("door") toward a destination Tile.
	 * 
	 * @param door
	 * @param destination
	 * @return first step from door to destination
	 */
	public static Step firstFromDoorway(Tile door, Tile destination) {
		return new Step(door.x, door.z, 0, 0, destination);		
	}
	
	
	/**
	 * Returns a Tile with the same coordinates as the Step
	 * 
	 * @return The base Tile representation of this Step
	 */
	public Tile getTile() {
		return new Tile(x, z);
	}


	@Override
	public int compareTo(Step o) {
		return value - o.value;
	}

}
