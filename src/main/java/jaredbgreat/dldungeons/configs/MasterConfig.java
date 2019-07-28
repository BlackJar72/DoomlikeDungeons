package jaredbgreat.dldungeons.configs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.Info;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLPaths;

@EventBusSubscriber
public class MasterConfig {
	
	private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec config;
	
	static {
		GeneralConfig.init(builder);
		config = builder.build();
	}
	
	
	public static void loadConfig(ForgeConfigSpec config) {
		Path path = makeConfDir();
		DoomlikeDungeons.logger.info("Loding Config at " + path);
		final CommentedFileConfig file = CommentedFileConfig.builder(path)
				.sync()
				.autosave()
				.writingMode(WritingMode.REPLACE)
				.build();
		DoomlikeDungeons.logger.info("Built Config for " + path);
		file.load();
		DoomlikeDungeons.logger.info("Loaded Config for " + path);
		config.setConfig(file);
	}
	
	
	private static Path makeConfDir() {
    	String dirName = FMLPaths.CONFIGDIR.get().toString() 
    			+ File.separator + Info.OLD_ID;
    	String fileName = dirName + File.separator + Info.OLD_ID + ".toml";
    	File dir = new File(dirName);
    	File file = new File(fileName);
    	if(!dir.exists()) {
    		dir.mkdir();
    	}
    	if(!file.exists()) {
    		try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.append("");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return file.toPath();
	}
	
	

}
