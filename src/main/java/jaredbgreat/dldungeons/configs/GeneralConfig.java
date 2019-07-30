package jaredbgreat.dldungeons.configs;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
	public static ForgeConfigSpec.BooleanValue announceComs;
	public static ForgeConfigSpec.IntValue difficulty;
	public static ForgeConfigSpec.IntValue frequencyScale;
	public static ForgeConfigSpec.IntValue minChunkXY;
	public static ForgeConfigSpec.ConfigValue<List<Integer>> dims;
	public static ForgeConfigSpec.BooleanValue easyFind;
	public static ForgeConfigSpec.BooleanValue installThemes;
	public static ForgeConfigSpec.BooleanValue installThemesCom;
	
	public static void init(ForgeConfigSpec.Builder builder) {
		builder.comment("General");
		
		announceComs = builder.comment("If true, console commands from the mod will be annouced in chat")
				            .define("general.AnounceCommands", true);
		
		difficulty = builder.comment("How hard: 0 = empty, 1 = baby, 2 = easy, 3 = normal, 4= hard, 5 = nightmare")
				            .defineInRange("general.Difficulty", 3, 0, 5);
		
		ArrayList<Integer> dimList = new ArrayList<Integer>();
		dimList.add(0); dimList.add(1);
		dims = builder.comment("These dimensions either lack dungeons or only they have them "
				+ "(see OnlySpawnInListedDimensions)")
				            .define("general.Dimensions", dimList);
		
	}
}
