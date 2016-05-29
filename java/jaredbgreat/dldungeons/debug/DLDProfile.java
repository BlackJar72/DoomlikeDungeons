package jaredbgreat.dldungeons.debug;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * A class for profiling dungeon generation, or more generally infrequent 
 * discrete events that use a non-trivial amount of time.  Such events
 * are not measured well by the vanilla profiler as it is designed to show 
 * the way CPU time in divided up between generally continuous activities per 
 * frame rather than measuring long but infrequent events.
 * 
 * @author Jared Blackburn
 *
 */
public class DLDProfile implements IProfiler {
	int depth;
	BufferedWriter save;
	ArrayList<Task> tasks;
	
	
	public DLDProfile() {
		tasks = new ArrayList<Task>();
		try {
			save = new BufferedWriter(new FileWriter("DLDProfile.log"));			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void finalize() {
		try {
			save.close();
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Adds a new task to the queue (represented as a list) and outputs
	 * a log of the task. 
	 */
	public void startTask(String name) {
		try {
			String out = depth + ": Starting task " + name + "\n";			
			save.write(out);
			save.flush();
			System.out.print(out);
			tasks.add(new Task(name));
			depth++;
		} catch (IOException e) {
			e.printStackTrace();			
		}		
	}
	
	
	/**
	 * Pops the last task from the queue (represented as a list) and prints a log
	 * of the event along with the time taken for the task (along with all sub-tasks)
	 * to be completed.
	 */
	public void endTask(String name) {
		try {
			Task task = tasks.remove(tasks.size()-1);
			depth--;
			String out = depth + ": Finishing task " + name + " in " + task.complete() +" ms\n";
			save.write(out);
			save.flush();
			System.out.print(out);
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	
	/**
	 * Output some information by printing it to the profilers log.
	 */
	public void infoOut(String info) {
		try {
			save.write(info);
			save.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
