package jaredbgreat.dldungeons.planner.astar;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.pieces.Doorway;

import java.util.PriorityQueue;

/**
 * A priority queue for storing doors which should sort
 * them in order of fitness as connections between rooms.
 * 
 * @author Jared Blackburn
 *
 */
public class DoorQueue extends PriorityQueue<Doorway> {
	private int roomid;
	
	
	public DoorQueue(int id) {
		super();
		roomid = id;
	}
	
	
	/**
	 * True if the other id passed is the same as that of the room
	 * that owns these doors.
	 * 
	 * @param id 
	 * @return id == roomid
	 */
	public boolean isRoom(int id) {
		return id == roomid;
	}	
}
