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
 * Classes with this interface can automatically select an
 * Autoselectable enum.
 * 
 * @author jared
 *
 */
public interface Autoselecting {
	
	/**
	 * Returns an enum constant from an enum that implements 
	 * Autoselectable.
	 * 
	 * @param random
	 * @return
	 */
	public Autoselectable select(Random random);

}
