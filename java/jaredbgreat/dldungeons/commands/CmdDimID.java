package jaredbgreat.dldungeons.commands;


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
	public String getCommandName() {
		return "dlddimid";
	}

	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/dlddimid";
	}

	
	@Override
	public List getCommandAliases() {
		return aliases;
	}

	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		int dim =  icommandsender.getEntityWorld().provider.dimensionId;
		System.out.println(dim);
		icommandsender.addChatMessage(new ChatComponentText("[DLDUNGEONS] " 
				+ icommandsender.getCommandSenderName() 
				+ " is in dimension " + dim));
//		..setColor(EnumChatFormatting.DARK_PURPLE).setItalic(true));
	}
	
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	
	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

}
