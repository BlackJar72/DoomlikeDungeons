package jaredbgreat.dldungeons.themes;


import java.io.*;
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
import jaredbgreat.dldungeons.util.parser.Tokenizer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * This is the file IO class for reading theme files.
 *
 * @author Jared Blackburn
 *
 */
public class ThemeReader {


    /**
     * Load block families.
     */
    public static void readBlockFamilies(InputStream file) {
        StringBuilder json;
        json = new StringBuilder();
        try {
            final BufferedReader instream= new BufferedReader(new InputStreamReader(file));
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


//    /**
//     * Attempts to open chest.cfg, and if successful will call readLoot
//     * to read it.
//     */
//    public static void openNBTConfig() {
//        BufferedReader instream = null;
//        File nbtconfig = new File(configDir.toString() + File.separator + "nbt.cfg");
//        if(nbtconfig.exists()) try {
//            instream = new BufferedReader(new
//                    FileReader(nbtconfig.toString()));
//            readNBT(instream);
//            if(instream != null) instream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } else {
//            Logging.logInfo("File nbt.cfg is missing; will fallabck on default loot");
//        }
//    }
//
//
//    /**
//     * This will read the nbt.cfg file and populate BNT registry from
//     * its data.
//     *
//     * @param instream
//     * @throws IOException
//     */
//    public static void readNBT(BufferedReader instream) throws IOException {
//        Logging.logInfo("Loading custom NBT tags (nbt.cfg)");
//        Tokenizer tokens = null;
//        String line = null;
//        while((line = instream.readLine()) != null) {
//            if(line.length() < 2) continue;
//            if(line.charAt(0) == '#') continue;
//            NBTHelper.parseNBTLine(line);
//        }
//    }


    /**
     * Attempts to open chest.cfg, and if successful will call readLoot
     * to read it.
     */
    public static void openLoot(InputStream file, String name) {
        System.out.println("DLD: Loading Chest File: " + name);
        LootCategory cat = LootHandler.getLootHandler().createCategory(name);
        try {
            final BufferedReader instream = new BufferedReader(new InputStreamReader(file));
            readLoot(instream, cat.getLists());
            instream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This will read the chests.cfg file and populate the loots list from
     * its data.
     *
     * @param instream
     * @throws IOException
     */
    public static void readLoot(final BufferedReader instream, final LootListSet loots) throws IOException {
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
                //FIXME??? Re-Add custom NBT if possible (then the below can be uncommented)
                /*while(tokens.hasMoreTokens()) {
                    loot.addNbt(tokens.nextToken());
                }
                loot.trimNbt();*/
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
    public static void readTheme(InputStream file, String name) {
        System.out.println("DLD: Loading Theme File: " + name);
        BufferedReader instream = null;
        try {
            instream = new BufferedReader(new InputStreamReader(file));
            parseTheme(instream, name);
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
            e.printStackTrace();
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
            } if(token.equalsIgnoreCase("chestsfile")) {
                theme.lootCat = tokens.nextToken();
                continue;
            } if(token.equalsIgnoreCase("type")) {
                theme.type = typeParser(tokens);
                for(ThemeType type : theme.type) {
                    type.addThemeToType(theme, type);
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
        Theme.themes.add(theme);
        System.out.println("**** DLD: Loaded Theme " + theme.name);
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
        String num;
        try {
            if(tokens.hasMoreTokens()) {
                num = tokens.nextToken();
                value = Integer.parseInt(num);
                if((value > 5) && (value < 224)) valid = true;
            }
        } catch(Exception e) {
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
        String num;
        try {
            if(tokens.hasMoreTokens()) {
                num = tokens.nextToken();
                value = Float.parseFloat(num);
            }
        } catch(Exception e) {
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
        String num;
        try {
            if(tokens.hasMoreTokens()) {
                num = tokens.nextToken().trim();
                value = Integer.parseInt(num);
            }
        } catch(Exception e) {
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
            biomes.add(ForgeRegistries.BIOMES.getValue(new ResourceLocation(tokens.nextToken())));
        }
        return biomes;
    }


    /**
     * This will convert tokens in string format to a set of ThemeFlags.
     *
     *
     SWAMPY,
     OCEANIC,
     HARD,
     EASY,
     NETHER;
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
            else if(name.equalsIgnoreCase("OCEANIC")) flags.add(ThemeFlags.OCEANIC);
            else if(name.equalsIgnoreCase("HARD")) flags.add(ThemeFlags.HARD);
            else if(name.equalsIgnoreCase("EASY")) flags.add(ThemeFlags.EASY);
            else if(name.equalsIgnoreCase("NETHER")) flags.add(ThemeFlags.NETHER);
        }
        return flags;
    }


}
