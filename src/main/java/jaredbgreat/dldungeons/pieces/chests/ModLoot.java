package jaredbgreat.dldungeons.pieces.chests;

import com.github.xyroc.dldungeons.DLDungeons;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModLoot {
    public static final DeferredRegister<LootPoolEntryType> LOOT_ENTRIES =
            DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, DLDungeons.MODID);

    public static final DeferredHolder<LootPoolEntryType, LootPoolEntryType> DUNGEON_CHEST =
            LOOT_ENTRIES.register("dungeon_chest",
                    () -> new LootPoolEntryType(DungeonChestEntry.CODEC));
}
