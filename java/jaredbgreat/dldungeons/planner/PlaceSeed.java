package jaredbgreat.dldungeons.planner;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.pieces.Shape;
import jaredbgreat.dldungeons.rooms.AbstractRoom;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.rooms.RoomType;

/* 
 * These seeds represent a potential Room, Shape, or decoraction.
 * For size is predetermined and it is only needed to check that it fits.
 * For Rooms and Shapes a terget size will first be determined, after which the seed will
 * attempt to grow (spread) to the fill that area.  If there is not enough space on one 
 * dimension growth will stop.  After a final size has been reach it will then be checked 
 * to be sure it meets minimum room size of the minimum size to fully represent the shape
 * that is intended (e.g., and 'E' shape must be at least 5 blocks across to have all 3 
 * protrusions and both spaces between them.
 */


public class PlaceSeed {
	
	
	private int w = 0;
	private int lowEdge, highEdge;
	
	
	private boolean canAddX = true, canAddZ = true, canSubX = true, canSubZ = true;
	int x, y, z, height, grownX = 1, grownZ = 1, endX, beginX, endZ, beginZ;
	
	
	public PlaceSeed(int startX, int startY, int startZ) {
		// These may change once the the seeds has grown
		x = endX = beginX = startX;
		y = startY;
		z = endZ = beginZ = startZ;
	}
	
	
	public Room growRoom(int xdim, int zdim, int height, 
						 Dungeon dungeon, Room parent, Room previous) {
		if(dungeon.rooms.realSize() >= dungeon.size.maxRooms) return null;
		if(dungeon.random.nextBoolean()) 
			return growRoomX(xdim, zdim, height, dungeon, parent, previous);
		else return growRoomZ(xdim, zdim, height, dungeon, parent, previous);
	}
	
	
	public Room growRoomX(int xdim, int zdim, int height, 
						 Dungeon dungeon, Room parent, Room previous) {
		//System.out.println("Running growRoomX.");
		int container;
		// Parent should always be null unless growing an island sub-room.
		if(parent == null) {
			container = 0;
			w = 0;
		}
		else {
			container = parent.id;
			w = 1;
		}
		if((x >= dungeon.size.width) || (x < 0) || (z >= dungeon.size.width) || (z < 0)) return null;
		if(dungeon.map.room[x][z] != container) return null;
		//System.out.println("Growing room from PlaceSeed");
			while((canAddX || canSubX) && grownX < xdim) {
				if((endX+1) >= dungeon.size.width) canAddX = false;
				if((beginX-1) < 0) canSubX = false;
				if(canAddX) {
					//System.out.println("Found room #" + dungeon.map.room[endX+1][z] + " at " + (endX+1) + "," + z);
					if(dungeon.map.room[endX+w][z] == container) {
						grownX += 1;
						endX += 1;
						//System.out.println("Growing room to " + (endX) + "," + z);
					} else canAddX = false;
				} if(canSubX) {
					//System.out.println("Found room #" + dungeon.map.room[beginX-1][z] + " at " + (beginX-1) + "," + z);
					if(dungeon.map.room[beginX-w][z] == container) {
						grownX += 1;
						beginX -= 1;
						//System.out.println("Growing room to " + (beginX) + "," + z);
					} else canSubX = false;
				}
			}
			while((canAddZ || canSubZ) && grownZ < zdim) {
				lowEdge  = beginX + 1 - w;
				highEdge = endX  - 1 + w;
				for(int i = lowEdge; i <= highEdge; i++) {
					if((endZ+1) >= dungeon.size.width) canAddZ = false;
					else {
						//System.out.println("Found room #" + dungeon.map.room[i][endZ+1] + " at " + i + "," + (endZ+1));
						if(dungeon.map.room[i][endZ+w] != container) canAddZ = false;
					}
					if((beginZ-1) < 0) canSubZ = false;
					else {
						//System.out.println("Found room #" + dungeon.map.room[i][beginZ-1] + " at " + i + "," + (beginZ-1));
						if(dungeon.map.room[i][beginZ-w] != container) canSubZ = false;								
					}
				}
				if(canAddZ) {
					//System.out.println("Growing room to " + x + "," + (endZ+1));
					grownZ += 1;
					endZ += 1;
				}
				if(canSubZ) {
					//System.out.println("Growing room to " + x + "," + (beginZ-1));
					grownZ += 1;
					beginZ -= 1;
				}
			}
		if((grownX < 5) || (grownZ < 5)) {
			//System.out.println("Seed could not grow sufficiently; aborting room generation, returning null.");
			return null; // Not big enough for a room!
		}
		else {
			dungeon.roomCount++;
			//System.out.println("Room grown.");	
			return AbstractRoom.makeRoom(beginX, endX, beginZ, endZ, y, y + height, dungeon, parent, previous);	
		}
	}
	
	
	public Room growRoomZ(int xdim, int zdim, int height, 
						 Dungeon dungeon, Room parent, Room previous) {
		//System.out.println("Running growRoomZ.");
		int container;
		// Parent should always be null unless growing an island sub-room.
		if(parent == null) {
			container = 0;
			w = 0;
		}
		else {
			container = parent.id;
			w = 1;
		}
		if((x >= dungeon.size.width) || (x < 0) || (z >= dungeon.size.width) || (z < 0)) return null;
		if(dungeon.map.room[x][z] != container) return null;
		//System.out.println("Growing room from PlaceSeed");
			while((canAddZ || canSubZ) && grownZ < zdim) {
				if((endZ+1) >= dungeon.size.width) canAddZ = false;
				if((beginZ-1) < 0) canSubZ = false;
				if(canAddZ) {
					//System.out.println("Found room #" + dungeon.map.room[x][endZ+1] + " at " + x + "," + (endZ+1));
					if(dungeon.map.room[x][endZ+w] == container) {
						grownZ += 1;
						endZ += 1;
						//System.out.println("Growing room to " + x + "," + (endZ));
					} else canAddZ = false;
				} if(canSubZ) {
					//System.out.println("Found room #" + dungeon.map.room[x][beginZ-1] + " at " + x + "," + (beginZ-1));
					if(dungeon.map.room[x][beginZ-w] == container) {
						grownZ += 1;
						beginZ -= 1;
						//System.out.println("Growing room to " + x + "," + (beginZ));
					} else canSubZ = false;
				}
			}
			while((canAddX || canSubX) && grownX < xdim) {
				lowEdge  = beginZ + 1 - w;
				highEdge = endZ  - 1 + w;
				for(int i = lowEdge; i <= highEdge; i++) {
					if((endX+1) >= dungeon.size.width) canAddX = false;
					else {
						//System.out.println("Found room #" + dungeon.map.room[endX+1][i] + " at " + (endX+1) + "," + i);
						if(dungeon.map.room[endX+w][i] != container) canAddX = false;
					}
					if((beginX-1) < 0) canSubX = false;
					else {
						//System.out.println("Found room #" + dungeon.map.room[beginX-1][i] + " at " + (beginX-1) + "," + i);
						if(dungeon.map.room[beginX-w][i] != container) canSubX = false;								
					}
				}
				if(canAddX) {
					//System.out.println("Growing room to " + (endX+1) + "," + z);
					grownX += 1;
					endX += 1;
				}
				if(canSubX) {
					//System.out.println("Growing room to " + (beginX-1) + "," + z);
					grownX += 1;
					beginX -= 1;
				}
			}
		if((grownX < 5) || (grownZ < 5)) {
			//System.out.println("Seed could not grow sufficiently; aborting room generation, returning null.");
			return null; // Not big enough for a room!
		}
		else {
			dungeon.roomCount++;
			//System.out.println("Room grown.");	
			return AbstractRoom.makeRoom(beginX, endX, beginZ, endZ, y, y + height, dungeon, parent, previous);		
		}
	}
	
	
	
	
	
	//TODO:  Create classes for other content

}
