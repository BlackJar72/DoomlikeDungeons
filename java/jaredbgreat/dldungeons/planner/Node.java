package jaredbgreat.dldungeons.planner;


/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
