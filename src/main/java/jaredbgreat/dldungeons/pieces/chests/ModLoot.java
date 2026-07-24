package jaredbgreat.dldungeons.pieces.chests;

import com.github.xyroc.dldungeons.DLDungeons;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModLoot {
    public static final DeferredRegister<LootPoolEntryType> LOOT_ENTRIES =
            DeferredRegister.create(Registries.LOOT_POOL_ENTRY_TYPE, DLDungeons.MODID);

    public static final RegistryObject<LootPoolEntryType> DUNGEON_CHEST =
            LOOT_ENTRIES.register("dungeon_chest",
                    () -> new LootPoolEntryType(new DungeonChestEntry.Serializer()));
}
