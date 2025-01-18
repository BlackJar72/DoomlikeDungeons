package jaredbgreat.dldungeons.pieces.chests;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.Collections;
import java.util.Random;

public enum ChestType {

    WEAK (new WeakProcessor()),
    BASIC (new BasicProcessor()),
    TREASURE (new TreasureProcessor());

    public static final Codec<ChestType> CODEC = Codec.INT.xmap(ordinal -> values()[ordinal], Enum::ordinal);

    public final IChestProcessor processor;

    interface IChestProcessor {
        void place(Chest owner, WorldGenLevel world, int x, int y, int z, RandomSource random, LootCategory category);
        void fillChest(Chest owner, ChestBlockEntity chest, LootType kind, RandomSource random, LootCategory category);
        public void initSlots();
    }

    ChestType(IChestProcessor processor) {
        this.processor = processor;
    }

    //****************************************************************************************************************//
    //                                     CHEST PROCESSOR IMPLEMENTATIONS                                            //
    //****************************************************************************************************************//

    public static class BasicProcessor implements IChestProcessor {

        @Override
        public void place(Chest owner, WorldGenLevel world, int x, int y, int z, RandomSource random, LootCategory category) {
            BlockPos pos = new BlockPos(x, y, z);
            owner.level += random.nextInt(2);
            owner.level = Math.min(owner.level, LootCategory.LEVELS - 1);
            if(world.getBlockState(pos).getBlock() != Blocks.CHEST) {
                System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at "
                        + x + ", " + y + ", " + z + " (basic chest).");
                return;
            }
            ChestBlockEntity contents = (ChestBlockEntity) world.getBlockEntity(pos);
            int which = random.nextInt(3);
            switch (which) {
                case 0:
                    fillChest(owner, contents, LootType.HEAL, random, category);
                    break;
                case 1:
                    fillChest(owner, contents, LootType.GEAR, random, category);
                    break;
                case 2:
                    fillChest(owner, contents, LootType.RANDOM, random, category);
                    break;
            }
        }

        @Override
        public void fillChest(Chest owner, ChestBlockEntity chest, LootType kind, RandomSource random, LootCategory category) {
            int num = random.nextInt(Math.max(2, Chest.A1 + (owner.level / Chest.B1))) + Chest.C1;
            for(int i = 0; i < num; i++) {
                ItemStack treasure = category.getLoot(kind, owner.level, random).getLoot();
                if(treasure != null) chest.setItem(random.nextInt(27), treasure);
            }
        }

        @Override
        public void initSlots() {}
    }


    public static class WeakProcessor extends BasicProcessor {

        @Override
        public void place(Chest owner, WorldGenLevel world, int x, int y, int z, RandomSource random, LootCategory category) {
            BlockPos pos = new BlockPos(x, y, z);
            ChestBlockEntity contents = (ChestBlockEntity) world.getBlockEntity(pos);
            if (world.getBlockState(pos).getBlock() != Blocks.CHEST) {
                System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at "
                        + x + ", " + y + ", " + z + " (basic chest).");
                return;
            }
            if (random.nextBoolean()) {
                if (random.nextBoolean()) fillChest(owner, contents, LootType.GEAR, random, category);
                else fillChest(owner, contents, LootType.HEAL, random, category);
            } else {
                fillChest(owner, contents, LootType.GEAR, random, category);
                fillChest(owner, contents, LootType.HEAL, random, category);
            }
        }
    }


    public static class TreasureProcessor extends BasicProcessor {

        @Override
        public void place(Chest owner, WorldGenLevel world, int x, int y, int z, RandomSource random, LootCategory category) {
            BlockPos pos = new BlockPos(x, y, z);
            Collections.shuffle(Chest.slots, new Random(random.nextLong()));
            owner.slot = 0;
            owner.level += random.nextInt(2);
            if(Chest.NERF && !owner.withBoss && (owner.level > 6)) {
                owner.level = Math.max(6, owner.level - 2);
            }
            if(owner.level >= LootCategory.LEVELS) owner.level = LootCategory.LEVELS - 1;
            ItemStack treasure;
            if(world.getBlockState(pos).getBlock() != Blocks.CHEST) {
                System.err.println("[DLDUNGEONS] ERROR! Trying to put loot into non-chest at "
                        + x + ", " + y + ", " + z + " (treasure chest).");
                return;
            }
            ChestBlockEntity contents = (ChestBlockEntity) world.getBlockEntity(pos);
            int num;
            num = random.nextInt(Math.max(2, Chest.A2 + (owner.level / Chest.B2))) + Chest.C2;
            for(int i = 0; i < num; i++) {
                treasure = category.getLoot(LootType.HEAL, owner.level, random).getLoot();
                contents.setItem(Chest.slots.get(owner.slot), treasure);
                owner.slot++;
            }
            num = random.nextInt(Math.max(2, Chest.A2 + (owner.level / Chest.B2))) + Chest.C2;
            for(int i = 0; i < num; i++) {
                treasure = category.getLoot(LootType.GEAR, owner.level, random).getLoot();
                contents.setItem(Chest.slots.get(owner.slot), treasure);
                owner.slot++;
            }
            num = random.nextInt(Math.max(2, Chest.A2 + (owner.level / Chest.B2))) + Chest.C2;
            for(int i = 0; i < num; i++) {
                LootResult lootResult = category.getLoot(LootType.LOOT,
                        owner.level + 1 + random.nextInt(2), random);
                treasure = lootResult.getLoot();
                contents.setItem(Chest.slots.get(owner.slot), treasure);
                if(Chest.NERF && (lootResult.getLevel() > 6) && !owner.withBoss) {
                    owner.level--;
                }
                owner.slot++;
            }
            if(random.nextInt(7) < owner.level) {
                if(owner.level >= 6) {
                    treasure = category.getLists().special.getLoot(random).getStack(random);
                    contents.setItem(Chest.slots.get(owner.slot), treasure);
                    owner.slot++;
                    if(random.nextBoolean()) {
                        treasure = category.getLists().discs.getLoot(random).getStack(random);
                        contents.setItem(Chest.slots.get(owner.slot), treasure);
                        owner.slot++;
                    }
                } else {
                    treasure = category.getLists().discs.getLoot(random).getStack(random);
                    contents.setItem(Chest.slots.get(owner.slot), treasure);
                    owner.slot++;
                }
            }
        }

        @Override
        public void initSlots() {
            for(int i = 0; i < 27; i++) Chest.slots.add(i);
        }
    }

}
