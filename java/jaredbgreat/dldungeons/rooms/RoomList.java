package jaredbgreat.dldungeons.rooms;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RoomList extends ArrayList<Room> {

	private int numRooms, index;
	private Room[] rooms;
	
	
	public RoomList(int slots) {
		super(slots);
		add(0, Room.roomNull);
		numRooms = 0;
	}
	
	
	public int realSize() {
		return numRooms;
	}


	public boolean isReallyEmpty() {
		return (numRooms == 0);
	}

	@Override
    public ListIterator listIterator() {
        return super.listIterator(1);
    }
	
	
	@Override
    public Iterator iterator() {
        return super.listIterator(1);
    }
	
	
	@Override
	public boolean add(Room room) {
		boolean out = super.add(room);
		if(out) {
			numRooms++;
		}
		return out;
	}



	@Override
	public Room remove(int index) {
		System.err.println("[DLDUNGEONS] WARNING! Trying to remove room from list "
	                     + "(Rooms cannot be removed)!");
		return null; // Rooms cannot be removed
	}
	
}