package jaredbgreat.dldungeons.themes;

/*
This modification (mod) is free software: you can redistribute it and/or modify
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
 * The methods in this class should not usually be called directly.
 * 
 * @author jared
 *
 */
public enum ThemeType {	
	DUNGEON		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
	NECRO		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    URBAN		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
	FOREST		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    PLAINS		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    MOUNTAIN	(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    SWAMP		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    WATER		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    DESERT		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    WASTELAND	(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    JUNGLE		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    FROZEN		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    FIERY		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    NETHER		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    END			(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    MUSHROOM	(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    MAGICAL		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    SHADOW		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    PARADISE	(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]),
    TECH   		(new ArrayList[4], new ArrayList<Theme>(), new ArrayList[4]);
	
	
	public final ArrayList<String>[] mobs;
	public final ArrayList<Theme>    themes;
	public final ArrayList<String>[] mobsOut;
	
	private static final EnumSet<ThemeType> all = EnumSet.allOf(ThemeType.class);
	
	
	ThemeType(ArrayList<String>[] mobs, ArrayList<Theme> themes, ArrayList<String>[] mobsOut) {
		this.mobs 	= mobs;
		this.themes = themes;
		this.mobsOut = mobsOut;
		for(int i = 0; i < 4; i++) {
			this.mobs[i]    = new ArrayList<String>();
			this.mobsOut[i] = new ArrayList<String>(); 
		}
	}
	
	
	public static ThemeType type(String name) {
		return ThemeType.valueOf(name.toUpperCase().trim());
	}
	
	
	public static void addThemeToType(Theme theme, String theType) {
		type(theType).themes.add(theme);
	}
	
	
	public static void addThemeToType(Theme theme, ThemeType theType) {
		theType.themes.add(theme);
	}
	
	
	public static void addMobToType(String mob, int level, String theType) {
		if(ConfigHandler.disableAPI || ConfigHandler.noMobChanges) return;		
		ArrayList<String> list = type(theType).mobs[level];
		if(!list.contains(mob))	list.add(mob);
	}
	
	
	public static void removeMobFromType(String mob, int level, String theType) {
		if(ConfigHandler.disableAPI || ConfigHandler.noMobChanges) return;		
		ArrayList<String> list = type(theType).mobs[level];
		if(!list.contains(mob))	list.add(mob);
	}
	
	
	public static void removeMobFromTypeNow(String mob, int level, String theType) {
		if(ConfigHandler.disableAPI || ConfigHandler.noMobChanges) return;
		for(Theme theme : type(theType).themes) {
			if(!mob.isEmpty()) theme.removeMob(mob, level);
		}
	}


	public static void addMobToTypeNow(String mob, int level, String theType) {
		if(ConfigHandler.disableAPI || ConfigHandler.noMobChanges) return;
		for(Theme theme : type(theType).themes) {
			if(!mob.isEmpty()) theme.addMob(mob, level);
		}
	}
	
	
	public static void SyncMobLists() {
		for(ThemeType current : all) {
			if(current.themes.isEmpty()) continue;
			for(Theme theme : current.themes) {
				for(int i = 0; i < 4; i++) {
					if(current.mobs[i].isEmpty()) continue;
					for(String mob : current.mobs[i]) {
						theme.addMob(mob, i);
						}
					}
				theme.fixMobs();
			}
		}
		for(ThemeType current : all) {
			if(current.themes.isEmpty()) continue;
			for(Theme theme : current.themes) {
				for(int i = 0; i < 4; i++) {
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
