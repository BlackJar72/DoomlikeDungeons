package com.github.xyroc.dldungeons.init;

import com.github.xyroc.dldungeons.DLDungeons;
import com.github.xyroc.dldungeons.structure.piece.DLDungeonPiece;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ModStructurePieceTypes {

    public static StructurePieceType DLDUNGEON_PIECE;

    public static void init() {
        // Reference to the piece classes' constructor which takes NBT data so that the game knows how to load it from disk.
        final StructurePieceType.ContextlessType type = DLDungeonPiece::new;
        DLDUNGEON_PIECE = register("dldungeonpiece", type);
    }

    private static StructurePieceType register(String name, StructurePieceType.ContextlessType type) {
        return Registry.register(Registry.STRUCTURE_PIECE, DLDungeons.resource(name), type);
    }

}
