package jaredbgreat.dldungeons.themes;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import java.util.Random;

public enum Degree implements Autoselectable {
	
	
	NONE	( 0),
	FEW 	( 1),
	SOME	( 3),
	PLENTY	( 5),
	HEAPS	( 9),
	ALL		(10);
	
	
	public final int value;
	public static final int scale = 10;
	
	
	Degree(int val) {
		value = val;
	}
	
	
	public boolean use(Random random) {
		return(random.nextInt(scale) < value);
	}	
}
