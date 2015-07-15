package jaredbgreat.dldungeons.themes;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeLists {

	
	static HashSet<Theme> forest    = new HashSet<Theme>();
	static HashSet<Theme> plains    = new HashSet<Theme>();
	static HashSet<Theme> mountain  = new HashSet<Theme>();
	static HashSet<Theme> hills     = new HashSet<Theme>();
	static HashSet<Theme> swamp     = new HashSet<Theme>();
	static HashSet<Theme> water     = new HashSet<Theme>();
	static HashSet<Theme> desert    = new HashSet<Theme>();
	static HashSet<Theme> frozen    = new HashSet<Theme>();
	static HashSet<Theme> jungle    = new HashSet<Theme>();
	static HashSet<Theme> wasteland = new HashSet<Theme>();
	static HashSet<Theme> nether    = new HashSet<Theme>();
	static HashSet<Theme> end       = new HashSet<Theme>();
	static HashSet<Theme> mushroom  = new HashSet<Theme>();
	static HashSet<Theme> magical   = new HashSet<Theme>();
	static HashSet<Theme> minigame  = new HashSet<Theme>();
 
	static HashSet<Theme> nforest    = new HashSet<Theme>();
	static HashSet<Theme> nplains    = new HashSet<Theme>();
	static HashSet<Theme> nmountain  = new HashSet<Theme>();
	static HashSet<Theme> nhills     = new HashSet<Theme>();
	static HashSet<Theme> nswamp     = new HashSet<Theme>();
	static HashSet<Theme> nwater     = new HashSet<Theme>();
	static HashSet<Theme> ndesert    = new HashSet<Theme>();
	static HashSet<Theme> nfrozen    = new HashSet<Theme>();
	static HashSet<Theme> njungle    = new HashSet<Theme>();
	static HashSet<Theme> nwasteland = new HashSet<Theme>();
	static HashSet<Theme> nnether    = new HashSet<Theme>();
	static HashSet<Theme> nend       = new HashSet<Theme>();
	static HashSet<Theme> nmushroom  = new HashSet<Theme>();
	static HashSet<Theme> nmagical   = new HashSet<Theme>();
	
    
    public static void registerTheme(Theme theme) {
		if(theme.biomes.contains(Type.END))       {
			end.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to end.");
		}
		if(theme.biomes.contains(Type.DESERT))    {
			desert.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to desert.");
		}
		if(theme.biomes.contains(Type.FOREST))    {
			forest.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to forest.");
		}
		if(theme.biomes.contains(Type.FROZEN))    {
			frozen.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to frozen.");
		}
		if(theme.biomes.contains(Type.HILLS))     {
			hills.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to hills.");
		}
		if(theme.biomes.contains(Type.JUNGLE))    {
			jungle.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to jungle.");
		}
		if(theme.biomes.contains(Type.MAGICAL))   {
			magical.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to magical.");
		}
		if(theme.biomes.contains(Type.MOUNTAIN))  {
			mountain.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to mountain.");
		}
		if(theme.biomes.contains(Type.MUSHROOM))  {
			mushroom.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to mushroom.");
		}
		if(theme.biomes.contains(Type.NETHER))    {
			nether.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to nether.");
		}
		if(theme.biomes.contains(Type.PLAINS))    {
			plains.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to plains.");
		}
		if(theme.biomes.contains(Type.SWAMP))     {
			swamp.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to swamp.");
		}
		if(theme.biomes.contains(Type.WASTELAND)) {
			wasteland.add(theme);
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to wasteland.");
		}
		if(theme.biomes.contains(Type.WATER) || theme.biomes.contains(Type.BEACH)) {
			water.add(theme);  
			System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to water.");  	
		}
    }
	
    
    public static void removeTheme(Theme theme) {
		if(theme.notIn.contains(Type.END))       {
			nend.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from end.");
		}
		if(theme.notIn.contains(Type.DESERT))    {
			ndesert.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from desert.");
		}
		if(theme.notIn.contains(Type.FOREST))    {
			nforest.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from forest.");
		}
		if(theme.notIn.contains(Type.FROZEN))    {
			nfrozen.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from frozen.");
		}
		if(theme.notIn.contains(Type.HILLS))     {
			nhills.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from hills.");
		}
		if(theme.notIn.contains(Type.JUNGLE))    {
			njungle.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from jungle.");
		}
		if(theme.notIn.contains(Type.MAGICAL))   {
			nmagical.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from magical.");
		}
		if(theme.notIn.contains(Type.MOUNTAIN))  {
			nmountain.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from mountain.");
		}
		if(theme.notIn.contains(Type.MUSHROOM))  {
			nmushroom.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from mushroom.");
		}
		if(theme.notIn.contains(Type.NETHER))    {
			nnether.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from nether.");
		}
		if(theme.notIn.contains(Type.PLAINS))    {
			nplains.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from plains.");
		}
		if(theme.notIn.contains(Type.SWAMP))     {
			nswamp.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from swamp.");
		}
		if(theme.notIn.contains(Type.WASTELAND)) {
			nwasteland.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from wasteland.");
		}
		if(theme.notIn.contains(Type.WATER) || theme.notIn.contains(Type.BEACH)) {
			nwater.add(theme);
			System.out.println("[DLDUNGEONS] Removing theme " + theme + " from water.");  	
		}
    }
    
    
    public static Theme getTheme(BiomeGenBase biome, Random random) {
		//System.out.println("[DLDUNGEONS] Dungeon biome is " + biome + ".");
    	HashSet<Theme> set = new HashSet<Theme>();
    	ArrayList<Theme> use = new ArrayList<Theme>();
    	set.clear();
    	use.clear();
		if(BiomeDictionary.isBiomeOfType(biome, Type.DESERT)) {
			set.addAll(desert);
			//System.out.println("[DLDUNGEONS] Adding them list for desert.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.END)) {
			set.addAll(end);
			//System.out.println("[DLDUNGEONS] Adding them list for desert.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.FOREST)) {
			set.addAll(forest);
			//System.out.println("[DLDUNGEONS] Adding them list for forest.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.FROZEN)) {
			set.addAll(frozen);
			//System.out.println("[DLDUNGEONS] Adding them list for frozen.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.HILLS)) {
			set.addAll(hills);
			//System.out.println("[DLDUNGEONS] Adding them list for hills.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE)) {
			set.addAll(jungle);
			//System.out.println("[DLDUNGEONS] Adding them list for jungle.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL)) {
			set.addAll(magical);
			//System.out.println("[DLDUNGEONS] Adding them list for magical.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MOUNTAIN)) {
			set.addAll(mountain);
			//System.out.println("[DLDUNGEONS] Adding them list for moutains.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MUSHROOM)) {
			set.addAll(mushroom);
			//System.out.println("[DLDUNGEONS] Adding them list for mushroom.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.NETHER)) {
			set.addAll(nether);
			//System.out.println("[DLDUNGEONS] Adding them list for nether.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.PLAINS)) {
			set.addAll(plains);
			//System.out.println("[DLDUNGEONS] Adding them list for plains.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SWAMP)) {
			set.addAll(swamp);
			//System.out.println("[DLDUNGEONS] Adding them list for swamp.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.WASTELAND)) {
			set.addAll(wasteland);
			//System.out.println("[DLDUNGEONS] Adding them list for wasteland.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.WATER) || 
				BiomeDictionary.isBiomeOfType(biome, Type.BEACH)) {
			set = (water);
			//System.out.println("[DLDUNGEONS] Adding them list for water.");
		}		

		if(BiomeDictionary.isBiomeOfType(biome, Type.DESERT)) {
			set.removeAll(ndesert);
			//System.out.println("[DLDUNGEONS] Adding them list for desert.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.END)) {
			set.removeAll(nend);
			//System.out.println("[DLDUNGEONS] Adding them list for desert.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.FOREST)) {
			set.removeAll(nforest);
			//System.out.println("[DLDUNGEONS] Adding them list for forest.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.FROZEN)) {
			set.removeAll(nfrozen);
			//System.out.println("[DLDUNGEONS] Adding them list for frozen.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.HILLS)) {
			set.removeAll(nhills);
			//System.out.println("[DLDUNGEONS] Adding them list for hills.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE)) {
			set.removeAll(njungle);
			//System.out.println("[DLDUNGEONS] Adding them list for jungle.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL)) {
			set.removeAll(nmagical);
			//System.out.println("[DLDUNGEONS] Adding them list for magical.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MOUNTAIN)) {
			set.removeAll(nmountain);
			//System.out.println("[DLDUNGEONS] Adding them list for moutains.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MUSHROOM)) {
			set.removeAll(nmushroom);
			//System.out.println("[DLDUNGEONS] Adding them list for mushroom.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.NETHER)) {
			set.removeAll(nnether);
			//System.out.println("[DLDUNGEONS] Adding them list for nether.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.PLAINS)) {
			set.removeAll(nplains);
			//System.out.println("[DLDUNGEONS] Adding them list for plains.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SWAMP)) {
			set.removeAll(nswamp);
			//System.out.println("[DLDUNGEONS] Adding them list for swamp.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.WASTELAND)) {
			set.removeAll(nwasteland);
			//System.out.println("[DLDUNGEONS] Adding them list for wasteland.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.WATER) || 
				BiomeDictionary.isBiomeOfType(biome, Type.BEACH)) {
			set.removeAll(nwater);
			//System.out.println("[DLDUNGEONS] Adding them list for water.");
		}
		
    	if(set.isEmpty()) {
    		return null;
    	} else {
    		use.addAll(set);
			System.out.println("[DLDUNGEONS] The following themes are avalable for " + biome.biomeName + ":");
    		for(Theme theme : use) {
    			System.out.println("[DLDUNGEONS] " + theme.name);
    		}
    		Theme theme = use.get((random.nextInt(use.size())));
    		System.out.println("[DLDUNGEONS] Selected and returning " + theme +".");
    		return theme;
    	}
    }
    
    
    public static void registerWithMinigame(Theme theme) {
    	minigame.add(theme);
    }
    
    
    public static void reset() {
    	forest    = new HashSet<Theme>();
    	plains    = new HashSet<Theme>();
    	mountain  = new HashSet<Theme>();
    	hills     = new HashSet<Theme>();
    	swamp     = new HashSet<Theme>();
    	water     = new HashSet<Theme>();
    	desert    = new HashSet<Theme>();
    	frozen    = new HashSet<Theme>();
    	jungle    = new HashSet<Theme>();
    	wasteland = new HashSet<Theme>();
    	nether    = new HashSet<Theme>();
    	end       = new HashSet<Theme>();
    	mushroom  = new HashSet<Theme>();
    	magical   = new HashSet<Theme>();
    	
    	nforest    = new HashSet<Theme>();
    	nplains    = new HashSet<Theme>();
    	nmountain  = new HashSet<Theme>();
    	nhills     = new HashSet<Theme>();
    	nswamp     = new HashSet<Theme>();
    	nwater     = new HashSet<Theme>();
    	ndesert    = new HashSet<Theme>();
    	nfrozen    = new HashSet<Theme>();
    	njungle    = new HashSet<Theme>();
    	nwasteland = new HashSet<Theme>();
    	nnether    = new HashSet<Theme>();
    	nend       = new HashSet<Theme>();
    	nmushroom  = new HashSet<Theme>();
    	nmagical   = new HashSet<Theme>();
    	ThemeReader.readThemes();
    }
	
}
