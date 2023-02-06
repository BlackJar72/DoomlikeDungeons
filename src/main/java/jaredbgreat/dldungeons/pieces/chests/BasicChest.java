package jaredbgreat.dldungeons.pieces.chests;

import java.util.Random;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.AbstractBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraftforge.common.MinecraftForge;

/**
 * Represents a typical loot chest, including its coordinates and loot level.
 * 
 * @author Jared Blackburn
 *
 */
public class BasicChest {
	
	public final int mx, my, mz;
	protected volatile int level;
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
	public void place(ISeedReader world, int x, int y, int z, Random random) {
		BlockPos pos = new BlockPos(x, y, z);
		level += random.nextInt(2);
		level = Math.min(6, Math.min(level, LootCategory.LEVELS - 1));
		if(world.getBlockState(pos).getBlock() != AbstractBlock.chest) {
			System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at " 
									+ x + ", " + y + ", " + z + " (basic chest).");
			return;
		}
		ChestTileEntity contents = (ChestTileEntity)world.getBlockEntity(pos);
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
		
		MinecraftForge.EVENT_BUS.post(new DLDEvent.AfterChestTileEntity(world, 
				contents, which, x, y, z, random, level));
		
	}
	
	
	/**
	 * Fills the chest with loot of the specified kind (lootType).
	 * 
	 * @param chest
	 * @param kind
	 * @param random
	 */
	protected void fillChest(ChestTileEntity chest, LootType kind, Random random) {		
		int num = random.nextInt(Math.max(2, A1 + (level / B1))) + C1;
		for(int i = 0; i < num; i++) {
			ItemStack treasure = category.getLoot(kind, level, random).getLoot();
			if(treasure != null) chest.setItem(random.nextInt(27), treasure);
		}
		if(!ConfigHandler.vanillaLoot) {
			ItemStack treasure = category.getLoot(LootType.LOOT, 
					Math.min(6, level), random).getLoot();
			if(treasure != null) chest.setItem(random.nextInt(27), treasure);
		}
	}
	
	
	public static void setBasicLootNumbers(int a, int b, int c) {
		A1 = a;
		B1 = b;
		C1 = c;
	}
}
