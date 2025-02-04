package jaredbgreat.dldungeons.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BlockSaveHandler {
    private static final BlockSaveHandler instance = new BlockSaveHandler();

    // This seemed like a simple ides, but turned into a convoluted mess (c.f., BlockFamily codec)
    // So this is why its sometime said that all game code is spaghetti code (eventually it will become such)

    private List<String> blocks = new ArrayList<>();
    private List<BlockFamily> blockFamilies = new ArrayList<>();

    public static void HandleSave() {
        instance.blockFamilies = BlockFamily.getFamiliesAsList();
        instance.blocks = RegisteredBlock.getRegisteredNames();
    }




//********************************************************************************************************************//
//                                              GENERAL HELPER METHODS                                                //
//********************************************************************************************************************//



    public static List<String> stringsToList(IBlockPlacer[] array) {
        List<IBlockPlacer> bl = Arrays.asList(array);
        List<String> output = new ArrayList<>(bl.size());
        for(IBlockPlacer bp : bl) {
            output.add(bp.getName());
        }
        return output;
    }
}
