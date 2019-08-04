package jaredbgreat.dldungeons.feature;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.BasePlacement;

public class DungeonPlacementBS extends BasePlacement<DungeonPlacementConfig> {

	@Override
	public <C extends IFeatureConfig> boolean generate(IWorld worldIn,
			IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, Random random, BlockPos pos,
			DungeonPlacementConfig placementConfig, Feature<C> featureIn, C featureConfig) {
		// TODO Auto-generated method stub
		return true;
	}

}
