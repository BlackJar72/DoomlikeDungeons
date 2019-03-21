package jaredbgreat.dldungeons.planner.astar;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.Tile;

public class Step2 extends Step {

	public Step2(int x, int z, Step previous, Tile destination, Dungeon dungeon) {
		super(x, z, previous, destination, dungeon);
		if(!dungeon.map.astared[x][z]) value += 7;
		if(dungeon.map.room[x][z] == 0) value += 512; 
	}
	
	
	public Step2(int x, int z, int i, int j, Tile destination) {
		super(x, z, i, j, destination);
	}


	public static Step2 firstFromDoorway(Tile door, Tile destination) {
		return new Step2(door.x, door.z, 0, 0, destination);		
	}

	
}
