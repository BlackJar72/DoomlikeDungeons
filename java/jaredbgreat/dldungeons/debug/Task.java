package jaredbgreat.dldungeons.debug;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

public class Task {
	String name;
	long start, stop;
	
	
	/**
	 * Creates a new task for use in the profiler
	 * 
	 * @param name
	 */
	Task(String name) {
		this.name = name;
		start = System.currentTimeMillis();
	}
	
	
	/**
	 * Returns the time taken to complete a task; technically,
	 * no action is needed to complete the task other than waiting
	 * on garbage collection once its scope is out.
	 * 
	 * @return Time taken to complete the task in ms.
	 */
	long complete() {
		stop = System.currentTimeMillis();
		return stop - start;
	}
}
