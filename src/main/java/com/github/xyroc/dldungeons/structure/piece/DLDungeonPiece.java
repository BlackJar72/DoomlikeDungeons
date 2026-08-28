package com.github.xyroc.dldungeons.structure.piece;

import com.github.xyroc.dldungeons.DLDungeons;
import com.github.xyroc.dldungeons.init.ModStructurePieceTypes;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
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

    private volatile Tag cachedTag;

    public DLDungeonPiece(BoundingBox boundingBox, Dungeon dungeon) {
        super(ModStructurePieceTypes.DLDUNGEON_PIECE.get(), 0, boundingBox);// The second argument is only used for jigsaw structures. Ignore it.
        this.dungeon = dungeon;
    }

    public DLDungeonPiece(CompoundTag nbt) {
        super(ModStructurePieceTypes.DLDUNGEON_PIECE.get(), nbt);
        Tag dungeonTag = nbt.get(NBT_KEY_DUNGEON);
        this.dungeon = Dungeon.CODEC.parse(NbtOps.INSTANCE, dungeonTag)
                .getOrThrow(error -> new RuntimeException("Error decoding dungeon: " + error));
        this.cachedTag = dungeonTag;
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        dungeon.random = random;
        // Do world generation here.
        dungeon.map.buildInChunk(dungeon, chunkPos.x, chunkPos.z, level);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        Tag encoded = cachedTag;
        if (encoded == null) {
            DLDungeons.LOGGER.debug("Encoding dungeon at chunk {},{}", dungeon.map.chunkX, dungeon.map.chunkZ);
            encoded = Dungeon.CODEC.encodeStart(NbtOps.INSTANCE, dungeon)
                    .getOrThrow((error) -> new RuntimeException("Error saving dungeon: " + error));
            cachedTag = encoded;
        }
        tag.put(NBT_KEY_DUNGEON, encoded);
    }

}
