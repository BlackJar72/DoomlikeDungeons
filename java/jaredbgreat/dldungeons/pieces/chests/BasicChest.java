package jaredbgreat.dldungeons.pieces.chests;


/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	


import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.builder.DBlock;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public class BasicChest {
	
	public int mx, my, mz, level;
	
	
	public BasicChest(int x, int y, int z, int level) {
		this.mx = x;
		this.my = y;
		this.mz = z;
		this.level = level;
	}
	
		
	public void place(World world, int x, int y, int z, Random random) {
		level += random.nextInt(2);
		if(level >= LootCategory.LEVELS) level = LootCategory.LEVELS - 1;
		DBlock.placeChest(world, x, y, z);
		if(world.getBlock(x, y, z) != DBlock.chest) return;
		TileEntityChest contents = (TileEntityChest)world.getTileEntity(x, y, z);
		if(ConfigHandler.vanillaLoot && (!ConfigHandler.stingyLoot || random.nextBoolean())) 
			vanillaChest(contents, random);
		int which = random.nextInt(2);
		switch (which) {
		case 0:
			fillChest(contents, LootCategory.gear, random);
			break;
		case 1:
			fillChest(contents, LootCategory.heal, random);
			break;
		}
	}
	
	
	private void vanillaChest(TileEntityChest chest, Random random) {
		int which = random.nextInt(6);
		ChestGenHooks chinf;
		switch (which) {
		case 0: 
			chinf = ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST); 
			break;
		case 1:
			chinf = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST);
			break;
		case 2:
			chinf = ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST);
			break;
		case 3:
			chinf = ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR);
			break;
		case 4:
			chinf = ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH);
			break;
		case 5:
		default:
			chinf = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
			break;
		}		
        WeightedRandomChestContent.generateChestContents(random, 
        		chinf.getItems(random), chest, chinf.getCount(random));
	}
	
	
	protected void fillChest(TileEntityChest chest, LootCategory kind, Random random) {		
		int num;
		if(ConfigHandler.stingyLoot) num = random.nextInt(2 + (level / 2)) + 1;
		else num = random.nextInt(3 + (level / 2)) + 2;
		for(int i = 0; i < num; i++) {
			ItemStack treasure = kind.levels[level].getLoot(random).getStack(random);
			if(treasure != null) chest.setInventorySlotContents(random.nextInt(27), treasure);
		}
		if(!ConfigHandler.vanillaLoot) {
			ItemStack treasure = LootCategory.loot.levels[level].getLoot(random).getStack(random);
			if(treasure != null) chest.setInventorySlotContents(random.nextInt(27), treasure);
		}
	}
}
