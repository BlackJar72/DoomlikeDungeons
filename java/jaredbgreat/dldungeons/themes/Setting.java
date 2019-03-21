package jaredbgreat.dldungeons.themes;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

/**
 * An enumeration of general theme types, analogous to the biome types 
 * used by forge.  This is used with the API to add monsters to themes, 
 * including those that may have been created by the player or a third
 * party. 
 * 
 * @author Jared Blackburn
 *
 */
public class Setting {
	
    public enum Type
    {
        FOREST,
        PLAINS,
        MOUNTAIN,
        SWAMP,
        WATER,
        DESERT,
        FROZEN,
        JUNGLE,
        WASTELAND,
        NETHER,
        END,
        MUSHROOM,
        MAGICAL,
        DUNGEON,
        URBAN,
        TOMB,
        INFERNO,
        SPIRIT,
        SHADOW,
        CELESTIAL;
    }

}
