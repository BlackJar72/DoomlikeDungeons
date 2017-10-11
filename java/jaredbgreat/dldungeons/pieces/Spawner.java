package jaredbgreat.dldungeons.pieces;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		


/**
 * Represents a spawner that should be placed, including its location
 * in xyz coordinates and the name of the mob to be spawned.
 * 
 * @author Jared Blackburn
 *
 */
public class Spawner {
	
	private final int x, y, z, room, level;
	private final String mob;
	
	public Spawner(int x, int y, int z, int room, int level, String mob) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.room = room;
		this.level = level;
		this.mob = mob;
	}
	
	
	public int getX() {
		return x;
	}
	
	
	public int getY() {
		return y;
	}
	
	
	public int getZ() {
		return z;
	}
	
	
	public int getRoom() {
		return room;
	}
	
	
	public int getLevel() {
		return level;
	}
	
	
	public String getMob() {
		return mob;
	}
}
