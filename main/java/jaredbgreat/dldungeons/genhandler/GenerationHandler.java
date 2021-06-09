package jaredbgreat.dldungeons.genhandler;

import jaredbgreat.dldungeons.Info;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber
public class GenerationHandler {
	public static final GenerationHandler HANDLER = new GenerationHandler();;
	public static PlaceAlways always = new PlaceAlways(NoPlacementConfig.CODEC);
	public static SectionFeature sectionFeature = new SectionFeature(NoFeatureConfig.CODEC);
	
	
	@SubscribeEvent
	public static void registerFeatures(BiomeLoadingEvent event) {
		event.getGeneration().addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, 
				sectionFeature.configured(IFeatureConfig.NONE)
				              .decorated(always.configured(NoPlacementConfig.INSTANCE)));
	}


	@SubscribeEvent
	public static void registerPlacements(RegistryEvent.Register<Placement<?>> event) {
		event.getRegistry().register(always.setRegistryName(Info.ID, "always"));
	}

	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(sectionFeature.setRegistryName(Info.ID, "dungeon"));
	}
	
	
	public static GenerationHandler getHandler() {
		/*if(HANDLER == null) {
			HANDLER = new GenerationHandler();
		}*/
		return HANDLER;
	}
	
}
