package jaredbgreat.dldungeons.planner.astar;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

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
