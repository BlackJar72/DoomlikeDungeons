package jaredbgreat.dldungeons;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.builder.Builder;
import jaredbgreat.dldungeons.debug.Logging;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.pieces.chests.TreasureChest;
import jaredbgreat.dldungeons.planner.mapping.MapMatrix;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.themes.ThemeReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
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
	private static final boolean DEFAULT_THIN_SPAWNERS = true;
	
	// Vanilla loot will not be added in version of Mincraft 1.9+
	// Instead all dungeons will have some loot enchanted.
	private static final boolean DEFAULT_VANILLA_LOOT = false;	
	private static final boolean EASY_FIND  = true;	
	private static final boolean SINGLE_ENTRANCE = true;

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
	public    static boolean singleEntrance = SINGLE_ENTRANCE;
	
	public    static boolean announceCommands = DEFAULT_ANNOUNCE_COMMANDS;
	public    static boolean vanillaLoot = DEFAULT_VANILLA_LOOT;
	public    static boolean thinSpawners = DEFAULT_THIN_SPAWNERS;	
		
	private   static final boolean PROFILE = false;
	protected static boolean profile;	
	
	private   static final boolean FAILFAST = false;
	public static boolean failfast = FAILFAST;
	
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
		config.addCustomCategoryComment("General", "Main config settings");
		int freqScale = config.get("General", "FrequencyScale", DEFAULT_SCALE, 
				"Determines the average distance between dungeons (+2 is twice as far appart)").getInt();
		if((freqScale > 30) || (freqScale < 4)) freqScale = DEFAULT_SCALE;
		GenerationHandler.setFrequency(freqScale);
		Logging.logInfo("Frequency Scaling Factor Set To: " + freqScale);
		
		int minXZ = config.get("General", "MinChunkXY", DEFAULT_MINXZ, 
				"Spawn protection: this is the minimum number of chunks from spawn before dungeon generate")
				.getInt();
		if(minXZ < 0) minXZ = DEFAULT_MINXZ;
		GenerationHandler.setMinXZ(minXZ);
		Logging.logInfo("Minimum X Factor Set To: " + minXZ);
		
		int diff = config.get("General", "Difficulty", DEFAULT_DIF, 
				"How hard: 0 = empty, 1 = baby, 2 = easy, 3 = normal, 4= hard, 5 = nightmare")
				.getInt();
		if((diff < 0) || (diff > 5)) diff = DEFAULT_DIF;
		parseDiff(diff);
		Logging.logInfo("Difficulty set to: " + difficulty.label);
		
		int[] dims = config.get("General", "Dimensions", DEFAULT_DIMS, 
				"These dimensions either lack dungeons or only they have them "
				+ "(see OnlySpawnInListedDimensions)")
				.getIntList();
		GenerationHandler.setDimensions(dims);
		System.out.print("Dimensions listed in config file: ");
		for(int i = 0; i < dims.length; i++) System.out.print(dims[i] + ", ");
		System.out.println();
		
		naturalSpawn = config.get("General", "SpawnWithWordgen", DEFAULT_NATURAL_SPAWN, 
				"True to have dungeons generate naturally, false otherwise.").getBoolean(true);
		Logging.logInfo("Will spawn dungeons in with world generation? " + naturalSpawn);
		
		obeyRule = config.get("General", "ObeyFeatureSpawningRule", DEFAULT_OBEY_RULE, 
				"If true worlds that are set to not generate (vanilla) structures "
				+ "will also not have these dungesons")
				.getBoolean(true);
		Logging.logInfo("Will spawn dungeons even with structures disabled? " + !obeyRule);
		
		positiveDims = config.get("General", "OnlySpawnInListedDims", DEFAULT_POSITIVE_DIMS, 
				"Determines if the dimesions list is black list of white list; "
				+ System.lineSeparator() + "if true only listed dimensions have dungeons, "
				+ System.lineSeparator() + "otherswise all but those will have them").getBoolean(true);
		if(positiveDims) System.out.print("Will only spawn in these dimensions: ");
		else System.out.print("Will never spawn in these dimensions: ");
		for(int i = 0; i < dims.length; i++) System.out.print(dims[i] + ", ");
		System.out.println();
		
		writeLists = config.get("General", "ExportLists", DEFAULT_WRITE_LISTS, 
				"If true a \"lists\" folder will be created and files showing names and ideas for all "
				+ System.lineSeparator() + "mobs / items / blocks will be made.  This is good for editing "
				+ System.lineSeparator() + "themes.").getBoolean(DEFAULT_WRITE_LISTS);
		Logging.logInfo("Will export item, block, and mob lists? " + writeLists);
		
		announceCommands = config.get("General", "AnnounceCommands", DEFAULT_ANNOUNCE_COMMANDS,
				"If true, console commands from the mod will be annouced in chat")
				.getBoolean(DEFAULT_ANNOUNCE_COMMANDS);
		Logging.logInfo("Will announce use of OP/cheat commands? " + announceCommands);
		
		easyFind = config.get("General", "EasyFind", EASY_FIND, 
				"If true dungeons will all have an entrance with a room or ruin, unless the theme "
				+ System.lineSeparator() + "is one that never has entrances.").getBoolean(EASY_FIND);
		Logging.logInfo("Will dungeons be easy to find? " + easyFind);
		
		singleEntrance = config.get("General", "SingleEntrances", SINGLE_ENTRANCE, 
				"If true all dungeons will exactly one entrance (if the theme allows entrances), "
				+ System.lineSeparator() 
				+ "like a typical dungeon crawler (Dungeons will be harder but much no more, no less, "
				+ System.lineSeparator() 
				+ "better loot.) If false entrance are random (how the mod originally worked). ")
				.getBoolean(SINGLE_ENTRANCE);
		Logging.logInfo("Will dungeon all have single entrances? " + singleEntrance);
		
		installThemes = config.get("General", "InstallThemes", INSTALL_THEMES, 
				"If true themes will automaticall be (re)installed if the themes folder is empty.")
				.getBoolean(INSTALL_THEMES);
		Logging.logInfo("Will themes be automatically install if themes folder is empty? " 
				+ installThemes);
		
		installCmd = config.get("General", "InstallThemesByCommand", INSTALL_CMD, 
				"If true themes can be (re)installed by the console commands \"\\dldInstallThemes\" "
				+ System.lineSeparator() + "and \"\\dldForceInstallThemes.\"").getBoolean(INSTALL_CMD);
		Logging.logInfo("Can themes be (re)installed by command? " + installCmd);
		
		thinSpawners = config.get("General", "ThinSpawners", DEFAULT_THIN_SPAWNERS,
				"If true smaller dungeons will have some of there spawners removed to "
				+ "make them more like larger dungeons.")
				.getBoolean(DEFAULT_THIN_SPAWNERS);
		
		neverInBiomes = config.get("General", "NeverInBiomeTypes", NEVER_IN_BIOMES, 
				"Dungeons will not generate in these biome types (uses Forge biome dictionary")
				.getStringList();
		processBiomeExclusions(neverInBiomes);
		
		
		// API Stuff
		config.addCustomCategoryComment("API", "Turns some API calls on or off");
		disableAPI = config.get("API", "DisableApiCalls", DISABLE_API, 
				"If true the API is disabled")
				.getBoolean(DISABLE_API);
		Logging.logInfo("Will use API? " + !disableAPI);

		noMobChanges = config.get("API", "DontAllowApiOnMobs", NO_MOB_CHANGES, 
				"If false other mods that use the API can add mobs to the dungeons, if true they cannot.  "
				+ System.lineSeparator() + "(This is good if hand designing custom themes for a mod pack.)")
				.getBoolean(NO_MOB_CHANGES);
		Logging.logInfo("Will allow API base mob change? " + !noMobChanges);
		
		
		// Debugging

		config.addCustomCategoryComment("Debugging", 
				"Things for debugging the mod; you should probably leave these all off, "
				+ System.lineSeparator()
				+ "some are cheats, so cause lots of lag.");
		Builder.setDebugPole(config.get("Debugging", "BuildPole", false, 
				"CHEAT: If true all dungeons will have a giant quarts pole through the middle and be "
				+ System.lineSeparator() + "surrounded by a boarder of floating llapis,  This is to make "
			    + System.lineSeparator() + "them easy to find when testing.")
			    .getBoolean(false));
		
		MapMatrix.setDrawFlyingMap(config.get("Debugging", "BuildFlyingMap", false, 
				"CHEAT: If true the layout of the dungeon will be built from floating glowstone (etc.) "
				+ System.lineSeparator() + "-- on some versions it also causes *EXTREME LAG*!")
				.getBoolean(false));
		
		failfast = config.get("Debugging", "FailfastLoot", false, 
				"If true this will cause the system to crash on loot config errors."
				+ System.lineSeparator() + "This is good for debugging modpac.")
			    .getBoolean(false);
		
		profile = config.get("Debugging", "AutoProfilingOn", PROFILE, 
				"If true reports on dungeon planning and build times will be exproted to files and the commandline")
				.getBoolean(PROFILE);
		Logging.logInfo("Will self-profile? " + profile);
		
		//Loot
		int a, b, c;
		config.addCustomCategoryComment("Loot", 
				"This will change the value and quantity of loo; be careful, too "
				+ System.lineSeparator() 
				+ "could cause a crash if there isn't enough room in the chest.  "
				+ System.lineSeparator() 
				+ "The basic chest formula is for the whole cheset, the treasure "
				+ System.lineSeparator() 
				+ "chest formula is per loot normal category -- you get 3 time that "
				+ System.lineSeparator() 
				+ "many items.");
		a = config.getInt("A1", "Loot", 3, 0, 9, 
				"Part of the loot quantity numbers for basic chests; " 
				+ System.lineSeparator() 
				+ " random.nextInt(A1 + (RoomDifficulty / B1)) + C1");
		b = config.getInt("B1", "Loot", 1, 0, 9, 
				"Part of the loot quantity numbers for basic chests; " 
						+ System.lineSeparator() 
						+ " random.nextInt(A1 + (RoomDifficulty / B1)) + C1");
		c = config.getInt("C1", "Loot", 3, 0, 9, 
				"Part of the loot quantity numbers for basic chests; " 
						+ System.lineSeparator() 
						+ " random.nextInt(A1 + (RoomDifficulty / B1)) + C1");
		BasicChest.setBasicLootNumbers(a, b, c);
		a = config.getInt("A2", "Loot", 2, 0, 9, 
				"Part of the loot quantity numbers for treasure chests; " 
				+ System.lineSeparator() 
				+ " random.nextInt(A2 + (RoomDifficulty / B2)) + C2");
		b = config.getInt("B2", "Loot", 3, 0, 9, 
				"Part of the loot quantity numbers for treasure chests; " 
						+ System.lineSeparator() 
						+ " random.nextInt(A2 + (RoomDifficulty / B2)) + C2");
		c = config.getInt("C2", "Loot", 2, 0, 9, 
				"Part of the loot quantity numbers for treasure chests; " 
						+ System.lineSeparator() 
						+ " random.nextInt(A2 + (RoomDifficulty / B2)) + C2");
		TreasureChest.setBasicLootNumbers(a, b, c);
		Room.setLootBonus(config.getInt("Loot Bonus", "Loot", 1, -9, 9, 
				"This modifies the value of the loot, in case you think default is "
					+ System.lineSeparator() + "too generous or too stingy."));
		
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
			Logging.logInfo("Warning: Could not create " + listsDir + ".");
		} else if (!listsDir.isDirectory()) {
			Logging.logInfo("Warning: " + listsDir 
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
		File itemlist = new File(listsDir.toString() + File.separator + "items.txt");
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
			System.err.println("Error: Could not write file items.txt");
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
			System.err.println("Error: Could not write file blocks.txt");
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
		Logging.logInfo("themesdir will be set to " + themesDirName);
		themesDir = new File(themesDirName);
		Logging.logInfo("themesdir File is be set to " + themesDir);
		if(!themesDir.exists()) {
			themesDir.mkdir();
		} 		
		if(!themesDir.exists()) {
			Logging.logInfo("Warning: Could not create " + themesDirName + ".");
		} else if (!themesDir.isDirectory()) {
			Logging.logInfo("Warning: " + themesDirName 
					+ " is not a directory (folder); no themes loaded.");
		} else ThemeReader.setThemesDir(themesDir);
		File chestDir = new File(configDir.toString() + File.separator + ThemeReader.chestDirName);
		if(!chestDir.exists()) {
			chestDir.mkdir();
		}
		exporter = new Externalizer(configDir.toString() + File.separator);
		exporter.makeChestCfg();
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
			System.err.println("ERROR: Could not create config directory");
		} else if(!out.isDirectory()) {
			System.err.println("ERROR: Config directory is not a directory!");
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
			Logging.logInfo("adding " + str + " to excusion list");
			try { 
				Type value = Type.getType(str);
				if(value != null) {
					biomeExclusions.add(value);
				}
			} catch(Exception e) {
				System.err.println("Error in config! " + str + " is not valid biome dictionary type!");
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
