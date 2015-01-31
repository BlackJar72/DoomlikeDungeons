package jaredbgreat.dldungeons.debug;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

public class Task {
	String name;
	long start, stop;
	
	Task(String name) {
		this.name = name;
		start = System.currentTimeMillis();
	}
	
		
	long complete() {
		stop = System.currentTimeMillis();
		return stop - start;
	}
}
