package jaredbgreat.dldungeons.planner.astar;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.Tile;

public class Step2 extends Step {

	public Step2(int x, int z, Step previous, Tile destination, Dungeon dungeon) {
		super(x, z, previous, destination, dungeon);
		if(!dungeon.map.astared[x][z]) value += 7;
		if(dungeon.map.room[x][z] == 0) value += 256; 
	}
	
	
	public Step2(int x, int z, int i, int j, Tile destination) {
		super(x, z, i, j, destination);
	}


	public static Step2 firstFromDoorway(Tile door, Tile destination) {
		return new Step2(door.x, door.z, 0, 0, destination);		
	}

	
}
