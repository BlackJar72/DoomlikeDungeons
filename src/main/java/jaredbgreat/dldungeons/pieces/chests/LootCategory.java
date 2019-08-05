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
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;

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
	public ItemStack getLoot(LootType type, int level, Random random) {
		if(level <= 6) {
			level = Math.min(6, (level + random.nextInt(2) - random.nextInt(2)));
		}
		if(level < 0) level = 0;
		switch(type) {
		case GEAR:
			if(random.nextBoolean()) {
				return getEnchantedGear(level, random);
			} else {
				return gear[Math.min(6, level)].getLoot(random).getStack(random);
			}
		case HEAL:
			return heal[Math.min(6, level)].getLoot(random).getStack(random);
		case LOOT:
			if(level > 6) {
				if(level > random.nextInt(100)) {
					return lists.special.getLoot(random).getStack(random);					
				} else {
					level = 6;
				}
			}
			if(random.nextInt(10) == 0) {
				return getEnchantedBook(level, random);
			} else {
				return loot[level].getLoot(random).getStack(random);
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
	private ItemStack getEnchantedGear(int lootLevel, Random random) {
		ItemStack out = null;
		float portion = random.nextFloat() / 2f;
		int lootPart = Math.min(6, Math.max(0, (int)((((float)lootLevel) * (1f - portion)) + 0.5f)));
		int enchPart = Math.min(30, Math.max(0, (int)(((float)lootLevel) * portion * 10f)));
		LootItem item = gear[lootPart].getLoot(random);
		if(enchPart >= 1 && isEnchantable(item)) {
			out = item.getStack(random);
			out = EnchantmentHelper.addRandomEnchantment(random, out, enchPart, true);
		} else {
			out = gear[Math.min(6, lootLevel)].getLoot(random).getStack(random);
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
	private boolean isEnchantable(LootItem in) {
		Item item = (Item)in.item;
		return (item instanceof SwordItem 
				|| item instanceof ToolItem 
				|| item instanceof ArmorItem
				|| item instanceof BowItem);
	}
	
	
	/**
	 * Creates a random enchanted book and returns it as an ItemStack
	 * 
	 * @param level
	 * @param random
	 * @return
	 */
	private ItemStack getEnchantedBook(int level, Random random) {
		ItemStack out = new ItemStack(Items.BOOK, 1);
		out = EnchantmentHelper.addRandomEnchantment(random, out, Math.min(30, (int)(level * 7.5)), true);
		return out;
	}
	
	
	public LootListSet getLists() {
		return lists;
	}
}

