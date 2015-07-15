package jaredbgreat.dldungeons.commands;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.server.command.ForgeCommand;


public class CmdGo extends ForgeCommand {

    public CmdGo(MinecraftServer server) {
		super(server);
	}


	private List aliases = new ArrayList<String>();
	
    
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	
	@Override
	public String getCommandName() {
		return "dldgo";
	}

	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/dldgo";
	}

	
	@Override
	public List getCommandAliases() {
		return aliases;
	}

	
	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		EntityPlayer player = icommandsender.getEntityWorld().getPlayerEntityByName(icommandsender.getCommandSenderName());
		
		int oldDim = player.dimension;
		int dim = -6;
		if(player.dimension == -6) dim = 0;
		
		MinecraftServer server = MinecraftServer.getServer();
				
        WorldServer oserver = DimensionManager.getWorld(oldDim);
		WorldServer dserver = DimensionManager.getWorld(dim);
		
		//oserver.theProfiler.startSection("changeDimension");
		player.dimension = dim;
		oserver.removeEntity(player);
		player.isDead = false;
		//oserver.theProfiler.startSection("reposition");
		//oserver.theProfiler.startSection("moving");
        player.posY = dserver.getTopSolidOrLiquidBlock(player.chunkCoordX, player.chunkCoordZ);
		dserver.spawnEntityInWorld(player);
		player.setWorld(dserver);
		//oserver.theProfiler.endSection();
		//oserver.theProfiler.endStartSection("reloading");
        //EntityPlayer newplayer = (EntityPlayer) EntityList.createEntityByName(EntityList.getEntityString(player), dserver);
        //newplayer.copyDataFrom(player, true);
        //newplayer.posY = dserver.getTopSolidOrLiquidBlock(player.chunkCoordX, player.chunkCoordZ);
		//dserver.spawnEntityInWorld(newplayer);
		//player.isDead = true;
		//oserver.theProfiler.endSection();
        oserver.resetUpdateEntityTick();
        dserver.resetUpdateEntityTick();
		//oserver.theProfiler.endSection();
	}
	
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 7; // No one should really be using this command for now!
    }

	
	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return null;
	}

	
	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

}
