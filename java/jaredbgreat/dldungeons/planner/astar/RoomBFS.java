package jaredbgreat.dldungeons.planner.astar;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.planner.Level;
import jaredbgreat.dldungeons.rooms.Room;
import java.util.ArrayDeque;
import java.util.ArrayList;


public class RoomBFS {
	private ArrayDeque<Room> roomQueue = new ArrayDeque<Room>();
	private Level dungeon;
	private ArrayList<Room> nodes;
	private boolean[] checked;
	

	public RoomBFS(Level dungeon) {
		this.dungeon = dungeon;
		nodes = new ArrayList<Room>(dungeon.numNodes);
		checked = new boolean[dungeon.roomCount + 1];
		for(int i = dungeon.numNodes + 1; i > 0; i--) {
			Room room = dungeon.rooms.get(i);
			if(room.isNode) nodes.add(room);
		}
	}
	
	
	public ArrayList<ArrayList<Room>> check() {
		ArrayList<ArrayList<Room>> sections = new ArrayList<ArrayList<Room>>();
		while(!nodes.isEmpty()) {
			sections.add(search(nodes.get(0)));
		}
//		System.out.println("[DLDUNGEONS] Dungeon has " + sections.size()
//				+ " sections.");
//		if(sections.size() > 1) System.out.println("[DLDUNGEONS] Dungeon "
//				+ "is not connected!");
		return sections;
	}
	
	
	public ArrayList<Room> search(Room room) {
		Room next;
		roomQueue.add(room);
		ArrayList<Room> found = new ArrayList<Room>();
		while(!nodes.isEmpty() && !roomQueue.isEmpty()) {
			next = roomQueue.poll();
			checked[next.id] = true;
			if(next.isNode) {
				nodes.remove(next);
				found.add(next);
			}
			for(DoorQueue exit : next.connections) {
				if(!checked[exit.peek().otherside])
					roomQueue.add(dungeon.rooms.get(exit.peek().otherside));
			}
		}
		return found;
	}
}
