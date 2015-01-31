package jaredbgreat.dldungeons.planner;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.rooms.Room;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Route {
	

	Node start, finish;
	Room current1, current2, temp;
	float realXDist, realZDist;
	int   bXDist, bZDist, dir1, dir2;
	boolean xMatch, zMatch, finishTurn, complete, comp1, comp2;
	ArrayList<Room> side1, side2;
	
	
	protected Route(Node start, Node finish) {
		this.start  = start;
		this.finish = finish;
		current1 = start.hubRoom;
		current2 = finish.hubRoom;
		side1 = new ArrayList<Room>();
		side1.add(current1);
		side2 = new ArrayList<Room>();
		side2.add(current2);
		finishTurn = false;
		complete = false;
		
		if(realXDist > 3.0f) {
			bXDist = current2.endX - current1.beginX;
			xMatch = false;
		} else if(realXDist < -3.0f) {
			bXDist = current2.beginX - current1.endX;
			xMatch = false;
		} else {
			bXDist = (int)realXDist;
			xMatch = true;
		}
		
		if(realZDist > 3.0f) {
			bZDist = current2.endZ - current1.beginZ;
			zMatch = false;
		} else if(realZDist < -3.0f) {
			bZDist = current2.beginZ - current1.endZ;
			zMatch = false;
		} else {
			bZDist = (int)realZDist;
			zMatch = true;
		}		
	}
	
	
	protected void drawConnections(Dungeon dungeon) throws Throwable {
		int limit = dungeon.size.maxRooms;
		while(!complete && (limit > 0)) {
			limit--;
			if(dungeon.rooms.size() >= dungeon.size.maxRooms) break;
			drawConnection(dungeon);			
			if(complete || (limit < 0)) break;
		}
		start = finish = null;
		current1 = current2 = temp = null;
		side1.clear();
		side2.clear();
		side1 = side2 = null;
		super.finalize();
	}
	
	
	public void drawConnection(Dungeon dungeon) {
		//System.out.println("Running drawConnections(Dungeon dungeon)");
		getGrowthDir(dungeon.random);
		int height = dungeon.baseHeight;
		int x = dungeon.random.nextInt(dungeon.size.width);
		int z = dungeon.random.nextInt(dungeon.size.width);
		int xdim = dungeon.random.nextInt(dungeon.size.maxRoomSize - 5) + 6;
		int zdim = dungeon.random.nextInt(dungeon.size.maxRoomSize - 5) + 6;
		if(close(dungeon.size.maxRoomSize - 1)) xdim = zdim = dungeon.size.maxRoomSize;
		int ymod = (xdim <= zdim) ? (int) Math.sqrt(xdim) : (int) Math.sqrt(zdim);
		int roomHeight = dungeon.random.nextInt((dungeon.verticle.value / 2) + ymod + 1) + 2;
		if(finishTurn) {
			dir1 = (dir1 + 2) % 4;
			dir2 = (dir2 + 2) % 4;
			temp = current2.connector(dungeon, dir1, xdim, zdim, roomHeight, this);
			if((temp == null) || side2.contains(temp)) temp = current2.connector(dungeon, dir2, xdim, zdim, roomHeight, this);
			if((temp == null) || side2.contains(temp)) {
				comp2 = true; // for now, give up
			} else if(side1.contains(temp)) {
				complete = true; // Success!
			} else {
				side2.add(temp);
				current2 = temp;
			}
		} else {
			temp = current1.connector(dungeon, dir1, xdim, zdim, roomHeight, this);
			if(temp == null|| side1.contains(temp)) temp = current1.connector(dungeon, dir2, xdim, zdim, roomHeight, this);
			if(temp == null|| side1.contains(temp)) {
				comp1 = true; // for now, give up
			} else if(side2.contains(temp)) {
				complete = true; // Success!
			} else {
				side1.add(temp);
				current1 = temp;
			}
		}
		if(!complete) complete = comp1 && comp2;
		if(comp1) finishTurn = true;
		else if(comp2) finishTurn = false;
		else finishTurn = !finishTurn;
	}
	
	
	
	
	
	// Helper methods
	
	
	private boolean xOverlap() {
		return ((current1.endX > current2.beginX) && (current2.endX > current1.beginX));	
	}
	
	
	private boolean zOverlap() {
		return ((current1.endZ > current2.beginZ) && (current2.endZ > current1.beginZ));	
		}
	
	
	private boolean touchesOnX() {
		if(!zOverlap()) return false;
		return ((current1.beginX == current2.endX) || (current1.endX == current2.beginX));
	}
	
	
	private boolean touchesOnZ() {
		if(!xOverlap()) return false;
		return ((current1.beginZ == current2.endZ) || (current1.endZ == current2.beginZ));
	}
	
	
	private boolean touching() {
		return (touchesOnX() || touchesOnZ());
	}
	
	
	private int touchDir() {
		if(zOverlap()) {
			if(current1.endX == current2.beginX) return 0;
			if(current1.beginX == current2.endX) return 2;
		} else if(xOverlap()) {
			if(current1.endZ == current2.beginZ) return 1;
			if(current1.beginZ == current2.endZ) return 3;
		}
		return -1; // Not touching
	}
	
	
	private boolean close(int range) {
		if(Math.abs(current1.beginX - current2.endX) > range) return false; 
		if(Math.abs(current2.beginX - current1.endX) > range) return false;
		if(Math.abs(current1.beginZ - current2.endZ) > range) return false;
		if(Math.abs(current2.beginZ - current1.endZ) > range) return false;
		return true;
	}
	
	
	private void getGrowthDir(Random random) {
		boolean posX = (current1.realZ < current2.realZ);
		boolean posZ = (current1.realZ < current2.realZ);
		if(xOverlap()) {
			if(posZ) {
				dir1 = 1;
				dir2 = random.nextInt(2) * 2;
			}
			else {
				dir1 = 3;
				dir2 = random.nextInt(2) * 2;
			}
		} else if (zOverlap()) {
			if(posX) {
				dir1 = 0;
				dir2 = 1 + random.nextInt(2) * 2;
			}
			else {
				dir1 = 2;
				dir2 = 1 + random.nextInt(2) * 2;
			}
		} else if (random.nextInt((int)(Math.abs(realXDist) + Math.abs(realZDist) + 1)) 
				> (int)(Math.abs(realXDist))) {
			if(posX) {
				dir1 = 0;
				if(posZ) dir2 = 1;
				else dir2 = 3;
			}
			else {
				dir1 = 2;
				if(posZ) dir2 = 1;
				else dir2 = 3;
			}
		} else {
			if(posZ) {
				dir1 = 1;
				if(posX) dir2 = 0;
				else dir2 = 2;
			}
			else {
				dir1 = 3;
				if(posX) dir2 = 0;
				else dir2 = 2;			
			}
		}
	}
	
	
}
