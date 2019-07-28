package jaredbgreat.dldungeons.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
	public static ForgeConfigSpec.IntValue difficulty;
	
	public static void init(ForgeConfigSpec.Builder builder) {
		builder.comment("General");
		
		difficulty = builder.comment("How hard: 0 = empty, 1 = baby, 2 = easy, 3 = normal, 4= hard, 5 = nightmare")
				            .defineInRange("general.difficulty", 3, 0, 5);
	}
}
