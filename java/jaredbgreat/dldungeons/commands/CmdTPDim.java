package jaredbgreat.dldungeons.commands;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.Info;
import jaredbgreat.dldungeons.tputils.Teleportation;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import com.google.common.collect.Lists;

public class CmdTPDim extends CommandBase {
	private static final String NAME = "tptodim";
	private final List<String> aliases = Lists.newArrayList(Info.ID, NAME, "tpdim", "todim");

	@Override
	public String getName() {
		return NAME;
	}
	

	@Override
	public String getUsage(ICommandSender sender) {
		return "/" + NAME + " [dimension ID] (optional: [x] [y] [z])";
	}
	
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	
	@Override
	public List<String> getAliases() {
		return aliases;
	}
	

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException {
		if(!(sender instanceof EntityPlayer)) {
			return;
		}
		if(args.length < 1) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED 
					+ "Not enougn arguments; use \"tptodim [dimension ID]\"."));
			return;
		}
		int dimID;
		try {
			dimID = Integer.parseInt(args[0]);
		} catch(NumberFormatException ex) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED 
					+ "Invalid Dimension ID (must be an integer)"));
			return;
		}
		if(args.length < 4) {
			BlockPos pos = sender.getPosition();
			Teleportation.tpToBlockPos((EntityPlayer)sender, dimID, 
					pos.getX(), pos.getY(), pos.getZ());
		} else {
			int x, y, z;
			try {
				x = Integer.parseInt(args[1]);
				y = Integer.parseInt(args[2]);
				z = Integer.parseInt(args[3]);
			} catch(NumberFormatException ex) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED 
						+ "Invalid coordinates (must by integers)"));
				return;
			}
			Teleportation.tpToBlockPos((EntityPlayer)sender, dimID, x, y, z);
		}
	}

}
