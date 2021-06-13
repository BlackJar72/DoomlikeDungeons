package jaredbgreat.dldungeons.themes;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.builder.BlockFamily;
import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.chests.LootCategory;
import jaredbgreat.dldungeons.pieces.chests.LootHandler;
import jaredbgreat.dldungeons.pieces.chests.LootItem;
import jaredbgreat.dldungeons.pieces.chests.LootListSet;
import jaredbgreat.dldungeons.pieces.chests.TreasureChest;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.util.debug.Logging;
import jaredbgreat.dldungeons.util.parser.Tokenizer;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * This is the file IO class for reading theme files.
 * 
 * @author Jared Blackburn
 *
 */
public class ThemeReader {
	
	private static File configDir;
	private static File themesDir;
	private static File chestDir;
	private static File blocksDir;
	public static final String CHESTS_DIR = "SpecialChests";
	public static final String BLOCKS_DIR = "BlockFamilies";
	private static ArrayList<File> files;
	
	private static final String ESTRING = "";
	
	
	/**
	 * Set the directory / folder in which themes are found.
	 * 
	 * @param dir
	 */
	public static void setThemesDir(File dir) {
		Logging.logInfo("themesdir is " + dir);
		themesDir = dir;
	}
	
	
	/**
	 * Set the directory / folder in which config files are store,
	 * or more accurately, when this class will look for chests.cfg 
	 * and which is the parent of the themes and list directories.
	 * 
	 * @param dir
	 */
	public static void setConfigDir(File dir) {
		Logging.logInfo("themesdir is " + dir);
		configDir = dir;
		
	}
	
	
	/**
	 * Get the path for the themes directory.
	 * 
	 * @return path to themes as String
	 */
	public static String getThemesDir() {
		return themesDir + File.separator;
	}
	
	
	/**
	 * Get the path for the mods config directory.
	 * 
	 * @return path to the config directory as a String
	 */
	public static String getConfigDir() {
		return configDir + File.separator;
	}
	
	
	/**
	 * This will find all the extra chest files in a directory and return the 
	 * number.  Technically this simply looks for any file ending in ".cfg" and 
	 * thus must be applied to the correct folder as this method assumes you 
	 * have passed in the folder for extra chests.
	 * 
	 * @return
	 */
	private static int findChestFiles(File dir) {
		int num = 0;
		files = new ArrayList<File>();
		String[] fileNames = dir.list();
		// If still empty somehow don't try to read files!
		if(fileNames.length < 1) return 0;
		for(String name : fileNames) {
			if(name.length() >= 5) {
				if(name.substring(name.length() - 4).equalsIgnoreCase(".cfg")) {
					files.add(new File(name));
					num++;
				}
			}
		}
		return num;
	}
	
	
	/**
	 * This will look into the themes folder and add theme files 
	 * to the list of files to read themes from.
	 * 
	 * Technically it will treat any file ending in ".cfg" and found 
	 * inside the theme's directory except for the supplied template 
	 * as theme, whether it holds valid theme data or not.
	 * 
	 * Themes are read as one per file, so no file can contain more 
	 * than one theme, nor can a theme be split between multiple files.
	 * 
	 * If the themes folder is absent of empty it will attempt to fill it 
	 * by calling exporter.makerThemes.
	 * 
	 * @return
	 */
	private static int findThemeFiles(File dir) {
		int num = 0;
		Externalizer exporter;
		files = new ArrayList<File>();
		String[] fileNames = dir.list();
		if(fileNames.length < 1) {
			// If the directory is empty, assume first run and fill it
			exporter = new Externalizer(dir.toString() + File.separator);
			exporter.makeThemes();
			fileNames = dir.list();
		}
		// If still empty somehow don't try to read files!
		if(fileNames.length < 1) return 0;
		for(String name : fileNames) {
			if(name.length() > 4) {
				if(name.equals("template.cfg")) continue;
				else if(name.substring(name.length() - 4).equalsIgnoreCase(".cfg")) {
					files.add(new File(name));
					num++;
				}
			}
		}
		return num;
	}
		

	private static int findBlockFiles(File dir) {
		int num = 0;
		Externalizer exporter;
		files = new ArrayList<File>();
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String[] fileNames = dir.list();		
		if(fileNames.length < 1) {
			// If the directory is empty, assume first run and fill it
			exporter = new Externalizer(dir.toString() + File.separator);
			exporter.makeBlocks();
			fileNames = dir.list();
		}
		// If still empty somehow don't try to read files!
		if(fileNames.length < 1) return 0;
		for(String name : fileNames) {
			if(name.length() > 5) {
				if(name.substring(name.length() - 5).equalsIgnoreCase(".json")) {
					files.add(new File(name));
					num++;
				}
			}
		}
		if(num > 0) {
			readBlockFamilies(blocksDir);
		}
		return num;
	}

	
	/**
	 * This will find and read all the theme files in the themes
	 * directory, and will then read the chests.cfg from the main 
	 * config directory.
	 * 
	 * findFiles is called to get the list of files to read, while 
	 * readTheme and openLoot are called to open the files.
	 * @param configd 
	 */
	public static void readThemes(String configd) {
		// Open loot first, so files are available
		configDir = new File(configd);
		TreasureChest.initSlots();
		openLoot("chests.cfg", true);
		chestDir = new File(configDir.toString() + File.separator + CHESTS_DIR);
		if(!chestDir.exists()) {
			chestDir.mkdir();
		}
		int num = findChestFiles(chestDir);
		Logging.logInfo("Found " + num + " special chest configs.");
		for(File file : files) openLoot(file.toString(), false);
		
		// Load block families
		blocksDir = new File(configDir.toString() + File.separator + BLOCKS_DIR);
		num = findBlockFiles(blocksDir);
		Logging.logInfo("Found " + num + " block family configs.");
		
		// Now the actual themes
		num = findThemeFiles(themesDir);
		Logging.logInfo("Found " + num + " themes.");
		for(File file : files) readTheme(file);
	}
	
	
	/**
	 * Load block familes.
	 * 
	 * @param dir folder where block familes are stored.
	 */
	private static void readBlockFamilies(File dir) {
		if(!(dir.exists() && dir.isDirectory() && dir.canRead())) {
			Logging.logInfo("WARNING: " + dir 
					+ " did not exist or was not a valid directory!");
			return;
		}
		StringBuilder json;
		BufferedReader instream = null;
		String[] fileNames = dir.list();
		for(String fn : fileNames) {
			if(fn.substring(fn.length() - 5).equalsIgnoreCase(".json")) {
				json = new StringBuilder();				
				fn = dir.toString() + File.separator + fn;				
				try {
					instream = new BufferedReader(new FileReader(fn));
					while(instream.ready()) {
						json.append(instream.readLine());
					}
					instream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}				
				RegisteredBlock.add(BlockFamily.makeBlockFamily(json.toString()));
			}
		}
	}
	
	
	/**
	 * This will read the nbt.cfg file and populate BNT registry from
	 * its data.
	 * 
	 * @param instream
	 * @throws IOException
	 */
	public static void readNBT(BufferedReader instream) throws IOException {
		Logging.logInfo("Loading custom NBT tags (nbt.cfg)");		
		Tokenizer tokens = null;
		String line = null;		
		while((line = instream.readLine()) != null) {
			if(line.length() < 2) continue;
			if(line.charAt(0) == '#') continue;
			//NBTHelper.parseNBTLine(line); // TODO?
		}
	}
	
	
	/**
	 * Attempts to open chest.cfg, and if successful will call readLoot 
	 * to read it.
	 */
	public static void openLoot(String name, boolean isMain) {
		LootCategory cat = LootHandler.getLootHandler().createCategory(name);
		BufferedReader instream = null;
		File chests;
		if(isMain) {
			chests = new File(configDir.toString() + File.separator + name);
		} else {
			chests = new File(configDir.toString() + File.separator 
							+ CHESTS_DIR + File.separator + name);
		}
		if(chests.exists()) try {
			instream = new BufferedReader(new 
					FileReader(chests.toString()));
			readLoot(instream, name, cat.getLists());
			if(instream != null) instream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} else {
			Logging.logInfo("File " + name + " is missing; will fallabck on default loot");
			cat.getLists().addDefaultLoot();
		}
	}
	
	
	/**
	 * This will read the chests.cfg file and populate the loots list from
	 * its data.
	 * 
	 * @param instream
	 * @throws IOException
	 */
	public static void readLoot(BufferedReader instream, String filename, LootListSet loots) throws IOException {
		Logging.logInfo("Loading chest loot file (" + filename + ")");
		
		Tokenizer tokens = null;
		String line = null;
		String item;
		String type;
		int level;
		String modid;
		String name;
		LootItem loot;
		int min;
		int max;
		
		while((line = instream.readLine()) != null) {
			if(line.length() < 2) continue;
			if(line.charAt(0) == '#') continue;
			tokens = new Tokenizer(line, " ,;:\t\n\r\f=");
			if(!tokens.hasMoreTokens()) continue;
			type = tokens.nextToken().toLowerCase();
			if(!tokens.hasMoreTokens()) continue;
			level = intParser(tokens);
			if(!tokens.hasMoreTokens() || (level == 0)) continue;
			modid = tokens.nextToken();
			if(modid.toLowerCase().equalsIgnoreCase("item") 
					|| modid.toLowerCase().equalsIgnoreCase("block"))
				modid = "minecraft";
			if(!tokens.hasMoreTokens()) continue;
			name = tokens.nextToken();
			if(!tokens.hasMoreTokens()) continue;
			min = intParser(tokens);
			if(!tokens.hasMoreTokens() || (min < 1)) continue;
			max = intParser(tokens);
			item = modid + ":" + name;
			loot = new LootItem(item, min, max, level);
			if(item != null && loot != null) {
				while(tokens.hasMoreTokens()) {
					//loot.addNbt(tokens.nextToken()); // TODO?
				}
				loot.trimNbt();
				loots.addItem(loot, type, level);
			}
		}
		loots.addDiscs();
	}			
		
	
	/**
	 * This will attempt to open a theme file, and if successful will call 
	 * parseTheme read the data. 
	 * 
	 * @param file
	 */
	private static void readTheme(File file) {
		BufferedReader instream = null;
		try {
			instream = new BufferedReader(new 
					FileReader(themesDir.toString() + File.separator + file.toString()));
			parseTheme(instream, file.toString());
			if(instream != null) instream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			if(instream != null) {
				try {
					instream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			Logging.logError("Theme " + file.toString() 
					+ " contained a fatal error!");
			e.printStackTrace();
			File broken = new File(themesDir.toString() 
					+ File.separator + file.toString());
			File errorFile;
			File errorDir = new File(themesDir.toString() 
					+ File.separator + "errors");
			if(!errorDir.exists()) {
				errorDir.mkdir();
			}
			int i = 0;
			do {
				errorFile = new File(errorDir.toString() + File.separator 
						+ file.toString() + ".err" + i++);
			} while (errorFile.exists());
                        if(!broken.renameTo(errorFile)) {
                            try {
                                Files.move(broken.toPath(), errorFile.toPath());
                            } catch (IOException ex) {
                                Logger.getLogger(ThemeReader.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }			
			if(errorFile.exists()) {
				Logging.logError("Theme " + errorFile
					+ " was renamed to prevent "
					+ " further reading attempts; please fix it.");
				 
			} else {
				Logging.logError("Theme " + errorFile
						+ " was NOT renamed to prevent "
						+ " further reading attempts (please fix it);"
						+ " something went wrong!");					 
				}
		}
	}
	
	
	/**
	 * This will read a themes data and convert it into a working theme in
	 * the running mod.
	 * 
	 * @param instream
	 * @param name
	 * @throws IOException
	 * @throws NoSuchElementException
	 */
	public static void parseTheme(BufferedReader instream, String name) 
			throws IOException, NoSuchElementException {
		//DoomlikeDungeons.profiler.startTask("Parsing theme " + name);		
		Theme theme = new Theme();
		theme.name = name;
		theme.version = 2; // Don't assume old version now; that version can't work since MC1.8
		Tokenizer tokens = null;
		String line = null;
		String token;
		String delimeters = " ,;\t\n\r\f="; // Don't assume old version now; that version can't work since MC1.8
		while((line = instream.readLine()) != null) {
			if(line.length() < 2) continue;
			if(line.charAt(0) == '#') continue;
			tokens = new Tokenizer(line, delimeters);
			if(!tokens.hasMoreTokens()) continue;
			token = tokens.nextToken().toLowerCase();
			if(token.equalsIgnoreCase("miny")) {
				theme.minY = intParser(theme.minY, tokens);
				continue;
			} if(token.equalsIgnoreCase("maxy")) {
				theme.maxY = intParser(theme.maxY, tokens);
				continue;
			} if(token.equalsIgnoreCase("buildfoundation")) {
				theme.buildFoundation = booleanParser(theme.buildFoundation, tokens);
				continue;
			} if(token.equalsIgnoreCase("sizes")) {
				theme.sizes = sizeParser(theme.sizes, tokens);
				continue;
			} if(token.equalsIgnoreCase("dimensionwhitelist")) {
				theme.dimensionWhitelist = dimensionParser(tokens);
				continue;
			} if(token.equalsIgnoreCase("outside")) {
				theme.outside = elementParser(theme.outside, tokens);
				continue;
			} if(token.equalsIgnoreCase("liquids")) {
				theme.liquids = elementParser(theme.liquids, tokens);
				continue;
			} if(token.equalsIgnoreCase("subrooms")) {
				theme.subrooms = elementParser(theme.subrooms, tokens);
				continue;
			} if(token.equalsIgnoreCase("islands")) {
				theme.islands = elementParser(theme.islands, tokens);
				continue;
			} if(token.equalsIgnoreCase("pillars")) {
				theme.pillars = elementParser(theme.pillars, tokens);
				continue;
			} if(token.equalsIgnoreCase("symmetry")) {
				theme.symmetry = elementParser(theme.symmetry, tokens);
				continue;
			} if(token.equalsIgnoreCase("variability")) {
				theme.variability = elementParser(theme.variability, tokens);
				continue;
			} if(token.equalsIgnoreCase("degeneracy")) {
				theme.degeneracy = elementParser(theme.degeneracy, tokens);
				continue;
			} if(token.equalsIgnoreCase("complexity")) {
				theme.complexity = elementParser(theme.complexity, tokens);
				continue;
			} if(token.equalsIgnoreCase("verticle")) {
				theme.verticle = elementParser(theme.verticle, tokens);
				continue;
			} if(token.equalsIgnoreCase("naturals")) {
				theme.naturals = elementParser(theme.naturals, tokens);
				continue;
			} if(token.equalsIgnoreCase("entrances")) {
				theme.entrances = elementParser(theme.entrances, tokens);
				continue;
			} if(token.equalsIgnoreCase("air")) {
				theme.air = blockParser(theme.air, tokens, theme.version);
				continue;
			} if(token.equalsIgnoreCase("walls")) {
				theme.walls = blockParser(theme.walls, tokens, theme.version);
				continue;
			} if(token.equalsIgnoreCase("caveblock")) {
				theme.caveWalls = blockParser(theme.caveWalls, tokens, theme.version);
				continue;
			} if(token.equalsIgnoreCase("floors")) {
				theme.floors = blockParser(theme.floors, tokens, theme.version);
				continue;
			} if(token.equalsIgnoreCase("ceilings")) {
				theme.ceilings = blockParser(theme.ceilings, tokens, theme.version);
				continue;
			} if(token.equalsIgnoreCase("fencing")) {
				theme.fencing = blockParser(theme.fencing, tokens, theme.version);
				continue;
			} if(token.equalsIgnoreCase("liquid")) {
				theme.liquid = blockParser(theme.liquid, tokens, theme.version);
				continue;
			} if(token.equalsIgnoreCase("pillarblock")) {
				theme.pillarBlock = blockParser(theme.pillarBlock, tokens, theme.version);
				continue;
			} if(token.equalsIgnoreCase("commonmobs")) {
				theme.commonMobs = parseMobs(theme.commonMobs, tokens);
				continue;
			} if(token.equalsIgnoreCase("hardmobs")) {
				theme.hardMobs = parseMobs(theme.hardMobs, tokens);
				continue;
			} if(token.equalsIgnoreCase("brutemobs")) {
				theme.bruteMobs = parseMobs(theme.bruteMobs, tokens);
				continue;
			} if(token.equalsIgnoreCase("elitemobs")) {
				theme.eliteMobs = parseMobs(theme.eliteMobs, tokens);
				continue;
			} if(token.equalsIgnoreCase("bossmobs")) {
				theme.bossMobs = parseMobs(theme.bossMobs, tokens);
				continue;
			} if(token.equalsIgnoreCase("biomes")) {
				theme.biomes = biomeParser(tokens);
				continue;
			} if(token.equalsIgnoreCase("biomewhitelist")) {
				theme.biomewl.addAll(specificBiomeParser(tokens));
				continue;
			} if(token.equalsIgnoreCase("biomeblacklist")) {
				theme.biomebl.addAll(specificBiomeParser(tokens));
				continue;
			} if(token.equalsIgnoreCase("chestsfile")) {
				theme.lootCat = tokens.nextToken();
				continue;
			} if(token.equalsIgnoreCase("type")) {
				theme.type = typeParser(tokens);
				for(ThemeType type : theme.type) {
					ThemeType.addThemeToType(theme, type);
				}
				if(theme.type.contains(ThemeType.WATER)) 
					theme.air = new int[]{RegisteredBlock.add("minecraft:water")};
				if(theme.type.contains(ThemeType.SWAMP)) theme.flags.add(ThemeFlags.SWAMPY);
				continue;
			} if(token.equalsIgnoreCase("flags")) {
				theme.flags = flagParser(tokens);
				continue;
			} if(token.equalsIgnoreCase("version")) {
				theme.version = (int)floatParser(theme.version, tokens);
			} 
		}
		if(theme.air.length < 1) {
			theme.air = new int[]{RegisteredBlock.add("minecraft:air")};
		}
		theme.fixMobs();
		theme.biomeRegister();
		if(theme.caveWalls.length < 1) {
			theme.caveWalls = theme.walls;
		}
	}


	/**
	 * Read a Degree Element tag's data.
	 * 
	 * @param el
	 * @param tokens
	 * @return 
	 */
	private static Element elementParser(Element el, Tokenizer tokens) {
		boolean valid = false;
		int[] values = new int[]{0, 0, 0, 0, 0, 0};
		String num;
		for(int i = 0; (i < values.length) && tokens.hasMoreTokens(); i++) {
			num = tokens.nextToken();
			values[i] = Integer.parseInt(num);
			if(values[i] < 0) values[i] = 0;
			if(values[i] > 0) valid = true;
		}
		if(valid) return new Element(values[0], values[1], values[2], values[3], values[4], values[5]);
		else return el;
	}
	
	
	/**
	 * Read a SizeElement tag's data.
	 * 
	 * @param el
	 * @param tokens
	 * @return
	 */
	private static SizeElement sizeParser(SizeElement el, Tokenizer tokens) {
		boolean valid = false;
		int[] values = new int[]{0, 0, 0, 0, 0};
		String num;
		for(int i = 0; (i < values.length) && tokens.hasMoreTokens(); i++) {
			num = tokens.nextToken();
			values[i] = Integer.parseInt(num);
			if(values[i] < 0) values[i] = 0;
			if(values[i] > 0) valid = true;
		}
		if(valid) return new SizeElement(values[0], values[1], values[2], values[3], values[4]);
		else return el;
	}

	/**
	 * Read a DimensionList tag's data.
	 * @param tokens Tokenizer
	 * @return Dimension id array
	 */
	private static int[] dimensionParser(Tokenizer tokens) {
		if (tokens.getToken(1) == null || tokens.getToken(1).equalsIgnoreCase("all"))
			return new int[0];
		int[] rtn = new int[tokens.countTokens()-1];
		int i = 0;
		while (tokens.hasMoreTokens())
			rtn[i++] = Integer.parseInt(tokens.nextToken());
		return rtn;
	}
	
	
	/**
	 * Read integer data, converting it from a String format to an int in 
	 * the range from 6 to 223; values outside this range will be treat as 
	 * el.  This is used for reading the minimum and maximum altitude values.
	 * 
	 * @param el
	 * @param tokens
	 * @return
	 */
	private static int intParser(int el, Tokenizer tokens) {
		boolean valid = false;
		int value = 0;
		String num = ESTRING;
		try {
			if(tokens.hasMoreTokens()) {
				num = tokens.nextToken();
				value = Integer.parseInt(num);
				if((value > 5) && (value < 224)) valid = true;
			}
		} catch(Exception e) {
			Logging.logError("ThemeReader.intParser(int el, Tokenizer tokens) tried to read non-number as integer");
			Logging.logError("Value passed as and integer was: " + num);
			e.printStackTrace();
			return el;
		}
		if(valid) return value;
		else return el;
	}
	
	
	/**
	 * Read floating point data, converting it from a String format to a float.
	 * 
	 * @param el
	 * @param tokens
	 * @return
	 */
	private static float floatParser(float el, Tokenizer tokens) {
		float value = 0f;
		String num = ESTRING;
		try {
			if(tokens.hasMoreTokens()) {
				num = tokens.nextToken();
				value = Float.parseFloat(num);
			}
		} catch(Exception e) {
			Logging.logError("ThemeReader.floatParser(float el, Tokenizer tokens) tried to read non-number as float");
			Logging.logError("Value passed as and foat was: " + num);
			return el;
		}
		return value;
	}
	
	
	/**
	 * This will read integer data, converting it from a String format 
	 * to an int.  This is the method that should be used for reading 
	 * general int data.  If the token passed in is not a valid integer 
	 * the method return -1.
	 * 
	 * @param tokens
	 * @return
	 */
	private static int intParser(Tokenizer tokens) {
		int value = 0;
		String num = ESTRING;
		try {
			if(tokens.hasMoreTokens()) {
				num = tokens.nextToken().trim();
				value = Integer.parseInt(num);
			}
		} catch(Exception e) {
			Logging.logError("ThemeReader.intParser(Tokenizer tokens) tried to read non-number as integer");
			Logging.logError("Value passed as and integer was: " + num);
			return -1;
		}
		return value;
	}
	
	
	/**
	 * This will parse boolean data, converting it from a string format 
	 * to a boolean. 
	 * 
	 * @param el
	 * @param tokens
	 * @return
	 */
	private static boolean booleanParser(boolean el, Tokenizer tokens) {
		boolean bool;
		if(tokens.hasMoreTokens()) {
			bool = Boolean.parseBoolean(tokens.nextToken());
		} else return el;
		return bool;
	}
	
	
	/**
	 * This will read in a block data and convert it from string format to an 
	 * of int's holding dungeon block id's (indices in DBlock.registry).  The 
	 * int's are then appended to the passed in int array "el"; this allows 
	 * multiple lines of data to be used for one block related component.
	 * 
	 * These blocks can then 
	 * 
	 * @param el
	 * @param tokens
	 * @param version
	 * @return
	 * @throws NoSuchElementException
	 */
	private static int[] blockParser(int[] el, 
			Tokenizer tokens, float version) throws NoSuchElementException {
		ArrayList<String> values = new ArrayList<String>();
		String nums;
		while(tokens.hasMoreTokens()) {
			nums = tokens.nextToken();
			values.add(String.valueOf(RegisteredBlock.add(nums)));
		}
		int[] out = new int[values.size() + el.length];
		for(int i = 0; i < el.length; i++) {
			out[i] = el[i];
		}
		for(int i = 0; i < values.size(); i++) {
			out[i + el.length] = Integer.parseInt(values.get(i));
		}
		return out;
	}
	
	
	/**
	 * This will turn tokens read from the them file to be added to the list 
	 * of mobs names to use for creating spawners.
	 * 
	 * @param el
	 * @param tokens
	 * @return
	 */
	private static ArrayList<String> parseMobs(ArrayList<String> el, Tokenizer tokens) {
		ArrayList<String> mobs;
		if(el != null) {
			mobs = el;
		} else {
			mobs = new ArrayList<String>();
		}
		while(tokens.hasMoreTokens()) {
			String nextMob = tokens.nextToken();
			mobs.add(nextMob);
		}
		return mobs;
	}
	
	
	/**
	 * This will convert tokens in string format to a set of biome types. 
	 * 
	 * @param tokens
	 * @return
	 */
	private static HashSet<Category> biomeParser(Tokenizer tokens) {
		String name;
		HashSet<Category> biomes = new HashSet<>();
		while(tokens.hasMoreTokens()) {		
			name = tokens.nextToken().toUpperCase();
			// Old biome types
			if(name.equalsIgnoreCase("ALL")) { 
				biomes.add(Category.NONE); 
				biomes.add(Category.TAIGA); 
				biomes.add(Category.EXTREME_HILLS); 
				biomes.add(Category.JUNGLE); 
				biomes.add(Category.MESA); 
				biomes.add(Category.PLAINS); 
				biomes.add(Category.SAVANNA); 
				biomes.add(Category.ICY); 
				biomes.add(Category.THEEND); 
				biomes.add(Category.BEACH); 
				biomes.add(Category.FOREST); 
				biomes.add(Category.OCEAN); 
				biomes.add(Category.DESERT); 
				biomes.add(Category.RIVER); 
				biomes.add(Category.SWAMP); 
				biomes.add(Category.MUSHROOM); 
				biomes.add(Category.NETHER);
			}
			else if(name.equalsIgnoreCase("NONE")) biomes.add(Category.NONE); 
			else if(name.equalsIgnoreCase("TAIGA)")) biomes.add(Category.TAIGA); 
			else if(name.equalsIgnoreCase("EXTREME_HILLS")) biomes.add(Category.EXTREME_HILLS); 
			else if(name.equalsIgnoreCase("JUNGLE")) biomes.add(Category.JUNGLE); 
			else if(name.equalsIgnoreCase("MESA")) biomes.add(Category.MESA); 
			else if(name.equalsIgnoreCase("PLAINS")) biomes.add(Category.PLAINS); 
			else if(name.equalsIgnoreCase("SAVANNA")) biomes.add(Category.SAVANNA); 
			else if(name.equalsIgnoreCase("ICY")) biomes.add(Category.ICY); 
			else if(name.equalsIgnoreCase("THEEND")) biomes.add(Category.THEEND); 
			else if(name.equalsIgnoreCase("BEACH")) biomes.add(Category.BEACH); 
			else if(name.equalsIgnoreCase("FOREST")) biomes.add(Category.FOREST); 
			else if(name.equalsIgnoreCase("OCEAN")) biomes.add(Category.OCEAN); 
			else if(name.equalsIgnoreCase("DESERT")) biomes.add(Category.DESERT); 
			else if(name.equalsIgnoreCase("RIVER")) biomes.add(Category.RIVER); 
			else if(name.equalsIgnoreCase("SWAMP")) biomes.add(Category.SWAMP); 
			else if(name.equalsIgnoreCase("MUSHROOM")) biomes.add(Category.MUSHROOM); 
			else if(name.equalsIgnoreCase("NETHER")) biomes.add(Category.NETHER);			
		}
		return biomes;
	}
	
	
	/**
	 * This will convert tokens in string format to a set of ThemeTypes.
	 * 
	 * @param tokens
	 * @return
	 */
	private static EnumSet<ThemeType> typeParser(Tokenizer tokens) {
		String name;
		EnumSet<ThemeType> types = EnumSet.noneOf(ThemeType.class);
		while(tokens.hasMoreTokens()) {
			name = tokens.nextToken().toUpperCase();
			if(name.equalsIgnoreCase("FOREST")) types.add(ThemeType.FOREST);
			else if(name.equalsIgnoreCase("PLAINS")) types.add(ThemeType.PLAINS);
			else if(name.equalsIgnoreCase("MOUNTAINS")) types.add(ThemeType.MOUNTAIN);
			else if(name.equalsIgnoreCase("SWAMP")) types.add(ThemeType.SWAMP);
			else if(name.equalsIgnoreCase("WATER")) types.add(ThemeType.WATER);
			else if(name.equalsIgnoreCase("DESERT")) types.add(ThemeType.DESERT);
			else if(name.equalsIgnoreCase("FROZEN")) types.add(ThemeType.FROZEN);
			else if(name.equalsIgnoreCase("JUNGLE")) types.add(ThemeType.JUNGLE);
			else if(name.equalsIgnoreCase("WASTELAND")) types.add(ThemeType.WASTELAND);
			else if(name.equalsIgnoreCase("NETHER")) types.add(ThemeType.NETHER);
			else if(name.equalsIgnoreCase("END")) types.add(ThemeType.END);
			else if(name.equalsIgnoreCase("MUSHROOM")) types.add(ThemeType.MUSHROOM);
			else if(name.equalsIgnoreCase("MAGICAL")) types.add(ThemeType.MAGICAL);
			else if(name.equalsIgnoreCase("DUNGEON")) types.add(ThemeType.DUNGEON);
			else if(name.equalsIgnoreCase("URBAN")) types.add(ThemeType.URBAN);
			else if(name.equalsIgnoreCase("NECRO")) types.add(ThemeType.NECRO);
			else if(name.equalsIgnoreCase("FIERY")) types.add(ThemeType.FIERY);
			else if(name.equalsIgnoreCase("SHADOW")) types.add(ThemeType.SHADOW);
			else if(name.equalsIgnoreCase("TECH")) types.add(ThemeType.TECH);
			else if(name.equalsIgnoreCase("PARADISE")) types.add(ThemeType.PARADISE);
		}
		return types;
	}
	
	/**
	 * This will parse a resource locations into Biomes and resturn 
	 * a Set of all listed Biomes.
	 * 
	 * @param tokens
	 * @return
	 */
	private static Set<Biome> specificBiomeParser(Tokenizer tokens) {
		Set<Biome> biomes = new HashSet<>();
		while(tokens.hasMoreTokens()) {
			biomes.add(ForgeRegistries.BIOMES.getValue(
					new ResourceLocation(tokens.nextToken())));
		}
		return biomes;
	}
	
	
	/**
	 * This will convert tokens in string format to a set of ThemeFlags.
	 * 
	 * @param tokens
	 * @return
	 */
	private static EnumSet<ThemeFlags> flagParser(Tokenizer tokens) {
		String name;
		EnumSet<ThemeFlags> flags = EnumSet.noneOf(ThemeFlags.class);
		while(tokens.hasMoreTokens()) {
			name = tokens.nextToken().toUpperCase();
			if(name.equalsIgnoreCase("SWAMPY")) flags.add(ThemeFlags.SWAMPY);
			else if(name.equalsIgnoreCase("WATER")) flags.add(ThemeFlags.WATER);
			else if(name.equalsIgnoreCase("SURFACE")) flags.add(ThemeFlags.SURFACE);
		}
		return flags;
	}
	
		
}
