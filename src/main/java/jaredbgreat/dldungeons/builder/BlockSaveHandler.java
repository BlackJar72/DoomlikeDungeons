package jaredbgreat.dldungeons.builder;

import com.github.xyroc.dldungeons.DLDungeons;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

public final class BlockSaveHandler {
    private static final String FILE_NAME = "dld_block_data.dat";

    // This seemed like a simple ides, but turned into a convoluted mess (c.f., BlockFamily codec)
    // So this is why its sometime said that all game code is spaghetti code (eventually it will become such)

    private List<String> blocks;
    private List<BlockFamily> blockFamilies;

    public static final Codec<BlockSaveHandler> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    BlockFamily.CODEC.listOf().fieldOf("families").forGetter( handler -> handler.blockFamilies),
                    Codec.STRING.listOf().fieldOf("blocks").forGetter((handler -> handler.blocks))
            )
            .apply(instance, (families, blocks) -> {
                final BlockSaveHandler handler = new BlockSaveHandler();
                handler.blockFamilies = families;
                handler.blocks = blocks;
                return handler;
            }));


    public static void handleLoad() {
        // Using this instance allows a single codec to handle all everythings, encoding the class instance
        // and saving it into a file.
        BlockSaveHandler handler = new BlockSaveHandler();
        handler.handleLoadInstance();
    }


    private void handleLoadInstance() {
        // TODO: These need to be loaded from file, not get them from exist lists
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


    private void handleSaveInstance() {
        blockFamilies = BlockFamily.getFamiliesAsList();
        blocks = RegisteredBlock.getRegisteredNames();
        // TODO: Write out to save
        DataResult<Tag> saveResults = CODEC.encodeStart(NbtOps.INSTANCE, this);
        saveResults
                .resultOrPartial(DLDungeons.LOGGER::error)
                .ifPresent(this::saveData);
    }


    private void saveData(Tag tag) {
        System.out.println();
        System.out.println(tag.getAsString());
        System.out.println();
/*
        CompoundTag saveData = new CompoundTag();
        saveData.put("dld_block_data", tag);
        Path worldPath = Minecraft.getInstance().level.getServer().getWorldPath(LevelResource.ROOT);
        File saveFile = new File(worldPath.toString() + File.separator + "data" + File.separator + FILE_NAME);
*/

// I GIVE UP -- FINAL MOD VERSION, back to maintenance mode, only update current mod version to new game versions! (While that is doable.)


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
