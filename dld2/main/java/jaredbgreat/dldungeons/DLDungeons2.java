package jaredbgreat.dldungeons;

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

@Mod(modid=DLD2Info.ID, name=DLD2Info.NAME, version=DLD2Info.VERSION, 
	acceptableRemoteVersions="*")
public class DLDungeons2 {
	public  static IProfiler profiler;
	
    @Instance(Info.ID)
    public static DLDungeons2 instance;
    
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Logging.logInfo(Info.NAME + " is in preInit, should now load config.");
    	Logging.logInfo("Config should now be loaded.");  
    	if(ConfigHandler.profile) profiler = new DLDProfile();
    	else profiler = new DoNothing();
    }
    
    
    @EventHandler 
    public void init(FMLInitializationEvent event) {}
    
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {}
    
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {}
	
	
	
}
