package jaredbgreat.dldungeons.genhandler;

import java.util.Random;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class PlaceAlways extends Placement<NoPlacementConfig> {

	public PlaceAlways(Codec<NoPlacementConfig> codec) {
		super(codec);
	}

	@Override
	public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, 
				NoPlacementConfig config, BlockPos blockPos) {
		return Stream.of(new BlockPos(blockPos));
	}
}
