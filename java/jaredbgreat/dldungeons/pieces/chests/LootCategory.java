package jaredbgreat.dldungeons.pieces.chests;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import static jaredbgreat.dldungeons.pieces.chests.LootList.gear1;
import static jaredbgreat.dldungeons.pieces.chests.LootList.gear2;
import static jaredbgreat.dldungeons.pieces.chests.LootList.gear3;
import static jaredbgreat.dldungeons.pieces.chests.LootList.gear4;
import static jaredbgreat.dldungeons.pieces.chests.LootList.gear5;
import static jaredbgreat.dldungeons.pieces.chests.LootList.gear6;
import static jaredbgreat.dldungeons.pieces.chests.LootList.gear7;
import static jaredbgreat.dldungeons.pieces.chests.LootList.heal1;
import static jaredbgreat.dldungeons.pieces.chests.LootList.heal2;
import static jaredbgreat.dldungeons.pieces.chests.LootList.heal3;
import static jaredbgreat.dldungeons.pieces.chests.LootList.heal4;
import static jaredbgreat.dldungeons.pieces.chests.LootList.heal5;
import static jaredbgreat.dldungeons.pieces.chests.LootList.heal6;
import static jaredbgreat.dldungeons.pieces.chests.LootList.heal7;
import static jaredbgreat.dldungeons.pieces.chests.LootList.loot1;
import static jaredbgreat.dldungeons.pieces.chests.LootList.loot2;
import static jaredbgreat.dldungeons.pieces.chests.LootList.loot3;
import static jaredbgreat.dldungeons.pieces.chests.LootList.loot4;
import static jaredbgreat.dldungeons.pieces.chests.LootList.loot5;
import static jaredbgreat.dldungeons.pieces.chests.LootList.loot6;
import static jaredbgreat.dldungeons.pieces.chests.LootList.loot7;
import static jaredbgreat.dldungeons.pieces.chests.LootType.GEAR;
import static jaredbgreat.dldungeons.pieces.chests.LootType.HEAL;
import static jaredbgreat.dldungeons.pieces.chests.LootType.LOOT;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

/**
 * A representation of all available loot by type and level.  This class
 * is actually primarily responsible for selecting specific items of
 * the requested type and level rather than for storage, though it 
 * does store arrays of LootList for use in the selection process.
 * 
 * @author Jared Blackburn
 *
 */
public class LootCategory {
	
	public static final int LEVELS = 7; 	
	public LootList[] levels = new LootList[LEVELS];
	
	
	public LootCategory(LootList[] loots) {
		levels = loots;
	};
	
	
	public static LootCategory gear = new LootCategory(new 
			LootList[]{gear1, gear2, gear3, gear4, gear5, gear6, gear7});
	
	
	public static LootCategory heal = new LootCategory(new 
			LootList[]{heal1, heal2, heal3, heal4, heal5, heal6, heal7});
	
	
	public static LootCategory loot = new LootCategory(new 
			LootList[]{loot1, loot2, loot3, loot4, loot5, loot6, loot7});
	
	
	/**
	 * Takes the loots type and level and returns an item stack of a random
	 * item fitting the type and level supplied.
	 * 
	 * @param type
	 * @param level
	 * @param random
	 * @return
	 */
	public static ItemStack getLoot(LootType type, int level, Random random) {
		if(level <= 6) {
			level = Math.min(6, (level + random.nextInt(2) - random.nextInt(2)));
		}
		if(level < 0) level = 0;
		switch(type) {
		case GEAR:
			if(level > 6) level = 6;
			if(random.nextBoolean()) {
				return getEnchantedGear(level, random);
			} else {
				return gear.levels[level].getLoot(random).getStack(random);
			}
		case HEAL:
			if(level > 6) level = 6;
			return heal.levels[level].getLoot(random).getStack(random);
		case LOOT:
			if(level > 6) {
				if(level > random.nextInt(100)) {
					return LootList.special.getLoot(random).getStack(random);					
				} else {
					level = 6;
				}
			}
			if(random.nextInt(10) == 0) {
				return getEnchantedBook(level, random);
			} else {
				return loot.levels[level].getLoot(random).getStack(random);
			}
		case RANDOM:
		default:
			switch(random.nextInt(3)) {
				case 0:
					return getLoot(GEAR, level, random);
				case 1:
					return getLoot(HEAL, level, random);
				case 2:
				default:	
					return getLoot(LOOT, level, random);
			}		
		}
	}
	
	
	/**
	 * Returns an item stack from the gear list with some of the items value
	 * (in terms of loot level) possibly converted to random enchantments and
	 * the remained used as the loot level of the item itself.
	 * 
	 * @param lootLevel
	 * @param random
	 * @return
	 */
	private static ItemStack getEnchantedGear(int lootLevel, Random random) {
		ItemStack out = null;
		float portion = random.nextFloat() / 2f;
		int lootPart = Math.min(6, Math.max(0, (int)((((float)lootLevel) * (1f - portion)) + 0.5f)));
		int enchPart = Math.min(30, Math.max(0, (int)(((float)lootLevel) * portion * 10f)));
		LootItem item = gear.levels[lootPart].getLoot(random);
		if(enchPart >= 1 && isEnchantable(item)) {
			out = item.getStack(random);
			out = EnchantmentHelper.addRandomEnchantment(random, out, enchPart, true);
		} else {
			out = gear.levels[lootLevel].getLoot(random).getStack(random);
		}		
		return out;
	}
	
	
	/**
	 * True if the item is in a category that should be considered
	 * for possible enchantment.
	 * 
	 * @param in
	 * @return
	 */
	private static boolean isEnchantable(LootItem in) {
		Item item = (Item)in.item;
		return (item instanceof ItemSword 
				|| item instanceof ItemTool 
				|| item instanceof ItemArmor
				|| item instanceof ItemBow);
	}
	
	
	/**
	 * Creates a random enchanted book and returns it as an ItemStack
	 * 
	 * @param level
	 * @param random
	 * @return
	 */
	private static ItemStack getEnchantedBook(int level, Random random) {
		ItemStack out = new ItemStack(Items.BOOK, 1);
		out = EnchantmentHelper.addRandomEnchantment(random, out, Math.min(30, (int)(level * 7.5)), true);
		return out;
	}	
}
