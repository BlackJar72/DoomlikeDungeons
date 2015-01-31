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

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public class WeakChest extends BasicChest {
	
	
	public WeakChest(int x, int y, int z) {
		super(x, y, z, 0);		
	}
	
	@Override
	public void place(World world, int x, int y, int z, Random random) {
		DBlock.placeChest(world, x, y, z);
		if(world.getBlock(x, y, z) != DBlock.chest) return;
		TileEntityChest contents = (TileEntityChest)world.getTileEntity(x, y, z);
		if(ConfigHandler.vanillaLoot && (!ConfigHandler.stingyLoot) && random.nextBoolean()) {
			ChestGenHooks chinf = ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST);
	        WeightedRandomChestContent.generateChestContents(random, 
	        		chinf.getItems(random), contents, chinf.getCount(random));
		}
		if(ConfigHandler.stingyLoot) {
			if(random.nextBoolean()) fillChest(contents, LootCategory.gear, random);
			else fillChest(contents, LootCategory.heal, random);
		} else {
			fillChest(contents, LootCategory.gear, random);
			fillChest(contents, LootCategory.heal, random);
		}
	}

}
