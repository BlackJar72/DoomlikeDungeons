package jaredbgreat.dldungeons;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.builder.Builder;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.themes.Theme;
import jaredbgreat.dldungeons.themes.ThemeReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.Configuration;

public class ConfigHandler {
	
	private static File mainConfig;
	private static File themesDir;
	private static File listsDir;
	protected static File configDir;
	
	private static final int DEFAULT_SCALE  = 8;
	private static final int DEFAULT_MINXZ  = 16;
	private static final int DEFAULT_DIF    = 3;
	
	private static final int[] DEFAULT_DIMS = {0, -1};
	
	private static final boolean DEFAULT_NODEND = true;
	private static final boolean DEFAULT_WRITE_LISTS = false;
	private static final boolean DEFAULT_NATURAL_SPAWN = true;
	private static final boolean DEFAULT_OBEY_RULE = true;
	private static final boolean DEFAULT_POSITIVE_DIMS = true;
	private static final boolean DEFAULT_ANNOUNCE_COMMANDS = true;
	private static final boolean DEFAULT_VANILLA_LOOT = true;	
	private static final boolean DEFAULT_STINGY_LOOT = false;	
	private static final boolean EASY_FIND = false;

	private static final boolean DISABLE_API = false;
	private static final boolean NO_MOB_CHANGES = false;
	
	private static final String[] NEVER_IN_BIOMES = new String[]{"END"};
	private static       String[] neverInBiomes   = NEVER_IN_BIOMES;
	public  static EnumSet<Type>  biomeExclusions = EnumSet.noneOf(Type.class);
	
	public static boolean disableAPI = DISABLE_API;
	public static boolean noMobChanges = NO_MOB_CHANGES;
	
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
		
//		boolean noEnd = config.get("General", "NotInEnd", DEFAULT_NODEND).getBoolean(true);
//		GenerationHandler.setEnd(noEnd);
//		if(noEnd) biomeExclusions.add(Type.END);
//		System.out.println("[DLDUNGEONS] NoEnd set to: " + noEnd);
		
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
		
		vanillaLoot = config.get("General", "IncludeVanillaChestLoot", 
				DEFAULT_VANILLA_LOOT).getBoolean(DEFAULT_VANILLA_LOOT);
		System.out.println("[DLDUNGEONS] Will include vanilla loot from vanilla chests? " + vanillaLoot);
		
		stingyLoot = config.get("General", "StingyWithLoot", 
				DEFAULT_STINGY_LOOT).getBoolean(DEFAULT_STINGY_LOOT);
		System.out.println("[DLDUNGEONS] Will be stingy with chests? " + stingyLoot);
		
		
		neverInBiomes = config.get("General", "NeverInBiomeTypes", NEVER_IN_BIOMES).getStringList();
		processBiomeExclusions1(neverInBiomes);
		
		
		// API Stuff
		disableAPI = config.get("API", "DisableApiCalls", DISABLE_API).getBoolean(DISABLE_API);
		System.out.println("[DLDUNGEONS] Will use? " + !disableAPI);

		noMobChanges = config.get("API", "DontAllowApiOnMobs", NO_MOB_CHANGES).getBoolean(NO_MOB_CHANGES);
		System.out.println("[DLDUNGEONS] Will allow API base mob change? " + !noMobChanges);
		
		
		// Debugging
		Builder.setDebugPole(config.get("Debugging", "BuildPole", false).getBoolean(false));
		
		profile = config.get("Debugging", "AutoProfilingOn", PROFILE).getBoolean(PROFILE);
		System.out.println("[DLDUNGEONS] Will self-profile? " + profile);
		
		
		// Saving it all
		openThemesDir();
		config.save();
	}
	
	
	protected static void reload() {
		init();
	}
	
	
	private static void processBiomeExclusions1(String[] array) {
		for(String str : array) {
			str = str.toUpperCase();
			System.out.println("[DLDUNGEONS] adding " + str + " to excusion list");
			try { 
				Type value = Type.valueOf(str);
				if(value != null) {
					biomeExclusions.add(value);
				}
			} catch(Exception e) {
				System.err.println("[DLDUNGEONS] Error in config! " + str + " is not valid biome dictionary type!");
			}
		}
	}
	
	
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
			listEntities();
			listItems();
			listBlocks();
		}
	}
	
	
	public static void listEntities() {	
		ArrayList<String> mobNames = new ArrayList<String>();
		mobNames.addAll(EntityList.stringToClassMapping.keySet());
		Collections.sort(mobNames);
		BufferedWriter outstream = null;
		File moblist = new File(listsDir.toString() + File.separator + "mobs.txt");
		if(moblist.exists()) moblist.delete(); 
		try {
			outstream = new BufferedWriter(new 
					FileWriter(moblist.toString()));			
			
			for(String name : mobNames){ 
				Class A = (Class)EntityList.stringToClassMapping.get(name);
				//System.out.println(EntityList.stringToClassMapping.get(name));
				//System.out.println(A);
				if(EntityLiving.class.isAssignableFrom(A) && !Modifier.isAbstract(A.getModifiers())) {
					//System.out.println("[DLDUNGEONS] Found living entity " + (String)name);
					outstream.write((String)name);
					outstream.newLine();
				}
			}
			
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	public static void listItems() {	
		BufferedWriter outstream = null;
		File itemlist = new File(listsDir.toString() + File.separator + "itmes.txt");
		if(itemlist.exists()) itemlist.delete(); 
		try {
			outstream = new BufferedWriter(new 
					FileWriter(itemlist.toString()));

			for(int i = 0; i < Item.itemsList.length; i++) {
				if(Item.itemsList[i] != null) {
					outstream.write(Item.itemsList[i].getUnlocalizedName() 
							+ ", ID = " + i);
					outstream.newLine();
				}
			}
			
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	public static void listBlocks() {	
		BufferedWriter outstream = null;
		File itemlist = new File(listsDir.toString() + File.separator + "blocks.txt");
		if(itemlist.exists()) itemlist.delete(); 
		try {
			outstream = new BufferedWriter(new 
					FileWriter(itemlist.toString()));

			for(int i = 0; i < Block.blocksList.length; i++) {
				if(Block.blocksList[i] != null) 
					if(!Block.blocksList[i].getUnlocalizedName().equals("tile.ForgeFiller")) {
						outstream.write(Block.blocksList[i].getUnlocalizedName() 
								+ ", ID = " + i);
						outstream.newLine();
				}
			}
			
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	private static void openThemesDir() {
		// This will open the directory for theme data; it will not load themes
		// as that must be done post-init incase blocks / mobs / items from
		// other mobs are requested by user themes.
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
	}
	
	
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
	
	
	private static void processBiomeExclusions(String[] array) {
		for(String str : array) {
			str = str.toUpperCase();
			System.out.println("[DLDUNGEONS] adding " + str + " to excusion list");
			try { 
				Type value = Type.valueOf(str);
				if(value != null) {
					biomeExclusions.add(value);
				}
			} catch(Exception e) {
				System.err.println("[DLDUNGEONS] Error in config! " + str + " is not valid biome dictionary type!");
			}
		}
	}
	
	
	public static String getConfigDir() {
		return configDir + File.separator;
	}

	
}
