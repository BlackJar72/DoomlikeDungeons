package jaredbgreat.dldungeons.planner.features;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		


import jaredbgreat.dldungeons.themes.Degree;

/**
 * A chance to add an area of lower floor height (i.e., a pit
 * or depression).
 * 
 * @author Jared Blackburn
 *
 */
public class Depression extends IslandPlatform {
	
	public Depression(Degree chance){
		super(chance);
	}
}
