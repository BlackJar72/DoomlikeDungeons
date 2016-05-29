package jaredbgreat.dldungeons.commands;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
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
	public String getCommandName() {
		return "dlddimid";
	}

	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/dlddimid";
	}
	

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException  {
		int dim =  sender.getEntityWorld().provider.getDimension();
		System.out.println(dim);
		sender.addChatMessage(new TextComponentString("[DLDUNGEONS] " 
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
