package jaredbgreat.dldungeons.pieces.chests;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.builder.DBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public class TreasureChest extends BasicChest {
	
	static ArrayList<Integer> slots = new ArrayList();	
	int slot;
	
	public TreasureChest(int x, int y, int z, int level) {
		super(x, y, z, level);
	}
	
	@Override
	public void place(World world, int x, int y, int z, Random random) {
		Collections.shuffle(slots, random);
		slot = 0;
		level += random.nextInt(2);
		if(level >= LootCategory.LEVELS) level = LootCategory.LEVELS - 1;
		ItemStack treasure;
		DBlock.placeChest(world, x, y, z);
		if(world.getBlock(x, y, z) != DBlock.chest) return;
		TileEntityChest contents = (TileEntityChest)world.getTileEntity(x, y, z);
		if(ConfigHandler.vanillaLoot) vanillaChest(contents, random);
		int num;
		num = random.nextInt(2 + (level / 3)) + 2;
		for(int i = 0; i < num; i++) {
			treasure = LootCategory.heal.levels[level].getLoot(random).getStack(random);
			if(contents.getStackInSlot(slot) != null) slot++;
			contents.setInventorySlotContents(slots.get(slot++).intValue(), treasure);
			if(slot > 25) return;  // This should not happen, but better safe than sorry
		}
		num = random.nextInt(2 + (level / 3)) + 2;
		for(int i = 0; i < num; i++) {
			treasure = LootCategory.gear.levels[level].getLoot(random).getStack(random);
			if(contents.getStackInSlot(slot) != null) slot++;
			contents.setInventorySlotContents(slots.get(slot++).intValue(), treasure);
			if(slot > 25) return;  // This should not happen, but better safe than sorry
		}
		if(ConfigHandler.stingyLoot) num = random.nextInt(2 + (level / 3)) + 2;
		else num = random.nextInt(3 + (level / 2)) + 2;
		for(int i = 0; i < num; i++) {
			treasure = LootCategory.loot.levels[level].getLoot(random).getStack(random);
			if(contents.getStackInSlot(slot) != null) slot++;
			contents.setInventorySlotContents(slots.get(slot++).intValue(), treasure);
			if(slot > 25) return;  // This should not happen, but better safe than sorry
		}
		if(random.nextInt(8) < level) {
			if(level >= 7) treasure = LootList.special.getLoot(random).getStack(random);
			else treasure = LootList.discs.getLoot(random).getStack(random);
			if(contents.getStackInSlot(slot) != null) slot++;
			contents.setInventorySlotContents(slots.get(slot++).intValue(), treasure);
			if(slot > 25) return;  // This should not happen, but better safe than sorry
		}
	}
	
	
	private void vanillaChest(TileEntityChest chest, Random random) {
		int which = random.nextInt(4);
		ChestGenHooks chinf;
		switch (which) {
		case 0:
			chinf = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST);
			break;
		case 1:
			chinf = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST);
			break;
		default:
			chinf = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
			break;
		}		
        WeightedRandomChestContent.generateChestContents(random, 
        		chinf.getItems(random), chest, chinf.getCount(random));
	}
	
	
	public static void initSlots() {
		for(int i = 0; i < 27; i++) slots.add(new Integer(i));
	}
}
