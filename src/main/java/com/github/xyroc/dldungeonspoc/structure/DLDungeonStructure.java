package com.github.xyroc.dldungeonspoc.structure;

import com.github.xyroc.dldungeonspoc.DLDPoC;
import com.github.xyroc.dldungeonspoc.init.ModStructureTypes;
import com.github.xyroc.dldungeonspoc.structure.piece.DLDungeonPiece;
import com.mojang.serialization.Codec;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.util.cache.Coords;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class DLDungeonStructure extends Structure {

    // Basic codec without any extra information
    public static final Codec<Structure> CODEC = simpleCodec(DLDungeonStructure::new);

    private DLDungeonStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        // Here the game gives us a potential generation spot (= a chunk position), and we must decide on whether we want to use it or not.
        // For example, we can check if all biomes in a radius around this spot are suitable.
        // However, there is no need to check the biome at the exact spot. Minecraft does that for us.
        // If we decide to use this spot, we return an Optional holding a "generation stub" here. If not, return an empty optional.

        final int chunkCenterX = context.chunkPos().getBlockX(7);
        final int chunkCenterZ = context.chunkPos().getBlockZ(7);
        final int chunkCenterGroundHeight = context.chunkGenerator()
                .getFirstOccupiedHeight(chunkCenterX, chunkCenterZ, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

        final BlockPos center = new BlockPos(chunkCenterX, chunkCenterGroundHeight, chunkCenterZ);

        final Structure.GenerationStub stub = new Structure.GenerationStub(center, (structurePiecesBuilder) -> {
            DLDPoC.LOGGER.info("Generating dungeon at block [{},{}]", chunkCenterX, chunkCenterZ);
            // Generate dungeon plan.
            final Dungeon dungeon;
            try {
                dungeon = new Dungeon(context.random(), new Coords(context.chunkPos().x, context.chunkPos().z, 0));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            final BoundingBox boundingBox = new BoundingBox(
                    dungeon.map.origenX,
                    context.heightAccessor().getMinBuildHeight(),
                    dungeon.map.origenZ,
                    dungeon.map.origenX + dungeon.size.width,
                    context.heightAccessor().getMaxBuildHeight(),
                    dungeon.map.origenZ + dungeon.size.width);
            structurePiecesBuilder.addPiece(new DLDungeonPiece(boundingBox, dungeon));
        });
        return Optional.of(stub);
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.DLDUNGEON;
    }

}
