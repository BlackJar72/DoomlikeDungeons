package jaredbgreat.dldungeons.themes;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.ConfigHandler;

import java.util.ArrayList;
import java.util.EnumSet;


/*This mod is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * This is an enumeration of theme types to be used with the API, primarily 
 * for adding mobs to themes which may have been created by another third 
 * party.
 * 
 * The methods in this class should not usually be called directly; they 
 * are inteded to be called through the mods API.
 * 
 * @author Jared Blackburn
 *
 */
public enum ThemeType {	
	DUNGEON		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
	NECRO		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    URBAN		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
	FOREST		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    PLAINS		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    MOUNTAIN	(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    SWAMP		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    WATER		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    DESERT		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    WASTELAND	(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    JUNGLE		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    FROZEN		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    FIERY		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    NETHER		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    END			(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    MUSHROOM	(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    MAGICAL		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    SHADOW		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    PARADISE	(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]),
    TECH   		(new ArrayList[5], new ArrayList<Theme>(), new ArrayList[5]);
	
	
	public final ArrayList<String>[] mobs;
	public final ArrayList<Theme>    themes;
	public final ArrayList<String>[] mobsOut;
		
	private static final EnumSet<ThemeType> all = EnumSet.allOf(ThemeType.class);
	
	
	ThemeType(ArrayList<String>[] mobs, ArrayList<Theme> themes, ArrayList<String>[] mobsOut) {
		this.mobs 	= mobs;
		this.themes = themes;
		this.mobsOut = mobsOut;
		for(int i = 0; i < 5; i++) {
			this.mobs[i]    = new ArrayList<String>();
			this.mobsOut[i] = new ArrayList<String>(); 
		}
	}
	
	
	/**
	 * Get a type from its name.  This is mostly a wrapper for valueOf, 
	 * but takes care of some string clean-up.
	 * 
	 * @param name
	 * @return
	 */
	public static ThemeType type(String name) {
		return ThemeType.valueOf(name.toUpperCase().trim());
	}
	
	
	/**
	 * This will register a theme as being of the type named with the 
	 * String.
	 * 
	 * @param theme
	 * @param theType
	 */
	public static void addThemeToType(Theme theme, String theType) {
		type(theType).themes.add(theme);
	}
	
	
	/**
	 * This will register a theme as belonging to a give type.
	 * 
	 * @param theme
	 * @param theType
	 */
	public static void addThemeToType(Theme theme, ThemeType theType) {
		theType.themes.add(theme);
	}
	
	
	/**
	 * This will register a mob to be added to themes of the given type and 
	 * as the given difficulty level.
	 * 
	 * @param mob
	 * @param level
	 * @param theType
	 */
	public static void addMobToType(String mob, int level, String theType) {
		if(ConfigHandler.disableAPI || ConfigHandler.noMobChanges) return;		
		ArrayList<String> list = type(theType).mobs[level];
		if(!list.contains(mob)) {
			list.add(mob);
		}
	}
	
	
	/**
	 * This will register a mob to be removed themes of the given type at the given 
	 * difficulty level.
	 * 
	 * @param mob
	 * @param level
	 * @param theType
	 */
	public static void removeMobFromType(String mob, int level, String theType) {
		if(ConfigHandler.disableAPI || ConfigHandler.noMobChanges) return;		
		ArrayList<String> list = type(theType).mobs[level];
		if(!list.contains(mob))	list.add(mob);
	}
	
	
	/**
	 * This will immediately remove the move from mob-lists of the given difficulty 
	 * level for all themes of the given type.
	 * 
	 * @param mob
	 * @param level
	 * @param theType
	 */
	public static void removeMobFromTypeNow(String mob, int level, String theType) {
		if(ConfigHandler.disableAPI || ConfigHandler.noMobChanges) return;
		for(Theme theme : type(theType).themes) {
			if(!mob.isEmpty()) theme.removeMob(mob, level);
		}
	}

	
	/**
	 * This will immediately add the named mob to all themes of the given type at 
	 * the given level. 
	 * 
	 * @param mob
	 * @param level
	 * @param theType
	 */
	public static void addMobToTypeNow(String mob, int level, String theType) {
		if(ConfigHandler.disableAPI || ConfigHandler.noMobChanges) return;
		for(Theme theme : type(theType).themes) {
			if(!mob.isEmpty()) theme.addMob(mob, level);
		}
	}
	
	
	/**
	 * This will apply all type-based mob additions and subtraction listed for all 
	 * themes of all types.
	 */
	public static void SyncMobLists() {
		for(ThemeType current : all) {
			//System.out.println("[DLD] Trying theme type " + current);
			if(current.themes.isEmpty()) continue;
			//System.out.println("[DLD] Adding to theme type " + current);
			for(Theme theme : current.themes) {
				for(int i = 0; i < 5; i++) {
					//System.out.println("[DLD] Starting on theme " + theme);
					if(current.mobs[i].isEmpty()) continue;
					for(String mob : current.mobs[i]) {
							//System.out.println("[DLD] Adding " + mob + "to theme " + theme);
							theme.addMob(mob, i);							
						}
					}
				theme.fixMobs();
			}
		}
		for(ThemeType current : all) {
			if(current.themes.isEmpty()) continue;
			for(Theme theme : current.themes) {
				for(int i = 0; i < 5; i++) {
					if(current.mobsOut[i].isEmpty()) continue;
					for(String mob : current.mobsOut[i]) {
						theme.removeMob(mob, i);
						}
					}
				theme.fixMobs();
			}
		}
	}
}
