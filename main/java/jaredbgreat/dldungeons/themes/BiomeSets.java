package jaredbgreat.dldungeons.themes;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;

/**
 * This handles the determination of which themes to use in 
 * each biome.
 * 
 * Included a HashSets for various Forge biome dictionary types, 
 * storing which dungeons should appear in each.  Additional sets 
 * are used to store with themes should never appear in each.
 * 
 * In a given biome the possible dungeon themes equate to the union 
 * of inclusion defining sets for types that describe it followed 
 * by the subtraction of the union of the sets of forbidden themes 
 * for those types.
 * 
 * @author Jared Blackburn
 *
 */
public class BiomeSets {
	
	/*
	 * FIXME?: Lots of set variables:
	 * 
	 * This should probably been handled with a collection, 
	 * most likely a HashMap between the sets and the enum 
	 * constants used (or else their string representation.
	 */
	
	static final HashMap<Category, ArrayList<Theme>> themeMap = new HashMap<>();
	// For old types
	static final ArrayList<Theme> forest    = new ArrayList<Theme>();
	static final ArrayList<Theme> plains    = new ArrayList<Theme>();
	static final ArrayList<Theme> hills     = new ArrayList<Theme>();
	static final ArrayList<Theme> swamp     = new ArrayList<Theme>();
	static final ArrayList<Theme> ocean     = new ArrayList<Theme>();
	static final ArrayList<Theme> desert    = new ArrayList<Theme>();
	static final ArrayList<Theme> frozen    = new ArrayList<Theme>();
	static final ArrayList<Theme> jungle    = new ArrayList<Theme>();
	static final ArrayList<Theme> mesa      = new ArrayList<Theme>();
	static final ArrayList<Theme> nether    = new ArrayList<Theme>();
	static final ArrayList<Theme> end       = new ArrayList<Theme>();
	static final ArrayList<Theme> mushroom  = new ArrayList<Theme>();
	static final ArrayList<Theme> taiga     = new ArrayList<Theme>();
	static final ArrayList<Theme> savanna   = new ArrayList<Theme>();
	static final ArrayList<Theme> beach     = new ArrayList<Theme>();
	static final ArrayList<Theme> river     = new ArrayList<Theme>();
	static final ArrayList<Theme> none      = new ArrayList<Theme>();
	
	
	/**
	 * This will look at a themes biome type data and add it 
	 * to the proper sets for both inclusion in those type.
	 * 
	 * @param theme
	 */
    public static void registerTheme(Theme theme) {
		if(theme.biomes.contains(Category.THEEND))       {
			end.add(theme);
		}
		themeMap.put(Category.THEEND, end);
		if(theme.biomes.contains(Category.FOREST))    {
			forest.add(theme);
		}
		themeMap.put(Category.FOREST, forest);
		if(theme.biomes.contains(Category.ICY))    {
			frozen.add(theme);
		}
		themeMap.put(Category.ICY, frozen);
		if(theme.biomes.contains(Category.EXTREME_HILLS))     {
			hills.add(theme);
		}
		themeMap.put(Category.EXTREME_HILLS, hills);
		if(theme.biomes.contains(Category.JUNGLE))    {
			jungle.add(theme);
		}
		themeMap.put(Category.JUNGLE, jungle);
		if(theme.biomes.contains(Category.NONE))   {
			none.add(theme);
		}
		themeMap.put(Category.NONE, none);
		if(theme.biomes.contains(Category.RIVER))  {
			river.add(theme);
		}
		themeMap.put(Category.RIVER, river);
		if(theme.biomes.contains(Category.MUSHROOM))  {
			mushroom.add(theme);
		}
		themeMap.put(Category.MUSHROOM, mushroom);
		if(theme.biomes.contains(Category.NETHER))    {
			nether.add(theme);
		}
		themeMap.put(Category.THEEND, nether);
		if(theme.biomes.contains(Category.PLAINS))    {
			plains.add(theme);
		}
		themeMap.put(Category.PLAINS, plains);
		if(theme.biomes.contains(Category.SWAMP))     {
			swamp.add(theme);
		}
		themeMap.put(Category.SWAMP, swamp);
		if(theme.biomes.contains(Category.TAIGA)) {
			taiga.add(theme);
		}
		themeMap.put(Category.TAIGA, taiga);
		if(theme.biomes.contains(Category.BEACH)) {
			beach.add(theme);  
		}
		themeMap.put(Category.BEACH, beach);
		if(theme.biomes.contains(Category.OCEAN)) {
			ocean.add(theme);  
		}
		themeMap.put(Category.OCEAN, ocean);
		if(theme.biomes.contains(Category.DESERT))     {
			desert.add(theme);
		}
		themeMap.put(Category.DESERT, desert);
		if(theme.biomes.contains(Category.MESA))     {
			mesa.add(theme);
		}
		themeMap.put(Category.MESA, mesa);
		if(theme.biomes.contains(Category.SAVANNA))     {
			savanna.add(theme);
		}
		themeMap.put(Category.SAVANNA, savanna);
    }
    
    
    /**
     * This will perform the set logic to determine which 
     * themes are available in the given biome then randomly 
     * select one from that set. 
     * 
     * @param biome
     * @param random
     * @return
     */
    public static Theme getTheme(Biome biome, Random random, int dimID) {
    	ArrayList<Theme> use = themeMap.get(biome.getBiomeCategory());		
    	if(use.isEmpty()) {
    		return null;
    	} else {
    		return use.get((random.nextInt(use.size())));
    	}
    }
	
}
