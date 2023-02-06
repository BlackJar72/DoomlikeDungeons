package jaredbgreat.dldungeons.themes;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import java.util.Random;

/**
 * This represents a discrete probability of a feature or 
 * characteristic to appear.  This can be interpret as 
 * a degree of frequency or probability, with human-friendly 
 * constant names attached.
 * 
 * @author Jared Blackurn
 *
 */
public enum Degree implements Autoselectable {
	
	
	NONE	( 0),
	FEW 	( 1),
	SOME	( 3),
	PLENTY	( 5),
	HEAPS	( 9),
	ALWAYS		(10);
	
	
	public final int value;
	public static final int scale = 10;
	
	
	Degree(int val) {
		value = val;
	}

	
	/**
	 * This will return true or false based on the probability 
	 * this Degree represents.
	 * 
	 * @param random
	 * @return
	 */
	public boolean use(Random random) {
		return(random.nextInt(scale) < value);
	}	
}
