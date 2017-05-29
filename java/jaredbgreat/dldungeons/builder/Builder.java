package jaredbgreat.dldungeons.builder;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * Forge event code by Charles Howard, 2016.
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import static jaredbgreat.dldungeons.builder.DBlock.lapis;
import static jaredbgreat.dldungeons.builder.DBlock.placeBlock;
import static jaredbgreat.dldungeons.builder.DBlock.quartz;
import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.Level;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;


public class Builder {
	
	private static boolean debugPole = false;
	
	
	/**
	 * This will place a dungeon into the world, and is called to create the dungeon object 
	 * (which plans the dungeon) when a dungeon is cheated in by command or generated through
	 * the API and have the dungeon built.  Basically, this is used any time and dungeons is 
	 * generated without the use of IChunkGenerator and IChunkProvider objects, that is, not
	 * through an IWorldGenerator.
	 * 
	 * @param random
	 * @param chunkX
	 * @param chunkZ
	 * @param world
	 * @throws Throwable
	 */
	public static void placeDungeon(Random random, int chunkX, int chunkZ, World world) throws Throwable {	
		if(world.isRemote) return; // Do not perform world-gen on the client!
		if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDungeonBegin(random, chunkX, chunkZ, world))) return;
		DoomlikeDungeons.profiler.startTask("Create Dungeons");
		Dungeon dungeon = new Dungeon(1, random, 
								world.getBiomeGenForCoords(new BlockPos((chunkX * 16), 64, (chunkZ * 16))), 
								world, chunkX, chunkZ);
		for(int i = dungeon.getNumLevels() - 1; i >= 0; i--) {
			placeLevel(dungeon.getLevel(i), chunkX, chunkZ, world); 
		}
		dungeon = null;
		MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDungeonFinish(random, chunkX, chunkZ, world, dungeon));
		DoomlikeDungeons.profiler.endTask("Create Dungeons");
	}
	
	
	/**
	 * This will place a dungeon into the world, and is called by the Generation handler to 
	 * create the dungeon object (which plans the dungeon) and have the dungeon built.
	 * 
	 * @param random
	 * @param chunkX
	 * @param chunkZ
	 * @param world
	 * @param chunkGenerator
	 * @param chunkProvider
	 * @throws Throwable
	 */
	public static void placeDungeon(Random random, int chunkX, int chunkZ, World world,
						IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) throws Throwable {	
		if(world.isRemote) return; // Do not perform world-gen on the client!
		if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDungeonBegin(random, chunkX, chunkZ, world))) return;
		DoomlikeDungeons.profiler.startTask("Create Dungeons");
		Dungeon dungeon = new Dungeon(1, random, 
							world.getBiomeGenForCoords(new BlockPos((chunkX * 16), 64, (chunkZ * 16))), 
						    world, chunkX, chunkZ);
		for(int i = dungeon.getNumLevels() - 1; i >= 0; i--) {
			placeLevel(dungeon.getLevel(i), chunkX, chunkZ, world); 
		}
		dungeon = null;
		MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDungeonFinish(random, chunkX, chunkZ, world, dungeon));
		DoomlikeDungeons.profiler.endTask("Create Dungeons");
	}
	
	
	public static void placeLevel(Level level, int chunkX, int chunkZ, World world) 
				throws Throwable {
		if(level.theme != null) {
			if(debugPole) debuggingPole(world, chunkX, chunkZ, level);
			buildDungeon(level);
		}
		level.preFinalize();
		level = null;
	}
	
	/**
	 * This will build the dungeon into the world, technically by having the dungeons map
	 * build itself.
	 * 
	 * @param dungeon
	 */
	public static void buildDungeon(Level dungeon /*TODO: Parameters*/) {
		//System.out.println("[DLDUNGONS] Inside Builder.placeDungeon; building dungeon");
		if(dungeon.theme != null) dungeon.map.build(dungeon);
	}
	
	
	/**
	 * This will build a quartz pillar to appear in the center of the dungeon from y=16 to y=240, 
	 * and a lapis lazuli boarder to appear around the area allotted for the dungeon at y=80.
	 * 
	 * This is only called if debugPole == true.
	 * 
	 * @param world
	 * @param chunkX
	 * @param chunkZ
	 * @param dungeon
	 */
	public static void debuggingPole(World world, int chunkX, int chunkZ, Level dungeon) {
		int x = (chunkX * 16) + 8;
		int z = (chunkZ * 16) + 8;
		for(int y = 16; y <= 241; y++) placeBlock(world, x, y, z, quartz);
		for(int i = -dungeon.size.radius; i <= dungeon.size.radius; i++) {
			placeBlock(world, x - dungeon.size.radius, 80, z + i, lapis);
			placeBlock(world, x + dungeon.size.radius, 80, z + i, lapis);
			placeBlock(world, x + i, 80, z - dungeon.size.radius, lapis);
			placeBlock(world, x + i, 80, z + dungeon.size.radius, lapis);
		}		
	}
	
	
	/**
	 * Set the debugPole variable.  Setting this to true will cause a quartz pillar to appear in the
	 * center of the dungeon from y=16 to y=240, and a lapis lazuli boarder to appear around the area
	 * allotted for the dungeon at y=80. 
	 * 
	 * @param val 
	 */
	public static void setDebugPole(boolean val) {
		debugPole = val;
	}
}
