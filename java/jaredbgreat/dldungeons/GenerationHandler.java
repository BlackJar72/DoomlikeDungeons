package jaredbgreat.dldungeons;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import static jaredbgreat.dldungeons.builder.Builder.placeDungeon;
import static jaredbgreat.dldungeons.builder.DBlock.placeBlock;
import static jaredbgreat.dldungeons.builder.DBlock.quartz;
import jaredbgreat.dldungeons.builder.Builder;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.themes.Sizes;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * The class responsible for determine where dungeons generate.  More 
 * specifically, it will determine if a chunk being generated should be 
 * the technical (not geometric) center of a dungeon.
 * 
 * @author Jared Blackburn
 */
public class GenerationHandler implements IWorldGenerator {
	
	private static int frequency;
	private static int factor = 6;
	private static int minXZ;
	private static Random mrand;
	private static HashSet<Integer> dimensions;
	
	public GenerationHandler() {
		GameRegistry.registerWorldGenerator(this, 100);
	}
	
	
	/**
	 * Called when a new chunk generates to determine if a dungeon should be 
	 * built centered on that chunk.
	 * 
	 * First it will make sure dungeons are aloud, including all config setting 
	 * and world setting as well as the dimension and biome of the chunk.
	 * 
	 * If dungeons are allowed here then it will check if the chunk is the one 
	 * in its area to have a dungeon.  
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		// Prevent bad spawns		
		if(world.isRemote) return;
		if(!dungeonsAllowedHere(world, chunkX, chunkZ)) return;
		
		if((chunkX * chunkX) + (chunkZ * chunkZ) < 1024) {
			//checkForDungeonInRange(world, chunkX, chunkZ, random);
		}
		
		// Actually determine if a dungeon should generate here		
		if(dungeonShouldGenerate(world, chunkX, chunkZ)) {
			try {
				placeDungeon(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			} catch (Throwable e) {
				System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
				e.printStackTrace();
			}
		}
	}


	/**
	 * This will determine if a dungeon should be able to generate at a given location.
	 * Specifically, it makes sure that coordinates, biomes, and dimension are all allowed 
	 * to have dungeons and the dungeons are allowed to generate in the world.
	 */
	public boolean dungeonsAllowedHere(World world, int chunkX, int chunkZ) {
		if((ConfigHandler.obeyRule && !world.getWorldInfo().isMapFeaturesEnabled())
				|| !ConfigHandler.naturalSpawn) return false; 
			
		Set<Type> types = BiomeDictionary.getTypes((world.getBiome(new BlockPos(chunkX * 16, 63, chunkZ * 16))));
		for(Type type : types) {			
			if(ConfigHandler.biomeExclusions.contains(type)) {
				return false;
			}
		}
		if((dimensions.contains(Integer.valueOf(world.provider.getDimension())) 
			!= ConfigHandler.positiveDims)) return false;
		
		if((Math.abs(chunkX - (world.getSpawnPoint().getX() / 16)) < minXZ) 
				|| (Math.abs(chunkZ - (world.getSpawnPoint().getZ() / 16)) < minXZ)) return false;
		
		return true;		
	}


	/**
	 * This will determine if a dungeon should generate with the given chunk as 
	 * its logical center. 
	 *
	 * To do this the map is logically divided  into square areas the size of which 
	 * depend on the frequency scale.  One chunk in each area is the technical center 
	 * around with a dungeon will generate.  The chunk coordinates are divided by the 
	 * area's width using integer division to round so that all chunks in the same area 
	 * are given the same two numbers.  These numbers are then used to create a random 
	 * seeds from which coordinates from 0 to the area width are generated. If
	 * the coords match the remainder of chunk coordinate divided by the width
	 * of the area this chunk will have a dungeon; since all chunks in the area
	 * will use the same random seed and thus have the same number one and only
	 * one of them will have a dungeon (assuming dungeons are allowed in the 
	 * worlds and in that chunk).
	 * 
	 * This allows dungeons to be placed randomly while ensuring a more consistent
	 * distribution than simply giving each a random probability to have one.
	 */
	public boolean dungeonShouldGenerate(World world, int chunkX, int chunkZ) {
		mrand = new Random(world.getSeed() + world.provider.getDimension()
				+ (2027 * (long)(chunkX / factor)) 
				+ (1987 * (long)(chunkZ / factor)));
		int xrand = mrand.nextInt();
		int zrand = mrand.nextInt();
		int xuse = ((chunkX + xrand) % factor);
		int zuse = ((chunkZ + zrand) % factor);
		return (xuse == 0) && (zuse == 0);
	}
	
	
	/**
	 * This will check for any dungeons with technical centers close enough 
	 * to the given chunk to be partially inside it.
	 *
	 * If it find it it will ....
	 */
	public void checkForDungeonInRange(World world, int chunkX, int chunkZ, Random rand) {
		int range = (Sizes.HUGE.width / 32) + 1;
		for(int i = chunkX - range; i < chunkX + range + 1; i++) 
			for(int j = chunkZ - range; j < chunkZ + range + 1; j++) {
				if(dungeonShouldGenerate(world, i, j)) {
					getDungeon(world, i, j, chunkX, chunkZ, rand);
				}
			}
	}
	
	
	/**
	 * This does something with dungeons that have been found by 
	 * checkForDungeonsInRange.
	 *
	 * What it does is yet to be determined, but it will likely try to find and 
	 * build them.
	 */
	private void getDungeon(World world, int dunX, int dunZ, int atX, int atZ, Random rand) {
		// TODO: Find / process / do something with (build?) dungeon
		//
		// For now I may use this to print debug-like info to make sure its 
		// finding correct locations for building dungeons.
		Dungeon out = Builder.dungeonCache.get(dunX, dunZ);
		BlockPos pos = new BlockPos((dunX * 16) + 8, 64, (dunZ * 16) + 8);
		if(out == null) {
	    	System.out.println("**Dungeon was NULL**");
			try {
				out = new Dungeon(rand, world.getBiome(pos), world, dunX, dunZ);
				Builder.dungeonCache.add(out);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int range = out.getRadius();
		
		String nl = System.lineSeparator();
		System.out.println(nl
				           + "*****************************" + nl
				           + "Chunk Gen at " + atX + ", " + atZ + nl
				           + "Found dungeon at chunk " + dunX + ", " + dunZ + nl
				           + "Dungeon: " + out.hashCode() + "; Coord " + out.getCoords() + " hash: " + out.getCoords().hashCode() + nl
				           // The displacement if from dungeon center to chunk (not chunk to dungeon!)
				           + "Displacemnt: " + (atX - dunX) + ", " + (atZ - dunZ) + nl
				           + "*****************************");
		if((Math.abs(atX - dunX) < range) && (Math.abs(atZ - dunZ) < range)) {
			for(int y = 16; y <= 241; y++) placeBlock(world, (atX * 16 + 8), y, (atZ * 16 + 8), quartz);
		}
	}
	
	
	/**
	 * Sets the frequency scale and converts into the 
	 * width (and height) of the areas for which dungeons
	 * are generated.
	 * 
	 * @param freqScale
	 */
	public static void setFrequency(int freqScale) {
		if((freqScale % 2) == 0) factor = 2;
		else factor = 3;
		System.out.println("freqScale = " + freqScale);
		factor = (1 << (freqScale / 2)) * factor;
		System.out.println("factor = " + factor);
	}
	
	
	/**
	 * Sets the minimum number of chunks from spawn a dungeon center must be.
	 * 
	 * @param value
	 */
	public static void setMinXZ(int value) {
		minXZ = value;		
	}
	
	
	/**
	 * Sets list of dimension id's to check for dungeons being allowed.
	 * 
	 * @param value
	 */
	public static void setDimensions(int[] value) {
		dimensions = new HashSet<Integer>();
		for(int i = 0; i < value.length; i++) {
			dimensions.add(Integer.valueOf(value[i]));
		}
	}
	
	
	/**
	 * Add a dimension id to the list of dimension to consider when 
	 * check if a dungeon is allowed.  (What this means can vary.)
	 * 
	 * @param value
	 */
	public static void addDimension(int value) {
		dimensions.add(Integer.valueOf(value));
	}
	
	
	/**
	 * Remove a dimension id from the list of those to consider when potentially 
	 * placing a dungeons.  (What this means can vary.)
	 * 
	 * @param value
	 */
	public static void subDimension(int value) {
		dimensions.remove(Integer.valueOf(value));
	}
}
