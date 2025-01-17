package com.github.xyroc.dldungeonspoc.init;

import com.github.xyroc.dldungeonspoc.DLDPoC;
import com.github.xyroc.dldungeonspoc.structure.DLDungeonStructure;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class ModStructureTypes {

    public static StructureType<?> DLDUNGEON;

    public static void init() {
        DLDUNGEON = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, DLDPoC.resource("dldungeon"), () -> DLDungeonStructure.CODEC);
    }

}
