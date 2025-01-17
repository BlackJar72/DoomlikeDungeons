package jaredbgreat.dldungeons.pieces.chests;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.ArrayList;

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
