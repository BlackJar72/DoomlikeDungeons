package jaredbgreat.dldungeons.genhandler;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class SectionFeature  extends Feature<NoFeatureConfig> {

	public SectionFeature(Codec<NoFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(ISeedReader sreader, ChunkGenerator chunkgen, Random random, BlockPos pos,
			NoFeatureConfig noconfig) {
		int cx = pos.getX() / 16, cz = pos.getZ() / 16;
		int x = (cx * 16), x2 = x + 16, z = (cz * 16), z2 = z + 16;
		BlockState bs = Blocks.QUARTZ_BLOCK.defaultBlockState(); 
		for(int i = x; i < x2; i++)
			for(int j = 16; j < 32; j++) 
				for(int k = z; k < z2; k++){
					BlockPos buildPos = new BlockPos(i, j ,k);
					sreader.setBlock(buildPos, bs, 3);
		}
		return true;
	}

}
