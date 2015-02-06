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

	// For old types
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
	//for new types
	static HashSet<Theme> hot        = new HashSet<Theme>();
	static HashSet<Theme> cold       = new HashSet<Theme>();
	static HashSet<Theme> sparse     = new HashSet<Theme>();
	static HashSet<Theme> dense      = new HashSet<Theme>();
	static HashSet<Theme> wet        = new HashSet<Theme>();
	static HashSet<Theme> dry        = new HashSet<Theme>();
	static HashSet<Theme> savanna    = new HashSet<Theme>();
	static HashSet<Theme> coniferous = new HashSet<Theme>();
	static HashSet<Theme> spooky     = new HashSet<Theme>();
	static HashSet<Theme> dead       = new HashSet<Theme>();
	static HashSet<Theme> lush       = new HashSet<Theme>();
	static HashSet<Theme> mesa       = new HashSet<Theme>();
	static HashSet<Theme> ocean     = new HashSet<Theme>();
	static HashSet<Theme> sandy      = new HashSet<Theme>();
	static HashSet<Theme> snowy      = frozen;
	//for hypothetical minigame (should if every be made)
	static HashSet<Theme> minigame  = new HashSet<Theme>();
	
	// NEGATIONS	
	// For old types
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
	//for new types
	static HashSet<Theme> nhot        = new HashSet<Theme>();
	static HashSet<Theme> ncold       = new HashSet<Theme>();
	static HashSet<Theme> nsparse     = new HashSet<Theme>();
	static HashSet<Theme> ndense      = new HashSet<Theme>();
	static HashSet<Theme> nwet        = new HashSet<Theme>();
	static HashSet<Theme> ndry        = new HashSet<Theme>();
	static HashSet<Theme> nsavanna    = new HashSet<Theme>();
	static HashSet<Theme> nconiferous = new HashSet<Theme>();
	static HashSet<Theme> nspooky     = new HashSet<Theme>();
	static HashSet<Theme> ndead       = new HashSet<Theme>();
	static HashSet<Theme> nlush       = new HashSet<Theme>();
	static HashSet<Theme> nmesa       = new HashSet<Theme>();
	static HashSet<Theme> nocean      = new HashSet<Theme>();
	static HashSet<Theme> nsandy      = new HashSet<Theme>();
	static HashSet<Theme> nsnowy      = nfrozen;
	
    
    public static void registerTheme(Theme theme) {
		if(theme.biomes.contains(Type.END))       {
			end.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to end.");
		}
		if(theme.biomes.contains(Type.DESERT))    {
			desert.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to desert.");
		}
		if(theme.biomes.contains(Type.FOREST))    {
			forest.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to forest.");
		}
		if(theme.biomes.contains(Type.FROZEN) || theme.biomes.contains(Type.SNOWY))    {
			frozen.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to frozen.");
		}
		if(theme.biomes.contains(Type.HILLS))     {
			hills.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to hills.");
		}
		if(theme.biomes.contains(Type.JUNGLE))    {
			jungle.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to jungle.");
		}
		if(theme.biomes.contains(Type.MAGICAL))   {
			magical.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to magical.");
		}
		if(theme.biomes.contains(Type.MOUNTAIN))  {
			mountain.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to mountain.");
		}
		if(theme.biomes.contains(Type.MUSHROOM))  {
			mushroom.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to mushroom.");
		}
		if(theme.biomes.contains(Type.NETHER))    {
			nether.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to nether.");
		}
		if(theme.biomes.contains(Type.PLAINS))    {
			plains.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to plains.");
		}
		if(theme.biomes.contains(Type.SWAMP))     {
			swamp.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to swamp.");
		}
		if(theme.biomes.contains(Type.WASTELAND)) {
			wasteland.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to wasteland.");
		}
		if(theme.biomes.contains(Type.WATER) || theme.biomes.contains(Type.BEACH)) {
			water.add(theme);  
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to water.");  	
		}
		if(theme.biomes.contains(Type.OCEAN)) {
			ocean.add(theme);  
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to water.");  	
		}
		if(theme.biomes.contains(Type.HOT))     {
			hot.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to hot.");
		}
		if(theme.biomes.contains(Type.COLD))     {
			cold.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to cold.");
		}
		if(theme.biomes.contains(Type.SPARSE))     {
			sparse.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to sparce.");
		}
		if(theme.biomes.contains(Type.DENSE))     {
			dense.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to dense.");
		}
		if(theme.biomes.contains(Type.WET))     {
			wet.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to wet.");
		}
		if(theme.biomes.contains(Type.DRY))     {
			dry.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to dry.");
		}
		if(theme.biomes.contains(Type.SAVANNA))     {
			savanna.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to savanna.");
		}
		if(theme.biomes.contains(Type.CONIFEROUS))     {
			coniferous.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to coniferous.");
		}
		if(theme.biomes.contains(Type.SPOOKY))     {
			spooky.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to spooky.");
		}
		if(theme.biomes.contains(Type.DEAD))     {
			dead.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to dead.");
		}
		if(theme.biomes.contains(Type.LUSH))     {
			lush.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to lush.");
		}
		if(theme.biomes.contains(Type.CONIFEROUS))     {
			coniferous.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to coniferous.");
		}
		if(theme.biomes.contains(Type.MESA))     {
			mesa.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to mesa.");
		}
		if(theme.biomes.contains(Type.SANDY))     {
			sandy.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to sandy.");
		}
    }
	
    
    public static void removeTheme(Theme theme) {
		if(theme.notIn.contains(Type.END))       {
			nend.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from end.");
		}
		if(theme.notIn.contains(Type.DESERT))    {
			ndesert.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from desert.");
		}
		if(theme.notIn.contains(Type.FOREST))    {
			nforest.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from forest.");
		}
		if(theme.notIn.contains(Type.FROZEN) || theme.notIn.contains(Type.SNOWY))    {
			nfrozen.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from frozen.");
		}
		if(theme.notIn.contains(Type.HILLS))     {
			nhills.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from hills.");
		}
		if(theme.notIn.contains(Type.JUNGLE))    {
			njungle.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from jungle.");
		}
		if(theme.notIn.contains(Type.MAGICAL))   {
			nmagical.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from magical.");
		}
		if(theme.notIn.contains(Type.MOUNTAIN))  {
			nmountain.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from mountain.");
		}
		if(theme.notIn.contains(Type.MUSHROOM))  {
			nmushroom.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from mushroom.");
		}
		if(theme.notIn.contains(Type.NETHER))    {
			nnether.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from nether.");
		}
		if(theme.notIn.contains(Type.PLAINS))    {
			nplains.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from plains.");
		}
		if(theme.notIn.contains(Type.SWAMP))     {
			nswamp.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from swamp.");
		}
		if(theme.notIn.contains(Type.WASTELAND)) {
			nwasteland.add(theme);
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from wasteland.");
		}
		if(theme.notIn.contains(Type.WATER) || theme.notIn.contains(Type.BEACH)) {
			nwater.add(theme);  
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from water.");  	
		}
		if(theme.notIn.contains(Type.OCEAN)) {
			nocean.add(theme);  
			//System.out.println("[DLDUNGEONS] Removing theme " + theme + " from water.");  	
		}
		if(theme.notIn.contains(Type.HOT))     {
			nhot.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to hot.");
		}
		if(theme.notIn.contains(Type.COLD))     {
			ncold.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to cold.");
		}
		if(theme.notIn.contains(Type.SPARSE))     {
			nsparse.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to sparce.");
		}
		if(theme.notIn.contains(Type.DENSE))     {
			ndense.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to dense.");
		}
		if(theme.notIn.contains(Type.WET))     {
			nwet.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to wet.");
		}
		if(theme.notIn.contains(Type.DRY))     {
			ndry.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to dry.");
		}
		if(theme.notIn.contains(Type.SAVANNA))     {
			nsavanna.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to savanna.");
		}
		if(theme.notIn.contains(Type.CONIFEROUS))     {
			nconiferous.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to coniferous.");
		}
		if(theme.notIn.contains(Type.SPOOKY))     {
			nspooky.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to spooky.");
		}
		if(theme.notIn.contains(Type.DEAD))     {
			ndead.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to dead.");
		}
		if(theme.notIn.contains(Type.LUSH))     {
			nlush.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to lush.");
		}
		if(theme.notIn.contains(Type.CONIFEROUS))     {
			nconiferous.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to coniferous.");
		}
		if(theme.notIn.contains(Type.MESA))     {
			nmesa.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to mesa.");
		}
		if(theme.notIn.contains(Type.SANDY))     {
			nsandy.add(theme);
			//System.out.println("[DLDUNGEONS] Assigning theme " + theme + " to sandy.");
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
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.END)) {
			set.addAll(end);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.FOREST)) {
			set.addAll(forest);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.FROZEN) // Probably not necessary...
				|| BiomeDictionary.isBiomeOfType(biome, Type.SNOWY)) { 
			set.addAll(frozen);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.HILLS)) {
			set.addAll(hills);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE)) {
			set.addAll(jungle);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL)) {
			set.addAll(magical);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MOUNTAIN)) {
			set.addAll(mountain);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MUSHROOM)) {
			set.addAll(mushroom);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.NETHER)) {
			set.addAll(nether);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.PLAINS)) {
			set.addAll(plains);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SWAMP)) {
			set.addAll(swamp);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.WASTELAND)) {
			set.addAll(wasteland);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.WATER) || 
				BiomeDictionary.isBiomeOfType(biome, Type.BEACH)) {
			set.addAll(water);
		}	
		if(BiomeDictionary.isBiomeOfType(biome, Type.HOT)) {
			set.addAll(hot);
		}	
		if(BiomeDictionary.isBiomeOfType(biome, Type.COLD)) {
			set.addAll(cold);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.DENSE)) {
			set.addAll(dense);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SPARSE)) {
			set.addAll(sparse);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.DRY)) {
			set.addAll(dry);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.WET)) {
			set.addAll(wet);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SAVANNA)) {
			set.addAll(savanna);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.CONIFEROUS)) {
			set.addAll(coniferous);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SPOOKY)) {
			set.addAll(spooky);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.DEAD)) {
			set.addAll(dead);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.LUSH)) {
			set.addAll(lush);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MESA)) {
			set.addAll(mesa);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SANDY)) {
			set.addAll(sandy);
		}
		
		
		// REMOVAL CODE BELOW
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
		if(BiomeDictionary.isBiomeOfType(biome, Type.OCEAN)) {
			set.removeAll(nocean);
			//System.out.println("[DLDUNGEONS] Adding them list for water.");
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.HOT)) {
			set.removeAll(nhot);
		}	
		if(BiomeDictionary.isBiomeOfType(biome, Type.COLD)) {
			set.removeAll(ncold);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.DENSE)) {
			set.removeAll(ndense);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SPARSE)) {
			set.removeAll(nsparse);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.DRY)) {
			set.removeAll(ndry);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.WET)) {
			set.removeAll(nwet);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SAVANNA)) {
			set.removeAll(nsavanna);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.CONIFEROUS)) {
			set.removeAll(nconiferous);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SPOOKY)) {
			set.removeAll(nspooky);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.DEAD)) {
			set.removeAll(ndead);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.LUSH)) {
			set.removeAll(nlush);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.MESA)) {
			set.removeAll(nmesa);
		}
		if(BiomeDictionary.isBiomeOfType(biome, Type.SANDY)) {
			set.removeAll(nsandy);
		}
		
    	if(set.isEmpty()) {
    		return null;
    	}
    	use.addAll(set);
    	Theme theme = use.get((random.nextInt(use.size())));
		System.out.println("[DLDUNGEONS] Selected and returning " + theme +".");
    	return theme;    	
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
    	hot        = new HashSet<Theme>();
    	cold       = new HashSet<Theme>();
    	sparse     = new HashSet<Theme>();
    	dense      = new HashSet<Theme>();
    	wet        = new HashSet<Theme>();
    	dry        = new HashSet<Theme>();
    	savanna    = new HashSet<Theme>();
    	coniferous = new HashSet<Theme>();
    	spooky     = new HashSet<Theme>();
    	dead       = new HashSet<Theme>();
    	lush       = new HashSet<Theme>();
    	mesa       = new HashSet<Theme>();
    	ocean     = new HashSet<Theme>();
    	sandy      = new HashSet<Theme>();
    	snowy      = frozen;
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
    	nhot        = new HashSet<Theme>();
    	ncold       = new HashSet<Theme>();
    	nsparse     = new HashSet<Theme>();
    	ndense      = new HashSet<Theme>();
    	nwet        = new HashSet<Theme>();
    	ndry        = new HashSet<Theme>();
    	nsavanna    = new HashSet<Theme>();
    	nconiferous = new HashSet<Theme>();
    	nspooky     = new HashSet<Theme>();
    	ndead       = new HashSet<Theme>();
    	nlush       = new HashSet<Theme>();
    	nmesa       = new HashSet<Theme>();
    	nocean      = new HashSet<Theme>();
    	nsandy      = new HashSet<Theme>();
    	nsnowy      = nfrozen;
    	ThemeReader.readThemes();
    }
	
}
