package jaredbgreat.dldungeons;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jaredbgreat.dldungeons.configs.ConfigHandler;
import jaredbgreat.dldungeons.configs.MasterConfig;
import jaredbgreat.dldungeons.debug.DLDProfile;
import jaredbgreat.dldungeons.debug.DoNothing;
import jaredbgreat.dldungeons.debug.IProfiler;
import jaredbgreat.dldungeons.pieces.DebugPole;
import jaredbgreat.dldungeons.structure.DungeonStructure;
import jaredbgreat.dldungeons.themes.ThemeReader;
import jaredbgreat.dldungeons.themes.ThemeType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

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
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A procedural multi-room dungeon generator for Minecraft inspired by the 
 * Oblige 3.57 level generator for Doom / Doom II / Heretic / Hexen etc.
 */
@Mod(Info.ID)
public class DoomlikeDungeons {
	public  static DoomlikeDungeons instance;
	public  static IProfiler profiler;
	public  static final String modid = Info.ID;
	public  static final Logger logger = LogManager.getLogger(Info.ID);
	// TODO: Re-implement these
	final Structure<NoFeatureConfig> dstruct;
	//private static GenerationHandler genHandler;
	//public  static IProfiler profiler;



	public DoomlikeDungeons() {
		instance = this;
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MasterConfig.config);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegistries);
		MasterConfig.loadConfig(MasterConfig.config);
		dstruct = DungeonStructure.DUNGEON_FEATURE;
		Feature.STRUCTURES.put(DungeonStructure.NAME.toLowerCase(Locale.ROOT), dstruct);
		MinecraftForge.EVENT_BUS.register(this);
	}


    private void setup(final FMLCommonSetupEvent event) {
    	logger.info(Info.TAG + Info.NAME + " should now load config.");
    	ConfigHandler.init();
		logger.info(Info.TAG + " Config should now be loaded.");
    	if(ConfigHandler.profile) {
    		profiler = new DLDProfile();
    	} else {
    		profiler = new DoNothing();
    	}
    	registerDungeon();
		ConfigHandler.generateLists();
		ThemeReader.readThemes();
		ThemeType.SyncMobLists();
    }


    private void clientRegistries(final FMLCommonSetupEvent event) {}


	public static Logger getLogger() {
		return logger;
	}
	
	
	public void registerDungeon() {
		for(Biome biome : ForgeRegistries.BIOMES) {
			biome.addStructure(dstruct, IFeatureConfig.NO_FEATURE_CONFIG);
			biome.addFeature(Decoration.UNDERGROUND_STRUCTURES,  
					Biome.createDecoratedFeature(dstruct, 
							NoFeatureConfig.NO_FEATURE_CONFIG, 
							Placement.NOPE, 
							NoPlacementConfig.NO_PLACEMENT_CONFIG));
		}
	}


}
