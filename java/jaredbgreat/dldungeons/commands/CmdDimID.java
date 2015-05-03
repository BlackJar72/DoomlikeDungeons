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

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.server.command.ForgeCommand;


public class CmdDimID extends ForgeCommand {

    public CmdDimID(MinecraftServer server) {
		super(server);
	}


	private List aliases = new ArrayList<String>();
	
    
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	
	@Override
	public String getName() {
		return "dlddimid";
	}

	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/dlddimid";
	}

	
	@Override
	public List getAliases() {
		return aliases;
	}
	

	@Override
	public void execute(ICommandSender icommandsender, String[] astring) {
		int dim =  icommandsender.getEntityWorld().provider.getDimensionId();
		System.out.println(dim);
		icommandsender.addChatMessage(new ChatComponentText("[DLDUNGEONS] " 
				+ icommandsender.getName() 
				+ " is in dimension " + dim));
//		..setColor(EnumChatFormatting.DARK_PURPLE).setItalic(true));
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
