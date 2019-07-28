package jaredbgreat.dldungeons;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import java.util.Random;

/**
 * An enumeration of difficulty setting for the mod.
 * 
 * This includes all the variable that effect dungeon 
 * generation on the basis of difficulty.  Basically 
 * that means how many mobs spawners to add and how 
 * hard the mobs will be.
 * 
 * Note that difficulty is partially depending on themes, 
 * as without some harder modded mobs there is not enough 
 * difficulty range in the mobs to fully realize the harder
 * difficulties.
 * 
 * @author Jared Blackburn
 *
 */
public enum Difficulty {
	
	NONE  (0, 0, 0, 0, 0,  -1, false, "No spawners."),
	BABY  (3, 0, 0, 0, 0, 884, false, "Baby mode."),
	NOOB  (4, 1, 1, 1, 1, 590, false, "Not too hard."),
	NORM  (5, 2, 1, 1, 2, 427, false, "Normal difficulty."),
	HARD  (6, 3, 2, 2, 3, 323, false, "Super violent."),
	NUTS  (7, 5, 2, 2, 4, 100,  true, "Insane horror!");
	
	
	public final int spawners;
	public final int promote;
	public final int maxlev;
	public final int nodelev;
	public final int bosslev;
	public final int blocksPerSpawner;
	public final boolean entrancemobs;
	public final String label;
	
	
	private Difficulty(int spawners, int promote, int mobmax, int nodelev,
			int bosslev, int blocksPerSpawner, boolean entrancemobs, 
			String label) {
		this.spawners = spawners;
		this.promote = promote;
		this.maxlev = mobmax;
		this.nodelev = nodelev;
		this.bosslev = bosslev;
		this.blocksPerSpawner = blocksPerSpawner;
		this.entrancemobs = entrancemobs;
		this.label = label;
	}
	
	
	/**
	 * Should a rooms have (a) spawner(s).
	 * 
	 * @param random
	 * @return
	 */
	public boolean addmob(Random random) {
		return(random.nextInt(10) < spawners);
	}
	
	
	/**
	 * Should a room with spawners have more than 
	 * one?
	 * 
	 * @param random
	 * @return
	 */
	public boolean multimob(Random random) {
		return(random.nextInt(20) < (spawners + promote));
	}
	
	
	/**
	 * Should a the difficulty of a mob be increased to a 
	 * higher level?
	 * 
	 * @param random
	 * @return
	 */
	private boolean promote(Random random) {
		return(random.nextInt(10) < promote);
	}
	
	
	/**
	 * What level mob should be placed in the spawner.
	 * 
	 * @param random
	 * @return
	 */
	public int moblevel(Random random) {
		int lev = 0;
		boolean pr = true;
		while(pr && (lev < maxlev)) {
			if(random.nextInt(10) < promote) {
				lev++;
			} else pr = false;
		}
		return lev;
	}
	
	
	/**
	 * What monster level to place the central extra spawner 
	 * below the treasure chest in destination (aka, "boss") 
	 * rooms.
	 * 
	 * @param random
	 * @return
	 */
	public int nodelevel(Random random) {
		int lev = nodelev;
		boolean pr = true;
		while(pr && (lev < bosslev)) {
			if(random.nextInt(10) < promote) {
				lev++;
			} else pr = false;
		}
		return lev;
	}	
}


