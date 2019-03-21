package jaredbgreat.dldungeons.planner.astar;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import java.util.PriorityQueue;

import jaredbgreat.dldungeons.pieces.Doorway;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.Tile;
import jaredbgreat.dldungeons.rooms.Room;

public class AStar2 extends AStar {
	PriorityQueue<Step2> edges;  // Steps to consider
	Step2 root;

	public AStar2(Dungeon dungeon, Room start, Room finish) {
		this.dungeon = dungeon;
		room = 0;
		x1 = 2;
		x2 = dungeon.size.width - 3;
		z1 = 1;
		z2 = dungeon.size.width - 2;
		
		end = new Tile((int)finish.realX, (int)finish.realZ);		
		if(end.x < x1) {
			x1 = end.x;
		}
		if (end.x > x2) {
			x2 = end.x;
		}
		if(end.z < z1) {
			z1 = end.z;
		} 
		if (end.z > z2) {
			z2 = end.z;
		}
		root = Step2.firstFromDoorway(new Tile((int)start.realX, (int)start.realZ), end);
		if(root.x < x1) {
			x1 = root.x;
		}		
		if (root.x > x2) {
			x2 = root.x;
		}
		if(root.z < z1) {
			z1 = root.z;
		}
		if (root.z > z2) {
			z2 = root.z;
		}
		
		spt = dungeon.map.nodedge;
		for(int i = x1; i <= x2; i++)
			for(int j = z1; j <= z2; j++)
				spt[i][j] = null;
		
		spt[root.x][root.z] = root;
		edges = new PriorityQueue<Step2>();
		edges.add(root);
	}
	
	
	/**
	 * This uses the data from AStar to make useful changes to the 
	 * dungeon.
	 * 
	 * @param end
	 */
	@Override
	public void makeRoute(Step end) {
		int roomid = dungeon.map.room[end.x][end.z];
		byte floory = dungeon.map.floorY[end.x][end.z];
		byte ceily  = (byte)(dungeon.baseHeight + 2);
		int size = dungeon.random.nextInt(2) + 1;
		Step child = end, parent = end.parent;
		if(parent == null) return;
		do {
			if(dungeon.map.room[child.x][child.z] != 0) {
				roomid = dungeon.map.room[child.x][child.z];
				floory = dungeon.map.floorY[child.x][child.z];
				ceily  = (byte)(floory + 2);
			} else {
				for(int i = -size; i <= size; i++) 
					for(int j = -size; j <= size; j++) {
						dungeon.map.floorY[child.x+i][child.z+j] = floory;
						dungeon.map.ceilY[child.x+i][child.z+j] = ceily;
						dungeon.map.hasLiquid[child.x+i][child.z+j] = false;
						if(dungeon.map.room[child.x+i][child.z+j] < 1) {
							dungeon.map.room[child.x+i][child.z+j] = roomid;
							dungeon.map.isWall[child.x+i][child.z+j] = true;
							dungeon.map.floor[child.x+i][child.z+j] = dungeon.floorBlock;
							dungeon.map.ceiling[child.x+i][child.z+j] = dungeon.cielingBlock;
							dungeon.map.wall[child.x+i][child.z+j] = dungeon.wallBlock1;
						
						}
						if(dungeon.map.astared[child.x+i][child.z+j] || 
								((Math.abs(i) < size) && (Math.abs(j) < size))) {
							dungeon.map.isDoor[child.x+i][child.z+j] = true;
							dungeon.map.isWall[child.x+i][child.z+j] = false;
						}
					}
			}
			dungeon.map.astared[child.x][child.z] = true;
			if(dungeon.map.isWall[child.x][child.z] ||
						dungeon.map.isFence[child.x][child.z]) 
					addDoor(parent, child);
			if(dungeon.map.hasLiquid[child.x][child.z]) 
					fixLiquid(parent, child, 
							(byte) dungeon.rooms.get(room).floorY);
			fixHeights(parent, child);
			child = parent;
			parent = child.parent;
		} while (parent != null);
		dungeon.map.astared[child.x][child.z] = true;
		if(dungeon.map.isWall[child.x][child.z] ||
					dungeon.map.isFence[child.x][child.z]) 
				dungeon.map.isDoor[child.x][child.z] = true;
		if(dungeon.map.hasLiquid[child.x][child.z]) {
			dungeon.map.hasLiquid[child.x][child.z] = false;
			dungeon.map.floorY[child.x][child.z] = 
					(byte) dungeon.rooms.get(room).floorY;
		}
	}
	
	/**
	 * This is what actually runs A* (and also calls other methods
	 * to make practical use of the results).
	 */
	@Override
	public void seek() {
		Step2 current;
		do {
			current = edges.poll();
			getEdgeStep(current);
		} while(!current.equals(end));
		makeRoute(current);
	}
	
	
	@Override
	protected void getEdgeStep(Step src) {
		Step2 nextEdge;
		int x, z;
		// Below should probably be a method call, but stingy with overhead 
		x = src.x - 1; z = src.z;
		if(x >= x1 && x <= x2) {
			nextEdge = new Step2(x, z, src, end, dungeon);
			if(spt[x][z] == null) {
				spt[x][z] = nextEdge;
				edges.add(nextEdge);
			}
		}
		x = src.x + 1; z = src.z;
		if(x >= x1 && x <= x2)  {
			nextEdge = new Step2(x, z, src, end, dungeon);
			if(spt[x][z] == null) {
				spt[x][z] = nextEdge;
				edges.add(nextEdge);
			}
		}
		x = src.x; z = src.z - 1;
		if(z >= z1 && z <= z2) {
			nextEdge = new Step2(x, z, src, end, dungeon);
			if(spt[x][z] == null) {
				spt[x][z] = nextEdge;
				edges.add(nextEdge);
			}
		}
		x = src.x; z = src.z + 1;
		if(z >= z1 && z <= z2)  {
			nextEdge = new Step2(x, z, src, end, dungeon);
			if(spt[x][z] == null) {
				spt[x][z] = nextEdge;
				edges.add(nextEdge);
			}
		}
	}

}
