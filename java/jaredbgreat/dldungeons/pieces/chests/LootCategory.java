package jaredbgreat.dldungeons.pieces.chests;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import static jaredbgreat.dldungeons.pieces.chests.LootType.GEAR;
import static jaredbgreat.dldungeons.pieces.chests.LootType.HEAL;
import static jaredbgreat.dldungeons.pieces.chests.LootType.LOOT;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
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
	private final LootListSet lists;
	public LootList[] gear;
	public LootList[] heal;
	public LootList[] loot;
	
	public LootCategory(LootListSet listset) {
		lists = listset;
		gear = new LootList[]{lists.gear1, lists.gear2, 
				lists.gear3, lists.gear4, lists.gear5, lists.gear6, lists.gear7};
		heal = new LootList[]{lists.heal1, lists.heal2, 
				lists.heal3, lists.heal4, lists.heal5, lists.heal6, lists.heal7};
		loot = new LootList[]{lists.loot1, lists.loot2, 
				lists.loot3, lists.loot4, lists.loot5, lists.loot6, lists.loot7};
	};
	
	
	
	
	/**
	 * Takes the loots type and level and returns an item stack of a random
	 * item fitting the type and level supplied.
	 * 
	 * @param type
	 * @param level
	 * @param random
	 * @return
	 */
	public LootResult getLoot(LootType type, int level, Random random) {
		if(level <= 6) {
			level = Math.min(6, (level + random.nextInt(2) - random.nextInt(2)));
		}
		if(level < 0) level = 0;
		switch(type) {
		case GEAR:
			if(random.nextBoolean()) {
				return getEnchantedGear(level, random);
			} else {
				int l = Math.min(6, level);
				return enchantedLowerLevel(gear[Math.min(6, l)].getLoot(random), l, random);
			}
		case HEAL:
			int l = Math.min(6, level);
			return new LootResult(heal[Math.min(6, l)].getLoot(random).getStack(random), l);
		case LOOT:
			if(level > 6) {
				if(level > random.nextInt(100)) {
					return new LootResult(lists.special.getLoot(random).getStack(random), 7);					
				} else {
					level = 6;
				}
			}
			if(random.nextInt(10) == 0) {
				return getEnchantedBook(level, random);
			} else {
				return new LootResult(loot[level].getLoot(random).getStack(random), level);
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
	private LootResult getEnchantedGear(int lootLevel, Random random) {
		ItemStack out;
		float portion = random.nextFloat() / 2f;
		int lootPart = Math.min(6, Math.max(0, (int)((((float)lootLevel) * (1f - portion)) + 0.5f)));
		int enchPart = Math.min(30, Math.max(0, (int)(((float)lootLevel) * portion * 10f)));
		LootItem item = gear[lootPart].getLoot(random);
		if(lootPart > item.level) {
			enchPart += ((lootPart - item.level) * 5) + 5;
		}
		if(enchPart >= 1 && isEnchantable(item)) {
			out = item.getStack(random);
			out = EnchantmentHelper.addRandomEnchantment(random, out, enchPart, true);
		} else {
			out = gear[Math.min(6, lootLevel)].getLoot(random).getStack(random);
		}		
		return new LootResult(out, Math.min(lootLevel, 6));
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
	private LootResult enchantedLowerLevel(LootItem item, int level, Random random) {
		ItemStack out;
		int diff = level - item.level;
		if(isEnchantable(item) && (diff > random.nextInt(2))) {
			int enchPart = ((level - item.level) * 5) + 5;
			out = item.getStack(random);
			out = EnchantmentHelper.addRandomEnchantment(random, out, enchPart, true);
		} else {
			out = item.getStack(random);
		}
		return new LootResult(out, level);
	}
	
	
	/**
	 * True if the item is in a category that should be considered
	 * for possible enchantment.
	 * 
	 * @param in
	 * @return
	 */
	private boolean isEnchantable(LootItem in) {
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
	private LootResult getEnchantedBook(int level, Random random) {
		ItemStack out = new ItemStack(Items.BOOK, 1);
		out = EnchantmentHelper.addRandomEnchantment(random, out, Math.min(30, (int)(level * 7.5)), true);
		return new LootResult(out, Math.min(level, 6));
	}
	
	
	public LootListSet getLists() {
		return lists;
	}
}

