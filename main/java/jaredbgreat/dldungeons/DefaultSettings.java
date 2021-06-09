package jaredbgreat.dldungeons;


import java.util.HashSet;

import net.minecraftforge.common.BiomeDictionary.Type;


/**
 * Just some default setting to use while trying to see if I can get the generation to 
 * work.
 */
public final class DefaultSettings {
	
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
	
}
