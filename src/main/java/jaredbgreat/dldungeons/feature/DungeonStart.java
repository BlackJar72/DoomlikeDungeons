package jaredbgreat.dldungeons.feature;

import java.util.Random;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.StructureStart;

public class DungeonStart extends StructureStart {
	
	 public DungeonStart(IWorld world, SharedSeedRandom random, int x, int z, Biome biome) {
		 fuckYouConstructor(world, new Random(random.nextLong()), x, z, biome);
	 }	
	 public DungeonStart(IWorld world, Random random, int x, int z, Biome biome) {
		 fuckYouConstructor(world, random, x, z, biome);
	 }
	 
	 
	 private void fuckYouConstructor(IWorld world, Random random, int x, int z, Biome biome) {
		 
	 }
	 

}
