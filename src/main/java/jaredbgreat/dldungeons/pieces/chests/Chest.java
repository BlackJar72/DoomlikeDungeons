package jaredbgreat.dldungeons.pieces.chests;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

import java.util.List;

public class Chest {

    // Static variables are now volatile to deal with concurrent use.
    static volatile boolean NERF;

    public static final int LEVELS = 7;

    // Anything thing not public is not package protected;
    // Nothing can be final as that would interfere with deserialization via codec.
    int mx, my, mz;
    protected int level;
    ChestType type;
    boolean withBoss;


    public static final Codec<Chest> CODEC = RecordCodecBuilder.create(builder -> builder
            .group(Codec.INT.listOf().fieldOf("data").forGetter(chest -> List.of(
                            chest.mx,
                            chest.my,
                            chest.mz,
                            chest.level)),
                    ChestType.CODEC.fieldOf("type").forGetter(chest -> chest.type),
                    Codec.BOOL.listOf().fieldOf("flags").forGetter(chest -> List.of(
                            chest.withBoss)))
                .apply(builder, (data, type, flags) -> {
                final Chest chest = new Chest();

                chest.mx = data.get(0);
                chest.my = data.get(1);
                chest.mz = data.get(2);
                chest.level = data.get(3);
                chest.type = type;
                chest.withBoss = flags.get(0);
                return chest;
            }));


    private Chest(){}


    public Chest(int x, int y, int z, int level, ChestType type) {
        this.mx = x;
        this.my = y;
        this.mz = z;
        this.level = level;
        this.type = type;
    }


    public void place(WorldGenLevel world, int x, int y, int z, RandomSource random, ResourceLocation lootCategory) {
        type.place(this, world, x, y, z, random, lootCategory);
    }


    /**
     * Test to see if the selected location is occupied by a spawner.
     * Used to make sure chests are not assigned the same locations.
     *
     * @param ox other X
     * @param oy other Y
     * @param oz other Z
     * @return
     */
    public boolean isLocation(int ox, int oy, int oz) {
        return ((ox == mx) && (oy == my) && (oz == mz));
    }


    public int getMX() { return mx; }
    public int getMY() { return my; }
    public int getMZ() { return mz; }


    //****************************************************************************************************************//
    //                                     SETTERS FOR STATIC VARIABLES                                               //
    //****************************************************************************************************************//


    public static void setNerf(boolean nerf) {
        NERF = nerf;
    }


    public Chest setWithBoss(boolean bossRoom) {
        withBoss = bossRoom;
        return this;
    }



}
