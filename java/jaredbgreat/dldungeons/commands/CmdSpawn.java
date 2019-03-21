package jaredbgreat.dldungeons.commands;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
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
