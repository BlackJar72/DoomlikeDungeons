package jaredbgreat.dldungeons.debug;

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
