package jaredbgreat.dldungeons;

/**
 * An algorithmic multi-room dungeon generator for Minecraft inspired by the 
 * Oblige 3.57 level generator for Doom / Doom II / Heretic / Hexen etc.
 */
/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.commands.CmdDimID;
import jaredbgreat.dldungeons.commands.CmdForceInstallThemes;
import jaredbgreat.dldungeons.commands.CmdInstallThemes;
import jaredbgreat.dldungeons.commands.CmdReload;
import jaredbgreat.dldungeons.commands.CmdSpawn;
import jaredbgreat.dldungeons.debug.DLDProfile;
import jaredbgreat.dldungeons.debug.DoNothing;
import jaredbgreat.dldungeons.debug.IProfiler;
import jaredbgreat.dldungeons.minigame.DLWorldProvider;
import jaredbgreat.dldungeons.themes.ThemeReader;
import jaredbgreat.dldungeons.themes.ThemeType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;


@Mod(modid=Info.ID, name=Info.NAME, version=Info.VERSION) 
//Server-side world-gen mod; no new blocks/items/mobs
@NetworkMod(clientSideRequired=false, serverSideRequired=false)       
public class DoomlikeDungeons {
	private static GenerationHandler dungeonPlacer;
	public  static IProfiler profiler;
	
    @Instance(value = Info.ID)
    public static DoomlikeDungeons instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	System.out.println(Info.NAME + " is in preInit, should now load config.");
    	ConfigHandler.configDir = ConfigHandler.findConfigDir(event.getModConfigurationDirectory());
    	ConfigHandler.init();
    	System.out.println("[DLDUNGEONS] Config should now be loaded."); 
    	if(ConfigHandler.profile) profiler = new DLDProfile();
    	else profiler = new DoNothing();
    }
    
    @EventHandler 
    public void init(FMLInitializationEvent event) {
    	if(ConfigHandler.naturalSpawn) dungeonPlacer = new GenerationHandler();
  	  	MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	ThemeReader.readThemes(); 
    	ThemeType.SyncMobLists();
    	ConfigHandler.generateLists();
    	//initMinigame();  // Do not initialize any of this!  Its not ready for use!
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
    	event.registerServerCommand(new CmdSpawn(event.getServer()));
    	event.registerServerCommand(new CmdReload(event.getServer()));
    	event.registerServerCommand(new CmdDimID(event.getServer()));
    	if(ConfigHandler.installCmd) {
    		event.registerServerCommand(new CmdInstallThemes(event.getServer()));
    		event.registerServerCommand(new CmdForceInstallThemes(event.getServer()));
    	}
    	//event.registerServerCommand(new CmdGo(event.getServer()));
    	ConfigHandler.generateLists();
    }
    
    
    public void initMinigame() {
    	DimensionManager.registerProviderType(-6, DLWorldProvider.class, true);
    	DimensionManager.registerDimension(-6, -6);
    }

}
