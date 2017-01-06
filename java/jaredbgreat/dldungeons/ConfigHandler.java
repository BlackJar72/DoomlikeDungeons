package jaredbgreat.dldungeons;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.builder.Builder;
import jaredbgreat.dldungeons.planner.mapping.MapMatrix;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.themes.ThemeReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.config.Configuration;


/**
 * This class reads and holds data from the main configuration file.
 * 
 * It might have been better to store many of these with the class they
 * directly influence, but this is not likely to be changed as the current 
 * system is already in place the rest of the code base is already mature.
 * 
 * In addition to reading and holding config data, this class also contains 
 * utility methods output information that can be useful in setting up 
 * configurations and writing theme files.
 */
public final class ConfigHandler {
	
	private static File mainConfig;
	private static File themesDir;
	private static File listsDir;
	protected static File configDir;
	
	private static final int DEFAULT_SCALE  = 8;
	private static final int DEFAULT_MINXZ  = 16;
	private static final int DEFAULT_DIF    = 3;
	
	private static final int[] DEFAULT_DIMS = {0, -1};
	
	private static final boolean DEFAULT_WRITE_LISTS = false;
	private static final boolean DEFAULT_NATURAL_SPAWN = true;
	private static final boolean DEFAULT_OBEY_RULE = true;
	private static final boolean DEFAULT_POSITIVE_DIMS = true;
	private static final boolean DEFAULT_ANNOUNCE_COMMANDS = true;
	
	// Vanilla loot will not be added in version of Mincraft 1.9+
	// Instead all dungeons will have some loot enchanted.
	private static final boolean DEFAULT_VANILLA_LOOT = false;	
	private static final boolean DEFAULT_STINGY_LOOT = false;	
	private static final boolean EASY_FIND = false;

	private static final boolean DISABLE_API = false;
	private static final boolean NO_MOB_CHANGES = false;
	
	public static boolean disableAPI = DISABLE_API;
	public static boolean noMobChanges = NO_MOB_CHANGES;
	
	private static final String[] NEVER_IN_BIOMES = new String[]{"END"};
	private static       String[] neverInBiomes   = NEVER_IN_BIOMES;
	public  static HashSet<Type>  biomeExclusions = new HashSet<Type>();
	
	protected static boolean writeLists = DEFAULT_WRITE_LISTS;	
	protected static boolean naturalSpawn = DEFAULT_NATURAL_SPAWN;	
	protected static boolean obeyRule = DEFAULT_OBEY_RULE;	
	protected static boolean positiveDims = DEFAULT_POSITIVE_DIMS;
	
	public    static boolean easyFind = EASY_FIND;
	
	public    static boolean announceCommands = DEFAULT_ANNOUNCE_COMMANDS;
	public    static boolean vanillaLoot = DEFAULT_VANILLA_LOOT;
	public    static boolean stingyLoot = DEFAULT_STINGY_LOOT;	
		
	private   static final boolean PROFILE = false;
	protected static boolean profile;
	
	private static final boolean INSTALL_THEMES = true;
	public  static       boolean installThemes = INSTALL_THEMES;
	
	private static final boolean INSTALL_CMD = true;
	public  static       boolean installCmd = INSTALL_CMD;
	
	public static Difficulty difficulty;

	
	// All methods and data are static. 
	// There is no reason this should ever be instantiated.
	private ConfigHandler(){/*Do nothing*/}
	
	
	/**
	 * This will read the them file and apply the themes.
	 */
	public static void init() {
		File file = new File(ConfigHandler.configDir.toString() 
			+ File.separator + Info.OLD_ID  + ".cfg");
		Configuration config = new Configuration(file);
		config.load();
		
		// General configuration
		int freqScale = config.get("General", "FrequencyScale", DEFAULT_SCALE).getInt();
		if((freqScale > 30) || (freqScale < 4)) freqScale = DEFAULT_SCALE;
		GenerationHandler.setFrequency(freqScale);
		System.out.println("[DLDUNGEONS] Frequency Scaling Factor Set To: " + freqScale);
		
		int minXZ = config.get("General", "MinChunkXY", DEFAULT_MINXZ).getInt();
		if(minXZ < 0) minXZ = DEFAULT_MINXZ;
		GenerationHandler.setMinXZ(minXZ);
		System.out.println("[DLDUNGEONS] Minimum X Factor Set To: " + minXZ);
		
		int diff = config.get("General", "Difficulty", DEFAULT_DIF).getInt();
		if((diff < 0) || (diff > 5)) diff = DEFAULT_DIF;
		parseDiff(diff);
		System.out.println("[DLDUNGEONS] Difficulty set to: " + difficulty.label);
		
		int[] dims = config.get("General", "Dimensions", DEFAULT_DIMS).getIntList();
		GenerationHandler.setDimensions(dims);
		System.out.print("[DLDUNGEONS] Dimensions listed in config file: ");
		for(int i = 0; i < dims.length; i++) System.out.print(dims[i] + ", ");
		System.out.println();
		
		naturalSpawn = config.get("General", "SpawnWithWordgen", DEFAULT_NATURAL_SPAWN).getBoolean(true);
		System.out.println("[DLDUNGEONS] Will spawn dungeons in with world generation? " + naturalSpawn);
		
		obeyRule = config.get("General", "ObeyFeatureSpawningRule", DEFAULT_OBEY_RULE).getBoolean(true);
		System.out.println("[DLDUNGEONS] Will spawn dungeons even with structures disabled? " + !obeyRule);
		
		positiveDims = config.get("General", "OnlySpawnInListedDims", DEFAULT_POSITIVE_DIMS).getBoolean(true);
		if(positiveDims) System.out.print("[DLDUNGEONS] Will only spawn in these dimensions: ");
		else System.out.print("[DLDUNGEONS] Will never spawn in these dimensions: ");
		for(int i = 0; i < dims.length; i++) System.out.print(dims[i] + ", ");
		System.out.println();
		
		writeLists = config.get("General", "ExportLists", DEFAULT_WRITE_LISTS).getBoolean(DEFAULT_WRITE_LISTS);
		System.out.println("[DLDUNGEONS] Will export item, block, and mob lists? " + writeLists);
		
		announceCommands = config.get("General", "AnnounceCommands", DEFAULT_ANNOUNCE_COMMANDS).getBoolean(DEFAULT_ANNOUNCE_COMMANDS);
		System.out.println("[DLDUNGEONS] Will announce use of OP/cheat commands? " + announceCommands);
		
		easyFind = config.get("General", "EasyFind", EASY_FIND).getBoolean(EASY_FIND);
		System.out.println("[DLDUNGEONS] Will dungeons be easy to find? " + easyFind);
		
		installThemes = config.get("General", "InstallThemes", INSTALL_THEMES).getBoolean(INSTALL_THEMES);
		System.out.println("[DLDUNGEONS] Will themes be automatically install if themes folder is empty? " + installThemes);
		
		installCmd = config.get("General", "InstallThemesByCommand", INSTALL_CMD).getBoolean(INSTALL_CMD);
		System.out.println("[DLDUNGEONS] Can themes be (re)installed by command? " + installCmd);
		
		stingyLoot = config.get("General", "StingyWithLoot", 
				DEFAULT_STINGY_LOOT).getBoolean(DEFAULT_STINGY_LOOT);
		System.out.println("[DLDUNGEONS] Will be stingy with chests? " + stingyLoot);
		
		
		neverInBiomes = config.get("General", "NeverInBiomeTypes", NEVER_IN_BIOMES).getStringList();
		processBiomeExclusions(neverInBiomes);
		
		
		// API Stuff
		disableAPI = config.get("API", "DisableApiCalls", DISABLE_API).getBoolean(DISABLE_API);
		System.out.println("[DLDUNGEONS] Will use API? " + !disableAPI);

		noMobChanges = config.get("API", "DontAllowApiOnMobs", NO_MOB_CHANGES).getBoolean(NO_MOB_CHANGES);
		System.out.println("[DLDUNGEONS] Will allow API base mob change? " + !noMobChanges);
		
		
		// Debugging
		Builder.setDebugPole(config.get("Debugging", "BuildPole", false).getBoolean(false));
		
		MapMatrix.setDrawFlyingMap(config.get("Debugging", "BuildFlyingMap", false).getBoolean(false));
		
		profile = config.get("Debugging", "AutoProfilingOn", PROFILE).getBoolean(PROFILE);
		System.out.println("[DLDUNGEONS] Will self-profile? " + profile);
		
		
		// Saving it all
		openThemesDir();
		config.save();
	}
	
	
	/**
	 * This will reload the config data; really just 
	 * wraps init.
	 */
	protected static void reload() {
		init();
	}
	
	
	/**
	 * This will output lists of blocks, items, and mobs know to the 
	 * game with their proper, unlocalized names.  This data is useful
	 * in editing theme files.
	 */
	public static void generateLists() {
		if(!writeLists) return;
		listsDir = new File(configDir.toString() + File.separator + "lists");
		
		if(!listsDir.exists()) {
			listsDir.mkdir();
		} 		
		if(!listsDir.exists()) {
			System.out.println("[DLDUNGEONS] Warning: Could not create " + listsDir + ".");
		} else if (!listsDir.isDirectory()) {
			System.out.println("[DLDUNGEONS] Warning: " + listsDir 
					+ " is not a directory (folder); no themes loaded.");
		} else {		
			listMobs();
			listItems();
			listBlocks();
		}
	}
	
	
	/**
	 * This will list all mobs, using there unlocalized names, writing 
	 * the data to the file lists/mobs.txt.
	 */
	public static void listMobs() {	
		Set<ResourceLocation> mobrl = EntityList.getEntityNameList();
		ArrayList<String> mobs = new ArrayList<String>();
		for(ResourceLocation mob : mobrl) {
				mobs.add(mob.toString());
		}
		Collections.sort(mobs);
		
		BufferedWriter outstream = null;
		File moblist = new File(listsDir.toString() + File.separator + "entities.txt");
		if(moblist.exists()) moblist.delete(); 
		try {
			outstream = new BufferedWriter(new 
					FileWriter(moblist.toString()));			
			
			for(String mob : mobs){ 
				outstream.write(mob.toString());
				outstream.newLine();
			}
			
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * This will list all items, using their complete unlocalized names 
	 * with mod id's, and write them the file lists/items.txt.  This 
	 * is useful for writing theme files.
	 */
	public static void listItems() {	
		BufferedWriter outstream = null;
		File itemlist = new File(listsDir.toString() + File.separator + "itmes.txt");
		if(itemlist.exists()) itemlist.delete(); 
		try {
			outstream = new BufferedWriter(new 
					FileWriter(itemlist.toString()));
			
			for(Object item : Item.REGISTRY){ 
				String name = Item.REGISTRY.getNameForObject((Item) item).toString();
				if(true) {
					outstream.write(name);
					outstream.newLine();
				}
			}
			
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			System.err.println("[DLDUNGEONS] Error: Could not write file items.txt");
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * This will list all blocks using their correct, unlocalized names, complete with 
	 * mod id's, and write them to the file lists/blocks.txt.  This is useful for editing 
	 * theme files.
	 */
	public static void listBlocks() {	
		BufferedWriter outstream = null;
		File itemlist = new File(listsDir.toString() + File.separator + "blocks.txt");
		if(itemlist.exists()) itemlist.delete(); 
		try {
			outstream = new BufferedWriter(new 
					FileWriter(itemlist.toString()));	
			
			for(Object block : Block.REGISTRY){ 
				String name = Block.REGISTRY.getNameForObject((Block)block).toString();
				if(true) {;
					outstream.write(name);
					outstream.newLine();
				}
			}
			
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			System.err.println("[DLDUNGEONS] Error: Could not write file blocks.txt");
			e.printStackTrace();
		}		
	}
	
	
	/**
	 * This will open the theme's directory for some general housekeeping 
	 * purposes.  I does not read the theme files, as this called by init 
	 * during pre-init phase of mod loading, while themes are loaded 
	 * post-init to allow other mods a chance to load and register their 
	 * content.
	 */
	private static void openThemesDir() {
		Externalizer exporter;
		String themesDirName = configDir.toString() + File.separator 
				+ "themes" + File.separator;
		System.out.println("[DLDUNGEONS] themesdir will be set to " + themesDirName);
		themesDir = new File(themesDirName);
		System.out.println("[DLDUNGEONS] themesdir File is be set to " + themesDir);
		if(!themesDir.exists()) {
			themesDir.mkdir();
		} 		
		if(!themesDir.exists()) {
			System.out.println("[DLDUNGEONS] Warning: Could not create " + themesDirName + ".");
		} else if (!themesDir.isDirectory()) {
			System.out.println("[DLDUNGEONS] Warning: " + themesDirName 
					+ " is not a directory (folder); no themes loaded.");
		} else ThemeReader.setThemesDir(themesDir);
		File chests = new File(configDir.toString() + File.separator + "chest.cfg");
		if(!chests.exists()) {
			exporter = new Externalizer(configDir.toString() + File.separator);
			exporter.makeChestCfg();
		}
		File nbtconf = new File(configDir.toString() + File.separator + "nbt.cfg");
		if(!nbtconf.exists()) {
			exporter = new Externalizer(configDir.toString() + File.separator);
			exporter.makeNBTCfg();
		}
	}
	
	
	/**
	 * This convert difficulty setting from an integer to a 
	 * Difficulty enum constant. 
	 * 
	 * @param diff
	 */
	protected static void parseDiff(int diff) {
		switch(diff) {
			case 0:
				difficulty = Difficulty.NONE;
				break;
			case 1:
				difficulty = Difficulty.BABY;
				break;
			case 2:
				difficulty = Difficulty.NOOB;
				break;
			case 4:
				difficulty = Difficulty.HARD;
				break;
			case 5:
				difficulty = Difficulty.NUTS;
				break;
			case 3:
			default:
				difficulty = Difficulty.NORM;
				
				break;
		}
	}
	
	
	/**
	 * This looks for the mods config directory, and attempts to 
	 * create it if it does not exist.  It will them set this as 
	 * the config directory and return it as a File.
	 * 
	 * @param fd
	 * @return the config directory / folder
	 */
	public static File findConfigDir(File fd) {
		File out = new File(fd.toString() + File.separator + Info.OLD_ID);
		if(!out.exists()) out.mkdir();
		
		if(!out.exists()) {
			System.err.println("[DLDUNGEONS] ERROR: Could not create config directory");
		} else if(!out.isDirectory()) {
			System.err.println("[DLDUNGEONS] ERROR: Config directory is not a directory!");
		} else {
			configDir = out;
			ThemeReader.setConfigDir(out);
		}
		return out;
	}
	
	
	/**
	 * This will put biome types in the string array into the list of 
	 * types where no dungeons show everr generate.
	 * 
	 * @param array
	 */
	private static void processBiomeExclusions(String[] array) {
		for(String str : array) {
			str = str.toUpperCase();
			System.out.println("[DLDUNGEONS] adding " + str + " to excusion list");
			try { 
				Type value = Type.getType(str);
				if(value != null) {
					biomeExclusions.add(value);
				}
			} catch(Exception e) {
				System.err.println("[DLDUNGEONS] Error in config! " + str + " is not valid biome dictionary type!");
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Returns the full path of the config directory as a String.
	 * 
	 * @return
	 */
	public static String getConfigDir() {
		return configDir + File.separator;
	}
}
