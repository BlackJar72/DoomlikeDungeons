package jaredbgreat.dldungeons.planner.astar;

import jaredbgreat.dldungeons.pieces.Doorway;
import java.util.PriorityQueue;

public class DoorQueue extends PriorityQueue<Doorway> {
	private int roomid;
	
	
	public DoorQueue(int id) {
		super();
		roomid = id;
	}
	
	
	public boolean isRoom(int id) {
		return id == roomid;
	}	
}
