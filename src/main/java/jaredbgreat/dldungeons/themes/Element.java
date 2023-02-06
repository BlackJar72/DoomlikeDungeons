package jaredbgreat.dldungeons.themes;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import java.util.Random;

/**
 * This a componenet of a theme representing a table of 
 * probabilities for a certain feature to be used.
 * 
 * Typically, this is used for architectural style variation,
 * such as levels of symmetry or complexity.
 * 
 * @author Jared Blackurn
 *
 */
public final class Element implements Autoselecting {
	
	private static final Degree NONE, FEW, SOME, PLENTY, HEAPS, ALWAYS;
	private final int prob1, prob2, prob3, prob4, prob5, prob6;
	private final int probScale;
	private final boolean never;
	
	static {
		NONE 	= Degree.NONE;
		FEW 	= Degree.FEW;
		SOME 	= Degree.SOME;
		PLENTY =  Degree.PLENTY;
		HEAPS 	= Degree.HEAPS;
		ALWAYS 	= Degree.ALWAYS;
		
	}
	
	public Element (int prob1,
					int prob2,
					int prob3,
					int prob4,
					int prob5,
					int prob6) {
		this.prob1 	= prob1;
		this.prob2 	= this.prob1 + prob2;
		this.prob3 	= this.prob2 + prob3;
		this.prob4 	= this.prob3 + prob4;
		this.prob5 	= this.prob4 + prob5;
		this.prob6 	= this.prob5 + prob6;
		probScale = this.prob6 + 1;
		never = this.prob6 == this.prob1;
	}
	
	
	/**
	 * Returns a Degree for the given element which will 
	 * be applied to the dungeon.
	 */
	public Degree select(Random random) {
		int roll = random.nextInt(probScale);
		if(roll < prob1) return Degree.NONE;
		else if(roll < prob2) return Degree.FEW;
		else if(roll < prob3) return Degree.SOME;
		else if(roll < prob4) return Degree.PLENTY;
		else if(roll < prob5) return Degree.HEAPS;
		else if(roll < prob6) return Degree.ALWAYS;
		else return Degree.NONE;
	}
	
	
	/**
	 * True if the theme can only return a degree of NONE,
	 * indicating the feature should never occur in dungeons of 
	 * this type (for example, and entrance from the surface to 
	 * a dungeons that is found on the surface).
	 * 
	 * @return
	 */
	public boolean never() {
		return never;     
	}
	

}
