package jaredbgreat.dldungeons.util.cache;


/**
 * A cache system using weak reference to automatically have its contents 
 * cleaned during garbage collection while remaining available until that time.
 * 
 * This is untested, so I have no idea how well (or if) this really works.
 * 
 * @author Jared Blackburn
 * @param <T>
 */
public class WeakCache <T extends IHaveCoords> {
    private volatile CacheReference<T>[] data;
    private final int minSize;
    private int capacity;
    private int lowLimit;
    private int length;
    private boolean altered;
    
    
    /**
     * Creates a cache with a default starting size elements.
     */
    @SuppressWarnings("unchecked")
	public WeakCache(int size) {
        data = new CacheReference[size];
        minSize = size;
        capacity = (size * 3) / 4;
        lowLimit = ((size - minSize) * 3) / 16;
        length = 0;
        altered = false;
    }
    
    
    /**
     * Creates a cache with a default starting size of 16 elements.
     */
    @SuppressWarnings("unchecked")
	public WeakCache() {
        data = new CacheReference[16];
        minSize = 16;
        capacity = 12;
        lowLimit = 0;
        length = 0;
        altered = false;
    }
    
    
    /**
     * At a new element to the cache.
     * 
     * @param item the object to be added.
     */
    public void add(T item) {
        int bucket = (item.getCoords().hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null || data[slot].get() == null) {
                data[slot] = new CacheReference<T>(item, this);
                if(++length > capacity) {
                    grow();
                }
                return;
            } else if(data[slot].get().equals(item)) {
                return;
            }else {
                offset++;
            }
        }
    }
    
    
    /**
     * Return the element at the given Coords.
     * @param coords
     * @return the object stored for those coordinates, or null.
     */
    public T get(Coords coords) {
    	if(altered) {
    		rebucketAll();
    	}
        int bucket = (coords.hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null || data[slot].get() == null) {
                return null;
            } else if(data[slot].get().getCoords().equals(coords)) {
                return (T)data[slot].get();
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
    public T get(int x, int z, int d) {
    	if(altered) {
    		rebucketAll();
    	}
        int bucket = (Coords.hashCoords(x, z, d) & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null || data[slot].get() == null) {
                return null;
            } else if(data[slot].get().getCoords().equals(x, z)) {
                return (T)data[slot].get();
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
    	if(altered) {
    		rebucketAll();
    	}
        int bucket = (coords.hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset < data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null || data[slot].get() == null) {
                return false;
            } else if(data[slot].get().getCoords().equals(coords)) {
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
    	if(altered) {
    		rebucketAll();
    	}
        Coords coords = in.getCoords();
        int bucket = (coords.hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset <= data.length) {
            int slot = (bucket + offset) % data.length;
            if(data[slot] == null || data[slot].get() == null) {
                return false;
            } else if(data[slot].get().getCoords().equals(coords)) {
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
    @SuppressWarnings("unchecked")
	private void grow() {
    	CacheReference<T>[] old = data;
        data = new CacheReference[(old.length * 3) / 2];
        for(int i = 0; i < old.length; i++) {
            if(old[i] != null && old[i].get() != null) {
                rebucket(old[i].get());
            }
        }
        capacity = (data.length * 3) / 4;
        lowLimit = ((data.length - minSize) * 3) / 16;
        altered  = false;
    }
    
    
    /**
     * This will shrink the data size when needed.
     */
    @SuppressWarnings("unchecked")
	private void shrink() {
    	CacheReference<T>[] old = data;
        data = new CacheReference[Math.max(old.length / 2, minSize)];
        for(int i = 0; i < old.length; i++) {
            if(old[i] != null && old[i].get() != null) {
                rebucket(old[i].get());
            }
        }
        capacity = (data.length * 3) / 4;
        lowLimit = ((data.length - minSize) * 3) / 16;
        altered  = false;
    }
    
    
    @SuppressWarnings({ "unchecked" })
	private void rebucketAll() {
        if(length < lowLimit) {
            shrink();
        } else {
	    	CacheReference<T>[] old = data;
	        data = new CacheReference[old.length];
	        for(int i = 0; i < old.length; i++) {
	            if(old[i] != null && old[i].get() != null) {
	                rebucket(old[i].get());
	            }
	        }
        }
        altered  = false;
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void rebucket(T item) {
        int bucket = (item.getCoords().hashCode() & 0x7fffffff) % data.length;
        int offset = 0;
        while(offset <= data.length) {
            int slot = (bucket + offset) % data.length;
            if((data[slot] == null) || 
            		(data[slot].get() != null && (data[slot].get().equals(item)))) {
                data[slot] = new CacheReference(item, this);
                return;
            }else {
                offset++;
            }
        }
    }
    
    
    /**
     * This will decrement the size by one and shrink the backing array if needed. 
     */
    public void reduce() {
    	length--;
    	altered = true;
    } 
}
