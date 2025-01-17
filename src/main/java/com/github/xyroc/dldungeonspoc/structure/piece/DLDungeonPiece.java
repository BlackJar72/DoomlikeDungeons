package com.github.xyroc.dldungeonspoc.structure.piece;

import com.github.xyroc.dldungeonspoc.DLDPoC;
import com.github.xyroc.dldungeonspoc.init.ModStructurePieceTypes;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class DLDungeonPiece extends StructurePiece {
    private static final String NBT_KEY_DUNGEON = "Dungeon";

    private final Dungeon dungeon;

    public DLDungeonPiece(BoundingBox boundingBox, Dungeon dungeon) {
        super(ModStructurePieceTypes.DLDUNGEON_PIECE, 0, boundingBox);// The second argument is only used for jigsaw structures. Ignore it.
        this.dungeon = dungeon;
    }

    public DLDungeonPiece(CompoundTag nbt) {
        super(ModStructurePieceTypes.DLDUNGEON_PIECE, nbt);
        this.dungeon = Dungeon.CODEC.parse(NbtOps.INSTANCE, nbt.get(NBT_KEY_DUNGEON)).getOrThrow(false, error -> {
            throw new RuntimeException("Error decoding dungeon: " + error);
        });
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        dungeon.random = random;
        // Do world generation here.
        dungeon.map.buildInChunk(dungeon, chunkPos.x, chunkPos.z, level);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        DLDPoC.LOGGER.info("Saving dungeon at chunk {},{}", dungeon.map.chunkX, dungeon.map.chunkZ);
        // Save dungeon plan to nbt here. Using a codec to do this because it's nice and clean, but you can do it the old-fashioned way as well.
        // The same goes for the constructor of this class which reads from nbt.
        tag.put(NBT_KEY_DUNGEON, Dungeon.CODEC.encodeStart(NbtOps.INSTANCE, dungeon).getOrThrow(false, (error) -> {
            throw new RuntimeException("Error saving dungeon: " + error);
        }));
    }

}
