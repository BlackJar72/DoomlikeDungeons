package jaredbgreat.dldungeons.pieces.chests;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Consumer;

public class DungeonChestEntry extends LootPoolSingletonContainer {

    final String category;
    final int chestType;
    final int level;
    final boolean boss;

    DungeonChestEntry(String category, int chestType, int level, boolean boss,
                      int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
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

    public static class Serializer extends LootPoolSingletonContainer.Serializer<DungeonChestEntry> {
        @Override
        public void serializeCustom(JsonObject json, DungeonChestEntry entry, JsonSerializationContext ctx) {
            json.addProperty("category", entry.category);
            json.addProperty("chest_type", entry.chestType);
            json.addProperty("level", entry.level);
            json.addProperty("boss", entry.boss);
        }

        @Override
        protected DungeonChestEntry deserialize(JsonObject json, JsonDeserializationContext ctx,
                                                int weight, int quality,
                                                LootItemCondition[] conditions, LootItemFunction[] functions) {
            String category = json.get("category").getAsString();
            int chestType = json.get("chest_type").getAsInt();
            int level = json.get("level").getAsInt();
            boolean boss = json.has("boss") && json.get("boss").getAsBoolean();
            return new DungeonChestEntry(category, chestType, level, boss, weight, quality, conditions, functions);
        }
    }
}
