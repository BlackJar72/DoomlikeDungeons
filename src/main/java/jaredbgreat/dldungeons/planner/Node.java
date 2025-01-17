package jaredbgreat.dldungeons.planner;


import jaredbgreat.dldungeons.rooms.Room;
import net.minecraft.util.RandomSource;


/**
 * A class representing node rooms (aka, hub-rooms) from and between
 * which other rooms will be added.  These are the rooms which will
 * become entrances or destinations ("boss" / treasure rooms).
 *
 * @author Jared Blackburn
 */
public class Node {
    Room hubRoom;

    public Node(int x, int y, int z, RandomSource random, Dungeon dungeon) {
        // Nodes should be on the larger end of the size scale for rooms...
        int xdim = random.nextInt((dungeon.size.maxRoomSize / 2) - 3)
                + (dungeon.size.maxRoomSize / 2) + 4;
        int zdim = random.nextInt((dungeon.size.maxRoomSize / 2) - 3)
                + (dungeon.size.maxRoomSize / 2) + 4;
        int ymod = (xdim <= zdim) ? (int) Math.sqrt(xdim) : (int) Math.sqrt(zdim);

        int height = random.nextInt((dungeon.verticle.value / 2) + ymod + 1) + 2;
        height = Math.min(12, Math.max(7, Math.max(height * 2, height + dungeon.random.nextInt(3) + 2)));


        // Then plant a seed and try to grow the room
        hubRoom = new RoomSeed(x, y, z).growRoom(xdim, zdim, height, dungeon, null, null);
    }
}
