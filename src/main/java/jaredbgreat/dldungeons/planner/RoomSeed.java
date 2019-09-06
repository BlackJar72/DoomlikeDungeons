package jaredbgreat.dldungeons.planner;


import jaredbgreat.dldungeons.pieces.Shape;
import jaredbgreat.dldungeons.rooms.AbstractRoom;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.rooms.RoomType;


/**
 * These "seeds" represent a potential Room.
 * 
 * Target room sizes are passed into the constructor, after which the seed will
 * attempt to grow (spread) to the fill that area.  If there is not enough space on one 
 * dimension growth will stop.  After a final size has been reach it is then checked 
 * to be sure it meets minimum the room size, that is at least five blocks on each 
 * dimension (x and z).
 */
public class RoomSeed {
	
	private int w = 0;
	private int lowEdge, highEdge;
	
	private boolean canAddX = true, canAddZ = true, canSubX = true, canSubZ = true;
	int x, y, z, height, grownX = 1, grownZ = 1, endX, beginX, endZ, beginZ;
	
	
	public RoomSeed(int startX, int startY, int startZ) {
		// These may change once the the seeds has grown
		x = endX = beginX = startX;
		y = startY;
		z = endZ = beginZ = startZ;
	}
	
	
	/**
	 * Attempts to "grow" a new room from the RoomSeed.  Two avoid a bias
	 * for creating long narrow rooms only along one dimension, the 
	 * dimension to expand first is randomly determined and one of two 
	 * methods are called to do the expansion. 
	 * 
	 * @param xdim
	 * @param zdim
	 * @param height
	 * @param dungeon
	 * @param parent
	 * @param previous
	 * @return A new room, or null if a room is not created
	 */
	public Room growRoom(int xdim, int zdim, int height, 
						 Dungeon dungeon, Room parent, Room previous) {
		if(dungeon.rooms.size() >= dungeon.size.maxRooms) return null;
		if(dungeon.random.nextBoolean()) 
			return growRoomX(xdim, zdim, height, dungeon, parent, previous);
		else return growRoomZ(xdim, zdim, height, dungeon, parent, previous);
	}
	
	
	/**
	 * This will attempt to grow a new room by expanding first along the x-axis, 
	 * then the z-axis.
	 * 
	 * The x-axis will first try to expand in each direction, until the both ends 
	 * are blocked by existing rooms or the edge of the dungeon, or the target size 
	 * is reached.  Afterward the seed will try to expand along the y-axis by adding 
	 * entire rows to each side, until it is either blocked on each side or the 
	 * target size is reached.
	 * 
	 * If either dimension is less the five blocks in length null will be returned,
	 * otherwise AbstractRoom.makeRoom is called to create a Room to return.
	 * 
	 * The code for this and growRoomZ with some variables reversed, since this was 
	 * the only simple solution to present itself when this class was designed and
	 * written.
	 * 
	 * @param xdim
	 * @param zdim
	 * @param height
	 * @param dungeon
	 * @param parent
	 * @param previous
	 * @return A new room if successfully create, or null if not
	 */
	public Room growRoomX(int xdim, int zdim, int height, 
						 Dungeon dungeon, Room parent, Room previous) {
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
			while((canAddX || canSubX) && grownX < xdim) {
				if((endX+1) >= dungeon.size.width) canAddX = false;
				if((beginX-1) < 0) canSubX = false;
				if(canAddX) {
					if(dungeon.map.room[endX+w][z] == container) {
						grownX += 1;
						endX += 1;
					} else canAddX = false;
				} if(canSubX) {
					if(dungeon.map.room[beginX-w][z] == container) {
						grownX += 1;
						beginX -= 1;
					} else canSubX = false;
				}
			}
			while((canAddZ || canSubZ) && grownZ < zdim) {
				lowEdge  = beginX + 1 - w;
				highEdge = endX  - 1 + w;
				for(int i = lowEdge; i <= highEdge; i++) {
					if((endZ+1) >= dungeon.size.width) canAddZ = false;
					else {
						if(dungeon.map.room[i][endZ+w] != container) canAddZ = false;
					}
					if((beginZ-1) < 0) canSubZ = false;
					else {
						if(dungeon.map.room[i][beginZ-w] != container) canSubZ = false;								
					}
				}
				if(canAddZ) {
					grownZ += 1;
					endZ += 1;
				}
				if(canSubZ) {
					grownZ += 1;
					beginZ -= 1;
				}
			}
		if((grownX < 5) || (grownZ < 5)) {
			return null; // Not big enough for a room!
		}
		else {
			dungeon.roomCount++;
			return AbstractRoom.makeRoom(beginX, endX, beginZ, endZ, y, y + height, dungeon, parent, previous);	
		}
	}
	
	
	/**
	 * This will attempt to grow a new room by expanding first along the z-axis, 
	 * then the x-axis.
	 * 
	 * The z-axis will first try to expand in each direction, until the both ends 
	 * are blocked by existing rooms or the edge of the dungeon, or the target size 
	 * is reached.  Afterward the seed will try to expand along the x-axis by adding 
	 * entire rows to each side, until it is either blocked on each side or the 
	 * target size is reached.
	 * 
	 * If either dimension is less the five blocks in length null will be returned,
	 * otherwise AbstractRoom.makeRoom is called to create a Room to return.
	 * 
	 * The code for this and growRoomX with some variables reversed, since this was 
	 * the only simple solution to present itself when this class was designed and
	 * written.
	 * 
	 * @param xdim
	 * @param zdim
	 * @param height
	 * @param dungeon
	 * @param parent
	 * @param previous
	 * @return A new room if successfully create, or null if not
	 */
	public Room growRoomZ(int xdim, int zdim, int height, 
						 Dungeon dungeon, Room parent, Room previous) {
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
			while((canAddZ || canSubZ) && grownZ < zdim) {
				if((endZ+1) >= dungeon.size.width) canAddZ = false;
				if((beginZ-1) < 0) canSubZ = false;
				if(canAddZ) {
					if(dungeon.map.room[x][endZ+w] == container) {
						grownZ += 1;
						endZ += 1;
					} else canAddZ = false;
				} if(canSubZ) {
					if(dungeon.map.room[x][beginZ-w] == container) {
						grownZ += 1;
						beginZ -= 1;
					} else canSubZ = false;
				}
			}
			while((canAddX || canSubX) && grownX < xdim) {
				lowEdge  = beginZ + 1 - w;
				highEdge = endZ  - 1 + w;
				for(int i = lowEdge; i <= highEdge; i++) {
					if((endX+1) >= dungeon.size.width) canAddX = false;
					else {
						if(dungeon.map.room[endX+w][i] != container) canAddX = false;
					}
					if((beginX-1) < 0) canSubX = false;
					else {
						if(dungeon.map.room[beginX-w][i] != container) canSubX = false;								
					}
				}
				if(canAddX) {
					grownX += 1;
					endX += 1;
				}
				if(canSubX) {
					grownX += 1;
					beginX -= 1;
				}
			}
		if((grownX < 5) || (grownZ < 5)) {
			return null; // Not big enough for a room!
		}
		else {
			dungeon.roomCount++;
			return AbstractRoom.makeRoom(beginX, endX, beginZ, endZ, y, y + height, dungeon, parent, previous);		
		}
	}
}
