package jaredbgreat.dldungeons.debug;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

/**
 * An interface for the profiling system.  This is implemented
 * by DLDProfile to processing profiling information and by
 * DoNothing to ignore it.  
 * 
 * @author Jared Blackburn
 *
 */
public interface IProfiler {
	
	public abstract void startTask(String name);
	public abstract void endTask(String name);
	public abstract void infoOut(String info);

}
