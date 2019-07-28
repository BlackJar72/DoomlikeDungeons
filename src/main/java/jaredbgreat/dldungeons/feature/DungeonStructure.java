package jaredbgreat.dldungeons.feature;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
	/* OK, this is analogous to generate in GenerationHandler, at least partly.  It determines 
	 * if a the structure should generate here, and as such should use most of the same 
	 * code, but instead of potentially generating a structure it should return true if 
	 * one should generate.
	 * 
	 * @see net.minecraft.world.gen.feature.structure.Structure#hasStartAt(net.minecraft.world.gen.IChunkGenerator, java.util.Random, int, int)
	 */
	protected boolean hasStartAt(IChunkGenerator world, Random rand, int chunkX, int chunkZ) {
		// FIXME: This probably belongs in isEnabled();
		//if((ConfigHandler.obeyRule && !world.getWorldInfo().isMapFeaturesEnabled())
		//		|| !ConfigHandler.naturalSpawn) return false;; 
		boolean blockedBiome = false;
		Set<Type> types = BiomeDictionary.getTypes((world.getBiomeProvider()
				.getBiome(new BlockPos(chunkX * 16, 63, chunkZ * 16), Biomes.DEFAULT)));
		for(Type type : types) {
			// TODO: Restore when there are configs being read.
			//blockedBiome = ConfigHandler.biomeExclusions.contains(type) || blockedBiome;
		}
		if(blockedBiome) return false;
		// FIXME: See if this can be fixed, otherwise trash it.  Sorry dimension modders....
		//if((dimensions.contains(Integer.valueOf(world.provider.getDimension())) 
        //				!= ConfigHandler.positiveDims)) return false;
		//if((Math.abs(chunkX - (world.getS.getSpawnPoint().getX() / 16)) < minXZ) 
		//		|| (Math.abs(chunkZ - (world.getSpawnPoint().getZ() / 16)) < minXZ)) return false;
		
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
	protected boolean isEnabledIn(IWorld worldIn) {
		return true; // FIXME: May change later
	}

	
	@Override
	protected StructureStart makeStart(IWorld worldIn, IChunkGenerator generator, SharedSeedRandom random, int x,
			int z) {
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

}
