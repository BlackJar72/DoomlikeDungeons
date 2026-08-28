package com.github.xyroc.dldungeons.init;

import com.github.xyroc.dldungeons.DLDungeons;
import com.github.xyroc.dldungeons.structure.DLDungeonStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructureTypes {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, DLDungeons.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<DLDungeonStructure>> DLDUNGEON =
            STRUCTURE_TYPES.register("dldungeon", () -> () -> DLDungeonStructure.CODEC);

}
