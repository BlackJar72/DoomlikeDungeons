package jaredbgreat.dldungeons.commands;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
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
	public String getCommandName() {
		return "dldreload";
	}

	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/dldreload";
	}

	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException {		
			System.out.println("[DLDUNGEONS] " 
					+ sender.getDisplayName().getFormattedText() 
					+ " Used /dldreload command; Reloading config file now.");
			ReadAPI.reloadConfig();
			if(ConfigHandler.announceCommands) sender
			.addChatMessage(new TextComponentString("[DLDUNGEONS] " 
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
