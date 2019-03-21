package jaredbgreat.dldungeons.commands;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.themes.BiomeSets;
import jaredbgreat.dldungeons.themes.ThemeReader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CmdInstallThemes extends CommandBase {

	public CmdInstallThemes() {
		super();
	}


	@Override
	public String getName() {
		return "dldInstallThemes";
	}


	@Override
	public String getUsage(ICommandSender sender) {
		return "/dldInstallThemes";
	}

	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException  {
		if(!ConfigHandler.installCmd) return; // Should never happen, but a failsafe
		Externalizer exporter = new Externalizer(ThemeReader.getThemesDir());
		exporter.makeThemes();
		exporter = new Externalizer(ConfigHandler.getConfigDir());
		exporter.makeChestCfg();
		sender.sendMessage(new TextComponentString("[DLDUNGEONS] " 
				+ sender.getDisplayName().getFormattedText() 
				+ " has reinstalled default themes (nothing will be overwritten)"));
		BiomeSets.reset();
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
