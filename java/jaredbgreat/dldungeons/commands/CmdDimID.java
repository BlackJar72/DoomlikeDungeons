package jaredbgreat.dldungeons.commands;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import static jaredbgreat.dldungeons.builder.Builder.placeDungeon;

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


public class CmdDimID extends ForgeCommand  {

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
		icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("[DLDUNGEONS] " 
				+ icommandsender.getCommandSenderName() 
				+ " is in dimension " + dim).setColor(EnumChatFormatting.DARK_PURPLE).setItalic(true));
	}
	
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 1;
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
