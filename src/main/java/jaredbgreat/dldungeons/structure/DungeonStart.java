package jaredbgreat.dldungeons.structure;

import java.util.Random;

import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.pieces.DebugPole;
import net.minecraft.nbt.CompoundNBT;
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
	}
	

	@Override
	public void init(ChunkGenerator<?> generator, TemplateManager tempman, 
				int chunkX, int chunkZ, Biome biome) {
		long seed = ((long)chunkX + (((long)chunkZ) << 32)) ^ generator.getSeed();
		Random random = new Random(seed);
		try {/*
			Dungeon dungeon = new Dungeon(random, biome, tempman, chunkX, chunkZ);
			dungeon.addPiecesToStructure(components);
		*/} catch (Throwable e) {
			DoomlikeDungeons.getLogger().error("Dungeon creation failes", e);
			e.printStackTrace();
		}
		if(true/*TODO: Base this on config*/) {
			CompoundNBT nbt = new CompoundNBT();
			nbt.putInt("x", (chunkX * 16) + 8);
			nbt.putInt("y", 16);
			nbt.putInt("z", (chunkZ * 16) + 8);
			DebugPole dp = new DebugPole(null, nbt);
			components.add(dp);	
		}
		recalculateStructureSize();
	}
	
	
	@Override
	public void generateStructure(IWorld worldIn, Random rand, MutableBoundingBox structurebb, ChunkPos pos) {
		// TODO Auto-generated method stub
		super.generateStructure(worldIn, rand, structurebb, pos);
		

//		DebugPole dp = new DebugPole(null, new CompoundNBT());
//		dp.x = pos.getXStart() + 8;
//		dp.y = 0; // doesnt matter in this case
//		dp.z = pos.getZStart() + 8;
	}
	
	
	

}
