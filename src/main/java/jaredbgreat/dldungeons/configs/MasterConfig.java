package jaredbgreat.dldungeons.configs;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import jaredbgreat.dldungeons.DoomlikeDungeons;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MasterConfig {
	
	private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec config;
	
	static {
		GeneralConfig.init(builder);
		config = builder.build();
	}
	
	public static void loadConfig(ForgeConfigSpec config, String path) {
		DoomlikeDungeons.logger.info("Loding Config at " + path);
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();
		DoomlikeDungeons.logger.info("Built Config for " + path);
		file.load();
		DoomlikeDungeons.logger.info("Loaded Config for " + path);
		config.setConfig(file);
	}
	
	

}
