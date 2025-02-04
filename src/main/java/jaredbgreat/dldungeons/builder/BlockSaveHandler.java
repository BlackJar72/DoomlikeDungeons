package jaredbgreat.dldungeons.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BlockSaveHandler {

    // This seemed like a simple ides, but turned into a convoluted mess (c.f., BlockFamily codec)
    // So this is why its sometime said that all game code is spaghetti code (eventually it will become such)

    private List<String> blocks;
    private List<BlockFamily> blockFamilies;

    // TODO: Codec

    public static void handleLoad() {
        // Using this instance allows a single codec to handle all everythings, encoding the class instance
        // and saving it into a file.
        BlockSaveHandler handler = new BlockSaveHandler();
        handler.handleLoadInstance();
    }


    private void handleLoadInstance() {
        // TODO / FIXME: These need to be loaded from file, not get them from exist lists
        blockFamilies = BlockFamily.getFamiliesAsList();
        blocks = RegisteredBlock.getRegisteredNames();
        for(String block : blocks) {
            RegisteredBlock.addOnLoad(block);
        }
        for(BlockFamily family : blockFamilies) {
            if(family != null) {
                BlockFamily.addFromLoad(family);
            }
        }
    }


    public static void handleSave() {
        // Using this instance allows a single codec to handle all everythings, encoding the class instance
        // and saving it into a file.
        BlockSaveHandler handler = new BlockSaveHandler();
        handler.handleSaveInstance();
    }


    public void handleSaveInstance() {
        blockFamilies = BlockFamily.getFamiliesAsList();
        blocks = RegisteredBlock.getRegisteredNames();
        // TODO: Write out to save
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
