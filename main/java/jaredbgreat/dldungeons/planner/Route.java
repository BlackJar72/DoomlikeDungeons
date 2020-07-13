package jaredbgreat.dldungeons.planner;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.rooms.Room;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a potential route between two rooms; its purpose 
 * is to find an actual concrete route and add it to the dungeon.  To do 
 * that it will produce a sequence of rooms from each end (start and 
 * finish), alternately adding a room to each in the general direction 
 * of the other until the squences connect at some point.
 * 
 * @author Jared Blackburn
 *
 */
public class Route {
	

	private final Node start, finish;
	private Room current1, current2, temp;
	private float realXDist, realZDist;
	private int   bXDist, bZDist, dir1, dir2;
	private boolean xMatch, zMatch, finishTurn, complete, comp1, comp2;
	private ArrayList<Room> side1, side2;
	
	
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
	
	
	/**
	 * This will attempt to draw the connecting serious of rooms by 
	 * calling drawConnection until either the connection is flagged as 
	 * complete or the dungeon has reach the maximum number of rooms. 
	 * 
	 * Note that the connection may be flagged complete either because 
	 * an actual connection has been found or because both sequences 
	 * (that from the start and that from the
	 * 
	 * @param dungeon
	 */
	protected void drawConnections(Dungeon dungeon) {
		int limit = dungeon.size.maxRooms;
		while(!complete && (limit > 0)) {
			limit--;
			if(dungeon.rooms.size() >= dungeon.size.maxRooms) return;
			drawConnection(dungeon);			
			if(complete || (limit < 0)) return;
		}
	}
	
	
	/**
	 * This will try to add a room to one of the sequences.  Which sequence gets 
	 * this turn is determined by the internal finishTurn variable, which alternates 
	 * with each call.  The location of the new rooms will always be closer on either 
	 * the x or z axis, though which is determined randomly, though weighted to favor 
	 * the axis on which the distance is greatest.
	 * 
	 * The connection is considered complete when either the sequence of rooms from 
	 * either end meet or when each fails to add a room on consecutive turns (a 
	 * give-up condition).
	 * 
	 * @param dungeon
	 */
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
	
	/**
	 * @return ture if the terminal rooms in both sequences overlap on the x axis.
	 */
	private boolean xOverlap() {
		return ((current1.endX > current2.beginX) && (current2.endX > current1.beginX));	
	}
	
	
	/**
	 * @return true if the terminal rooms in both sequences overlap on the z axis. 
	 */
	private boolean zOverlap() {
		return ((current1.endZ > current2.beginZ) && (current2.endZ > current1.beginZ));	
		}
	
	
	/**
	 * @return true if the room edges align on x and the rooms overlap on z
	 */
	private boolean touchesOnX() {
		if(!zOverlap()) return false;
		return ((current1.beginX == current2.endX) || (current1.endX == current2.beginX));
	}
	
	
	/**
	 * @return true if the room edges align on z and overlap on x.
	 */
	private boolean touchesOnZ() {
		if(!xOverlap()) return false;
		return ((current1.beginZ == current2.endZ) || (current1.endZ == current2.beginZ));
	}
	
	
	/**
	 * @return true if the the terminal rooms in each sequence are directly adjacent
	 */
	private boolean touching() {
		return (touchesOnX() || touchesOnZ());
	}
	
	
	/**
	 * This will determine which wall is touching between the terminal rooms on each 
	 * sequence.  Specifically this finds which direction to move from the terminal room in
	 * the sequence to that in the finish sequence.
	 * 
	 * @return the direction from the start sequence to the finish sequence.
	 */
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
	
	
	/**
	 * This determines if the terminal rooms in the each sequence are close, that is
	 * if they are close enough to fill the gap with a single room.  This is used to 
	 * prevent potential connections are not ruined by placing an room whose target 
	 * size is too short to connect but leave too small a gap for another room.
	 * 
	 * @param range
	 * @return
	 */
	private boolean close(int range) {
		if(Math.abs(current1.beginX - current2.endX) > range) return false; 
		if(Math.abs(current2.beginX - current1.endX) > range) return false;
		if(Math.abs(current1.beginZ - current2.endZ) > range) return false;
		if(Math.abs(current2.beginZ - current1.endZ) > range) return false;
		return true;
	}
	
	
	/**
	 * This uses randomness and the coordinate of the terminal rooms in each 
	 * sequence to determine in which direction a new rooms should be added.
	 * 
	 * The direction is picked in such a way that sequences attempt to "grow" 
	 * toward each other while also maintaining a degree of randomness.
	 * 
	 * @param random
	 */
	private void getGrowthDir(Random random) {
		boolean posX = (current1.realX < current2.realX);
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
