package jaredbgreat.dldungeons.planner.mapping;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

/**
 * This is a 1x1 column of block, or put differently, a block
 * in 2D.  This is used to repressent a location in the 2D map,
 * especially for use with pathfinding / graph algorithms, for 
 * which tiles are the vertices and adjency between tiles is an
 * edge.
 * 
 * This being used mostly as a C-struct, so everything is left 
 * public. 
 * 
 * @author Jared Blackburn
 *
 */
public class Tile {
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
