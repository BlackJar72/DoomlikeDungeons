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

import java.util.Random;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;


public class CmdSpawn extends CommandBase {

    public CmdSpawn() {
		super();
	}


	@Override
	public String getName() {
		return "dldspawn";
	}


	@Override
	public String getUsage(ICommandSender sender) {
		return "/dldspawn";
	}

	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException  {
		World world =  sender.getEntityWorld();
		BlockPos location = sender.getPosition();
		try {
			placeDungeon(new Random(), 
					location.getX() / 16, 
					location.getZ() / 16, 
					world);
		} catch (Throwable e) {
			System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
			e.printStackTrace();
		}
		if(ConfigHandler.announceCommands) sender.sendMessage(new TextComponentString("[DLDUNGEONS] " 
				+ sender.getDisplayName().getFormattedText() 
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
