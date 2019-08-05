package jaredbgreat.dldungeons.feature;

import static jaredbgreat.dldungeons.builder.DBlock.quartz;

import java.util.Random;

import jaredbgreat.dldungeons.builder.DBlock;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DungeonStart extends StructureStart {
	
	public DungeonStart(Structure<?> p_i51341_1_, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn,
			int referenceIn, long seed) {
		super(p_i51341_1_, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ,
			Biome biomeIn) {
		// TODO Auto-generated method stub
		
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
	public static void debuggingPole(IWorld world, int chunkX, int chunkZ) {
		int x = chunkX;
		int z = chunkZ;
		//int x = (chunkX * 16) + 8;
		//int z = (chunkZ * 16) + 8;
		for(int y = 16; y <= 241; y++) DBlock.placeBlock(world, x, y, z, quartz);
	}
	 
	 
	
//	/**
//	 * This will build a quartz pillar to appear in the center of the dungeon from y=16 to y=240, 
//	 * and a lapis lazuli boarder to appear around the area allotted for the dungeon at y=80.
//	 * 
//	 * This is only called if debugPole == true.
//	 * 
//	 * @param world
//	 * @param chunkX
//	 * @param chunkZ
//	 * @param dungeon
//	 */
//	public static void debuggingBox(IWorld world, int chunkX, int chunkZ, Dungeon dungeon) {
//		int x = (chunkX * 16) + 8;
//		int z = (chunkZ * 16) + 8;
//		for(int i = -dungeon.size.radius; i <= dungeon.size.radius; i++) {
//			placeBlock(world, x - dungeon.size.radius, 80, z + i, lapis);
//			placeBlock(world, x + dungeon.size.radius, 80, z + i, lapis);
//			placeBlock(world, x + i, 80, z - dungeon.size.radius, lapis);
//			placeBlock(world, x + i, 80, z + dungeon.size.radius, lapis);
//		}		
//	}

}
