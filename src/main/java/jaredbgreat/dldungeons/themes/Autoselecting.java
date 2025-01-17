package jaredbgreat.dldungeons.themes;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import net.minecraft.util.RandomSource;


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
	public Autoselectable select(RandomSource random);

}
