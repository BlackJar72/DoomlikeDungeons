package jaredbgreat.dldungeons.pieces;

import jaredbgreat.dldungeons.parser.Tokenizer;


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
	
	private final int x, y, z;
	private final String mob;
	private final String nbt;
	
	public Spawner(int x, int y, int z, String mob) {
		this.x = x;
		this.y = y;
		this.z = z;
		Tokenizer tokens = new Tokenizer(mob, "([{}])");
		this.mob = tokens.nextToken();
		if(tokens.hasMoreTokens()) {
			this.nbt = tokens.nextToken();
		} else {
			this.nbt = null;
		}
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
	
	
	public String getMob() {
		return mob;
	}
}
