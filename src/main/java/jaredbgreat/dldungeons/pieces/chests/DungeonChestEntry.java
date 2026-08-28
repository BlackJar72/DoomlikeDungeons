package jaredbgreat.dldungeons.pieces.chests;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import com.mojang.serialization.Codec;

import java.util.List;
import java.util.function.Consumer;

public class DungeonChestEntry extends LootPoolSingletonContainer {

    public static final MapCodec<DungeonChestEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("category").forGetter(entry -> entry.category),
                    Codec.INT.fieldOf("chest_type").forGetter(entry -> entry.chestType),
                    Codec.INT.fieldOf("level").forGetter(entry -> entry.level),
                    Codec.BOOL.optionalFieldOf("boss", Boolean.FALSE).forGetter(entry -> entry.boss))
            .and(singletonFields(instance))
            .apply(instance, DungeonChestEntry::new));

    final String category;
    final int chestType;
    final int level;
    final boolean boss;

    DungeonChestEntry(String category, int chestType, int level, boolean boss,
                      int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
        super(weight, quality, conditions, functions);
        this.category = category;
        this.chestType = chestType;
        this.level = level;
        this.boss = boss;
    }

    @Override
    public LootPoolEntryType getType() {
        return ModLoot.DUNGEON_CHEST.get();
    }

    @Override
    protected void createItemStack(Consumer<ItemStack> consumer, LootContext context) {
        DungeonLoot.generate(category, chestType, level, boss, context, consumer);
    }
}
