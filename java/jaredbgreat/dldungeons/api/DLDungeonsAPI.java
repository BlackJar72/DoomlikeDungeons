package jaredbgreat.dldungeons.api;

import jaredbgreat.dldungeons.ReadAPI;
import jaredbgreat.dldungeons.themes.Theme;
import jaredbgreat.dldungeons.themes.ThemeType;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;


/* 
 * This API is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/

public class DLDungeonsAPI { 
	
	// This has been updated to deal with the change to all lower case for the modid.
	private static final boolean dldloaded = isLoaded();
	
	
	
	/**
	 * This allows mod authors to check if Doomlike Dungeons is running in order to potentially
	 * register methods or classes the might use it, among other things.
	 * 
	 * The results should always be the same as...
	 *  
	 * <BLOCKQUOTE><CODE>cpw.mods.fml.common.Loader.isModLoaded("DLDungeonsJBG");</CODE></BLOCKQUOTE>
	 * 
	 * ...which should usually be used instead for reason that should be obvious.
	 * 
	 * @return boolean true if Doomlike Dungeons has been loaded by FML, false if not.
	 */
	public static boolean isLoaded() {
		return cpw.mods.fml.common.Loader.isModLoaded("DLDungeonsJBG") 
				  || cpw.mods.fml.common.Loader.isModLoaded("dldungeonsjbg");
	}
	
	
	/**
	 * This will spawn a dungeon at the given chunk coordinates.  The dungeon will not use the world seed; 
	 * this is the same as using the /dldspawn command.
	 * 
	 * @param world
	 * @param chunkX
	 * @param chunkZ
	 */
	public static void spawnDungeon(World world, int chunkX, int chunkZ) {
		if(!isLoaded()) return;
		ReadAPI.spawnDungeon(world, chunkX, chunkZ);
	}
	
	
	/** This will spawn a dungeon at the given chunk coordinates.  It will use the seed given, 
	 * which should typically be derived in some way from the world seed.
	 * 
	 * @param world
	 * @param chunkX
	 * @param chunkZ
	 * @param seed
	 */
	public static void spawnDungeon(World world, int chunkX, int chunkZ, long seed) {
		if(!isLoaded()) return;
		ReadAPI.spawnDungeon(world, chunkX, chunkZ, seed);
	}
	
	
	/** This will spawn a dungeon at the given chunk coordinates.   It will use the seed given, 
	 * which should typically be derived in some way from the world seed, but should NEVER be
	 * the actual world seed itself.  A simple way to do this is to use nextLong() on an instance
	 * of Random that is itself based on the world seed.
	 * 
	 * @param world
	 * @param chunkX
	 * @param chunkZ
	 * @param random
	 */
	public static void spawnDungeon(World world, int chunkX, int chunkZ, Random random) {
		if(!isLoaded()) return;
		ReadAPI.spawnDungeon(world, chunkX, chunkZ, random);
	}
	
	
	/**
	 * This will add a dimension to the list the blacklist / whitelist.
	 * 
	 * @param dim, the id of the dimension being added.
	 */
	public static void addDimension(byte dim) {
		if(!isLoaded()) return;
		ReadAPI.addDimension(dim);
	}
		
	
	/**
	 * This will remove a dimension to the list the blacklist / whitelist.
	 * 
	 * @param dim, the id of the dimension being removed.
	 */
	public static void subDimension(byte dim) {
		if(!isLoaded()) return;
		ReadAPI.subDimension(dim);
	}
	
	
	/**
	 * This will replace the dimension list (whitelist or blacklist with
	 * a new list.  It will take a list as a string in the format:
	 * 
	 * id1,id2...idx
	 * 
	 * @param dims
	 */
	public static void setDimensions(int[] dims) {
		if(!isLoaded()) return;
		ReadAPI.setDimensions(dims);
	}
	
	
	/**
	 * This will set the difficulty of dungeons being generated to 
	 * the value passed.
	 * 
	 * @param difficulty
	 */
	public static void setDifficulty(byte diff) {
		if(!isLoaded()) return;
		ReadAPI.setDifficulty(diff);
	}
	
	
	/**
	 * This will load a theme at run time.  It takes the path of the theme
	 * file as a parameter.
	 * 
	 * @param file
	 */
	public static void loadTheme(String file) {
		if(!isLoaded()) return;
		ReadAPI.loadTheme(file);
	}
	
	
	/**
	 * DEPRICATED: Use blacklistDimensions() instead.
	 * (This name was the result of a typo and should not be used.)
	 * 
	 * This will set the dimension list to act as a blacklist.  This should be
	 * used with caution, since players may have set it to act as a whitelist
	 * for reason dealing with mods other than the one calling the method.
	 */
	@Deprecated
	public static void blacklistThemes() {
		if(!isLoaded()) return;
		ReadAPI.blacklistDimensions();
	}
	
	
	/**
	 * This will set the dimension list to act as a blacklist.  This should be
	 * used with caution, since players may have set it to act as a whitelist
	 * for reason dealing with mods other than the one calling the method.
	 */
	public static void blacklistDimensions() {
		if(!isLoaded()) return;
		ReadAPI.blacklistDimensions();
	}
	
	
	/**
	 * This will set the dimension list to act as a whitelist.  This should be
	 * used with caution, since players may have set it to act as a blacklist
	 * for reason dealing with mods other than the one calling the method.
	 */
	public static void whitelistDimensions() {
		if(!isLoaded()) return;
		ReadAPI.whitelistDimensions();
	}
	
	
	/**
	 * This will reload the config file, effectively erasing any changes made during runtime.
	 * Alternately, if the config has been edited it will bring up the new settings.
	 */
	public static void reloadConfig() {
		if(!isLoaded()) return;
		ReadAPI.whitelistDimensions();
	}
	
	
	/**
	 * This will add a mob to all themes within a certain type.  This needs to be
	 * called during pre-init or init; all the themes will be updated with the added 
	 * mobs during post-init.  Is also best to add mobs to as many theme types as they 
	 * fit in, rather than just a few.
	 * 
	 * @param name, level, types (as many as needed) 
	 * 
	 * Level is the difficulty of the mob and must be between 0 and 3:
	 * 		0: Common Mobs (e.g., zombie, skeleton, spider)
	 * 		1: Tough  Mobs (e.g., creeper, blaze, enderman, cave spider)
	 * 		2: Brute  Mobs (e.g., witch, tougher Lycanite's Mobs, most Dungeons Mobs)
	 * 		3: Elite  Mobs (e.g., bosses, mini-bosses, MineFantasy dragon and knights)
	 * 
	 * Valid theme types are similar to biome types from the Forge biome dictionary, and 
	 * have many names in common.  You can list as many as needed.
	 * 
	 * Biome types that duplicate as dungeon theme types include:
	 * FOREST, PLAINS, MOUNTAIN (includes hills), SWAMP (includes sewers, flooded mines, 
	 * and all damp places), WATER	(submerged, water-filed), DESERT, WASTELAND, JUNGLE, FROZEN, 
	 * NETHER, END, MUSHROOM, and MAGICAL.
	 * 
	 * Other types specific for dungeons are:
	 * DUNGEON	(general dungeon-ish mobs, such as troll or ogres, should go here)
	 * NECRO	(crypts, tombs, and other undead-heavy places)
     * URBAN	(Ruined city mobs, such as humanoids, undead, and other city / ruin dwellers),
     * FIERY    (Creatures for hot / fiery places, such as volcanos, other than the nether, go here)
     * SHADOW	(dark / shadowy creatures),
     * PARADISE	(Celestial / Heavenly type creature)
     * TECH     (high-tech / sci-fi themes; or mechanical / alien / futuristic mobs);
	 */
	public static void addMob(String name, int level, String... types) {
		if(!isLoaded()) return;
		for(String type : types) {
			ThemeType.addMobToType(name, level, type);
		}
	}
	
	
	/**
	 * This will remove a mob to all themes within a certain type, possible useful
	 * if a modded mob is meant to replace a vanilla one.  This version requires
	 * the level of the mob to work and will only effect the mobs appearance for that level.
	 * 
	 * For more details see addMod(String name, int level, String... types)
	 */
	public static void removeMob(String name, int level, String... types) {
		if(!isLoaded()) return;
		for(String type : types) {
			ThemeType.removeMobFromType(name, level, type);
		}
	}
	
	
	/**
	 * This will remove a mob to all themes within a certain type, possible useful
	 * if a modded mob is meant to replace a vanilla one.  This version will remove 
	 * any use of the mob in dungeon of the specified type, regardless of level.
	 * 
	 * For more details see addMod(String name, int level, String... types)
	 */
	public static void removeMob(String name, String... types) {
		if(!isLoaded()) return;
		for(String type : types) {
			for(int i = 0; i < 5; i++)
				ThemeType.removeMobFromType(name, i, type);
		}
	}
}
