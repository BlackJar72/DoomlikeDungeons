package jaredbgreat.dldungeons.pieces.chests;

import net.minecraft.item.ItemStack;

public class LootResult {
	final ItemStack result;
	final int level;
	
	LootResult(ItemStack item, int l) {
		result = item;
		level = l;
	}
	
	
	public ItemStack getLoot() {
		return result;
	}
	
	
	public int getLevel() {
		return level;
	}
}
