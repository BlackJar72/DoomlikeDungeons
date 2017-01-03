package jaredbgreat.dldungeons.themes;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.nbt.NBTHelper;
import jaredbgreat.dldungeons.parser.Tokenizer;
import jaredbgreat.dldungeons.pieces.chests.LootItem;
import jaredbgreat.dldungeons.pieces.chests.LootList;
import jaredbgreat.dldungeons.pieces.chests.TreasureChest;
import jaredbgreat.dldungeons.setup.Externalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraftforge.common.BiomeDictionary.Type;

/**
 * This is the file IO class for reading theme files.
 * 
 * @author Jared Blackburn
 *
 */
public class ThemeReader {
	
	private static File configDir;
	private static File themesDir;
	private static ArrayList<File> files;
	
	private static final String ESTRING = "";
	
	
	/**
	 * Set the directory / folder in which themes are found.
	 * 
	 * @param dir
	 */
	public static void setThemesDir(File dir) {
		System.out.println("[DLDUNGEONS] themesdir is " + dir);
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
		System.out.println("[DLDUNGEONS] themesdir is " + dir);
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
	private static int findFiles() {
		int num = 0;
		Externalizer exporter;
		files = new ArrayList<File>();
		String[] fileNames = themesDir.list();
		if(fileNames.length < 1) {
			// If the directory is empty, assume first run and fill it
			exporter = new Externalizer(themesDir.toString() + File.separator);
			exporter.makeThemes();
			fileNames = themesDir.list();
		}
		// If still empty somehow don't try to read files!
		if(fileNames.length < 1) return 0;
		for(String name : fileNames) {
			if(name.length() >= 5) {
				if(name.equals("template.cfg")) continue;
				else if(name.substring(name.length() - 4).equals(".cfg")) {
					files.add(new File(name));
					num++;
				}
			}
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
	 */
	public static void readThemes() {
		openNBTConfig();
		int num = findFiles();
		System.out.println("[DLDUNGEONS] Found " + num + " themes.");
		for(File file : files) readTheme(file);
		TreasureChest.initSlots();
		openLoot();		
	}
	
	
	/**
	 * Attempts to open chest.cfg, and if successful will call readLoot 
	 * to read it.
	 */
	public static void openNBTConfig() {
		BufferedReader instream = null;
		File nbtconfig = new File(configDir.toString() + File.separator + "nbt.cfg");
		if(nbtconfig.exists()) try {
			instream = new BufferedReader(new 
					FileReader(nbtconfig.toString()));
			readNBT(instream);
			if(instream != null) instream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} else {
			System.out.println("[DLDUNGEONS] File nbt.cfg is missing; will fallabck on default loot");
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
		System.out.println("[DLDUNGEONS] Loading custom NBT tags (nbt.cfg)");		
		Tokenizer tokens = null;
		String line = null;		
		while((line = instream.readLine()) != null) {
			if(line.length() < 2) continue;
			if(line.charAt(0) == '#') continue;
			NBTHelper.parseNBTLine(line);
		}
	}	
	
	
	/**
	 * Attempts to open chest.cfg, and if successful will call readLoot 
	 * to read it.
	 */
	public static void openLoot() {
		BufferedReader instream = null;
		File chests = new File(configDir.toString() + File.separator + "chests.cfg");
		if(chests.exists()) try {
			instream = new BufferedReader(new 
					FileReader(chests.toString()));
			readLoot(instream);
			if(instream != null) instream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} else {
			System.out.println("[DLDUNGEONS] File chests.cfg is missing; will fallabck on default loot");
			LootList.addDefaultLoot();
		}
	}
	
	
	/**
	 * This will read the chests.cfg file and populate the loots list from
	 * its data.
	 * 
	 * @param instream
	 * @throws IOException
	 */
	public static void readLoot(BufferedReader instream) throws IOException {
		System.out.println("[DLDUNGEONS] Loading chest loot file (chests.cfg)");
		
		Tokenizer tokens = null;
		String line = null;
		String token;
		int itemid;
		String item;
		Block block;
		
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
			if(modid.toLowerCase().equals("item") 
					|| modid.toLowerCase().equals("block")
					|| modid.toLowerCase().equals("minecraft"))
				modid = "minecraft";
			if(!tokens.hasMoreTokens()) continue;
			name = tokens.nextToken();
			if(!tokens.hasMoreTokens()) continue;
			min = intParser(tokens);
			if(!tokens.hasMoreTokens() || (min < 1)) continue;
			max = intParser(tokens);
			item = modid + ":" + name;
			loot = new LootItem(item, min, max);
			if(item != null && loot != null) {
				while(tokens.hasMoreTokens()) {
					loot.addNbt(tokens.nextToken());
				}
				loot.trimNbt();
				LootList.addItem(loot, type, level);
			}
		}
		LootList.addDiscs();
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
			System.err.println("[DLDUNGEONS] Theme " + file.toString() 
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
				System.err.println("[DLDUNGEONS] Theme " + errorFile
					+ " was renamed to prevent "
					+ " further reading attempts; please fix it.");
				 
			} else {
				System.err.println("[DLDUNGEONS] Theme " + errorFile
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
		theme.version = 1.0f; // Assume old version until a newer version number is detected
		Tokenizer tokens = null;
		String line = null;
		String token;
		String delimeters = " ,:;\t\n\r\f="; // Assume old version until a newer version number is detected
		while((line = instream.readLine()) != null) {
			if(line.length() < 2) continue;
			if(line.charAt(0) == '#') continue;
			tokens = new Tokenizer(line, delimeters);
			if(!tokens.hasMoreTokens()) continue;
			token = tokens.nextToken().toLowerCase();
			if(token.equals("miny")) {
				theme.minY = intParser(theme.minY, tokens);
				continue;
			} if(token.equals("maxy")) {
				theme.maxY = intParser(theme.maxY, tokens);
				continue;
			} if(token.equals("buildfoundation")) {
				theme.buildFoundation = booleanParser(theme.buildFoundation, tokens);
				continue;
			} if(token.equals("sizes")) {
				theme.sizes = sizeParser(theme.sizes, tokens);
				continue;
			} if(token.equals("outside")) {
				theme.outside = elementParser(theme.outside, tokens);
				continue;
			} if(token.equals("liquids")) {
				theme.liquids = elementParser(theme.liquids, tokens);
				continue;
			} if(token.equals("subrooms")) {
				theme.subrooms = elementParser(theme.subrooms, tokens);
				continue;
			} if(token.equals("islands")) {
				theme.islands = elementParser(theme.islands, tokens);
				continue;
			} if(token.equals("pillars")) {
				theme.pillars = elementParser(theme.pillars, tokens);
				continue;
			} if(token.equals("symmetry")) {
				theme.symmetry = elementParser(theme.symmetry, tokens);
				continue;
			} if(token.equals("variability")) {
				theme.variability = elementParser(theme.variability, tokens);
				continue;
			} if(token.equals("degeneracy")) {
				theme.degeneracy = elementParser(theme.degeneracy, tokens);
				continue;
			} if(token.equals("complexity")) {
				theme.complexity = elementParser(theme.complexity, tokens);
				continue;
			} if(token.equals("verticle")) {
				theme.verticle = elementParser(theme.verticle, tokens);
				continue;
			} if(token.equals("naturals")) {
				theme.naturals = elementParser(theme.naturals, tokens);
				continue;
			} if(token.equals("entrances")) {
				theme.entrances = elementParser(theme.entrances, tokens);
				continue;
			} if(token.equals("walls")) {
				theme.walls = blockParser(theme.walls, tokens, theme.version);
				continue;
			} if(token.equals("caveblock")) {
				theme.caveWalls = blockParser(theme.caveWalls, tokens, theme.version);
				continue;
			} if(token.equals("floors")) {
				theme.floors = blockParser(theme.floors, tokens, theme.version);
				continue;
			} if(token.equals("ceilings")) {
				theme.ceilings = blockParser(theme.ceilings, tokens, theme.version);
				continue;
			} if(token.equals("fencing")) {
				theme.fencing = blockParser(theme.fencing, tokens, theme.version);
				continue;
			} if(token.equals("liquid")) {
				theme.liquid = blockParser(theme.liquid, tokens, theme.version);
				continue;
			} if(token.equals("pillarblock")) {
				theme.pillarBlock = blockParser(theme.pillarBlock, tokens, theme.version);
				continue;
			} if(token.equals("commonmobs")) {
				theme.commonMobs = parseMobs(theme.commonMobs, tokens);
				continue;
			} if(token.equals("hardmobs")) {
				theme.hardMobs = parseMobs(theme.hardMobs, tokens);
				continue;
			} if(token.equals("brutemobs")) {
				theme.bruteMobs = parseMobs(theme.bruteMobs, tokens);
				continue;
			} if(token.equals("elitemobs")) {
				theme.eliteMobs = parseMobs(theme.eliteMobs, tokens);
				continue;
			} if(token.equals("bossmobs")) {
				theme.bossMobs = parseMobs(theme.bossMobs, tokens);
				continue;
			} if(token.equals("biomes")) {
				theme.biomes = biomeParser(tokens);
				continue;
			} if(token.equals("notinbiomes")) {
				theme.notIn = biomeParser(tokens);
				continue;
			} if(token.equals("type")) {
				theme.type = typeParser(tokens);
				for(ThemeType type : theme.type) {
					type.addThemeToType(theme, type);
				}
				if(!(theme.version > 1.4f)) {
					if(theme.type.contains(ThemeType.WATER)) theme.flags.add(ThemeFlags.WATER);
					if(theme.type.contains(ThemeType.SWAMP)) theme.flags.add(ThemeFlags.SWAMPY);
				}
				continue;
			} if(token.equals("flags")) {
				theme.flags = flagParser(tokens);
				continue;
			} if(token.equals("version")) {
				theme.version = floatParser(theme.version, tokens);
				if(theme.version > 1.6) {
					delimeters = " ,;\t\n\r\f=";
				} else {
					delimeters = " ,;:\t\n\r\f=";
				}
				continue;
			} 
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
			System.err.println("[DLDUNGEONS] ThemeReader.intParser(int el, Tokenizer tokens) tried to read non-number as integer");
			System.err.println("[DLDUNGEONS] Value passed as and integer was: " + num);
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
			System.err.println("[DLDUNGEONS] ThemeReader.floatParser(float el, Tokenizer tokens) tried to read non-number as float");
			System.err.println("[DLDUNGEONS] Value passed as and foat was: " + num);
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
			System.err.println("[DLDUNGEONS] ThemeReader.intParser(Tokenizer tokens) tried to read non-number as integer");
			System.err.println("[DLDUNGEONS] Value passed as and integer was: " + num);
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
		boolean valid = false;
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
			if(version > 1.6) {
				values.add(String.valueOf(DBlock.add(nums, version)));
			} else {
				values.add(String.valueOf(DBlock.add(nums)));
			}
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
	private static EnumSet<Type> biomeParser(Tokenizer tokens) {
		String name;
		EnumSet<Type> biomes = EnumSet.noneOf(Type.class);
		while(tokens.hasMoreTokens()) {		
			name = tokens.nextToken().toUpperCase();
			// Old biome types
			if(name.equals("FOREST")) biomes.add(Type.FOREST);
			else if(name.equals("PLAINS")) biomes.add(Type.PLAINS);
			else if(name.equals("MOUNTAINS")) biomes.add(Type.MOUNTAIN);
			else if(name.equals("HILLS")) biomes.add(Type.HILLS);
			else if(name.equals("SWAMP")) biomes.add(Type.SWAMP);
			else if(name.equals("WATER")) biomes.add(Type.WATER);
			else if(name.equals("JUNGLE")) biomes.add(Type.JUNGLE);
			else if(name.equals("WASTELAND")) biomes.add(Type.WASTELAND);
			else if(name.equals("BEACH")) biomes.add(Type.BEACH);
			else if(name.equals("NETHER")) biomes.add(Type.NETHER);
			else if(name.equals("END")) biomes.add(Type.END);
			else if(name.equals("MUSHROOM")) biomes.add(Type.MUSHROOM);
			else if(name.equals("MAGICAL")) biomes.add(Type.MAGICAL);
			// New biome types
			else if(name.equals("HOT")) biomes.add(Type.HOT);
			else if(name.equals("COLD")) biomes.add(Type.COLD);
			else if(name.equals("DENSE")) biomes.add(Type.DENSE);
			else if(name.equals("SPARSE")) biomes.add(Type.SPARSE);
			else if(name.equals("WET")) biomes.add(Type.WET);
			else if(name.equals("DRY")) biomes.add(Type.DRY);
			else if(name.equals("SAVANNA")) biomes.add(Type.SAVANNA);
			else if(name.equals("CONIFEROUS")) biomes.add(Type.CONIFEROUS);
			else if(name.equals("SPOOKY")) biomes.add(Type.SPOOKY);
			else if(name.equals("DEAD")) biomes.add(Type.DEAD);
			else if(name.equals("LUSH")) biomes.add(Type.LUSH);
			else if(name.equals("MESA")) biomes.add(Type.MESA);
			else if(name.equals("SANDY")) biomes.add(Type.SANDY);
			else if(name.equals("SNOWY")) biomes.add(Type.SNOWY);			
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
			if(name.equals("FOREST")) types.add(ThemeType.FOREST);
			else if(name.equals("PLAINS")) types.add(ThemeType.PLAINS);
			else if(name.equals("MOUNTAINS")) types.add(ThemeType.MOUNTAIN);
			else if(name.equals("SWAMP")) types.add(ThemeType.SWAMP);
			else if(name.equals("WATER")) types.add(ThemeType.WATER);
			else if(name.equals("DESERT")) types.add(ThemeType.DESERT);
			else if(name.equals("FROZEN")) types.add(ThemeType.FROZEN);
			else if(name.equals("JUNGLE")) types.add(ThemeType.JUNGLE);
			else if(name.equals("WASTELAND")) types.add(ThemeType.WASTELAND);
			else if(name.equals("NETHER")) types.add(ThemeType.NETHER);
			else if(name.equals("END")) types.add(ThemeType.END);
			else if(name.equals("MUSHROOM")) types.add(ThemeType.MUSHROOM);
			else if(name.equals("MAGICAL")) types.add(ThemeType.MAGICAL);
			else if(name.equals("DUNGEON")) types.add(ThemeType.DUNGEON);
			else if(name.equals("URBAN")) types.add(ThemeType.URBAN);
			else if(name.equals("NECRO")) types.add(ThemeType.NECRO);
			else if(name.equals("FIERY")) types.add(ThemeType.FIERY);
			else if(name.equals("SHADOW")) types.add(ThemeType.SHADOW);
			else if(name.equals("TECH")) types.add(ThemeType.TECH);
			else if(name.equals("PARADISE")) types.add(ThemeType.PARADISE);
		}
		return types;
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
			if(name.equals("SWAMPY")) flags.add(ThemeFlags.SWAMPY);
			else if(name.equals("WATER")) flags.add(ThemeFlags.WATER);
			else if(name.equals("SURFACE")) flags.add(ThemeFlags.SURFACE);
		}
		return flags;
	}
	
		
}
