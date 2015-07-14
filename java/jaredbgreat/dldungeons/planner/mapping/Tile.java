package jaredbgreat.dldungeons.planner.mapping;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

public class Tile {
	//I've been bad with way over using public, but int this case its acting like a C struct
	public int x, z;
	public Tile(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	
	public boolean equals(Tile other) {
		return ((x == other.x) && (z == other.z));
	}
	
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof Tile) return equals((Tile) other);
		return false;
	}
	

	@Override
	public int hashCode() {
		return x + (z << 8) + (z << 16) + (x << 24);
	}
}
