package jaredbgreat.dldungeons;


import java.io.File;
import java.util.HashSet;

import jaredbgreat.dldungeons.genhandler.SectionFeature;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.pieces.chests.TreasureChest;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.themes.ThemeReader;
import jaredbgreat.dldungeons.util.config.ComplexConfig;
import jaredbgreat.dldungeons.util.debug.Logging;
import net.minecraftforge.common.BiomeDictionary.Type;


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
	static ComplexConfig config;
	
	static volatile File mainConfig;
	static volatile File themesDir;
	static volatile File listsDir;
	static volatile File configDir;
	
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
	private static final boolean BIG_HUBS  = false;	

	private static final boolean DISABLE_API = false;
	private static final boolean NO_MOB_CHANGES = false;
	
	public static volatile boolean disableAPI = DISABLE_API;
	public static volatile boolean noMobChanges = NO_MOB_CHANGES;
	
	public  static volatile HashSet<Type>  biomeExclusions = new HashSet<Type>();
	
	protected static boolean writeLists = DEFAULT_WRITE_LISTS;	
	protected static boolean naturalSpawn = DEFAULT_NATURAL_SPAWN;	
	protected static boolean obeyRule = DEFAULT_OBEY_RULE;	
	protected static boolean positiveDims = DEFAULT_POSITIVE_DIMS;
	
	public static volatile boolean easyFind = EASY_FIND;
	public static volatile boolean singleEntrance = SINGLE_ENTRANCE;
	public static volatile boolean bigHubs = EASY_FIND;
	
	
	public static volatile boolean announceCommands = DEFAULT_ANNOUNCE_COMMANDS;
	public static volatile boolean vanillaLoot = DEFAULT_VANILLA_LOOT;
	public static volatile boolean thinSpawners = DEFAULT_THIN_SPAWNERS;	
		
	private   static final boolean PROFILE = false;
	protected static volatile boolean profile;	
	
	private   static final boolean FAILFAST = false;
	public static volatile boolean failfast = FAILFAST;
	
	private static final boolean INSTALL_THEMES = true;
	public  static volatile boolean installThemes = INSTALL_THEMES;
	
	private static final boolean INSTALL_CMD = true;
	public  static volatile boolean installCmd = INSTALL_CMD;
	
	public static volatile Difficulty difficulty;
	
	// All methods and data are static. 
	// There is no reason this should ever be instantiated.
	private ConfigHandler(){/*Do nothing*/}
	
	
	/**
	 * This will read the them file and apply the themes.
	 */
	public static void init(String directory) {
		init(new File(directory + File.separator + Info.DIR + ".cfg"));
	}
	
	
	/**
	 * This will read the them file and apply the themes.
	 */
	public static void init(File file) {
		config = new ComplexConfig(file).open();		
		
		// General configuration
		config.createCategory("General", "Main config settings");
		
		int freqScale = config.getInt("FrequencyScale", "General", DEFAULT_SCALE, 4, 50,  
				"Determines the average distance between dungeons (+2 is twice as far appart).", 
				"This means that smaller numbers give more dungeons.  At 4 there should be a",
				"dungeon every 128x128 block area, at 8 there should be one for every 512x512 blocks,",
				"at 50 there should be about four dungeons per world.");
		SectionFeature.setFrequency(freqScale); // TODO/FIXME
		Logging.logInfo("Frequency Scaling Factor Set To: " + freqScale);
		
		int minXZ = config.getInt("MinChunkXY", "General", DEFAULT_MINXZ, 0, 1875000,  
				"Spawn protection: this is the minimum number of chunks from spawn before dungeon generate");		
		SectionFeature.setMinXZ(minXZ);
		Logging.logInfo("Minimum X Factor Set To: " + minXZ);
		
		int diff = config.getInt("Difficulty", "General", DEFAULT_DIF, 0, 5,
				"How hard: 0 = empty, 1 = baby, 2 = easy, 3 = normal, 4= hard, 5 = nightmare");
		if((diff < 0) || (diff > 5)) diff = DEFAULT_DIF;
		parseDiff(diff);
		Logging.logInfo("Difficulty set to: " + difficulty.label);
		
		int[] dims = config.getIntArray("Dimensions", "General", DEFAULT_DIMS, 
				"These dimensions either lack dungeons or only they have them "
				+ "(see OnlySpawnInListedDimensions)");
		SectionFeature.setDimensions(dims);
		
		System.out.print("Dimensions listed in config file: ");
		for(int i = 0; i < dims.length; i++) System.out.print(dims[i] + ", ");
		System.out.println();
		
		naturalSpawn = config.getBoolean("SpawnWithWordgen", "General", DEFAULT_NATURAL_SPAWN, 
				"True to have dungeons generate naturally, false otherwise.");
		Logging.logInfo("Will spawn dungeons in with world generation? " + naturalSpawn);
		
		obeyRule = config.getBoolean("ObeyFeatureSpawningRule", "General", DEFAULT_OBEY_RULE, 
				"If true worlds that are set to not generate (vanilla) structures "
				+ "will also not have these dungesons");
		Logging.logInfo("Will spawn dungeons even with structures disabled? " + !obeyRule);
		
		positiveDims = config.getBoolean("OnlySpawnInListedDims", "General", DEFAULT_POSITIVE_DIMS, 
				"Determines if the dimesions list is black list of white list; ",
				"if true only listed dimensions have dungeons, otherswise all but",
				"those will have them");
		if(positiveDims) Logging.logInfo("Will only spawn in these dimensions: ");
		else Logging.logInfo("Will never spawn in these dimensions: ");
		for(int i = 0; i < dims.length; i++) System.out.print(dims[i] + ", ");
		System.out.println();
		
		writeLists = config.getBoolean("ExportLists", "General", DEFAULT_WRITE_LISTS, 
				"If true a \"lists\" folder will be created and files showing names and ideas for all ",
				"mobs / items / blocks will be made.  This is good for editing themes.");
		Logging.logInfo("Will export item, block, and mob lists? " + writeLists);
		
		announceCommands = config.getBoolean("AnnounceCommands", "General", DEFAULT_ANNOUNCE_COMMANDS,
				"If true, console commands from the mod will be annouced in chat");
		Logging.logInfo("Will announce use of OP/cheat commands? " + announceCommands);
		
		easyFind = config.getBoolean("EasyFind", "General", EASY_FIND, 
				"If true dungeons will all have an entrance with a room or ruin, unless the theme ",
				"is one that never has entrances.");
		Logging.logInfo("Will dungeons be easy to find? " + easyFind);
		
		bigHubs = config.getBoolean("BigHubs", "General", BIG_HUBS, 
				"If true entrances and \"boss\" rooms will have extra high ceilings, good with tall mobs");
		Logging.logInfo("Hub room (entrances and boss rooms) will have high ceiling? " + bigHubs);
		
		singleEntrance = config.getBoolean("SingleEntrances", "General", SINGLE_ENTRANCE, 
				"If true all dungeons will exactly one entrance (if the theme allows entrances), ",
				"like a typical dungeon crawler (Dungeons will be harder but much no more, no less, ",
				"better loot.) If false entrance are random (how the mod originally worked). ");
		Logging.logInfo("Will dungeon all have single entrances? " + singleEntrance);
		
		installThemes = config.getBoolean("InstallThemes", "General", INSTALL_THEMES, 
				"If true themes will automaticall be (re)installed if the themes folder is empty.");
		Logging.logInfo("Will themes be automatically install if themes folder is empty? " 
				+ installThemes);
		
		installCmd = config.getBoolean("InstallThemesByCommand", "General", INSTALL_CMD, 
				"If true themes can be (re)installed by the console commands \"\\dldInstallThemes\" "
				+ System.lineSeparator() + "and \"\\dldForceInstallThemes.\"");
		Logging.logInfo("Can themes be (re)installed by command? " + installCmd);
		
		thinSpawners = config.getBoolean("ThinSpawners", "General", DEFAULT_THIN_SPAWNERS,
				"If true smaller dungeons will have some of there spawners removed to ",
				"make them more like larger dungeons.");
		
		
		
		// API Stuff
		config.createCategory("API", "Turns some API calls on or off");
		
		disableAPI = config.getBoolean("DisableApiCalls", "API", DISABLE_API, 
				"If true the API is disabled");
		Logging.logInfo("Will use API? " + !disableAPI);

		noMobChanges = config.getBoolean("DontAllowApiOnMobs", "API", NO_MOB_CHANGES, 
				"If false other mods that use the API can add mobs to the dungeons, if true they cannot.  ",
				"(This is good if hand designing custom themes for a mod pack.)");
		Logging.logInfo("Will allow API base mob change? " + !noMobChanges);
		
		
		// Debugging

		config.createCategory("Debugging", 
				"Things for debugging the mod; you should probably leave these all off, ",
				"some are cheats, so cause lots of lag.");
		
		failfast = config.getBoolean("FailfastLoot", "Debugging", false, 
				"If true this will cause the system to crash on loot config errors.",
				"This is good for debugging modpac.");
		
		profile = config.getBoolean("AutoProfilingOn", "Debugging", PROFILE, 
				"If true reports on dungeon planning and build times will be exproted to files and the commandline");
		Logging.logInfo("Will self-profile? " + profile);
		
		//Loot
		int a, b, c;
		boolean nerf;
		config.createCategory("Loot", 
				"This will change the value and quantity of loo; be careful, too ",
				"could cause a crash if there isn't enough room in the chest.  ",
				"The basic chest formula is for the whole cheset, the treasure ",
				"chest formula is per loot normal category -- you get 3 time that ",
				"many items.");
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
		nerf = config.getBoolean("NerfEpicLoot", "Loot", false, 
					"If true it will limit the amount of level 8 loot place with most spawners " 
							+ System.lineSeparator()
					+ "(anything less than a full boss); enable if want to have truly epic items " 
							+ System.lineSeparator()
					+ "like indestructable god picks) but don't want too many of them around.");
		TreasureChest.setBasicLootNumbers(a, b, c, nerf);
		Room.setLootBonus(config.getInt("Loot Bonus", "Loot", 1, -9, 9,
				"This modifies the value of the loot, in case you think default is "
					+ System.lineSeparator() + "too generous or too stingy."));
		
		// Saving it all
		//openThemesDir(); //TODO/FIXME
		config.save();
	}
	

	
	/**
	 * Returns the full path of the config directory as a String.
	 * 
	 * @return
	 */
	public static String getConfigDir() {
		return configDir + File.separator;
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
		File chestDir = new File(configDir.toString() + File.separator + ThemeReader.CHESTS_DIR);
		if(!chestDir.exists()) {
			chestDir.mkdir();
		}
		exporter = new Externalizer(configDir.toString() + File.separator);
		exporter.makeChestCfg();
	}
	
}
