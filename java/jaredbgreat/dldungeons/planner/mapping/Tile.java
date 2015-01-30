package jaredbgreat.dldungeons.planner.mapping;

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
		return x + (z << 8);
	}
}
