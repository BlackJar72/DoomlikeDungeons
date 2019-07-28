package jaredbgreat.dldungeons.themes;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

/**
 * An enumeration of special flags that can effect dungeon generation 
 * in special ways that can't be represented in terms of style elements.
 * 
 * The current version should never be considered complete or in a final 
 * form, as new flags, if needed, would be added by expanding the enum.
 * 
 * @author Jared Blackburn
 *
 */
public enum ThemeFlags {
	WATER,
	SWAMPY,
	SURFACE,		// This one still does nothing
	HARD,
	EASY; 
}
