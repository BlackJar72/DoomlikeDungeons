package jaredbgreat.dldungeons.pieces.chests;

import com.github.xyroc.dldungeons.DLDungeons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DungeonLoot {

    public static final int GEAR = 0, HEAL = 1, LOOT = 2, RANDOM = 3;
    public static final int WEAK = 0, BASIC = 1, TREASURE = 2;

    public static volatile int a1 = 3, b1 = 1, c1 = 3;
    public static volatile int a2 = 1, b2 = 1, c2 = 1;
    public static volatile ChestLootStyle style = ChestLootStyle.ORIGINAL;

    public static void setNumbers(int na1, int nb1, int nc1, int na2, int nb2, int nc2) {
        a1 = na1;
        b1 = nb1;
        c1 = nc1;
        a2 = na2;
        b2 = nb2;
        c2 = nc2;
    }

    public static void setStyle(ChestLootStyle newStyle) {
        style = newStyle;
    }

    public static void generate(String category, int chestType, int level, boolean boss,
                                LootContext ctx, Consumer<ItemStack> out) {
        RandomSource random = ctx.getRandom();
        switch (chestType) {
            case WEAK -> weak(category, ctx, random, out);
            case TREASURE -> treasure(category, level, boss, ctx, random, out);
            default -> basic(category, level, ctx, random, out);
        }
    }

    private static int basicCount(int level, RandomSource r) {
        return r.nextInt(Math.max(2, a1 + (level / Math.max(1, b1)))) + c1;
    }

    private static int treasureCount(int level, RandomSource r) {
        return r.nextInt(Math.max(2, a2 + (level / Math.max(1, b2)))) + c2;
    }

    private static void weak(String cat, LootContext ctx, RandomSource r, Consumer<ItemStack> out) {
        if (r.nextBoolean()) {
            if (r.nextBoolean()) fillType(cat, GEAR, 0, ctx, r, out);
            else fillType(cat, HEAL, 0, ctx, r, out);
        } else {
            fillType(cat, GEAR, 0, ctx, r, out);
            fillType(cat, HEAL, 0, ctx, r, out);
            if (r.nextBoolean()) fillType(cat, LOOT, 0, ctx, r, out);
        }
    }

    private static void basic(String cat, int level, LootContext ctx, RandomSource r, Consumer<ItemStack> out) {
        if (style == ChestLootStyle.MIXED) {
            fillType(cat, RANDOM, level, ctx, r, out);
        } else {
            int type = switch (r.nextInt(3)) {
                case 0 -> HEAL;
                case 1 -> GEAR;
                default -> RANDOM;
            };
            fillType(cat, type, level, ctx, r, out);
        }
    }

    private static void fillType(String cat, int type, int level, LootContext ctx, RandomSource r, Consumer<ItemStack> out) {
        int num = basicCount(level, r);
        for (int i = 0; i < num; i++) {
            ItemStack s = getLoot(cat, type, level, ctx, r).stack;
            if (!s.isEmpty()) out.accept(s);
        }
    }

    private static void treasure(String cat, int level, boolean boss, LootContext ctx, RandomSource r, Consumer<ItemStack> out) {
        int num = treasureCount(level, r);
        for (int i = 0; i < num; i++) {
            ItemStack s = getLoot(cat, HEAL, level, ctx, r).stack;
            if (!s.isEmpty()) out.accept(s);
        }
        num = treasureCount(level, r);
        for (int i = 0; i < num; i++) {
            ItemStack s = getLoot(cat, GEAR, level, ctx, r).stack;
            if (!s.isEmpty()) out.accept(s);
        }
        num = treasureCount(level, r);
        for (int i = 0; i < num; i++) {
            Result res = getLoot(cat, LOOT, level + 1 + r.nextInt(2), ctx, r);
            if (!res.stack.isEmpty()) out.accept(res.stack);
            if (Chest.NERF && res.level > 6 && !boss) {
                level--;
            }
        }
        if (r.nextInt(7) < level) {
            if (level >= 6) {
                accept(pick(cat, "special", 0, ctx), out);
                if (r.nextBoolean()) accept(pick(cat, "discs", 0, ctx), out);
            } else {
                accept(pick(cat, "discs", 0, ctx), out);
            }
        }
    }

    private static void accept(ItemStack s, Consumer<ItemStack> out) {
        if (!s.isEmpty()) out.accept(s);
    }

    private static Result getLoot(String cat, int type, int level, LootContext ctx, RandomSource r) {
        if (level <= 6) {
            level = Math.min(6, level + r.nextInt(2) - r.nextInt(2));
        }
        level = Math.max(level, 0);
        switch (type) {
            case GEAR:
                if (r.nextBoolean()) {
                    return enchantedGear(cat, level, ctx, r);
                } else {
                    int l = Math.min(6, level);
                    return new Result(pick(cat, "gear", l, ctx), l);
                }
            case HEAL: {
                int l = Math.min(6, level);
                return new Result(pick(cat, "heal", l, ctx), l);
            }
            case LOOT:
                if (level > 6) {
                    if (level > r.nextInt(100)) {
                        return new Result(pick(cat, "special", 0, ctx), 7);
                    } else {
                        level = 6;
                    }
                }
                if (r.nextInt(10) == 0) {
                    return new Result(enchantedBook(level, r), Math.min(level, 6));
                } else {
                    return new Result(pick(cat, "loot", level, ctx), level);
                }
            case RANDOM:
            default:
                return switch (r.nextInt(3)) {
                    case 0 -> getLoot(cat, GEAR, level, ctx, r);
                    case 1 -> getLoot(cat, HEAL, level, ctx, r);
                    default -> getLoot(cat, LOOT, level, ctx, r);
                };
        }
    }

    private static Result enchantedGear(String cat, int lootLevel, LootContext ctx, RandomSource r) {
        float portion = r.nextFloat() / 2f;
        int lootPart = Math.min(6, Math.max(0, (int) ((lootLevel * (1f - portion)) + 0.5f)));
        ItemStack stack = pick(cat, "gear", lootPart, ctx);
        int diff = lootLevel - lootPart;
        int enchPart = Math.min(5 + (diff * (diff + 1) / 2) * 5, diff * 10);
        if (enchPart >= 1 && isEnchantable(stack)) {
            stack = EnchantmentHelper.enchantItem(r, stack, enchPart, r.nextBoolean());
            return new Result(stack, Math.min(lootLevel, 6));
        } else {
            return new Result(pick(cat, "gear", Math.min(6, lootLevel), ctx), Math.min(lootLevel, 6));
        }
    }

    private static ItemStack enchantedBook(int level, RandomSource r) {
        ItemStack book = new ItemStack(Items.BOOK, 1);
        return EnchantmentHelper.enchantItem(r, book, Math.min(30, (int) (level * 7.5)), true);
    }

    private static boolean isEnchantable(ItemStack in) {
        Item item = in.getItem();
        return item instanceof TieredItem
                || item instanceof ArmorItem
                || item instanceof BowItem
                || item.isEnchantable(in);
    }

    private static ItemStack pick(String cat, String type, int level, LootContext ctx) {
        ResourceLocation rl;
        if (type.equals("special") || type.equals("discs")) {
            rl = new ResourceLocation(DLDungeons.MODID, "items/" + cat + "/" + type);
        } else {
            int n = Math.max(0, Math.min(6, level)) + 1;
            rl = new ResourceLocation(DLDungeons.MODID, "items/" + cat + "/" + type + "/level_" + n);
        }
        LootTable table = ctx.getResolver().getLootTable(rl);
        List<ItemStack> items = new ArrayList<>();
        table.getRandomItems(ctx, items::add);
        return items.isEmpty() ? ItemStack.EMPTY : items.get(0);
    }

    private static final class Result {
        final ItemStack stack;
        final int level;

        Result(ItemStack stack, int level) {
            this.stack = stack;
            this.level = level;
        }
    }
}
