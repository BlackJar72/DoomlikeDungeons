package jaredbgreat.dldungeons.pieces.chests;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * Forge event code by Charles Howard, 2016.
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.DBlock;

import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * Represents a typical loot chest, including its coordinates and loot level.
 * 
 * @author Jared Blackburn
 *
 */
public class BasicChest {
	
	public int mx, my, mz, level;
	private static int A1 = 2, B1 = 1, C1 = 2;
	
	
	public BasicChest(int x, int y, int z, int level) {
		this.mx = x;
		this.my = y;
		this.mz = z;
		this.level = level;
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
	public void place(World world, int x, int y, int z, Random random) {
		BlockPos pos = new BlockPos(x, y, z);
		level += random.nextInt(2);
		if(level >= LootCategory.LEVELS) level = LootCategory.LEVELS - 1;
		if(world.getBlockState(pos).getBlock() != DBlock.chest) {
			System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at " 
									+ x + ", " + y + ", " + z + " (basic chest).");
			return;
		}
		TileEntityChest contents = (TileEntityChest)world.getTileEntity(pos);
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
		
		
		MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.AfterChestTileEntity(world, contents, which, x, y, z, random, level));
		
	}
	
	
	/**
	 * Fills the chest with loot of the specified kind (lootType).
	 * 
	 * @param chest
	 * @param kind
	 * @param random
	 */
	protected void fillChest(TileEntityChest chest, LootType kind, Random random) {	
		int num = random.nextInt(2 + (level)) + 2;
		for(int i = 0; i < num; i++) {
			ItemStack treasure = LootCategory.getLoot(kind, level, random);
			if(treasure != null) chest.setInventorySlotContents(random.nextInt(27), treasure);
		}
		if(random.nextBoolean()) {
			ItemStack treasure = LootCategory.getLoot(LootType.RANDOM, level, random);
			if(treasure != null) chest.setInventorySlotContents(random.nextInt(27), treasure);
		}
	}
	
	
	public static void setBasicLootNumbers(int a, int b, int c) {
		A1 = a;
		B1 = b;
		C1 = c;
	}
	
}
