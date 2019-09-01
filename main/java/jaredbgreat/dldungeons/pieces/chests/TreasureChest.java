package jaredbgreat.dldungeons.pieces.chests;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.builder.DBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This represents the treasure chests found in destination nodes
 * (a.k.a., "boss rooms").  Each should have some of all three loot
 * types, generally of a high level. 
 * 
 * @author Jared Blackburn
 *
 */
public class TreasureChest extends BasicChest {
	
	private static int A2 = 3, B2 = 2, C2 = 2;
	
	private boolean withBoss;
	static ArrayList<Integer> slots = new ArrayList();	
	int slot;
	
	public TreasureChest(int x, int y, int z, int level, LootCategory category) {
		super(x, y, z, level, category);
	}
	

	/**
	 * This will place some loot of every each type, being sure to use
	 *  a separate random slot for each item so that none are overwritten. 
	 */
	@Override
	public void place(World world, int x, int y, int z, Random random) {
		BlockPos pos = new BlockPos(x, y, z);
		Collections.shuffle(slots, random);
		slot = 0;
		level += random.nextInt(2);
		if(level >= LootCategory.LEVELS) level = LootCategory.LEVELS - 1;
		ItemStack treasure;
		if(world.getBlockState(pos).getBlock() != DBlock.chest) {
			System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at " 
					+ x + ", " + y + ", " + z + " (treasure chest).");
			return;
		}
		TileEntityChest contents = (TileEntityChest)world.getTileEntity(pos);
		int num;
		num = random.nextInt(Math.max(2, A2 + (level / B2))) + C2;
		for(int i = 0; i < num; i++) {
			treasure = category.getLoot(LootType.HEAL, level, random).getLoot();
			contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
			slot++;
		}
		num = random.nextInt(Math.max(2, A2 + (level / B2))) + C2;
		for(int i = 0; i < num; i++) {
			treasure = category.getLoot(LootType.GEAR, level, random).getLoot();
			contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
			slot++;
		}
		num = random.nextInt(Math.max(2, A2 + (level / B2))) + C2;
		for(int i = 0; i < num; i++) {
			LootResult lootResult = category.getLoot(LootType.LOOT, 
					level + 1 + random.nextInt(2), random);
			treasure = lootResult.getLoot();
			contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
			if((lootResult.getLevel() > 6) && !withBoss) {
				level--;
			}
			slot++;
		}
		if(random.nextInt(7) < level) {
			if(level >= 6) {
				treasure = category.getLists().special.getLoot(random).getStack(random);
				contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
				slot++;
				if(random.nextBoolean()) {
					treasure = category.getLists().discs.getLoot(random).getStack(random);
					contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
					slot++;
				}
			} else {
				treasure = category.getLists().discs.getLoot(random).getStack(random);
				contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
				slot++;
			}
		}
	}
	
	
	public TreasureChest setWithBoss(boolean bossRoom) {
		withBoss = bossRoom;
		return this;
	}
	
	
	/**
	 * Returns true if a the slot is a valid part of a chests inventory.
	 * 
	 * @param slot
	 * @return
	 */
	private boolean validSlot(int slot) {
		return ((slot >= 0) && (slot < 27));
	}
	
	
	/**
	 * Initializes the slots list, which is shuffled to randomize item location in
	 * the chest without the risk of over writing one item with the another.  Called
	 * nut LootList.addDefaultLoot to populate the list.
	 */
	public static void initSlots() {
		for(int i = 0; i < 27; i++) slots.add(new Integer(i));
	}
}
