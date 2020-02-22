package jaredbgreat.dldungeons;

/* 
 * A procedural multi-room dungeon generator for Minecraft inspired by the 
 * Oblige 3.57 level generator for Doom / Doom II / Heretic / Hexen etc.
 * 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.commands.CmdDimID;
import jaredbgreat.dldungeons.commands.CmdForceInstallThemes;
import jaredbgreat.dldungeons.commands.CmdInstallThemes;
import jaredbgreat.dldungeons.commands.CmdReload;
import jaredbgreat.dldungeons.commands.CmdSpawn;
import jaredbgreat.dldungeons.debug.DLDProfile;
import jaredbgreat.dldungeons.debug.DoNothing;
import jaredbgreat.dldungeons.debug.IProfiler;
import jaredbgreat.dldungeons.debug.Logging;
import jaredbgreat.dldungeons.themes.ThemeReader;
import jaredbgreat.dldungeons.themes.ThemeType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


/**
 * A procedural multi-room dungeon generator for Minecraft inspired by the 
 * Oblige 3.57 level generator for Doom / Doom II / Heretic / Hexen etc.
 */
@Mod(modid=Info.ID, name=Info.NAME, version=Info.VERSION, acceptableRemoteVersions="*") 
public class DoomlikeDungeons {
	private static GenerationHandler dungeonPlacer;
	public  static IProfiler profiler;
	
    @Instance(Info.ID)
    public static DoomlikeDungeons instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Logging.logInfo(Info.NAME + " is in preInit, should now load config.");
    	ConfigHandler.configDir = ConfigHandler.findConfigDir(event.getModConfigurationDirectory());
    	ConfigHandler.init();
    	Logging.logInfo("Config should now be loaded.");  
    	if(ConfigHandler.profile) profiler = new DLDProfile();
    	else profiler = new DoNothing();
    }
    
    @EventHandler 
    public void init(FMLInitializationEvent event) {
    	if(ConfigHandler.naturalSpawn) dungeonPlacer = new GenerationHandler();
    	RegisteredBlock.add("minecraft:air");
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	ConfigHandler.generateLists();
    	ThemeReader.readThemes(); 
    	ThemeType.SyncMobLists();
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new CmdSpawn());
    	event.registerServerCommand(new CmdReload());
    	event.registerServerCommand(new CmdDimID());
    	//event.registerServerCommand(new CmdTPDim());
    	if(ConfigHandler.installCmd) {
    		event.registerServerCommand(new CmdInstallThemes());
    		event.registerServerCommand(new CmdForceInstallThemes());
    	}
    }
}
