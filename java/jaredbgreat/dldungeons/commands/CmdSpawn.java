package jaredbgreat.dldungeons.commands;


import static jaredbgreat.dldungeons.builder.Builder.placeDungeon;
import jaredbgreat.dldungeons.ConfigHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.server.command.ForgeCommand;


public class CmdSpawn extends ForgeCommand {

    public CmdSpawn(MinecraftServer server) {
		super(server);
	}


	private List aliases = new ArrayList<String>();
	
    
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	
	@Override
	public String getCommandName() {
		return "dldspawn";
	}

	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/dldspawn";
	}

	
	@Override
	public List getCommandAliases() {
		return aliases;
	}

	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		World world =  icommandsender.getEntityWorld();
		ChunkCoordinates location = icommandsender.getPlayerCoordinates();
		try {
			placeDungeon(new Random(), 
					location.posX / 16, 
					location.posZ / 16, 
					world);
		} catch (Throwable e) {
			System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
			e.printStackTrace();
		}
		if(ConfigHandler.announceCommands) icommandsender.addChatMessage(new ChatComponentText("[DLDUNGEONS] " 
				+ icommandsender.getCommandSenderName() 
				+ " just spawned a dungeon at X=" + location.posX 
				+ ", Z=" + location.posZ));
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
