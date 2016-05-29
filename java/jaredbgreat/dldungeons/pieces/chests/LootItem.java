package jaredbgreat.dldungeons.pieces.chests;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		


import jaredbgreat.dldungeons.debug.Logging;

import java.util.Random;
import java.util.StringTokenizer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


/**
 * A class to represent entries in loot tables.
 * 
 * This stores an item for use as loot in terms of the Minecraft Item instance 
 * along with minimum and maximum quantities and the items damage value (or 
 * metadata in block terms). 
 * 
 * @author Jared Blackburn
 * 
 */
public class LootItem {
	
	Item item;
	int min, max, meta;
	
	
	/**
	 * Add the named item, where name is a string including the
	 * items name and an optional damage value.
	 * 
	 * @param id
	 * @param min
	 * @param max
	 */
	public LootItem(String id, int min, int max) {
		metaParse(id);
		if(min > max) min = max;
		this.min = min;
		this.max = max;
		if(item == null) {
			Logging.LogError("[DLDUNGEONS] ERROR! Item read as \"" + id 
					+ "\" was was not in registry (returned null).");
		}
	}
	
	
	/**
	 * Create a LootItem using Item.
	 * 
	 * @param item
	 * @param min
	 * @param max
	 */
	public LootItem(Item item, int min, int max) {
		this.item = item;
		if(min > max) min = max;
		this.min = min;
		this.max = max;
	}
	
	
	/**
	 * Create a LootItem usign a block
	 * 
	 * @param item
	 * @param min
	 * @param max
	 */
	public LootItem(Block item, int min, int max) {
		this.item = Item.getItemFromBlock(item);
		if(min > max) min = max;
		this.min = min;
		this.max = max;
	}
	
	
	/**
	 * Parses its input string to set the values for item and meta
	 * (damage value).
	 * 
	 * @param in
	 */
	private void metaParse(String in) {
		StringTokenizer nums = new StringTokenizer(in, "({[:]})");
		String modid = nums.nextToken();
		String name  = nums.nextToken();
		item = GameRegistry.findItem(modid, name);
		if(item == null) {
			Logging.LogError("[DLDUNGEONS] ERROR! Item read as \"" + in 
					+ "\" was was not in registry (returned null).");
		}
		if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
	}
	
	
	/**
	 * Returns a randomly sized ItemStack of the Item.
	 * 
	 * @param random
	 * @return
	 */
	public ItemStack getStack(Random random) {
		ItemStack out;
		if(max <= min) {
			if(item instanceof Item)  out = new ItemStack(item, max);
			else                      out = new ItemStack(item, max);
		}
		else {
			if(item instanceof Item) 
				out = new ItemStack(item, random.nextInt(max - min) + min +1);
			else 
				out = new ItemStack(item, random.nextInt(max - min) + min +1);
		}
		if(out.getItem() == null) {
			return null;
		}
		if(out.getHasSubtypes() && meta >= 0) {
			out.setItemDamage(meta);
		} else {
			out.setItemDamage(0);
		}
		return out;
	}
	
	
	/*----------------------------------*/
	/*       Default Loots Below        */
	/*----------------------------------*/
	// Is this really necessary anymore?
	
	public static LootItem stoneSword   
			= new LootItem(GameRegistry.findItem("minecraft", "stone_sword"),   1, 1);
	public static LootItem ironSword    
			= new LootItem(GameRegistry.findItem("minecraft", "iron_sword"),    1, 1);
	public static LootItem diamondSword 
			= new LootItem(GameRegistry.findItem("minecraft", "diamond_sword"), 1, 1);
	public static LootItem bow          
			= new LootItem(GameRegistry.findItem("minecraft", "bow"),          1, 1);
	public static LootItem fewArrows    
			= new LootItem(GameRegistry.findItem("minecraft", "arrow"),        4, 12);
	public static LootItem someArrows   
			= new LootItem(GameRegistry.findItem("minecraft", "arrow"),        8, 16);
	public static LootItem manyArrows   
			= new LootItem(GameRegistry.findItem("minecraft", "arrow"),       16, 48);

	public static LootItem fewTorches   
			= new LootItem((Block)Block.getBlockFromName("torch"),   4, 12);
	public static LootItem someToreches 
			= new LootItem((Block)Block.getBlockFromName("torch"),  12, 16);
	public static LootItem manyTorches  
			= new LootItem((Block)Block.getBlockFromName("torch"),  16, 24);

	public static LootItem leatherHat   
			= new LootItem(GameRegistry.findItem("minecraft", "leather_helmet"), 1, 1);
	public static LootItem goldHat      
			= new LootItem(GameRegistry.findItem("minecraft", "golden_helmet"),    1, 1);
	public static LootItem ironHat      
			= new LootItem(GameRegistry.findItem("minecraft", "iron_helmet"),    1, 1);
	public static LootItem diamondHat   
			= new LootItem(GameRegistry.findItem("minecraft", "diamond_helmet"), 1, 1);
	public static LootItem leatherBoots 
			= new LootItem(GameRegistry.findItem("minecraft", "leather_boots"),  1, 1);
	public static LootItem goldBoots    
			= new LootItem(GameRegistry.findItem("minecraft", "golden_boots"),     1, 1);
	public static LootItem ironBoots    
			= new LootItem(GameRegistry.findItem("minecraft", "iron_boots"),     1, 1);
	public static LootItem diamondBoots 
			= new LootItem(GameRegistry.findItem("minecraft", "diamond_boots"), 1, 1);
	public static LootItem leatherPants 
			= new LootItem(GameRegistry.findItem("minecraft", "leather_leggings"),   1, 1);
	public static LootItem goldPants    
			= new LootItem(GameRegistry.findItem("minecraft", "golden_leggings"),      1, 1);
	public static LootItem ironPants    
			= new LootItem(GameRegistry.findItem("minecraft", "iron_leggings"),      1, 1);
	public static LootItem diamondPants 
			= new LootItem(GameRegistry.findItem("minecraft", "diamond_leggings"),   1, 1);
	public static LootItem leatherChest 
			= new LootItem(GameRegistry.findItem("minecraft", "leather_chestplate"),  1, 1);
	public static LootItem goldChest    
			= new LootItem(GameRegistry.findItem("minecraft", "golden_chestplate"),     1, 1);
	public static LootItem ironChest    
			= new LootItem(GameRegistry.findItem("minecraft", "iron_chestplate"),     1, 1);
	public static LootItem diamondChest 
			= new LootItem(GameRegistry.findItem("minecraft", "diamond_chestplate"),  1, 1);
	
	public static LootItem someBread    
			= new LootItem(GameRegistry.findItem("minecraft", "bread"),          2,  4);
	public static LootItem moreBread    
			= new LootItem(GameRegistry.findItem("minecraft", "bread"),          4,  8);
	public static LootItem someSteak    
			= new LootItem(GameRegistry.findItem("minecraft", "cooked_beef"),     2,  4);
	public static LootItem moreSteak    
			= new LootItem(GameRegistry.findItem("minecraft", "cooked_beef"),     4,  8);
	public static LootItem someChicken  
			= new LootItem(GameRegistry.findItem("minecraft", "cooked_chicken"),  2,  4);
	public static LootItem moreChicken  
			= new LootItem(GameRegistry.findItem("minecraft", "cooked_chicken"),  4,  8);
	public static LootItem someApples   
			= new LootItem(GameRegistry.findItem("minecraft", "apple"),       1,  3);
	public static LootItem moreApples   
			= new LootItem(GameRegistry.findItem("minecraft", "apple"),       2,  7);
	public static LootItem somePie      
			= new LootItem(GameRegistry.findItem("minecraft", "pumpkin_pie"),     1,  3);
	public static LootItem morePie      
			= new LootItem(GameRegistry.findItem("minecraft", "pumpkin_pie"),     2,  7);
	public static LootItem goldApple    
			= new LootItem(GameRegistry.findItem("minecraft", "golden_apple"),      1,  1);
	public static LootItem goldApples   
			= new LootItem(GameRegistry.findItem("minecraft", "golden_apple"),      1,  3);
	
	public static LootItem oneGold      
			= new LootItem(GameRegistry.findItem("minecraft", "gold_ingot"),      1, 1);
	public static LootItem someGold     
			= new LootItem(GameRegistry.findItem("minecraft", "gold_ingot"),      2, 5);
	public static LootItem moreGold     
			= new LootItem(GameRegistry.findItem("minecraft", "gold_ingot"),      3, 8);
	public static LootItem someIron     
			= new LootItem(GameRegistry.findItem("minecraft", "iron_ingot"),      1, 8);
	public static LootItem moreIron     
			= new LootItem(GameRegistry.findItem("minecraft", "iron_ingot"),      3, 12);	
	public static LootItem oneDiamond   
			= new LootItem(GameRegistry.findItem("minecraft", "iron_ingot"),        1, 1);
	public static LootItem diamonds     
			= new LootItem(GameRegistry.findItem("minecraft", "diamond"),        1, 4);
	public static LootItem manyDiamonds 
			= new LootItem(GameRegistry.findItem("minecraft", "diamond"),        3, 9);	
	public static LootItem oneEmerald   
			= new LootItem(GameRegistry.findItem("minecraft", "emerald"),        1, 1);
	public static LootItem emeralds     
			= new LootItem(GameRegistry.findItem("minecraft", "emerald"),        1, 4);
	public static LootItem manyEmerald  
			= new LootItem(GameRegistry.findItem("minecraft", "emerald"),        3, 7);
	
	public static LootItem saddle       
			= new LootItem(GameRegistry.findItem("minecraft", "saddle"),            1, 1);
	public static LootItem ironBarding  
			= new LootItem(GameRegistry.findItem("minecraft", "iron_horse_armor"),    1, 1);	
	public static LootItem goldBarding  
			= new LootItem(GameRegistry.findItem("minecraft", "golden_horse_armor"),    1, 1);
	public static LootItem diamondBard  
			= new LootItem(GameRegistry.findItem("minecraft", "diamond_horse_armor"), 1, 1);
	
	public static LootItem book         
			= new LootItem(GameRegistry.findItem("minecraft", "book"),           1, 1);
	public static LootItem someBooks    
			= new LootItem(GameRegistry.findItem("minecraft", "book"),           2, 5);
	public static LootItem moreBooks    
			= new LootItem(GameRegistry.findItem("minecraft", "book"),           3, 8);
	public static LootItem nameTag      
			= new LootItem(GameRegistry.findItem("minecraft", "name_tag"),        1, 1);	
	public static LootItem enderpearl   
			= new LootItem(GameRegistry.findItem("minecraft", "ender_pearl"),     1, 2);	
	public static LootItem enderpearls  
			= new LootItem(GameRegistry.findItem("minecraft", "ender_pearl"),     1, 9);
	public static LootItem eyeOfEnder   
			= new LootItem(GameRegistry.findItem("minecraft", "ender_eye"),     1, 2);
	public static LootItem blazeRod     
			= new LootItem(GameRegistry.findItem("minecraft", "blaze_rod"),       1, 2);
	public static LootItem netherstar   
			= new LootItem(GameRegistry.findItem("minecraft", "nether_star"),     1, 1);
	
	public static LootItem disc13       
			= new LootItem(GameRegistry.findItem("minecraft", "record_13"),      1, 1);
	public static LootItem discCat      
			= new LootItem(GameRegistry.findItem("minecraft", "record_cat"),     1, 1);
	public static LootItem discBlocks   
			= new LootItem(GameRegistry.findItem("minecraft", "record_blocks"),  1, 1);
	public static LootItem discChirp    
			= new LootItem(GameRegistry.findItem("minecraft", "stone_chirp"),   1, 1);	
	public static LootItem discFar      
			= new LootItem(GameRegistry.findItem("minecraft", "record_far"),     1, 1);	
	public static LootItem discMall     
			= new LootItem(GameRegistry.findItem("minecraft", "record_mall"),    1, 1);
	public static LootItem discMellohi  
			= new LootItem(GameRegistry.findItem("minecraft", "record_mellohi"), 1, 1);
	public static LootItem discStrad    
			= new LootItem(GameRegistry.findItem("minecraft", "record_strad"),   1, 1);
	public static LootItem discWard     
			= new LootItem(GameRegistry.findItem("minecraft", "record_ward"),    1, 1);
	public static LootItem disc11       
			= new LootItem(GameRegistry.findItem("minecraft", "record_11"),      1, 1);
	public static LootItem discWait     
			= new LootItem(GameRegistry.findItem("minecraft", "record_wait"),    1, 1);
}
