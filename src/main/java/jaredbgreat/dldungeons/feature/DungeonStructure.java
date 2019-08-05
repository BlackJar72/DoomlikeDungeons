package jaredbgreat.dldungeons.feature;

import java.util.HashSet;
import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import jaredbgreat.dldungeons.debug.Logging;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;

public class DungeonStructure extends Structure<DungeonFeatureConfig> {
	private static int frequency;
	private static int factor = 6;
	private static int minXZ;
	private static Random mrand;
	private static HashSet<Integer> dimensions;
		

	public DungeonStructure(Function<Dynamic<?>, ? extends DungeonFeatureConfig> p_i51427_1_) {
		super(p_i51427_1_);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	@SuppressWarnings({ "rawtypes", "unused" })
	/* OK, this is analogous to generate in GenerationHandler, at least partly.  It determines 
	 * if a the structure should generate here, and as such should use most of the same 
	 * code, but instead of potentially generating a structure it should return true if 
	 * one should generate.
	 * 
	 * @see net.minecraft.world.gen.feature.structure.Structure#hasStartAt(net.minecraft.world.gen.IChunkGenerator, java.util.Random, int, int)
	 */
	public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ) {
		System.err.println("Making a DUNGEON!");
		return true;
//		boolean blockedBiome = false;
//		Set<Type> types = BiomeDictionary.getTypes((world.getBiomeProvider()
//				.getBiome(new BlockPos(chunkX * 16, 63, chunkZ * 16), Biomes.DEFAULT)));
//		for(Type type : types) {
//			blockedBiome = ConfigHandler.biomeExclusions.contains(type) || blockedBiome;
//			if(blockedBiome) return false;
//		}
//		mrand = new Random(world.getSeed() 
//				+ (2027 * (long)(chunkX / factor)) 
//				+ (1987 * (long)(chunkZ / factor)));
//		int xrand = mrand.nextInt();
//		int zrand = mrand.nextInt();
//		int xuse = ((chunkX + xrand) % factor);
//		int zuse = ((chunkZ + zrand) % factor);
//		
//		if((xuse == 0) && (zuse == 0)) {
//			return true;
//		}	
//		return false;
	}
	
	
	
	protected boolean isEnabledIn(World world) {
		//if((ConfigHandler.obeyRule && !world.getWorldInfo().isMapFeaturesEnabled())
		//		|| !ConfigHandler.naturalSpawn) return false;
		//if((dimensions.contains(Integer.valueOf(world.getDimension().getType().getId())) 
		//		!= ConfigHandler.positiveDims)) return false;
		return true; //ConfigHandler.naturalSpawn; 
	}
	
	
	@Override
	public String getStructureName() {
		return "DoomlikeDungeon";
	}

	
	@Override
	public int getSize() {
		// TODO: This needs to be calculated based on dungeon size,
		//       probably width x 16 (I don't know for sure if this 
		//       should be in blocks or chunks).
		return 16;
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


	@Override
	public IStartFactory getStartFactory() {
		// TODO Auto-generated method stub
		return null;
	}

}
