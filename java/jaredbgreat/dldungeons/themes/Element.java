package jaredbgreat.dldungeons.themes;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
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
public class Element implements Autoselecting {
	
	private Degree none, few, some, plenty, heaps, all;
	private int prob1, prob2, prob3, prob4, prob5, prob6;
	private int probScale;
	
	
	public Element (int prob1,
					int prob2,
					int prob3,
					int prob4,
					int prob5,
					int prob6) {
		this.none 	= Degree.NONE;
		this.prob1 	= prob1;
		this.few 	= Degree.FEW;
		this.prob2 	= prob2;
		this.some 	= Degree.SOME;
		this.prob3 	= prob3;
		this.plenty = Degree.PLENTY;
		this.prob4 	= prob4;
		this.heaps 	= Degree.HEAPS;
		this.prob5 	= prob5;
		this.all 	= Degree.ALL;
		this.prob6 	= prob6;
		probScale = prob1 + prob2 + prob3 + prob4 + prob5 + prob6;
	}
	
	
	/**
	 * Returns a Degree for the given element which will 
	 * be applied to the dungeon.
	 */
	public Degree select(Random random) {
		int roll = random.nextInt(probScale);
		if(roll < prob1) return Degree.NONE;
		else roll -= prob1;
		if(roll < prob2) return Degree.FEW;
		else roll -= prob2;
		if(roll < prob3) return Degree.SOME;
		else roll -= prob3;
		if(roll < prob4) return Degree.PLENTY;
		else roll -= prob4;
		if(roll < prob5) return Degree.HEAPS;
		else roll -= prob5;
		if(roll < prob6) return Degree.ALL;
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
		return    ((prob1 !=0) 
				&& (prob2 ==0) 
				&& (prob3 ==0) 
				&& (prob4 ==0) 
				&& (prob5 ==0) 
				&& (prob6 ==0));     
	}
	

}
