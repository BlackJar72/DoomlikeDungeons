package jaredbgreat.dldungeons.genhandler;

import com.mojang.serialization.Codec;

import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

public class SectionStructure  extends Structure<NoFeatureConfig>{

	public SectionStructure(Codec<NoFeatureConfig> codec) {
		super(codec);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return null;
	}

}
