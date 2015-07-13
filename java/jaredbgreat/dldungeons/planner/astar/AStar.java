package jaredbgreat.dldungeons.planner.astar;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.pieces.Doorway;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.MapMatrix;
import jaredbgreat.dldungeons.planner.mapping.Tile;
import jaredbgreat.dldungeons.rooms.Room;

import java.util.PriorityQueue;

public class AStar {
	
	int room, x1, x2, z1, z2;   // Room id and bounds
	PriorityQueue<Step> edges;  // Steps to consider
	Step[][] spt;               // Not a true tree but will hold the data
	Dungeon dungeon;
	Step root;
	Tile end;
	
	protected AStar() {
		// Do not use!
	}
	
	public AStar(Room room, Dungeon dungeon, Doorway start, Doorway finish) {
		this.room = room.id;
		this.dungeon = dungeon;
		x1 = room.beginX;
		x2 = room.endX;
		z1 = room.beginZ;
		z2 = room.endZ;		
		spt = dungeon.map.nodedge;
		for(int i = x1; i <= x2; i++)
			for(int j = z1; j <= z2; j++)
				spt[i][j] = null;
		
		end = finish;		
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
		
		root = Step.firstFromDoorway(start, finish);
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
		
		spt[root.x][root.z] = root;
		edges = new PriorityQueue<Step>();
		edges.add(root);
	}
	
	/**
	 * This is what actually runs A* (and also calls other methods
	 * to make practical use of the results).
	 */
	public void seek() {
		Step current;
		int count = 0;
		do {
			count++;
			current = edges.poll();
			getEdgeStep(current);
		} while(!current.equals(end) && !edges.isEmpty());
		makeRoute(current);
	}
	
	
	/**
	 * This uses the data froom AStar to make useful changes to the 
	 * dungeon.
	 * 
	 * @param end
	 */
	public void makeRoute(Step end) {
		//TODO: Handle symmetries (or not...?)
		Step child = end.parent, parent = end;
		if(child == null) return;
		do {
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
	
	
	protected void addDoor(Step from, Step to) {
		dungeon.map.isDoor[to.x][to.z] = true;
		dungeon.map.isDoor[from.x][from.z] = true;
//		if(from.x != to.x) { 
//			if(DoorChecker.validateDoor(dungeon, 
//					new Doorway(to.x, to.z, true)))
//				dungeon.map.isDoor[to.x][to.z] = true;
//		} else { 
//			if(DoorChecker.validateDoor(dungeon, 
//					new Doorway(to.x, to.z, false)))
//				dungeon.map.isDoor[to.x][to.z] = true;
//		}		
	}
	
	
	protected void fixLiquid(Step from, Step to, byte floorY) {
		dungeon.map.hasLiquid[to.x][to.z] = false;
		dungeon.map.floorY[to.x][to.z] = 
				dungeon.map.floorY[from.x][from.z];
		if(dungeon.map.floorY[to.x][to.z] < floorY) 
			dungeon.map.floorY[to.x][to.z] = floorY;
	}
	
	
	protected void fixHeights(Step from, Step to) {
		// FIXME: This needs to be improved (centered) somehow
		int diff = dungeon.map.floorY[to.x][to.z] - dungeon.map.floorY[from.x][from.z];
		if(diff > 2) dungeon.map.floorY[to.x][to.z] 
				= (byte) (dungeon.map.floorY[from.x][from.z] - 1);
		if(diff < -2) dungeon.map.floorY[to.x][to.z] 
				= (byte) (dungeon.map.floorY[from.x][from.z] + 1);
		//TODO: Eventually add stairs for short and ladders for tall
	}
	
	
	protected void getEdgeStep(Step src) {
		Step nextEdge;
		int x, z;
		// Below should probably be a method call, but stingy with overhead 
		x = src.x - 1; z = src.z;
		if(x >= x1 && x <= x2) {
			nextEdge = new Step(x, z, src, end, dungeon);
			if((spt[x][z] == null) || (nextEdge.value < spt[x][z].value)) {
				spt[x][z] = nextEdge;
				edges.add(nextEdge);
			}
		}
		x = src.x + 1; z = src.z;
		if(x >= x1 && x <= x2)  {
			nextEdge = new Step(x, z, src, end, dungeon);
			if((spt[x][z] == null) || (nextEdge.value < spt[x][z].value)) {
				spt[x][z] = nextEdge;
				edges.add(nextEdge);
			}
		}
		x = src.x; z = src.z - 1;
		if(z >= z1 && z <= z2) {
			nextEdge = new Step(x, z, src, end, dungeon);
			if((spt[x][z] == null) || (nextEdge.value < spt[x][z].value)) {
				spt[x][z] = nextEdge;
				edges.add(nextEdge);
			}
		}
		x = src.x; z = src.z + 1;
		if(z >= z1 && z <= z2)  {
			nextEdge = new Step(x, z, src, end, dungeon);
			if((spt[x][z] == null) || (nextEdge.value < spt[x][z].value)) {
				spt[x][z] = nextEdge;
				edges.add(nextEdge);
			}
		}
	}
}
