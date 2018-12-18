package jaredbgreat.dldungeons.cache;

// Something about this clearly isn't working as expected; adding 
// extra data seems to prevent somethings from being retrieved 
// and/or added -- which makes no sense, so I have no idea what 
// I've done wrong.

/**
 * A cache system using a skiplist based hash map.
 * 
 * @author Jared Blackburn
 * @param <T>
 */
public class Cache <T extends ICachable> {
    private ICachable[] data;
    private final int minSize;
    private int capacity;
    private int lowLimit;
    private int length;
    
    
    /**
     * Creates a cache with a default starting size elements.
     */
    public Cache(int size) {
    	System.out.println("");
    	System.out.println("*******************");
    	System.out.println("**CREATING CACHE**");
        data = new ICachable[size];
        for(ICachable fuck : data) {
        	System.out.println(fuck);
        }
        minSize = size;
        capacity = (size * 3) / 4;
        lowLimit = ((size - minSize) * 3) / 16;
        length = 0;
    	System.out.println("*******************");
    	System.out.println("");
    }
    
    
    /**
     * Creates a cache with a default starting size of 16 elements.
     */
    public Cache() {
    	System.out.println("");
    	System.out.println("******************");
    	System.out.println("**CREATING CACHE**");
        data = new ICachable[16];
        for(ICachable fuck : data) {
        	System.out.println(fuck);
        }
        minSize = 16;
        capacity = 12;
        lowLimit = 0;
        length = 0;
    	System.out.println("******************");
    	System.out.println("");
    }
    
    
    /**
     * At a new element to the cache.
     * 
     * @param item the object to be added.
     */
    public void add(T item) {
    	//System.out.println("**CALLING add on item  " + item + "**");
        int bucket = (item.getCoords().hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
    	//System.out.println("**TRYING to at item  " + item.getCoords() + "; length = " + data.length + ", offset = " + offset + "**");
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
        	//System.out.println("**USING bucket  " + ((bucket + offset) % data.length) + "; (" + data[slot] +")");
            if(data[slot] == null) {
                data[slot] = item;
                data[slot].use();
            	//System.out.println("**ADDING item  " + item + " to cache**");
                if(++length > capacity) {
                    grow();
                }
                return;
            } else if(data[slot].getCoords().equals(item.getCoords())) {
            	//System.out.println("**Item  " + item + " was already in cache**");
                data[slot].use();
                return;
            } else {
                offset++;
            }
        }
    	System.out.println("**CACHE WAS FULL -- WTF!!!**");
    }
    
    
    /**
     * Return the element at the given Coords.
     * @param coords
     * @return the object stored for those coordinates, or null.
     */
    public T get(Coords coords) {
        int bucket = (coords.hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null) {
                return null;
            } else if(data[slot].getCoords().equals(coords)) {
                data[slot].use();
                return (T)data[slot];
            } else {
                offset++;
            }
        }        
        return null;
    }
    
    
    /**
     * Return the element at the given coordinate values x and z.
     * 
     * @param x
     * @param z
     * @return the object stored for those coordinates, or null.
     */
    public T get(int x, int z) {
        int bucket = (Coords.hashCoords(x, z) & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null) {
                return null;
            } else if(data[slot].getCoords().equals(x, z)) {
                data[slot].use();
                return (T)data[slot];
            } else {
                offset++;
            }
        }        
        return null;
    }
    
    
    /**
     * Will tell if an item for the given coordinates is in the cache.
     * 
     * @param coords
     * @return 
     */
    public boolean contains(Coords coords) {
        int bucket = (coords.hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null) {
                return false;
            } else if(data[slot].getCoords().equals(coords)) {
                return true;
            } else {
                offset++;
            }
        }        
        return false;
    }
    
    
    /**
     * Will tell if an item for the given coordinates is in the cache.
     * 
     * @param x
     * @param z
     * @return 
     */
    public boolean contains(int x, int z) {
        int bucket = (Coords.hashCoords(x, z) & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null) {
                return false;
            } else if(data[slot].getCoords().equals(x, z)) {
                return true;
            } else {
                offset++;
            }
        }        
        return false;
    }
    
    
    /**
     * Will tell if an item the same coords is in the cache.
     * 
     * @param in
     * @return 
     */
    public boolean contains(T in) {
        Coords coords = in.getCoords();
        int bucket = (coords.hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset <= data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null) {
                return false;
            } else if(data[slot].getCoords().equals(coords)) {
                return true;
            } else {
                offset++;
            }
        }        
        return false;
    }
    
    
    /**
     * This will grow the data size when needed.
     */
    private void grow() {
        ICachable[] old = data;
        ICachable[] data = new ICachable[(old.length * 3) / 2];
        for(int i = 0; i < old.length; i++) {
            if(old[i] != null) {
                rebucket(old[i]);
            }
        }
        capacity = (data.length * 3) / 4;
        lowLimit = Math.max(minSize, ((data.length - minSize) * 3) / 16);
    }
    
    
    /**
     * This will shrink the data size when needed.
     */
    private void shrink() {
        ICachable[] old = data;
        ICachable[] data = new ICachable[old.length / 2];
        for(int i = 0; i < old.length; i++) {
            if(old[i] != null) {
                rebucket(old[i]);
            }
        }
        capacity = (data.length * 3) / 4;
        lowLimit = ((data.length - minSize) * 3) / 16;
    }
    
    
    private void rebucket(ICachable item) {
        int bucket = (item.getCoords().hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset <= data.length) {
            int slot = (bucket + offset) % data.length;
            if((data[slot] == null) || (data[slot].getCoords().equals(item.getCoords()))) {
                data[slot] = item;
                return;
            }else {
                offset++;
            }
        }
    }
    
    
    /**
     * This will iterate the cache items and remove any that identify 
 themselves as isOldData().  Usually this should mean removing items from 
 the cache that haven't been used in a set amount of time (most often 
 30 seconds), though other criteria for isOldData() could be created.
     */
    public void cleanup() {
    	int startSize = data.length;
        for(int i = 0; i < data.length; i++) {
            if((data[i] != null) && (data[i].isOldData())) {
            	//System.out.println("**Removing item from cache**");
                data[i] = null;
                length--;
            }
        }
        if(length < lowLimit) {
            shrink();
        } else if(data.length != startSize) {
        	ICachable[] old = data;
        	data = new ICachable[data.length];
        	for(int i = 0; i < length; i++) {
        		if(old[i] != null) {
        			rebucket(old[i]);
        		}
        	}
        }
    } 
}
