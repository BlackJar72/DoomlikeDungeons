package com.github.xyroc.dldungeons.init;

import com.github.xyroc.dldungeons.DLDungeons;
import com.github.xyroc.dldungeons.structure.DLDungeonStructure;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructureTypes {

    public static StructureType<?> DLDUNGEON;

    public static void init() {
//        DLDUNGEON = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, DLDungeons.resource("dldungeon"), ()
//                -> DLDungeonStructure.CODEC);
    }

    public static final DeferredRegister<StructureType<?>> BLOCKS = DeferredRegister.create(
            // The registry we want to use.
            // Minecraft's registries can be found in BuiltInRegistries, NeoForge's registries can be found in NeoForgeRegistries.
            // Mods may also add their own registries, refer to the individual mod's documentation or source code for where to find them.
            BuiltInRegistries.STRUCTURE_TYPE,
            // Our mod id.
            DLDungeons.MODID
    );

}
