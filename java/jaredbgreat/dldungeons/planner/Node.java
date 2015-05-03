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


public class Node {
	int x, y, z;
	Room hubRoom;
	Node connect1, connect2;
	
	
	public Node(int x, int y, int z, Random random, Dungeon dungeon) {
		// First, determine a maximum size for the room
		this.x = x;
		this.y = y;
		this.z = z;
		// Nodes should be on the larger end of the size scale for rooms...
		int xdim = random.nextInt((dungeon.size.maxRoomSize / 2) - 3) 
				+ (dungeon.size.maxRoomSize / 2) + 4;
		int zdim = random.nextInt((dungeon.size.maxRoomSize / 2) - 3) 
				+ (dungeon.size.maxRoomSize / 2) + 4;
		int ymod = (xdim <= zdim) ? (int) Math.sqrt(xdim) : (int) Math.sqrt(zdim);
		int height = random.nextInt((dungeon.verticle.value / 2) + ymod + 1) + 2;
		// Then plant a seed and try to grow the room
		//hubRoom = new Room(x - (xdim / 2), x + (xdim / 2), z - (zdim / 2), z + (zdim / 2), y, y + height, dungeon);
		//System.out.println("Creating PlacingSeed and attempting to grow a room");
		hubRoom = new PlaceSeed(x, y, z).growRoom(xdim, zdim, height, dungeon, null, null);
		dungeon.nodeRooms.add(hubRoom);
		//System.out.println("Room #" + hubRoom.id + " has been placed.");
	}

}
