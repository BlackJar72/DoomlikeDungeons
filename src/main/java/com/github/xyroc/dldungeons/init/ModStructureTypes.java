package com.github.xyroc.dldungeons.init;

import com.github.xyroc.dldungeons.DLDungeons;
import com.github.xyroc.dldungeons.structure.DLDungeonStructure;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class ModStructureTypes {

    public static StructureType<?> DLDUNGEON;

    public static void init() {
        DLDUNGEON = Registry.register(Registry.STRUCTURE_TYPES, DLDungeons.resource("dldungeon"), () -> DLDungeonStructure.CODEC);
    }

}
