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
	public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ,
			Biome biomeIn) {
		//DoomlikeDungeons.logger.info("Building Debug Pole at " + chunkX + ", " + chunkZ);
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("x", (chunkX * 16) + 8);
		nbt.putInt("y", 16);
		nbt.putInt("z", (chunkZ * 16) + 8);
		DebugPole dp = new DebugPole(null, nbt);
		components.add(dp);	
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
