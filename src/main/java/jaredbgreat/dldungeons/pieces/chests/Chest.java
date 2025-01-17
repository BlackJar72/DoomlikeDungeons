package jaredbgreat.dldungeons.pieces.chests;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

import java.util.ArrayList;
import java.util.List;

public class Chest {

    // Static variables are now volatile to deal with concurrent use.
    static volatile int A1 = 2, B1 = 1, C1 = 2;
    static volatile int A2 = 3, B2 = 2, C2 = 2;
    static volatile boolean NERF;

    // Anything thing not public is not package protected;
    // Nothing can be final as that would interfere with deserialization via codec.
    int mx, my, mz;
    protected int level;
    ChestType type;
    LootType category;
    boolean withBoss;

    // These are used during placement and do not need to be serialized
    static ArrayList<Integer> slots = new ArrayList();
    int slot;


    public static final Codec<Chest> CODEC = RecordCodecBuilder.create(builder -> builder
            .group(Codec.INT.listOf().fieldOf("data").forGetter(chest -> List.of(
                            chest.mx,
                            chest.my,
                            chest.mz,
                            chest.level)),
                    ChestType.CODEC.fieldOf("type").forGetter(chest -> chest.type),
                    LootType.CODEC.fieldOf("category").forGetter(chest -> chest.category),
                    Codec.BOOL.listOf().fieldOf("flags").forGetter(chest -> List.of(
                            chest.withBoss)))
                .apply(builder, (data, type, category, flags) -> {
                final Chest chest = new Chest();

                chest.mx = data.get(0);
                chest.my = data.get(1);
                chest.mz = data.get(2);
                chest.level = data.get(2);
                chest.type = type;
                chest.category = category;
                chest.withBoss = flags.get(0);
                return chest;
            }));


    private Chest(){}


    public Chest(int x, int y, int z, int level, ChestType type, LootType category) {
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.level = level;
        this.type = type;
        this.category = category;
    }


    public void Place(WorldGenLevel world, int x, int y, int z, RandomSource random) {
        type.processor.place(this, world, x, y, z, random);
    }


    //****************************************************************************************************************//
    //                                     SETTERS FOR STATIC VARIABLES                                               //
    //****************************************************************************************************************//


    public static void setBasicLootNumbers(int a, int b, int c) {
        A1 = a;
        B1 = b;
        C1 = c;
    }


    public static void setBasicLootNumbers(int a, int b, int c, boolean nerf) {
        setBasicLootNumbers(a, b, c);
        NERF = nerf;
    }


    public static void setTreasureLootNumbers(int a, int b, int c) {
        A2 = a;
        B2 = b;
        C2 = c;
    }


    public void setWithBoss(boolean bossRoom) {
        withBoss = bossRoom;
    }



}
