package jaredbgreat.dldungeons.util.cache;

import jaredbgreat.dldungeons.util.math.SpatialHash;

public final class Coords {
	private static final SpatialHash nh = new SpatialHash(0xADD5C0DE);
	private final int x, z, d;
	
	
	public Coords(int x, int z, int d) {
        this.x = x;
        this.z = z;
        this.d = d;
	}
	
	
	@Override
	public boolean equals(Object other) {
        if(other instanceof Coords) {
                Coords o = (Coords)other;
                return ((o.x == x) && (o.z == z));
        }
        return false;
	}
	
	
	public boolean equals(int x, int z) {
        return (this.x == x) && (this.z == z);
	}
	
	
	@Override
	public int hashCode() {
            return nh.intFor(x, z, d);			
	}
        
        
    public static int hashCoords(int x, int z, int d) {
    		return nh.intFor(x, z, d);			
    }
        
        
    public int getX() {
            return x;
    }
        
        
    public int getZ() {
            return z;
    }
    
    
	public int getDimension() {
	        return d;
	}
    
    
    public String toString() {
    	return "(" + x + ", " + z + " in dimension " + d + ")";
    }
        
        
}

