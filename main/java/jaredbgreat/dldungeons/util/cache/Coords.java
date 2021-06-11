package jaredbgreat.dldungeons.util.cache;

import java.util.Comparator;
import java.util.List;

import jaredbgreat.dldungeons.util.math.SpatialHash;

public final class Coords implements Comparable<Coords> {
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
    
    
	public static final class HashCompare implements Comparator<Coords> {
		public static final HashCompare C = new HashCompare();
		@Override
		public int compare(Coords o1, Coords o2) {
			return o1.hashCode() - o2.hashCode();
		}
	}
	
	
	public static final List<Coords> HashSort(List<Coords> l) {
		l.sort(HashCompare.C);
		return l;
	}


	@Override
	/**
	 * For this class compareTo() compares the hash, to return a 
	 * consistent pseudorandom order (i.e., it should look random 
	 * but the order should be the same on any short of the same 
	 * Coords).
	 * 
	 * (No, this would not be good for a general coordinate system, 
	 * but this is not that
	 */
	public int compareTo(Coords o) {
		return hashCode() - o.hashCode();
	}
        
        
}

