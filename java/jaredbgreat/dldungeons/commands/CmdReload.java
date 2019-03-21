package jaredbgreat.dldungeons.commands;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.ReadAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CmdReload extends CommandBase {

    public CmdReload() {
		super();
	}


	@Override
	public String getName() {
		return "dldreload";
	}


	@Override
	public String getUsage(ICommandSender sender) {
		return "/dldreload";
	}

	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException  {		
			System.out.println("[DLDUNGEONS] " 
					+ sender.getDisplayName().getFormattedText() 
					+ " Used /dldreload command; Reloading config file now.");
			ReadAPI.reloadConfig();
			if(ConfigHandler.announceCommands) sender
			.sendMessage(new TextComponentString("[DLDUNGEONS] " 
					+ sender.getDisplayName().getFormattedText() 
					+ " Used /dldreload command; Reloading config file now."));
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
