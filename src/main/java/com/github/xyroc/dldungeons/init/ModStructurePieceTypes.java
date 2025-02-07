package com.github.xyroc.dldungeons.init;

import com.github.xyroc.dldungeons.DLDungeons;
import com.github.xyroc.dldungeons.structure.piece.DLDungeonPiece;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructurePieceTypes {

    public static StructurePieceType DLDUNGEON_PIECE;

    public static void init() {
        // Reference to the piece classes' constructor which takes NBT data so that the game knows how to load it from disk.
        final StructurePieceType.ContextlessType type = DLDungeonPiece::new;
        DLDUNGEON_PIECE = register("dldungeonpiece", type);
    }

    private static StructurePieceType register(String name, StructurePieceType.ContextlessType type) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, DLDungeons.resource(name), type);
    }

    public static final DeferredRegister<StructurePieceType> DUNGEON_PIECE = DeferredRegister.create(
            // The registry we want to use.
            // Minecraft's registries can be found in BuiltInRegistries, NeoForge's registries can be found in NeoForgeRegistries.
            // Mods may also add their own registries, refer to the individual mod's documentation or source code for where to find them.
            BuiltInRegistries.STRUCTURE_PIECE,
            // Our mod id.
            DLDungeons.MODID
    );

}
