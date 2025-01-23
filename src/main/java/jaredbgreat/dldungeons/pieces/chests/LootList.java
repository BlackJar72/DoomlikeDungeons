package jaredbgreat.dldungeons.pieces.chests;


/*
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */


import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * A list of loot items for a particular type and level.  This class also
 * contains static LootList members to statically hold all the loot used by
 * the mod.
 *
 * @author Jared Blackburn
 */
public class LootList extends CopyOnWriteArrayList<LootItem> {
    //final ArrayList<LootItem> dummy = new ArrayList<LootItem>();

    /**
     * Add the item, converting it to a LootItem.
     *
     * @param item
     * @param min
     * @param max
     */
    public void add(Item item, int min, int max) {
        add(new LootItem(item, min, max));
    }


    /**
     * Adds the block, converting it to LootItem.
     *
     * @param item
     * @param min
     * @param max
     */
    public void add(Block item, int min, int max) {
        add(new LootItem(item, min, max));
    }


    /**
     * Returns a random item (as a LootItem) stored in the list.
     *
     * @param random
     * @return
     */
    public LootItem getLoot(RandomSource random) {
        //assert !isEmpty() : "*** DLD: ERROR!  LootList is empty! ***";
        if (isEmpty()) {
            return null;
        }
        return get(random.nextInt(size()));
    }


    public String toString() {
        StringBuilder b = new StringBuilder(System.lineSeparator());
        for(LootItem item : this) {
            b.append(item).append(System.lineSeparator());
        }
        b.append(System.lineSeparator());
        return b.toString();
    }

}
