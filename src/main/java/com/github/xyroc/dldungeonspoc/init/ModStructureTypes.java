package com.github.xyroc.dldungeonspoc.init;

import com.github.xyroc.dldungeonspoc.DLDungeons;
import com.github.xyroc.dldungeonspoc.structure.DLDungeonStructure;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class ModStructureTypes {

    public static StructureType<?> DLDUNGEON;

    public static void init() {
        DLDUNGEON = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, DLDungeons.resource("dldungeon"), () -> DLDungeonStructure.CODEC);
    }

}
