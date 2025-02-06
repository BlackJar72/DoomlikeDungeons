package jaredbgreat.dldungeons.config;

import com.github.xyroc.dldungeons.DLDungeons;
import jaredbgreat.dldungeons.pieces.chests.Chest;
import jaredbgreat.dldungeons.rooms.Room;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = DLDungeons.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    //*****************************************************************************************************************/
    //                                          BEGIN DEFINING CONFIG OPTIONS                                         //
    //*****************************************************************************************************************/

    private static final ModConfigSpec.EnumValue<Difficulty> DIFFICULTY = BUILDER
            .comment(" DIFFICULTY: How hard do you want your dungeons? This is about spawner placement, not related to game difficulty: ")
            .comment(" " + Difficulty.NONE + " = " + Difficulty.NONE.label)
            .comment(" " + Difficulty.BABY + " = " + Difficulty.BABY.label)
            .comment(" " + Difficulty.NOOB + " = " + Difficulty.NOOB.label)
            .comment(" " + Difficulty.NORM + " = " + Difficulty.NORM.label)
            .comment(" " + Difficulty.HARD + " = " + Difficulty.HARD.label)
            .comment(" " + Difficulty.NUTS + " = " + Difficulty.NUTS.label)
            .comment(" The default (NORM for normal) is probably good for most players.")
            .comment(" Nightmare (NUTS) if for those who want wither boss and warden spanwers!")
            .defineEnum("Difficulty", Difficulty.NORM);

    private static final ModConfigSpec.BooleanValue ALL_HUGE = BUILDER
            .comment(" If true all the dungeons will be in the huge size category.")
            .comment("(Note: There is a bug that creates empty spawners, which is more common with smaller dungeons ")
            .comment("where the spawneer are packed more densely, so is is probably best to keep this as true.)")
            .define("All Huge", true);

    private static final ModConfigSpec.BooleanValue BIG_HUBS = BUILDER
            .comment(" If true entrance and \"boss\" rooms will have extra high ceilings, good with tall mobs.")
            .define("Big Hubs", false);

    private static final ModConfigSpec.BooleanValue EASY_FIND = BUILDER
            .comment(" If true all dungeons will have at least one entrance from the surface ")
            .comment(" and entrances will have a small building or ruin on top.")
            .define("Easy Find", true);

    private static final ModConfigSpec.BooleanValue SINGLE_ENTRANCE = BUILDER
            .comment(" If true all have exactly one entrance.")
            .define("Only Single Entrance", false);

    private static final ModConfigSpec.BooleanValue NERF_LOOT = BUILDER
            .comment("If true this will limit the amount of level 8 loot found with anything less than a true boss")
            .comment("(This includes epic, sometimes somewhat cheaty items,)")
            .define("Nerf Epic Loot", false);

    private static final ModConfigSpec.IntValue LOOT_BONUS = BUILDER
            .comment("Modifies the value of loot, incase you think its too stingy.")
            .defineInRange("Loot Bonus", 0, -9, 9);

    private static final ModConfigSpec.IntValue A1 = BUILDER
            .comment("Part of the formula for determine how many items go in basic chests.")
            .comment("Formula for number of loot items: number of items = random.NextIt(A1 + (Room Difficulty / B1)) + C1")
            .defineInRange("Common Chest A1", 3, 0, 9);

    private static final ModConfigSpec.IntValue B1 = BUILDER
            .comment("Part of the formula for determine how many items go in basic chests.")
            .comment("Formula for number of loot items: number of items = random.NextIt(A1 + (Room Difficulty / B1)) + C1")
            .defineInRange("Common Chest B1", 1, 0, 9);

    private static final ModConfigSpec.IntValue C1 = BUILDER
            .comment("Part of the formula for determine how many items go in basic chests.")
            .comment("Formula for number of loot items: number of items = random.NextIt(A1 + (Room Difficulty / B1)) + C1")
            .defineInRange("Common Chest C1", 3, 0, 9);

    private static final ModConfigSpec.IntValue A2 = BUILDER
            .comment("Part of the formula for determine how many items of each category go in \"boss room\" treasure chests.")
            .comment("Formula for number of loot items: number of items = random.NextIt(A2 + (Room Difficulty / B2)) + C2")
            .comment("The categories for these items are healing, gear, and loot.")
            .defineInRange("Common Chest A2", 1, 0, 9);

    private static final ModConfigSpec.IntValue B2 = BUILDER
            .comment("Part of the formula for determine how many items of each category go in \"boss room\" treasure chests.")
            .comment("Formula for number of loot items: number of items = random.NextIt(A2 + (Room Difficulty / B2)) + C2")
            .comment("The categories for these items are healing, gear, and loot.")
            .defineInRange("Common Chest B2", 1, 0, 9);

    private static final ModConfigSpec.IntValue C2 = BUILDER
            .comment("Part of the formula for determine how many items of each category go in \"boss room\" treasure chests.")
            .comment("Formula for number of loot items: number of items = random.NextIt(A2 + (Room Difficulty / B2)) + C2")
            .comment("The categories for these items are healing, gear, and loot.")
            .defineInRange("Common Chest C2", 1, 0, 9);

    //*****************************************************************************************************************/
    //                                          ENDING CONFIG OPTIONS                                                 //
    //*****************************************************************************************************************/

    public static final ModConfigSpec SPEC = BUILDER.build();


    public static boolean allHuge;
    public static boolean bigHubs;
    public static boolean easyFind;
    public static boolean singleEntrance;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        DLDungeons.LOGGER.info("Loading config!");
        Difficulty.setDifficulty(DIFFICULTY.get());
        allHuge = ALL_HUGE.get();
        bigHubs = BIG_HUBS.get();
        easyFind = EASY_FIND.get();
        singleEntrance = SINGLE_ENTRANCE.get();
        Room.setLootBonus(LOOT_BONUS.get());
        Chest.setBasicLootNumbers(A1.get(), B1.get(), C1.get(), NERF_LOOT.get());
        Chest.setTreasureLootNumbers(A2.get(), B2.get(), C2.get());
    }




    private static boolean returnTrue(final Object obj) {
        return true;
    }

}
