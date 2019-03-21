package jaredbgreat.dldungeons.commands;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;


public class CmdDimID extends CommandBase {

    public CmdDimID() {
		super();
	}


	private List aliases = new ArrayList<String>();




	@Override
	public String getName() {
		return "dlddimid";
	}


	@Override
	public String getUsage(ICommandSender sender) {
		return "/dlddimid";
	}
	

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException  {
		int dim =  sender.getEntityWorld().provider.getDimension();
		System.out.println(dim);
		sender.sendMessage(new TextComponentString("[DLDUNGEONS] " 
				+ sender.getDisplayName().getFormattedText() 
				+ " is in dimension " + dim));
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
