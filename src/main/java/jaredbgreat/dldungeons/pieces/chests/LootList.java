package jaredbgreat.dldungeons.pieces.chests;


/*
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */


import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;


/**
 * A list of loot items for a particular type and level.  This class also
 * contains static LootList members to statically hold all the loot used by
 * the mod.
 *
 * @author Jared Blackburn
 */
public class LootList extends ArrayList<LootItem> {
    ArrayList<LootItem> dummy = new ArrayList<LootItem>();

    /**
     * Add the item, converting it to a LootItem.
     *
     * @param item
     * @param min
     * @param max
     * @param prob
     */
    public void add(Item item, int min, int max, int prob) {
        add(new LootItem(item, min, max));
    }


    /**
     * Adds the block, converting it to LootItem.
     *
     * @param item
     * @param min
     * @param max
     * @param prob
     */
    public void add(Block item, int min, int max, int prob) {
        add(new LootItem(item, min, max));
    }


    /**
     * Returns a random item (as a LootItem) stored in the list.
     *
     * @param random
     * @return
     */
    public LootItem getLoot(RandomSource random) {
        LootItem out;
        if (isEmpty()) {
            return null;
        }
        // Done with removal now to somewhat increase randomness
        if (dummy.isEmpty() || random.nextInt(size()) > dummy.size()) {
            dummy.clear();
            dummy.addAll(this);
        }
        int which = random.nextInt(dummy.size());
        out = dummy.get(which);
        dummy.remove(which);
        return out;
    }

}
