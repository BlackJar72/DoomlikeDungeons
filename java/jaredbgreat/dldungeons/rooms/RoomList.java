package jaredbgreat.dldungeons.rooms;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RoomList implements List<Room>, Iterator<Room> {

	private int size, index;
	private Room[] rooms;
	
	
	public RoomList(int slots) {
		size = 0;
		rooms = new Room[slots + 1];
		rooms[0] = Room.roomNull;
	}
	
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return (size == 0);
	}


	@Override
	public boolean contains(Object o) {
		for(int i = 1; i < rooms.length; i++)
			if(rooms[size] == o) return true;
		return false;
	}


	@Override
	public Iterator<Room> iterator() {
		return this;
	}


	@Override
	public Object[] toArray() {
		return rooms;
	}


	@Override
	public <T> T[] toArray(T[] a) {
		return (T[])rooms;
	}


	@Override
	public boolean add(Room e) {
		if(size < rooms.length) {
			size++;
			rooms[size] = e;
			return true;
		}			
		else return false;
	}


	@Override
	public boolean remove(Object o) {
		return false; // Rooms cannot be reomved
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		boolean shit = true;
		if(c.getClass() != this.getClass()) return false;
		RoomList fuck = (RoomList) c;
		for(Room next : fuck) {
			if(!contains(next)) shit = false;
		}
		return shit;
	}


	@Override
	public boolean addAll(Collection<? extends Room> c) {
		if(size + c.size() > rooms.length) return false;
		for(Room next : c) {
			add(next);
		}
		return true;
	}


	@Override
	public boolean addAll(int index, Collection<? extends Room> c) {
		return addAll(c);
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void clear() {
		size = 0;
		rooms = new Room[rooms.length];
		rooms[0] = Room.roomNull;
	}


	@Override
	public Room get(int index) {
		return rooms[index];
	}


	@Override
	public Room set(int index, Room element) {
		if(index < rooms.length) {
			rooms[index] = element;
		}
		return rooms[index];
	}


	@Override
	public void add(int index, Room element) {
		rooms[index] = element;
	}


	@Override
	public Room remove(int index) {
		return null; // Rooms cannot be removed
	}


	@Override
	public int indexOf(Object o) {
		for(int i = 1; i < rooms.length; i++)
			if(rooms[i] == o) return i;
		return 0;
	}


	@Override
	public int lastIndexOf(Object o) {
		return indexOf(o);
	}


	@Override
	public ListIterator<Room> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ListIterator<Room> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Room> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean hasNext() {
		if((index >= size) || (rooms[index] == null)) return false;
		else return true;
	}


	@Override
	public Room next() {
		if(hasNext()) {
			index++;
			return rooms[index];
		}
		else return null;
	}


	@Override
	public void remove() {} // *NEVER* do this!!!
	
}