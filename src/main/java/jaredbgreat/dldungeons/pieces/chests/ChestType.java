package jaredbgreat.dldungeons.pieces.chests;

import com.github.xyroc.dldungeons.DLDungeons;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

public enum ChestType {

    WEAK("weak"),
    BASIC("basic"),
    TREASURE("treasure");

    public static final Codec<ChestType> CODEC = Codec.INT.xmap(ordinal -> values()[ordinal], Enum::ordinal);

    public final String kind;

    ChestType(String kind) {
        this.kind = kind;
    }

    public void place(Chest owner, WorldGenLevel world, int x, int y, int z, RandomSource random, ResourceLocation category) {
        int level = owner.level;
        if (this != WEAK) {
            level += random.nextInt(2);
        }
        if (this == TREASURE && Chest.NERF && !owner.withBoss && level > 6) {
            level = Math.max(6, level - 2);
        }
        level = Math.max(0, Math.min(Chest.LEVELS - 1, level));
        owner.level = level;

        BlockPos pos = new BlockPos(x, y, z);
        if (!(world.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity chest)) {
            DLDungeons.LOGGER.error("[DLDUNGEONS] Trying to put loot into non-chest at {}, {}, {}.", x, y, z);
            return;
        }
        String kind2 = (this == TREASURE && owner.withBoss) ? "treasure_boss" : kind;
        ResourceLocation table = new ResourceLocation(category.getNamespace(),
                "chest/" + category.getPath() + "/" + kind2 + "/level_" + (level + 1));
        chest.setLootTable(table, random.nextLong());
    }
}
