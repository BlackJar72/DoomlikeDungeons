package jaredbgreat.dldungeons.commands;

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.ReadAPI;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.server.command.ForgeCommand;

public class CmdReload extends ForgeCommand {

    public CmdReload(MinecraftServer server) {
		super(server);
	}


	private List aliases = new ArrayList<String>();
	
    
	@Override
	public int compareTo(Object o) {
		return 0;
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
	public List getCommandAliases() {
		return aliases;
	}

	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {		
			System.out.println("[DLDUNGEONS] " + icommandsender.getCommandSenderName()
					+ " Used /dldreload command; Reloading config file now.");
			ReadAPI.reloadConfig();
			if(ConfigHandler.announceCommands) icommandsender.addChatMessage(new ChatComponentText("[DLDUNGEONS] " + icommandsender.getCommandSenderName()
					+ " Used /dldreload command; Reloading config file now."));
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
