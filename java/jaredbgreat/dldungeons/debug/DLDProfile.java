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
	
	
	public void startTask(String name) {
		try {
			String out = depth + ": Starting task " + name + "\n";			
			save.write(out);
			save.flush();
			System.out.print(out);
			tasks.add(new Task(name));
			depth++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public void endTask(String name) {
		try {
			Task task = tasks.remove(tasks.size()-1);
			depth--;
			String out = depth + ": Finishing task " + name + " in " + task.complete() +" ms\n";
			save.write(out);
			save.flush();
			System.out.print(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
}
