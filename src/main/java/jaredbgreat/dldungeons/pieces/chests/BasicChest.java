package jaredbgreat.dldungeons.pieces.chests;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

/**
 * Represents a typical loot chest, including its coordinates and loot level.
 * 
 * @author Jared Blackburn
 *
 */
public class BasicChest {
	
	public final int mx, my, mz;
	protected int level;
	private static volatile int A1 = 2, B1 = 1, C1 = 2;
	protected final LootCategory category;
	
	
	public BasicChest(int x, int y, int z, int level, LootCategory category) {
		this.mx = x;
		this.my = y;
		this.mz = z;
		this.level = level;
		this.category = category;
	}
	
	
	/**
	 * This adds a tile entity to the chest, and then calls fillChest to fill it.
	 * The chest block is placed in the maps by Dungeon.addChestBlocks using 
	 * DBlock.placeChest.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param random
	 */
	public void place(WorldGenLevel world, int x, int y, int z, RandomSource random) {
		BlockPos pos = new BlockPos(x, y, z);
		level += random.nextInt(2);
		level = Math.min(6, Math.min(level, LootCategory.LEVELS - 1));
		if(world.getBlockState(pos).getBlock() != Blocks.CHEST) {
			System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at " 
									+ x + ", " + y + ", " + z + " (basic chest).");
			return;
		}
		ChestBlockEntity contents = (ChestBlockEntity) world.getBlockEntity(pos);
		int which = random.nextInt(3);
		switch (which) {
		case 0:
			fillChest(contents, LootType.HEAL, random);
			break;
		case 1:
			fillChest(contents, LootType.GEAR, random);
			break;
		case 2:
			fillChest(contents, LootType.RANDOM, random);
			break;
		}
	}
	
	
	/**
	 * Fills the chest with loot of the specified kind (lootType).
	 * 
	 * @param chest
	 * @param kind
	 * @param random
	 */
	protected void fillChest(ChestBlockEntity chest, LootType kind, RandomSource random) {
		int num = random.nextInt(Math.max(2, A1 + (level / B1))) + C1;
		for(int i = 0; i < num; i++) {
			ItemStack treasure = category.getLoot(kind, level, random).getLoot();
			if(treasure != null) chest.setItem(random.nextInt(27), treasure);
		}
	}
	
	
	public static void setBasicLootNumbers(int a, int b, int c) {
		A1 = a;
		B1 = b;
		C1 = c;
	}
}
