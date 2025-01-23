package jaredbgreat.dldungeons.pieces.chests;


/*
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.fml.Logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

import static jaredbgreat.dldungeons.pieces.chests.LootListSet.LootListType.GL;
import static jaredbgreat.dldungeons.pieces.chests.LootListSet.LootListType.HL;
import static jaredbgreat.dldungeons.pieces.chests.LootType.*;
import static jaredbgreat.dldungeons.pieces.chests.LootListSet.LootListType.*;


/**
 * A representation of all available loot by type and level.  This class
 * is actually primarily responsible for selecting specific items of
 * the requested type and level rather than for storage, though it
 * does store arrays of LootList for use in the selection process.
 *
 * @author Jared Blackburn
 */
public class LootCategory {

    public static final LootCategory PLACEHOLDER;

    static {
        final LootListSet loot = new LootListSet();
        loot.addDefaultLoot();
        PLACEHOLDER = new LootCategory(loot, "PLACEHOLDER");
    }

    public final String name;

    static final ConcurrentHashMap<String, String> NBT_MAP = new ConcurrentHashMap<>();

    public static final int LEVELS = 7;
    private final LootListSet lists;

    public LootCategory(LootListSet listset, String name) {
        this.name = name;
        lists = listset;
    }


    public void printOut() {
        //System.out.println(lists);
    }


    public static void AddNBT(InputStream file, String name) {
        StringBuilder builder = new StringBuilder();
        try {
            final BufferedReader instream = new BufferedReader(new InputStreamReader(file));
            while(instream.ready()) {
                builder.append(instream.readLine());
            }
            NBT_MAP.put(name, builder.toString());
            instream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Takes the loots type and level and returns an item stack of a random
     * item fitting the type and level supplied.
     *
     * @param type
     * @param level
     * @param random
     * @return
     */
    public LootResult getLoot(LootType type, int level, RandomSource random) {
        if (level <= 6) {
            level = Math.min(6, (level + random.nextInt(2) - random.nextInt(2)));
        }
        if (level < 0) level = 0;
        switch (type) {
            case GEAR:
                if (random.nextBoolean()) {
                    return getEnchantedGear(level, random);
                } else {
                    int l = Math.min(6, level);
                    return enchantedLowerLevel(lists.getList(GL,Math.min(6, l)).getLoot(random), l, random);
                }
            case HEAL:
                int l = Math.min(6, level);
                return new LootResult(lists.getList(HL, Math.min(6, l)).getLoot(random).getStack(random), l);
            case LOOT:
                if (level > 6) {
                    if (/*level > random.nextInt(100)*/ true) {
                        return new LootResult(lists.special.getLoot(random).getStack(random), 7);
                    } else {
                        level = 6;
                    }
                }
                if (random.nextInt(10) == 0) {
                    return getEnchantedBook(level, random);
                } else {
                    return new LootResult(lists.getList(LL, level).getLoot(random).getStack(random), level);
                }
            case RANDOM:
            default:
                return switch (random.nextInt(3)) {
                    case 0 -> getLoot(GEAR, level, random);
                    case 1 -> getLoot(HEAL, level, random);
                    default -> getLoot(LOOT, level, random);
                };
        }
    }


    /**
     * Returns an item stack from the gear list with some of the items value
     * (in terms of loot level) possibly converted to random enchantments and
     * the remained used as the loot level of the item itself.
     *
     * @param lootLevel
     * @param random
     * @return
     */
    private LootResult getEnchantedGear(int lootLevel, RandomSource random) {
        ItemStack out;
        float portion = random.nextFloat() / 2f;
        int lootPart = Math.min(6, Math.max(0, (int) ((((float) lootLevel) * (1f - portion)) + 0.5f)));
        LootItem item = lists.getList(GL, lootPart).getLoot(random);
        int diff = lootLevel - item.level + 1;
        int enchPart = Math.min((5 + (diff * (diff + 1) / 2) * 5), diff * 10);
        out = item.getStack(random);
        if (enchPart >= 1 && isEnchantable(out)) {
            out = EnchantmentHelper.enchantItem(random, out, enchPart, random.nextBoolean());
        } else {
            return enchantedLowerLevel(lists.getList(GL, Math.min(6, lootLevel)).getLoot(random), lootLevel, random);
        }
        return new LootResult(out, Math.min(lootLevel, 6));
    }


    /**
     * Returns an item stack from the gear list with some of the items value
     * (in terms of loot level) possibly converted to random enchantments and
     * the remained used as the loot level of the item itself.
     *
     * @param random
     * @return
     */
    private LootResult enchantedLowerLevel(LootItem item, int level, RandomSource random) {
        ItemStack out;
        int diff = level - item.level;
        out = item.getStack(random);
        if (isEnchantable(out) && (diff > random.nextInt(2))) {
            int enchPart = Math.min((5 + (level * (level + 1) / 2) * 5), level * 10);
            out = EnchantmentHelper.enchantItem(random, out, enchPart, random.nextBoolean());
        }
        return new LootResult(out, level);
    }


    /**
     * True if the item is in a category that should be considered
     * for possible enchantment.
     *
     * @param in
     * @return
     */
    private boolean isEnchantable(ItemStack in) {
        Item item = (Item) in.getItem();
        return (item instanceof TieredItem
                || item instanceof ArmorItem
                || item instanceof BowItem
                || item.isEnchantable(in));
    }


    /**
     * Creates a random enchanted book and returns it as an ItemStack
     *
     * @param level
     * @param random
     * @return
     */
    private LootResult getEnchantedBook(int level, RandomSource random) {
        ItemStack out = new ItemStack(Items.BOOK, 1);
        out = EnchantmentHelper.enchantItem(random, out, Math.min(30, (int) (level * 7.5)), true);
        return new LootResult(out, Math.min(level, 6));
    }


    public LootListSet getLists() {
        return lists;
    }
}

