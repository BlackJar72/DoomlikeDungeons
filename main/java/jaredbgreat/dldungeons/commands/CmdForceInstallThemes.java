package jaredbgreat.dldungeons.commands;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.themes.BiomeSets;
import jaredbgreat.dldungeons.themes.ThemeReader;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CmdForceInstallThemes extends CommandBase {

	public CmdForceInstallThemes() {
		super();
	}


	private List aliases = new ArrayList<String>();


	@Override
	public String getName() {
		return "dldForceInstallThemes";
	}


	@Override
	public String getUsage(ICommandSender sender) {
		return "/dldForceInstallThemes";
	}

	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException {
		if(!ConfigHandler.installCmd) return; // Should never happen, but a failsafe
		Externalizer exporter = new Externalizer(ThemeReader.getThemesDir());
		exporter.forceThemes();
		exporter = new Externalizer(ConfigHandler.getConfigDir());
		exporter.forceChestCfg();
		sender.sendMessage(new TextComponentString("[DLDUNGEONS] " 
				+ sender.getDisplayName().getFormattedText() 
				+ " has forced reinstalled default themes (existing themes will be overwritten!)"));
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
