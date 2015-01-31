package jaredbgreat.dldungeons.pieces;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		


public class Spawner {
	
	public int x, y, z;
	public String mob;
	
	public Spawner(int x, int y, int z, String mob) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mob = mob;
	}
}
