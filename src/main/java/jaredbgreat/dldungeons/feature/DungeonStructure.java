package jaredbgreat.dldungeons.feature;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import jaredbgreat.dldungeons.configs.ConfigHandler;
import jaredbgreat.dldungeons.debug.Logging;
import net.minecraft.init.Biomes;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class DungeonStructure extends Structure<DungeonFeatureConfig> {	
	private static int frequency;
	private static int factor = 6;
	private static int minXZ;
	private static Random mrand;
	private static HashSet<Integer> dimensions;
	
	

	@Override
	@SuppressWarnings("rawtypes")
	/* OK, this is analogous to generate in GenerationHandler, at least partly.  It determines 
	 * if a the structure should generate here, and as such should use most of the same 
	 * code, but instead of potentially generating a structure it should return true if 
	 * one should generate.
	 * 
	 * @see net.minecraft.world.gen.feature.structure.Structure#hasStartAt(net.minecraft.world.gen.IChunkGenerator, java.util.Random, int, int)
	 */
	protected boolean hasStartAt(IChunkGenerator world, Random rand, int chunkX, int chunkZ) {
		boolean blockedBiome = false;
		Set<Type> types = BiomeDictionary.getTypes((world.getBiomeProvider()
				.getBiome(new BlockPos(chunkX * 16, 63, chunkZ * 16), Biomes.DEFAULT)));
		for(Type type : types) {
			blockedBiome = ConfigHandler.biomeExclusions.contains(type) || blockedBiome;
			if(blockedBiome) return false;
		}
		mrand = new Random(world.getSeed() 
				+ (2027 * (long)(chunkX / factor)) 
				+ (1987 * (long)(chunkZ / factor)));
		int xrand = mrand.nextInt();
		int zrand = mrand.nextInt();
		int xuse = ((chunkX + xrand) % factor);
		int zuse = ((chunkZ + zrand) % factor);
		
		if((xuse == 0) && (zuse == 0)) {
			return true;
		}	
		return false;
	}

	
	@Override
	protected boolean isEnabledIn(IWorld world) {
		//if((ConfigHandler.obeyRule && !world.getWorldInfo().isMapFeaturesEnabled())
		//		|| !ConfigHandler.naturalSpawn) return false;
		//if((dimensions.contains(Integer.valueOf(world.getDimension().getType().getId())) 
		//		!= ConfigHandler.positiveDims)) return false;
		return ConfigHandler.naturalSpawn; 
	}


	@Override
	@SuppressWarnings("rawtypes")
	protected StructureStart makeStart(IWorld worldIn, IChunkGenerator generator, 
			SharedSeedRandom random, int x,	int z) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	protected String getStructureName() {
		return "DoomlikeDungeon";
	}

	
	@Override
	public int getSize() {
		// TODO: This needs to be calculated based on dungeon size,
		//       probably width x 16 (I don't know for sure if this 
		//       should be in blocks or chunks).
		return 0;
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
		Logging.logInfo("freqScale = " + freqScale);
		factor = (1 << (freqScale / 2)) * factor;
		Logging.logInfo("factor = " + factor);
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
