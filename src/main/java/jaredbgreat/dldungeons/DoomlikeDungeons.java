package jaredbgreat.dldungeons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jaredbgreat.dldungeons.configs.ConfigHandler;
import jaredbgreat.dldungeons.configs.MasterConfig;

/* 
 * A procedural multi-room dungeon generator for Minecraft inspired by the 
 * Oblige 3.57 level generator for Doom / Doom II / Heretic / Hexen etc.
 * 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * A procedural multi-room dungeon generator for Minecraft inspired by the 
 * Oblige 3.57 level generator for Doom / Doom II / Heretic / Hexen etc.
 */
@Mod(Info.ID)
public class DoomlikeDungeons {
	public  static DoomlikeDungeons instance;
	public  static final String modid = Info.ID;
	public static final Logger logger = LogManager.getLogger(Info.ID);
	// TODO: Re-implement these
	//private static GenerationHandler dungeonPlacer;
	//public  static IProfiler profiler;



	public DoomlikeDungeons() {
		instance = this;
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MasterConfig.config);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		MasterConfig.loadConfig(MasterConfig.config);
		MinecraftForge.EVENT_BUS.register(this);

	}


    private void setup(final FMLCommonSetupEvent event) {
    	//FIXME: Most of this needs to be done better
		//TODO: Old preInit()
    	logger.info(Info.TAG + Info.NAME + " should now load config.");
    	ConfigHandler.init();
		logger.info(Info.TAG + " Config should now be loaded.");
    	//if(ConfigHandler.profile) profiler = new DLDProfile();
    	//else profiler = new DoNothing();

		//FIXME: GenerationHandler won't be what it was! A Feature?  Or a Carver? But not what it has been....
		//TODO: Old init()
		//if(ConfigHandler.naturalSpawn) dungeonPlacer = new GenerationHandler();


		// TODO: Re-Write these -- probably little change here, but some.
		// TODO: Old postInit()
		//ConfigHandler.generateLists();
		//ThemeReader.readThemes();
		//ThemeType.SyncMobLists();
    }


    private void clientRegistries(final FMLCommonSetupEvent event) {}


	public static Logger getLogger() {
		return logger;
	}


}
