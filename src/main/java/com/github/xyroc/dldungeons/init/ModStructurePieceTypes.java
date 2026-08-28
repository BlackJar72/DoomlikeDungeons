package com.github.xyroc.dldungeons.init;

import com.github.xyroc.dldungeons.DLDungeons;
import com.github.xyroc.dldungeons.structure.piece.DLDungeonPiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructurePieceTypes {

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, DLDungeons.MODID);

    // Reference to the piece classes' constructor which takes NBT data so that the game knows how to load it from disk.
    public static final DeferredHolder<StructurePieceType, StructurePieceType> DLDUNGEON_PIECE =
            STRUCTURE_PIECE_TYPES.register("dldungeonpiece",
                    () -> (StructurePieceType.ContextlessType) DLDungeonPiece::new);

}
