package jaredbgreat.dldungeons.pieces.chests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import jaredbgreat.dldungeons.builder.DBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ChestTileEntity;
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
		if(world.getBlockState(pos) != DBlock.chest) {
			System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at " 
					+ x + ", " + y + ", " + z + " (treasure chest).");
			return;
		}
		ChestTileEntity contents = (ChestTileEntity)world.getTileEntity(pos);
		int num;
		num = random.nextInt(Math.max(2, A2 + (level / B2))) + C2;
		for(int i = 0; i < num; i++) {
			treasure = category.getLoot(LootType.HEAL, level, random);
			contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
			slot++;
		}
		num = random.nextInt(Math.max(2, A2 + (level / B2))) + C2;
		for(int i = 0; i < num; i++) {
			treasure = category.getLoot(LootType.GEAR, level, random);
			contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
			slot++;
		}
		num = random.nextInt(Math.max(2, A2 + (level / B2))) + C2;
		for(int i = 0; i < num; i++) {
			treasure = category.getLoot(LootType.LOOT, 
					level + 1 + random.nextInt(2), random);
			contents.setInventorySlotContents(slots.get(slot).intValue(), treasure);
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
	
	
	public void setWithBoss(boolean bossRoom) {
		withBoss = bossRoom;
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
