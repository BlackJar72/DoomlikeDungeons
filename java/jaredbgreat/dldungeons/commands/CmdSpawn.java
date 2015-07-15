package jaredbgreat.dldungeons.commands;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import static jaredbgreat.dldungeons.builder.Builder.placeDungeon;

import jaredbgreat.dldungeons.ConfigHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.server.command.ForgeCommand;


public class CmdSpawn extends ForgeCommand  {

    public CmdSpawn(MinecraftServer server) {
		super(server);
		// TODO Auto-generated constructor stub
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
		
		if(ConfigHandler.announceCommands) icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("[DLDUNGEONS] " 
				+ icommandsender.getCommandSenderName() 
				+ " just spawned a dungeon at X=" + location.posX 
				+ ", Z=" + location.posZ).setColor(EnumChatFormatting.DARK_PURPLE).setItalic(true));
		
		try {
			placeDungeon(new Random(), 
					location.posX / 16, 
					location.posZ / 16, 
					world);
		} catch (Throwable e) {
			System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("[DLDUNGEONS] Danger! " 
					+ "Failed to finalize a dungeon after building!")
					.setColor(EnumChatFormatting.RED).setBold(true).setItalic(true));
			e.printStackTrace();
		}
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
