package com.github.xyroc.dldungeons.datapack;

import com.github.xyroc.dldungeons.DLDungeons;
import jaredbgreat.dldungeons.pieces.chests.LootCategory;
import jaredbgreat.dldungeons.themes.Theme;
import jaredbgreat.dldungeons.themes.ThemeReader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ResourceReloadHandler implements PreparableReloadListener {
    /**
     * Base directory within the data pack.
     * Should be unique to avoid clashes with other data packs.
     */
    private static final String BASE_DIRECTORY = "dldungeons";
    private static final String THEMING_DIRECTORY = BASE_DIRECTORY + "/theming";
    private static final String BLOCK_FAMILIES_DIRECTORY = THEMING_DIRECTORY + "/block_families";
    private static final String SPECIAL_CHESTS_DIRECTORY = THEMING_DIRECTORY + "/special_chests";
    private static final String NBT_DIRECTORY = THEMING_DIRECTORY + "/nbt";
    private static final String THEMES_DIRECTORY = THEMING_DIRECTORY + "/themes";

    private static final String CFG_FILE_ENDING = ".cfg";
    private static final String JSON_FILE_ENDING = ".json";
    private static final String SNBT_FILE_ENDING = ".snbt";

    private static final Predicate<ResourceLocation> IS_CFG_FILE = location -> location.getPath().endsWith(CFG_FILE_ENDING);
    private static final Predicate<ResourceLocation> IS_JSON_FILE = location -> location.getPath().endsWith(JSON_FILE_ENDING);
    private static final Predicate<ResourceLocation> IS_SNBT_FILE = location -> location.getPath().endsWith(SNBT_FILE_ENDING);


    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor backgroundExecutor, Executor gameExecutor) {
        return preparationBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
            Theme.purgeThemes();
            loadSNBT(resourceManager);
            loadBlockFamilies(resourceManager);
            loadSpecialChests(resourceManager);
            loadThemes(resourceManager);
            Theme.SortThemes();
        }, gameExecutor);
    }


    private void loadBlockFamilies(ResourceManager resourceManager) {
        loadFilesInDirectory(resourceManager, BLOCK_FAMILIES_DIRECTORY, IS_JSON_FILE, (location, file) -> {
            final ResourceLocation key = keyFromLocation(location, BLOCK_FAMILIES_DIRECTORY, JSON_FILE_ENDING);
            // Read file from input stream and insert into some data structure for later use.
            // final BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            ThemeReader.readBlockFamilies(file);
        });
    }

    private void loadThemes(ResourceManager resourceManager) {
        loadFilesInDirectory(resourceManager, THEMES_DIRECTORY, IS_CFG_FILE, (location, file) -> {
            final ResourceLocation key = keyFromLocation(location, THEMES_DIRECTORY, CFG_FILE_ENDING);
            // Read file from input stream and insert into some data structure for later use.
            ThemeReader.readTheme(file, key.toString());
        });
    }

    private void loadSpecialChests(ResourceManager resourceManager) {
        loadFilesInDirectory(resourceManager, SPECIAL_CHESTS_DIRECTORY, IS_CFG_FILE, (location, file) -> {
            final ResourceLocation key = keyFromLocation(location, SPECIAL_CHESTS_DIRECTORY, CFG_FILE_ENDING);
            // Read file from input stream and insert into some data structure for later use.
            ThemeReader.openLoot(file, key.toString());
        });
    }

    private void loadSNBT(ResourceManager resourceManager) {
        loadFilesInDirectory(resourceManager, NBT_DIRECTORY, IS_SNBT_FILE, (location, file) -> {
            final ResourceLocation key = keyFromLocation(location, NBT_DIRECTORY, SNBT_FILE_ENDING);
            // Read file from input stream and insert into some data structure for later use.
            LootCategory.AddNBT(file, key.toString());
        });
    }

    /**
     * Load an individual file within the data pack.
     * Throws an error if the file does not exist.
     */
    private void loadIndividualFile(ResourceManager resourceManager, ResourceLocation location, Consumer<InputStream> consumer) {
        resourceManager.getResource(location).ifPresentOrElse(resource -> {
            try {
                DLDungeons.LOGGER.debug("Loading individual file: {}", location);
                consumer.accept(resource.open());
            } catch (IOException e) {
                throw new RuntimeException("Error loading file " + location, e);
            }
        }, () -> {
            throw new RuntimeException("Missing file " + location);
        });
    }

    /**
     * Load all files within a directory in the data pack.
     */
    private void loadFilesInDirectory(ResourceManager resourceManager,
                                      String directory,
                                      Predicate<ResourceLocation> validLocations,
                                      BiConsumer<ResourceLocation, InputStream> consumer) {
        resourceManager.listResources(directory, validLocations).forEach((location, resource) -> {
            try {
                final InputStream file = resource.open();
                DLDungeons.LOGGER.debug("Loading file: {}", location);
                consumer.accept(location, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Creates a key for a given resource location. Removes the base directory, the following slash and the file ending.
     *
     * @param location      the initial resource location.
     * @param baseDirectory the base path without the last slash. ( dirA/dirB not dirA/dirB/ )
     * @param fileEnding    the file ending to remove at the end of the path
     * @return the key
     */
    private static ResourceLocation keyFromLocation(ResourceLocation location, String baseDirectory, String fileEnding) {
        final String path = location.getPath();
        return new ResourceLocation(location.getNamespace(), path.substring(baseDirectory.length() + 1, path.length() - fileEnding.length()));
    }
}
