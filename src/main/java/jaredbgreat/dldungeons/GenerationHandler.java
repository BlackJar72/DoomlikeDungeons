package jaredbgreat.dldungeons;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import jaredbgreat.dldungeons.configs.ConfigHandler;
import jaredbgreat.dldungeons.debug.Logging;
import jaredbgreat.dldungeons.structure.DungeonStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.registries.ForgeRegistries;


/*
 * Still not sure if an update of this or doing something similar in Structure will prevail;
 * IWorldGenerator is far simpleer and more flexible, but doesn't seem to be well supported 
 * any more (with some calling for it to be done away with).
 */
public class GenerationHandler implements IWorldGenerator {	
	private static int factor = 6;
	private static int minXZ;
	private static Random mrand;
	private static HashSet<Integer> dimensions;
	Structure<NoFeatureConfig> dstruct;

	public GenerationHandler() {
		dstruct = new DungeonStructure();
		dstruct.setRegistryName(Info.ID, "doomlike_dungeon");
		for(Biome biome : ForgeRegistries.BIOMES) {
			biome.addStructure(dstruct, IFeatureConfig.NO_FEATURE_CONFIG);
		}
	}
	
	
	/**
	 * Called when a new chunk generates to determine if a dungeon should be 
	 * built centered on that chunk.
	 * 
	 * First it will make sure dungeons are aloud, including all config setting 
	 * and world setting as well as the dimension and biome of the chunk.
	 * 
	 * If dungeons are allowed here then it will check if the chunk is the one 
	 * in its area to have a dungeon.  To do this the map is logically divided 
	 * into square areas the size of which depend on the frequency scale.  One 
	 * chunk in each area is the technical center around with a dungeon will 
	 * generate.  The chunk coordinates are divided by the area's width using 
	 * integer division to round so that all chunks in the same area are given
	 * the same two numbers.  These numbers are then used to create a random 
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
	@Override
	@SuppressWarnings("rawtypes")
	public void generate(Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator,
			AbstractChunkProvider chunkProvider) {
		// Prevent bad spawns
		if(world.isRemote) return;
		if((ConfigHandler.obeyRule && !world.getWorldInfo().isMapFeaturesEnabled())
				|| !ConfigHandler.naturalSpawn) return; 
		boolean blockedBiome = false;
		Set<Type> types = BiomeDictionary.getTypes((world.getBiome(new BlockPos(chunkX * 16, 63, chunkZ * 16))));
		for(Type type : types) {			
			blockedBiome = ConfigHandler.biomeExclusions.contains(type) || blockedBiome;
		}
		if(blockedBiome) return;
		if((dimensions.contains(Integer.valueOf(world.getDimension().getType().getId())) 
				!= ConfigHandler.positiveDims)) return;
		if((Math.abs(chunkX - (world.getSpawnPoint().getX() / 16)) < minXZ) 
				|| (Math.abs(chunkZ - (world.getSpawnPoint().getZ() / 16)) < minXZ)) return;
		
		mrand = new Random(world.getSeed() 
				+ (2027 * (long)(chunkX / factor)) 
				+ (1987 * (long)(chunkZ / factor)));
		int xrand = mrand.nextInt();
		int zrand = mrand.nextInt();
		int xuse = ((chunkX + xrand) % factor);
		int zuse = ((chunkZ + zrand) % factor);
		
		if((xuse == 0) && (zuse == 0)) {
			try {
				//placeDungeon(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			} catch (Throwable e) {
				System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
				e.printStackTrace();
			}
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
