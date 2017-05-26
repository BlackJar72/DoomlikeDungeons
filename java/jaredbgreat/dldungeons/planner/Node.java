package jaredbgreat.dldungeons.planner;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.rooms.Room;

import java.util.Random;


/**
 * A class representing node rooms (aka, hub-rooms) from and between 
 * which other rooms will be added.  These are the rooms which will
 * become entrances or destinations ("boss" / treasure rooms). 
 * 
 * @author Jared Blackburn
 *
 */
public class Node {
	Room hubRoom;
	
	
	public Node(int x, int y, int z, Random random, Level dungeon) {	
		// Nodes should be on the larger end of the size scale for rooms...
		int xdim = random.nextInt((dungeon.size.maxRoomSize / 2) - 3) 
				+ (dungeon.size.maxRoomSize / 2) + 4;
		int zdim = random.nextInt((dungeon.size.maxRoomSize / 2) - 3) 
				+ (dungeon.size.maxRoomSize / 2) + 4;
		int ymod = (xdim <= zdim) ? (int) Math.sqrt(xdim) : (int) Math.sqrt(zdim);
		int height = random.nextInt((dungeon.verticle.value / 2) + ymod + 1) + 2;
		
		// Then plant a seed and try to grow the room
		hubRoom = new RoomSeed(x, y, z).growRoom(xdim, zdim, height, dungeon, null, null);
//		if(hubRoom != null) {
//			dungeon.nodeRooms.add(hubRoom);
//		}
	}
}
