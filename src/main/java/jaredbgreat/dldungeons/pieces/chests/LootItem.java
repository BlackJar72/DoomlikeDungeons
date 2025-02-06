package jaredbgreat.dldungeons.pieces.chests;


import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import java.util.NoSuchElementException;


/**
 * A class to represent entries in loot tables.
 * <p>
 * This stores an item for use as loot in terms of the Minecraft Item instance
 * along with minimum and maximum quantities and the items damage value (or
 * metadata in block terms).
 *
 * @author Jared Blackburn
 */
public class LootItem {

    Item item;
    int min, max, level;
    String nbtData;

    /**
     * Add the named item, where name is a string including the
     * items name and an optional damage value.
     *
     * @param id
     * @param min
     * @param max
     */
    public LootItem(String id, int min, int max, int level, String nbtKey) {
        this.item = getItem(id);
        if (min > max) min = max;
        this.min = min;
        this.max = max;
        this.level = level;
        this.nbtData = nbtKey;
        if (item == null) {
            String error = "[DLDUNGEONS] ERROR! Item read as \"" + id
                    + "\" was was not in registry (returned null).";
            throw new NoSuchElementException(error);
        }
    }


    public void addNBT(String nbtKey) {
        nbtData = nbtKey;
    }


    public Item getItem() { return item; }


    /**
     * Create a LootItem using Item.
     *
     * @param item
     * @param min
     * @param max
     */
    public LootItem(Item item, int min, int max) {
        this.item = item;
        if (min > max) min = max;
        this.min = min;
        this.max = max;
        // For these items, don't care about level
        this.level = 6;
    }


    public LootItem(Item item, int min, int max, String nbtData) {
        this.item = item;
        if (min > max) min = max;
        this.min = min;
        this.max = max;
        // For these items, don't care about level
        this.level = 6;
        this.nbtData = nbtData;
    }


    /**
     * Create a LootItem using a block
     *
     * @param item
     * @param min
     * @param max
     */
    public LootItem(Block item, int min, int max) {
        this.item = Item.byBlock(item);
        if (min > max) min = max;
        this.min = min;
        this.max = max;
        // For these items, don't care about level
        this.level = 6;
    }


    private static Item getItem(String in) {
        return BuiltInRegistries.ITEM.get(new ResourceLocation(in));
    }


    /**
     * Returns a randomly sized ItemStack of the Item.
     *
     * @param random
     * @return
     */
    public ItemStack getStack(RandomSource random) {
        ItemStack out;
        if (max <= min) {
            out = new ItemStack(item, max);
        } else {
            out = new ItemStack(item, random.nextInt(max - min) + min + 1);
        }
        if (out.getItem() == null) {
            return null;
        }
        if((nbtData != null) && !nbtData.isEmpty() && LootCategory.NBT_MAP.containsKey(nbtData)) {
            try {
                out.setTag(TagParser.parseTag(LootCategory.NBT_MAP.get(nbtData)));
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return out;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[").append(item.toString()).append(", ").append(min).append(", ").append(max)
                .append(", ").append(level).append("; ").append(nbtData).append("]");
        return b.toString();
    }


    /*----------------------------------*/
    /*       Default Loots Below        */
    /*----------------------------------*/
    // Is this really necessary anymore?

    public static LootItem stoneSword
            = new LootItem(Items.STONE_SWORD, 1, 1);
    public static LootItem ironSword
            = new LootItem(Items.IRON_SWORD, 1, 1);
    public static LootItem diamondSword
            = new LootItem(Items.DIAMOND_SWORD, 1, 1);
    public static LootItem diamondPick
            = new LootItem(Items.DIAMOND_PICKAXE, 1, 1);
    public static LootItem bow
            = new LootItem(Items.BOW, 1, 1);
    public static LootItem fewArrows
            = new LootItem(Items.ARROW, 4, 12);
    public static LootItem someArrows
            = new LootItem(Items.ARROW, 8, 16);
    public static LootItem manyArrows
            = new LootItem(Items.ARROW, 16, 48);

    public static LootItem fewTorches
            = new LootItem(Items.TORCH, 4, 12);
    public static LootItem someToreches
            = new LootItem(Items.TORCH, 12, 16);
    public static LootItem manyTorches
            = new LootItem(Items.TORCH, 16, 24);

    public static LootItem leatherHat
            = new LootItem(Items.LEATHER_HELMET, 1, 1);
    public static LootItem goldHat
            = new LootItem(Items.GOLDEN_HELMET, 1, 1);
    public static LootItem ironHat
            = new LootItem(Items.IRON_HELMET, 1, 1);
    public static LootItem diamondHat
            = new LootItem(Items.DIAMOND_HELMET, 1, 1);
    public static LootItem leatherBoots
            = new LootItem(Items.LEATHER_BOOTS, 1, 1);
    public static LootItem goldBoots
            = new LootItem(Items.GOLDEN_BOOTS, 1, 1);
    public static LootItem ironBoots
            = new LootItem(Items.IRON_BOOTS, 1, 1);
    public static LootItem diamondBoots
            = new LootItem(Items.DIAMOND_BOOTS, 1, 1);
    public static LootItem leatherPants
            = new LootItem(Items.LEATHER_LEGGINGS, 1, 1);
    public static LootItem goldPants
            = new LootItem(Items.GOLDEN_LEGGINGS, 1, 1);
    public static LootItem ironPants
            = new LootItem(Items.IRON_LEGGINGS, 1, 1);
    public static LootItem diamondPants
            = new LootItem(Items.DIAMOND_LEGGINGS, 1, 1);
    public static LootItem leatherChest
            = new LootItem(Items.LEATHER_CHESTPLATE, 1, 1);
    public static LootItem goldChest
            = new LootItem(Items.GOLDEN_CHESTPLATE, 1, 1);
    public static LootItem ironChest
            = new LootItem(Items.IRON_CHESTPLATE, 1, 1);
    public static LootItem diamondChest
            = new LootItem(Items.DIAMOND_CHESTPLATE, 1, 1);

    public static LootItem someBread
            = new LootItem(Items.BREAD, 2, 4);
    public static LootItem moreBread
            = new LootItem(Items.BREAD, 4, 8);
    public static LootItem someSteak
            = new LootItem(Items.COOKED_BEEF, 2, 4);
    public static LootItem moreSteak
            = new LootItem(Items.COOKED_BEEF, 4, 8);
    public static LootItem someChicken
            = new LootItem(Items.COOKED_CHICKEN, 2, 4);
    public static LootItem moreChicken
            = new LootItem(Items.COOKED_CHICKEN, 4, 8);
    public static LootItem someApples
            = new LootItem(Items.APPLE, 1, 3);
    public static LootItem moreApples
            = new LootItem(Items.APPLE, 2, 7);
    public static LootItem somePie
            = new LootItem(Items.PUMPKIN_PIE, 1, 3);
    public static LootItem morePie
            = new LootItem(Items.PUMPKIN_PIE, 2, 7);
    public static LootItem goldApple
            = new LootItem(Items.GOLDEN_APPLE, 1, 1);
    public static LootItem goldApples
            = new LootItem(Items.GOLDEN_APPLE, 1, 3);

    public static LootItem oneGold
            = new LootItem(Items.GOLD_INGOT, 1, 1);
    public static LootItem someGold
            = new LootItem(Items.GOLD_INGOT, 2, 5);
    public static LootItem moreGold
            = new LootItem(Items.GOLD_INGOT, 3, 8);
    public static LootItem someIron
            = new LootItem(Items.IRON_INGOT, 1, 8);
    public static LootItem moreIron
            = new LootItem(Items.IRON_INGOT, 3, 12);
    public static LootItem oneDiamond
            = new LootItem(Items.IRON_INGOT, 1, 1);
    public static LootItem diamonds
            = new LootItem(Items.DIAMOND, 1, 4);
    public static LootItem manyDiamonds
            = new LootItem(Items.DIAMOND, 3, 9);
    public static LootItem oneEmerald
            = new LootItem(Items.EMERALD, 1, 1);
    public static LootItem emeralds
            = new LootItem(Items.EMERALD, 1, 4);
    public static LootItem manyEmerald
            = new LootItem(Items.EMERALD, 3, 7);

    public static LootItem saddle
            = new LootItem(Items.SADDLE, 1, 1);
    public static LootItem ironBarding
            = new LootItem(Items.IRON_HORSE_ARMOR, 1, 1);
    public static LootItem goldBarding
            = new LootItem(Items.GOLDEN_HORSE_ARMOR, 1, 1);
    public static LootItem diamondBard
            = new LootItem(Items.DIAMOND_HORSE_ARMOR, 1, 1);

    public static LootItem book
            = new LootItem(Items.BOOK, 1, 1);
    public static LootItem someBooks
            = new LootItem(Items.BOOK, 2, 5);
    public static LootItem moreBooks
            = new LootItem(Items.BOOK, 3, 8);
    public static LootItem nameTag
            = new LootItem(Items.NAME_TAG, 1, 1);
    public static LootItem enderpearl
            = new LootItem(Items.ENDER_PEARL, 1, 2);
    public static LootItem enderpearls
            = new LootItem(Items.ENDER_PEARL, 1, 9);
    public static LootItem eyeOfEnder
            = new LootItem(Items.ENDER_EYE, 1, 2);
    public static LootItem blazeRod
            = new LootItem(Items.BLAZE_ROD, 1, 2);
    public static LootItem netherstar
            = new LootItem(Items.NETHER_STAR, 1, 1);

    public static LootItem disc13
            = new LootItem(Items.MUSIC_DISC_13, 1, 1);
    public static LootItem discCat
            = new LootItem(Items.MUSIC_DISC_CAT, 1, 1);
    public static LootItem discBlocks
            = new LootItem(Items.MUSIC_DISC_BLOCKS, 1, 1);
    public static LootItem discChirp
            = new LootItem(Items.MUSIC_DISC_CHIRP, 1, 1);
    public static LootItem discFar
            = new LootItem(Items.MUSIC_DISC_FAR, 1, 1);
    public static LootItem discMall
            = new LootItem(Items.MUSIC_DISC_MALL, 1, 1);
    public static LootItem discMellohi
            = new LootItem(Items.MUSIC_DISC_MELLOHI, 1, 1);
    public static LootItem discStrad
            = new LootItem(Items.MUSIC_DISC_STRAD, 1, 1);
    public static LootItem discWard
            = new LootItem(Items.MUSIC_DISC_WARD, 1, 1);
    public static LootItem disc11
            = new LootItem(Items.MUSIC_DISC_11, 1, 1);
    public static LootItem discWait
            = new LootItem(Items.MUSIC_DISC_WAIT, 1, 1);
}
