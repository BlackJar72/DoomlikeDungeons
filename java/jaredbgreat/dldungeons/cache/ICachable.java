package jaredbgreat.dldungeons.cache;


public interface ICachable {	
	
	/**
	 * Updates last used time.
	 */
	public void use();
	/**
	 * Is this ready to be removed?
	 * 
	 * @return
	 */
	public boolean isOldData();
	/**
	 * Was this an old item returned from the cache.
	 * 
	 * @return
	 */
    public Coords getCoords();
	/**
	 * Mark whether this is in the cache.
	 */
}
