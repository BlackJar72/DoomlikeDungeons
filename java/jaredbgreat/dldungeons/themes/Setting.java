package jaredbgreat.dldungeons.themes;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
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
