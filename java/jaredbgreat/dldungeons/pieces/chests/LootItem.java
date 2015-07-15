package jaredbgreat.dldungeons.pieces.chests;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.debug.Logging;
import jaredbgreat.dldungeons.builder.DBlock;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class LootItem {
	
	Object item;
	int min, max, meta = 0;
	boolean notBlock;
	
	
	public LootItem(String item, int min, int max) {
		metaParse(item);
		if(min > max) min = max;
		this.min = min;
		this.max = max;
		notBlock = true;
	}
	
	
	public LootItem(Item item, int min, int max) {
		this.item = item;
		if(min > max) min = max;
		this.min = min;
		this.max = max;
		notBlock = true;
	}
	
	
	public LootItem(Block item, int min, int max) {
		this.item = item;
		if(min > max) min = max;
		this.min = min;
		this.max = max;
		notBlock = false;
	}
	
	
	private void metaParse(String in) {
		StringTokenizer nums = new StringTokenizer(in, "({[:]})");
		try{
			item = Item.itemsList[Integer.parseInt(nums.nextToken())];
		} catch (NumberFormatException ex) {
			String modid = nums.nextToken();
			String name  = nums.nextToken();
			item = GameRegistry.findItem(modid, name);
		}
		if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
	}
	
	
	public ItemStack getStack(Random random) {
		ItemStack out;
		if(item == null) {
			System.err.println("[DLDUNGEONS]Loot item was null!");
			return null;
		}
		if(max == min) {
			if(notBlock) out = new ItemStack((Item)item, max);
			else out = new ItemStack((Block)item, max);
		}
		else {
			//System.out.println("[DLDUNGEONS] Adding " + item + " to chest.");
			if(notBlock) 
				out = new ItemStack((Item)item, random.nextInt(max - min) + min +1);
			else 
				out = new ItemStack((Block)item, random.nextInt(max - min) + min +1);
		}
		if(meta != 0) out.setItemDamage(meta);
		return out;
	}
	
	
	/*----------------------------------*/
	/*       Deafult Loots Below        */
	/*----------------------------------*/
	
	public static LootItem woodBlock    = new LootItem(Block.wood,        1, 1);
	
	public static LootItem stoneSword   = new LootItem(Item.swordStone,   1, 1);
	public static LootItem ironSword    = new LootItem(Item.swordIron,    1, 1);
	public static LootItem diamondSword = new LootItem(Item.swordDiamond, 1, 1);
	public static LootItem bow          = new LootItem(Item.bow,          1, 1);
	public static LootItem fewArrows    = new LootItem(Item.arrow,        4, 12);
	public static LootItem someArrows   = new LootItem(Item.arrow,        8, 16);
	public static LootItem manyArrows   = new LootItem(Item.arrow,       16, 48);

	public static LootItem fewTorches   = new LootItem(Block.torchWood,   4, 12);
	public static LootItem someToreches = new LootItem(Block.torchWood,  12, 16);
	public static LootItem manyTorches  = new LootItem(Block.torchWood,  16, 24);

	public static LootItem leatherHat   = new LootItem(Item.helmetLeather, 1, 1);
	public static LootItem goldHat      = new LootItem(Item.helmetGold,    1, 1);
	public static LootItem ironHat      = new LootItem(Item.helmetIron,    1, 1);
	public static LootItem diamondHat   = new LootItem(Item.helmetDiamond, 1, 1);
	public static LootItem leatherBoots = new LootItem(Item.bootsLeather,  1, 1);
	public static LootItem goldBoots    = new LootItem(Item.bootsGold,     1, 1);
	public static LootItem ironBoots    = new LootItem(Item.bootsIron,     1, 1);
	public static LootItem diamondBoots = new LootItem(Item.helmetDiamond, 1, 1);
	public static LootItem leatherPants = new LootItem(Item.legsLeather,   1, 1);
	public static LootItem goldPants    = new LootItem(Item.legsGold,      1, 1);
	public static LootItem ironPants    = new LootItem(Item.legsIron,      1, 1);
	public static LootItem diamondPants = new LootItem(Item.legsDiamond,   1, 1);
	public static LootItem leatherChest = new LootItem(Item.plateLeather,  1, 1);
	public static LootItem goldChest    = new LootItem(Item.plateGold,     1, 1);
	public static LootItem ironChest    = new LootItem(Item.plateIron,     1, 1);
	public static LootItem diamondChest = new LootItem(Item.plateDiamond,  1, 1);
	
	public static LootItem someBread    = new LootItem(Item.bread,          2,  4);
	public static LootItem moreBread    = new LootItem(Item.bread,          4,  8);
	public static LootItem someSteak    = new LootItem(Item.beefCooked,     2,  4);
	public static LootItem moreSteak    = new LootItem(Item.beefCooked,     4,  8);
	public static LootItem someChicken  = new LootItem(Item.chickenCooked,  2,  4);
	public static LootItem moreChicken  = new LootItem(Item.chickenCooked,  4,  8);
	public static LootItem someApples   = new LootItem(Item.appleRed,       1,  3);
	public static LootItem moreApples   = new LootItem(Item.appleRed,       2,  7);
	public static LootItem somePie      = new LootItem(Item.pumpkinPie,     1,  3);
	public static LootItem morePie      = new LootItem(Item.pumpkinPie,     2,  7);
	public static LootItem goldApple    = new LootItem(Item.appleGold,      1,  1);
	public static LootItem goldApples   = new LootItem(Item.appleGold,      1,  3);
	
	public static LootItem oneGold      = new LootItem(Item.ingotGold,      1, 1);
	public static LootItem someGold     = new LootItem(Item.ingotGold,      2, 5);
	public static LootItem moreGold     = new LootItem(Item.ingotGold,      3, 8);
	public static LootItem someIron     = new LootItem(Item.ingotGold,      1, 8);
	public static LootItem moreIron     = new LootItem(Item.ingotGold,      3, 12);	
	public static LootItem oneDiamond   = new LootItem(Item.diamond,        1, 1);
	public static LootItem diamonds     = new LootItem(Item.diamond,        1, 4);
	public static LootItem manyDiamonds = new LootItem(Item.diamond,        3, 9);	
	public static LootItem oneEmerald   = new LootItem(Item.emerald,        1, 1);
	public static LootItem emeralds     = new LootItem(Item.emerald,        1, 4);
	public static LootItem manyEmerald  = new LootItem(Item.emerald,        3, 7);
	
	public static LootItem saddle       = new LootItem(Item.saddle,            1, 1);
	public static LootItem ironBarding  = new LootItem(Item.horseArmorIron,    1, 1);	
	public static LootItem goldBarding  = new LootItem(Item.horseArmorGold,    1, 1);
	public static LootItem diamondBard  = new LootItem(Item.horseArmorDiamond, 1, 1);
	
	public static LootItem book         = new LootItem(Item.book,           1, 1);
	public static LootItem someBooks    = new LootItem(Item.book,           2, 5);
	public static LootItem moreBooks    = new LootItem(Item.book,           3, 8);
	public static LootItem nameTag      = new LootItem(Item.diamond,        1, 1);	
	public static LootItem enderpearl   = new LootItem(Item.enderPearl,     1, 2);	
	public static LootItem enderpearls  = new LootItem(Item.enderPearl,     1, 9);
	public static LootItem eyeOfEnder   = new LootItem(Item.eyeOfEnder,     1, 2);
	public static LootItem blazeRod     = new LootItem(Item.blazeRod,       1, 2);
	public static LootItem netherstar   = new LootItem(Item.netherStar,     1, 1);
	
	public static LootItem disc13       = new LootItem(Item.record13,      1, 1);
	public static LootItem discCat      = new LootItem(Item.recordCat,     1, 1);
	public static LootItem discBlocks   = new LootItem(Item.recordBlocks,  1, 1);
	public static LootItem discChirp    = new LootItem(Item.recordChirp,   1, 1);	
	public static LootItem discFar      = new LootItem(Item.recordFar,     1, 1);	
	public static LootItem discMall     = new LootItem(Item.recordMall,    1, 1);
	public static LootItem discMellohi  = new LootItem(Item.recordMellohi, 1, 1);
	public static LootItem discStrad    = new LootItem(Item.recordStrad,   1, 1);
	public static LootItem discWard     = new LootItem(Item.recordWard,    1, 1);
	public static LootItem disc11       = new LootItem(Item.record11,      1, 1);
	public static LootItem discWait     = new LootItem(Item.recordWait,    1, 1);
}
