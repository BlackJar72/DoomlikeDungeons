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
 * A theme element representing sizes that dungeons of 
 * using that them can occur in.
 * 
 * @author Jared Blackburn
 *
 */
public class SizeElement implements Autoselecting {
	
	private Sizes tiny, small, medium, large, huge;
	private int prob1, prob2, prob3, prob4, prob5;
	private int probScale;
	
	
	public SizeElement (int prob1,
						int prob2,
						int prob3,
						int prob4,
						int prob5) {
		this.tiny 	= Sizes.TINY;
		this.prob1 	= prob1;
		this.small 	= Sizes.SMALL;
		this.prob2 	= prob2;
		this.medium = Sizes.MEDIUM;
		this.prob3 	= prob3;
		this.large  = Sizes.LARGE;
		this.prob4 	= prob4;
		this.huge 	= Sizes.HUGE;
		this.prob5 	= prob5;
		probScale = prob1 + prob2 + prob3 + prob4 + prob5;
	}
	
	
	/**
	 * A Size category for the dungeon.
	 */
	public Sizes select(Random random) {
		int roll = random.nextInt(probScale);
		if(roll < prob1) return Sizes.TINY;
		else roll -= prob1;
		if(roll < prob2) return Sizes.SMALL;
		else roll -= prob2;
		if(roll < prob3) return Sizes.MEDIUM;
		else roll -= prob3;
		if(roll < prob4) return Sizes.LARGE;
		else roll -= prob4;
		if(roll < prob5) return Sizes.HUGE;
		else return Sizes.MEDIUM;
	}
}
