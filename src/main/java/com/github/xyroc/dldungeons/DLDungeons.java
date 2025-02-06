package com.github.xyroc.dldungeons;

import com.github.xyroc.dldungeons.datapack.ResourceReloadHandler;
import com.github.xyroc.dldungeons.init.ModStructurePieceTypes;
import com.github.xyroc.dldungeons.init.ModStructureTypes;
import jaredbgreat.dldungeons.config.Config;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DLDungeons.MODID)
public class DLDungeons {
    public static final String MODID = "dldungeonsjbg";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DLDungeons(IEventBus modEventBus) {

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        // Register ourselves for server and other game events we are interested in
        forgeEventBus.addListener(this::onAddReloadListener);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Register structure types.
        ModStructureTypes.init();
        // Register structure piece types.
        ModStructurePieceTypes.init();

    }

    private void onAddReloadListener(final AddReloadListenerEvent event) {
        event.addListener(new ResourceReloadHandler());
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
