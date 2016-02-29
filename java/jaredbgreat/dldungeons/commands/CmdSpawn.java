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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.server.command.ForgeCommand;


public class CmdSpawn extends CommandBase {

    public CmdSpawn() {
		super();
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
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		World world =  icommandsender.getEntityWorld();
		BlockPos location = icommandsender.getPosition();
		try {
			placeDungeon(new Random(), 
					location.getX() / 16, 
					location.getZ() / 16, 
					world);
		} catch (Throwable e) {
			System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
			e.printStackTrace();
		}
		if(ConfigHandler.announceCommands) icommandsender.addChatMessage(new ChatComponentText("[DLDUNGEONS] " 
				+ icommandsender.getDisplayName().getFormattedText() 
				+ " just spawned a dungeon at X=" + location.getX()
				+ ", Z=" + location.getZ()));
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
