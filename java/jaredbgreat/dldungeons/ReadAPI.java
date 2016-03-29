package jaredbgreat.dldungeons;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license:  
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import static jaredbgreat.dldungeons.builder.Builder.placeDungeon;
import jaredbgreat.dldungeons.themes.ThemeReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import net.minecraft.world.World;

public class ReadAPI {	
	
	/*
	 * This will spawn a dungeon at the given chunk coordinates.  The dungeon will not use the world seed; 
	 * this is the same as using the /dldspawn command.
	 */
	public static void spawnDungeon(World world, int chunkX, int chunkZ) {
		if(ConfigHandler.disableAPI) return;
		try {
			placeDungeon(new Random(), chunkX, chunkZ, world);
		} catch (Throwable e) {
			System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
			e.printStackTrace();
		}
	}
	
	
	/* This will spawn a dungeon at the given chunk coordinates.  It will use the seed given, 
	 * which should typically be derived in some way from the world seed, but should NEVER be
	 * the actual world seed itself.  A simple way to do this is to use nextLong() on an instance
	 * of Random that is itself based on the world seed.
	 */
	public static void spawnDungeon(World world, int chunkX, int chunkZ, long seed) {
		if(ConfigHandler.disableAPI) return;
		try {
			placeDungeon(new Random(seed), chunkX, chunkZ, world);
		} catch (Throwable e) {
			System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
			e.printStackTrace();
		}
	}
	
	
	/* This will spawn a dungeon at the given chunk coordinates, using a random number
	 * generated that is passed to it.  Usually this is better than passing a seed unless the
	 * seed is derived in a specialized way for some reason.
	 */
	public static void spawnDungeon(World world, int chunkX, int chunkZ, Random random) {
		if(ConfigHandler.disableAPI) return;
		try {
			placeDungeon(random, chunkX, chunkZ, world);
		} catch (Throwable e) {
			System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
			e.printStackTrace();
		}
	}
	
	
	/*
	 * This will add a dimension to the list the blacklist / whitelist.
	 */
	public static void addDimension(byte dim) {
		if(ConfigHandler.disableAPI) return;
			GenerationHandler.addDimension(dim);
	}
		
	
	/*
	 * This will remove a dimension to the list the blacklist / whitelist.
	 * 
	 */
	public static void subDimension(int dim) {
		if(ConfigHandler.disableAPI) return;
		GenerationHandler.subDimension(dim);
	}
	
	
	/*
	 * This will replace the dimension list (whitelist or blacklist with
	 * a new list.  It will take a list as a string in the format:
	 */
	public static void setDimensions(int[] dims) {
		if(ConfigHandler.disableAPI) return;
		GenerationHandler.setDimensions(dims);
	}
	
	
	/*
	 * This will set the difficulty of dungeons being generated to 
	 * the value passed.
	 */
	public static void setDifficulty(int diff) {
		if(ConfigHandler.disableAPI) return;
		ConfigHandler.parseDiff(diff);
	}
	
	
	/*
	 * This will load a theme at run time.  It takes the path of the theme
	 * file as a parameter.
	 */
	public static void loadTheme(String file) {
		if(ConfigHandler.disableAPI) return;
		File theme = new File(file);
		BufferedReader instream = null;
		try {
			instream = new BufferedReader(new 
					FileReader(ConfigHandler.configDir.toString() + File.separator + file.toString()));
			System.out.println("[DLDUNGEONS] Loading theme file " + file.toString());
			ThemeReader.parseTheme(instream, file.toString());
			if(instream != null) instream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	/*
	 * This will set the dimension list to act as a blacklist.  This should be
	 * used with caution, since players may have set it to act as a whitelist
	 * for reason dealing with mods other than the one calling the method.
	 */
	public static void blacklistDimensions() {
		if(ConfigHandler.disableAPI) return;
		ConfigHandler.positiveDims = false;
	}
	
	
	/*
	 * This will set the dimension list to act as a whitelist.  This should be
	 * used with caution, since players may have set it to act as a blacklist
	 * for reason dealing with mods other than the one calling the method.
	 */
	public static void whitelistDimensions() {
		if(ConfigHandler.disableAPI) return;
		ConfigHandler.positiveDims = true;
	}
	
	
	public static void saveWorldData(World world) {
		if(ConfigHandler.disableAPI) return;
		File saveDir = world.getSaveHandler().getWorldDirectory();
	}
	
	
	/*
	 * This will reload the config file, effectively erasing any changes made during runtime.
	 * Alternately, if the config has been edited it will bring up the new settings.
	 */
	public static void reloadConfig() {
		if(ConfigHandler.disableAPI) return;
		ConfigHandler.reload();
	}
	
	
	public static boolean apiOn() {
		return !ConfigHandler.disableAPI;
	}

}
