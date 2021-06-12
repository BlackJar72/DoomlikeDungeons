package jaredbgreat.dldungeons;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jaredbgreat.dldungeons.genhandler.GenerationHandler;
import jaredbgreat.dldungeons.setup.Externalizer;
import jaredbgreat.dldungeons.util.debug.DebugOut;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Info.ID)
public class DoomlikeDungeons {	
    public final GenerationHandler generation;
    public final String configDir;

    public DoomlikeDungeons() {
    	
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);       
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        configDir = System.getProperty("user.dir") + File.separator + "config" + File.separator + "DLDungeons" + File.separator;
        generation = GenerationHandler.getHandler();
    }
    
    
    private void exportFiles() {
        Externalizer exporter = new Externalizer(configDir + "themes" + File.separator);
        exporter.makeThemesDir();
        exporter.makeThemes();
        exporter.makeBlocks();
    }

    
    private void setup(final FMLCommonSetupEvent event) {
    	
        // some preinit code
    }

    
    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        // LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
    }

    
    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
    }

    
    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        // LOGGER.info("Got IMC {}", event.getIMCStream().
        //        map(m->m.getMessageSupplier().get()).
        //        collect(Collectors.toList()));
    }
    
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    
    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        }
    }
}
