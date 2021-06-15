package jaredbgreat.dldungeons.builder;


import java.util.List;
import java.util.Random;

import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.util.cache.Coords;
import jaredbgreat.dldungeons.util.cache.WeakCache;
import jaredbgreat.dldungeons.util.debug.DebugOut;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;


// TODO: Everything, once the dungeons themselves are back!


public class Builder {
	private static final WeakCache<Dungeon> DUNGEON_CACHE = new WeakCache<>();	
	
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
	public static void placeDungeon(Random random, int chunkX, int chunkZ, ISeedReader world, ChunkGenerator chunkgen) throws Throwable {	
		//DoomlikeDungeons.profiler.startTask("Create Dungeons");
		Dungeon dungeon = new Dungeon(world.getBiome(new BlockPos((chunkX * 16), 64, (chunkZ * 16))), 
								world, chunkX, chunkZ);
		buildDungeon(dungeon);
		dungeon = null;
		MinecraftForge.EVENT_BUS.post(new DLDEvent.PlaceDungeonFinish(chunkX, chunkZ, world, dungeon));
		//DoomlikeDungeons.profiler.endTask("Create Dungeons");
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
	public static void placeDungeon(int chunkX, int chunkZ, ISeedReader world) throws Throwable {	
		//DoomlikeDungeons.profiler.startTask("Create Dungeons");
		Dungeon dungeon = new Dungeon( 
							world.getBiome(new BlockPos((chunkX * 16), 64, (chunkZ * 16))), 
						    world, chunkX, chunkZ);
		buildDungeon(dungeon);
		dungeon = null;
		//DoomlikeDungeons.profiler.endTask("Create Dungeons");
	}
	
	
	public static void buildDungeonChunk(final int cx, final int cz, final Coords dc, final ISeedReader world) throws Throwable {
		Dungeon dungeon = DUNGEON_CACHE.get(dc);
		if(dungeon == null) {
			dungeon = new Dungeon(world, dc);
			DUNGEON_CACHE.add(dungeon);
		}
		if((dungeon != null) && (dungeon.map != null)) {
			dungeon.map.buildInChunk(dungeon, cx, cz);
		}
	}
	
	
	public static void buildDungeonsChunk(final int cx, final int cz, final List<Coords> dcs, final ISeedReader world) throws Throwable {
		for(Coords dc : dcs) {
			buildDungeonChunk(cx, cz, dc, world);
		}
	}
	
	
	/**
	 * This will build the dungeon into the world, technically by having the dungeons map
	 * build itself.
	 * 
	 * @param dungeon
	 */
	public static void buildDungeon(Dungeon dungeon /*TODO: Parameters*/) {
		//System.out.println("[DLDUNGONS] Inside Builder.placeDungeon; building dungeon");
		//if(dungeon.theme != null) dungeon.map.build(dungeon);
		if(dungeon.theme != null) dungeon.map.buildByChunksTest(dungeon);
	}
	
}
