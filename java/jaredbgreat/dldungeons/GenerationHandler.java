package jaredbgreat.dldungeons;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import static jaredbgreat.dldungeons.builder.Builder.placeDungeon;

import java.util.HashSet;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class GenerationHandler implements IWorldGenerator {
	
	private static int frequency;
	private static int factor = 6;
	private static int minXZ;
	private static Random mrand;
	private static HashSet<Integer> dimensions;
	
	public GenerationHandler() {
		GameRegistry.registerWorldGenerator(this, 100);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		// Prevent bad spawns
		if(world.isRemote) return; // Do not perform world-gen on the client!
		if((ConfigHandler.obeyRule && !world.getWorldInfo().isMapFeaturesEnabled())
				|| !ConfigHandler.naturalSpawn) return;  // Second part is redundant for safety
//		if((world.getBiomeGenForCoords(chunkX * 16, chunkZ * 16) == BiomeGenBase.sky) && noEnd) return;
		boolean blockedBiome = false;
		Type[] types = BiomeDictionary.getTypesForBiome((world.getBiomeGenForCoords(new BlockPos(chunkX * 16, 63, chunkZ * 16))));
		for(Type type : types) {			
			blockedBiome = ConfigHandler.biomeExclusions.contains(type) || blockedBiome;
			//System.out.println("[DLDUNGONS] biome type " + type.toString() +", blockedBiome = " + blockedBiome);
		}
		if(blockedBiome) return;
		if((dimensions.contains(Integer.valueOf(world.provider.getDimension())) != ConfigHandler.positiveDims)) return;
		if((Math.abs(chunkX - (world.getSpawnPoint().getX() / 16)) < minXZ) 
				|| (Math.abs(chunkZ - (world.getSpawnPoint().getZ() / 16)) < minXZ)) return;
		
		//Get some numbers		
		//DoomlikeDungeons.profiler.startTask("Checking for Dungeon");
		mrand = new Random(world.getSeed() + world.provider.getDimension()
				+ (2027 * (long)(chunkX / factor)) 
				+ (1987 * (long)(chunkZ / factor)));
		int xrand = mrand.nextInt();
		int zrand = mrand.nextInt();
		int xuse = ((chunkX + xrand) % factor);
		int zuse = ((chunkZ + zrand) % factor);
		
		if((xuse == 0) && (zuse == 0)) {
			try {
				placeDungeon(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			} catch (Throwable e) {
				System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
				e.printStackTrace();
			} // Put a Dungeon in the areaS
		}
		//DoomlikeDungeons.profiler.endTask("Checking for Dungeon");
	}	
	
	
	public static void setFrequency(int freqScale) {
		if((freqScale % 2) == 0) factor = 2;
		else factor = 3;
		System.out.println("freqScale = " + freqScale);
		factor = (1 << (freqScale / 2)) * factor;
		System.out.println("factor = " + factor);
	}
	
	
	public static void setMinXZ(int value) {
		minXZ = value;		
	}
	
	
//	public static void setEnd(boolean value) {
//		noEnd = value;		
//	}
	
	
	public static void setDimensions(int[] value) {
		dimensions = new HashSet<Integer>();
		dimensions.clear();
		for(int i = 0; i < value.length; i++) {
			dimensions.add(Integer.valueOf(value[i]));
		}
	}
	
	
	public static void addDimension(int value) {
		dimensions.add(Integer.valueOf(value));
	}
	
	
	public static void subDimension(int value) {
		dimensions.remove(Integer.valueOf(value));
	}
	
}
