package jaredbgreat.dldungeons;

/**
 * An algorithmic multi-room dungeon generator for Minecraft inspired by the 
 * Oblige 3.57 level generator for Doom / Doom II / Heretic / Hexen etc.
 */

/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	


import jaredbgreat.dldungeons.commands.CmdDimID;
import jaredbgreat.dldungeons.commands.CmdForceInstallThemes;
import jaredbgreat.dldungeons.commands.CmdInstallThemes;
import jaredbgreat.dldungeons.commands.CmdReload;
import jaredbgreat.dldungeons.commands.CmdSpawn;
import jaredbgreat.dldungeons.debug.DLDProfile;
import jaredbgreat.dldungeons.debug.DoNothing;
import jaredbgreat.dldungeons.debug.IProfiler;
import jaredbgreat.dldungeons.themes.ThemeReader;
import jaredbgreat.dldungeons.themes.ThemeType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;


//Temporary stand in names, not for release
@Mod(modid=Info.ID, name=Info.NAME, version=Info.VERSION) 
//Server-side world-gen mod; no new blocks/items/mobs
//@NetworkMod(clientSideRequired=false, serverSideRequired=false)       
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
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	ConfigHandler.generateLists();
    	ThemeReader.readThemes(); 
    	ThemeType.SyncMobLists();
    	
		System.out.println();
		System.out.println("OUTPUT OF MOBS BY THEME:");
//    	for(Theme theme : Theme.themes) {
//    		System.out.println();
//    		theme.mobsOut();
//    		System.out.println();
//    	}
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
    	//ConfigHandler.reload();
    }
    
    
    
    
    

}
