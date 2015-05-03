package jaredbgreat.dldungeons.commands;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.themes.BiomeLists;
import jaredbgreat.dldungeons.themes.ThemeReader;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.server.command.ForgeCommand;

public class CmdInstallThemes extends ForgeCommand {

	public CmdInstallThemes(MinecraftServer server) {
		super(server);
	}


	private List aliases = new ArrayList<String>();
	
    
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	
	@Override
	public String getName() {
		return "dldInstallThemes";
	}

	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/dldInstallThemes";
	}

	
	@Override
	public List getAliases() {
		return aliases;
	}

	
	@Override
	public void execute(ICommandSender icommandsender, String[] astring) {
		if(!ConfigHandler.installCmd) return; // Should never happen, but a failsafe
		Externalizer exporter = new Externalizer(ThemeReader.getThemesDir());
		exporter.makeThemes();
		exporter = new Externalizer(ConfigHandler.getConfigDir());
		exporter.makeChestCfg();
		icommandsender.addChatMessage(new ChatComponentText("[DLDUNGEONS] " 
				+ icommandsender.getName() 
				+ " has reinstalled default themes (nothing will be overwritten)"));
//		..setColor(EnumChatFormatting.DARK_PURPLE).setItalic(true));
		BiomeLists.reset();
	}
	
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	
	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}
}
