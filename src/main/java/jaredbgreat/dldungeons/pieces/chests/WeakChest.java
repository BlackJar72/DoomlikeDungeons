package jaredbgreat.dldungeons.pieces.chests;

/*
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

/**
 * Represents a weak / junk chest found in rooms without spawners and containing
 * small amounts of starter materials.
 *
 * @author Jared Blackburn
 */
public class WeakChest extends BasicChest {


    public WeakChest(int x, int y, int z, LootCategory category) {
        super(x, y, z, 0, category);
    }

    @Override
    public void place(WorldGenLevel world, int x, int y, int z, RandomSource random) {
        BlockPos pos = new BlockPos(x, y, z);
        ChestBlockEntity contents = (ChestBlockEntity) world.getBlockEntity(pos);
        if (world.getBlockState(pos).getBlock() != Blocks.CHEST) {
            System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at "
                    + x + ", " + y + ", " + z + " (basic chest).");
            return;
        }
        if (random.nextBoolean()) {
            if (random.nextBoolean()) fillChest(contents, LootType.GEAR, random);
            else fillChest(contents, LootType.HEAL, random);
        } else {
            fillChest(contents, LootType.GEAR, random);
            fillChest(contents, LootType.HEAL, random);
        }
    }

}
