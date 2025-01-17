package com.github.xyroc.dldungeonspoc.init;

import com.github.xyroc.dldungeonspoc.DLDPoC;
import com.github.xyroc.dldungeonspoc.structure.piece.DLDungeonPiece;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ModStructurePieceTypes {

    public static StructurePieceType DLDUNGEON_PIECE;

    public static void init() {
        // Reference to the piece classes' constructor which takes NBT data so that the game knows how to load it from disk.
        final StructurePieceType.ContextlessType type = DLDungeonPiece::new;
        DLDUNGEON_PIECE = register("dldungeonpiece", type);
    }

    private static StructurePieceType register(String name, StructurePieceType.ContextlessType type) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, DLDPoC.resource(name), type);
    }

}
