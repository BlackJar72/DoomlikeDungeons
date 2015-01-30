package jaredbgreat.dldungeons.pieces;

import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.Tile;

public class Doorway extends Tile implements Comparable<Doorway> {
	public boolean xOriented;
	public int priority;
	public int otherside;
	
	
	public Doorway(int x, int z, boolean xOriented) {
		super(x, z);
		this.xOriented = xOriented;
		priority = 0;
	}
	
	
	// Number of sides wit a pool
	public void prioritize(Dungeon dungeon, int start) {
		if(xOriented) {
			if(dungeon.map.hasLiquid[x+1][z]) priority++;
			if(dungeon.map.hasLiquid[x-1][z]) priority++;
			if(dungeon.map.room[x+1][z] == start) 
				otherside = dungeon.map.room[x-1][z];
			else otherside = dungeon.map.room[x+1][z];
		} else {
			if(dungeon.map.hasLiquid[x][z+1]) priority++;
			if(dungeon.map.hasLiquid[x][z-1]) priority++;
			if(dungeon.map.room[x][z+1] == start) 
				otherside = dungeon.map.room[x][z-1];
			else otherside = dungeon.map.room[x][z+1];			
		}
	}
	
	
	public void connect(int start) {
		
	}


	@Override
	public int compareTo(Doorway o) {
		return priority - o.priority;
	}
	
	
	public Tile getTile() {
		return new Tile(x, z);
	}
	
	
	
}
