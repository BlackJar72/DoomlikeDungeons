package jaredbgreat.dldungeons.cache;

import net.minecraft.server.MinecraftServer;

public abstract class AbstractCachable implements ICachable { 
    private final Coords coords;
    private long timestamp;
    
    
    public AbstractCachable(int x, int z) {
        coords = new Coords(x, z);
        timestamp = MinecraftServer.getCurrentTimeMillis();
    }
    
    
    public AbstractCachable(Coords coords) {
        this.coords = coords;
        timestamp = MinecraftServer.getCurrentTimeMillis();
    }
    
    
    @Override
    public void use() {
    	timestamp = MinecraftServer.getCurrentTimeMillis();		
    }
    
    
    @Override
    public boolean isOldData() {
    	long t = MinecraftServer.getCurrentTimeMillis() - timestamp;
    	return ((t > 30000) || (t < 0));	
    }
    
    
    @Override
    public Coords getCoords() {
        return coords;
    }
    
    
    public String toString() {
    	return super.toString() + " at " + coords.toString();
    }
    
}
