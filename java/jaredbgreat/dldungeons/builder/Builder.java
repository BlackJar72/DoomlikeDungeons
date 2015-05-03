package jaredbgreat.dldungeons.builder;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import static jaredbgreat.dldungeons.builder.DBlock.lapis;
import static jaredbgreat.dldungeons.builder.DBlock.placeBlock;
import static jaredbgreat.dldungeons.builder.DBlock.quartz;
import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.planner.Dungeon;

import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;


public class Builder {
	
	private static boolean debugPole = false;
	

	public static void placeDungeon(Random random, int chunkX, int chunkZ, World world) throws Throwable {	
		if(world.isRemote) return; // Do not perform world-gen on the client!z
		DoomlikeDungeons.profiler.startTask("Create Dungeons");
		Dungeon dungeon = new Dungeon(random, world.getBiomeGenForCoords(new BlockPos((chunkX * 16), 64, (chunkZ * 16))), 
									  world, chunkX, chunkZ);

		System.out.println("[DLDUNGONS] Running Builder.buildDungeon; building dungeon");
		buildDungeon(dungeon);
		System.out.println("[DLDUNGONS] Dungeon should be built now!");
		//if(true) debuggingPole(world, chunkX, chunkZ, dungeon);
		dungeon.finalize();
		dungeon = null;
		DoomlikeDungeons.profiler.endTask("Create Dungeons");
	}
	

	public static void placeDungeon(Random random, int chunkX, int chunkZ, World world,
						IChunkProvider chunkGenerator, IChunkProvider chunkProvider) throws Throwable {	
		if(world.isRemote) return; // Do not perform world-gen on the client!
		DoomlikeDungeons.profiler.startTask("Create Dungeons");
		Dungeon dungeon = new Dungeon(random, world.getBiomeGenForCoords(new BlockPos((chunkX * 16), 64, (chunkZ * 16))), 
									  world, chunkX, chunkZ);
		
		System.out.println("[DLDUNGONS] Running Builder.buildDungeon; building dungeon");
		if(dungeon.theme != null) {
			if(debugPole) debuggingPole(world, chunkX, chunkZ, dungeon);
			buildDungeon(dungeon);
			System.out.println("[DLDUNGONS] Dungeon should be built now!");
		}
		dungeon.finalize();
		dungeon = null;
		DoomlikeDungeons.profiler.endTask("Create Dungeons");
	}
	
	
	public static void buildDungeon(Dungeon dungeon /*TODO: Parameters*/) {
		//System.out.println("[DLDUNGONS] Inside Builder.placeDungeon; building dungeon");
		if(dungeon.theme != null) dungeon.map.build(dungeon);
	}
	
	
	public static void debuggingPole(World world, int chunkX, int chunkZ, Dungeon dungeon) {
		//Pointless stand-in for generation testing, and to help find dungeons later
		int x = (chunkX * 16) + 8;
		int z = (chunkZ * 16) + 8;
		for(int y = -16; y <= 241; y++) placeBlock(world, x, y, z, quartz);
		for(int i = -dungeon.size.radius; i <= dungeon.size.radius; i++) {
			placeBlock(world, x - dungeon.size.radius, 80, z + i, lapis);
			placeBlock(world, x + dungeon.size.radius, 80, z + i, lapis);
			placeBlock(world, x + i, 80, z - dungeon.size.radius, lapis);
			placeBlock(world, x + i, 80, z + dungeon.size.radius, lapis);
		}		
	}
	
	
	public static void setDebugPole(boolean val) {
		debugPole = val;
	}
}
