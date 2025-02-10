package jaredbgreat.dldungeons.builder;

import com.github.xyroc.dldungeons.DLDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class RegisteredBlock extends AbstractBlock implements IMapBlock {
    private final String name;   // The name
    private final IBlockPlacer block;

    public static final Block quartz = Blocks.QUARTZ_BLOCK;
    public static final Block lapis = Blocks.LAPIS_BLOCK;
    public static final Block water = Blocks.WATER;
    public static final Block air = Blocks.AIR;

    // All blocks, complete with meta-data used by the mod
    public static final CopyOnWriteArrayList<RegisteredBlock> registry = new CopyOnWriteArrayList<>();


    @Override
    public String getName() {
        return name;
    }


    /**
     * Clear everything in preparation for reload; this should only be done at the beginning of a data pack reload,
     * to keep ids from different worlds, which may have different data packs, from scrambling each other's ids.  It
     * should NOT be called any other time!
     *
     * This does NOT make it safe to change data packs on existing worlds, but does worlds with different data packs
     * from scrambling each other if loaded into in different orders (and would me a necessary first step in persistent
     * block data which might solve the problems associate with changing blocks within w world though it does not
     * do that on its own).
     */
    public static void clear() {
        registry.clear();
    }


    /**
     * Construct a dungeon block using a theme format from version 1.7 of the mod
     * or newer.
     *
     * @param id
     */
    private RegisteredBlock(String id) throws NoSuchElementException {
        this.name = id;
        block = DBlock.makeDBlock(id);
        if (block.toString().contains("minecraft:air")
                && !id.contains("minecraft:air")) {
            String error = "[DLDUNGEONS] ERROR! Block read as \"" + id
                    + "\" parsed into an air block!";
            throw new NoSuchElementException(error);
        }
    }


    public static List<String> getRegisteredNames() {
        List<String> output = new ArrayList<>(registry.size());
        for(RegisteredBlock rb : registry) {
            output.add(rb.name);
        }
        return output;
    }


    /**
     * Construct a dungeon block using a theme format from version 1.7 of the mod
     * or newer.
     */
    private RegisteredBlock(BlockFamily family) throws NoSuchElementException {
        this.name = family.getName();
        block = family;
    }


    /************************************************************************************/
    /*                STATIC UTILITIES BELOW (non-static methods above)                 */
    /************************************************************************************/


    /**
     * This will place a block from the DBlock registry into the world based on its internal
     * ID (i.e., its registry index).
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     */
    public static void place(WorldGenLevel world, int x, int y, int z, int block) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!isProtectedBlock(world, pos))
            registry.get(block).place(world, pos);
        FluidState fluidState = world.getFluidState(new BlockPos(x, y, z));
        if (!fluidState.isEmpty()) {
            world.scheduleTick(pos, fluidState.getType(), 0);
        }
    }


    /**
     * This will place a fence/wall block (or other that needs post processing) from the DBlock registry into the world
     * based on its internal ID (i.e., its registry index).
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     */
    public static void placeFence(WorldGenLevel world, int x, int y, int z, int block) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!isProtectedBlock(world, pos))
            registry.get(block).place(world, pos);
        world.getChunk(pos).markPosForPostprocessing(pos);
    }


    /**
     * Turns a string labeling the block into a DBlock and adds it to the registry if
     * its not already present.  It will return the new DBlocks registry index for
     * use as an internal id.
     * <p>
     * This is for use with mod versions newer than 1.7.
     *
     * @param id
     * @return
     * @throws NoSuchElementException
     */
    public static int add(String id) throws NoSuchElementException {
        if (id.startsWith("$")) {
            return add(BlockFamily.getBlockFamily(id));
        }
        RegisteredBlock block = new RegisteredBlock(id);
        if (!registry.contains(block)) {
            registry.add(block);
        }
        return registry.indexOf(block);
    }


    /**
     * A version of add for loading from a saved data file.  It does not assume BlockFamilies exist, but
     * instead creates them in a placeholder form that will later be turned into a proper form.
     *
     * @param id
     * @return
     * @throws NoSuchElementException
     */
    public static int addOnLoad(String id) throws NoSuchElementException {
        if (id.startsWith("$")) {
            return add(BlockFamily.loadBlockFamily(id));
        }
        RegisteredBlock block = new RegisteredBlock(id);
        if (!registry.contains(block)) {
            registry.add(block);
        }
        return registry.indexOf(block);
    }


    /**
     * Turns a string labeling the block into a DBlock and adds it to the registry if
     * its not already present.  It will return the new DBlocks registry index for
     * use as an internal id.
     * <p>
     * This is for use with mod versions newer that 1.7.
     *
     * @return
     * @throws NoSuchElementException
     */
    public static int add(BlockFamily family) throws NoSuchElementException {
        RegisteredBlock blocks = new RegisteredBlock(family);
        if (!registry.contains(blocks)) {
            registry.add(blocks);
        }
        return registry.indexOf(blocks);
    }


    /**
     * Gets the RegisteredBlock that has been given the internal id by this
     * mod.
     *
     * @param id
     * @return
     * @throws NoSuchElementException
     */
    public static RegisteredBlock get(int id) throws NoSuchElementException {
        return registry.get(id);
    }


    /**
     * Gets the block placer for a the given id.
     *
     * @param id
     * @return
     * @throws NoSuchElementException
     */
    public static IBlockPlacer getPlacer(int id) throws NoSuchElementException {
        return registry.get(id);
    }


    /**
     * Purely a wrapper for block/state placing/setting methods typically found in world,
     * allowing easier updating when block representation or method signature / location
     * changes.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param block
     */
    public static boolean placeBlock(WorldGenLevel world, int x, int y, int z, Block block) {
        BlockPos pos = new BlockPos(x, y, z);
        if (isProtectedBlock(world, pos)) return false;
        world.setBlock(pos, block.defaultBlockState(), 2);
        return true;
    }

    
    public static void placeBlock(WorldGenLevel world, int x, int y, int z, BlockState block, int a, int b) {
        BlockPos pos = new BlockPos(x, y, z);
        if (isProtectedBlock(world, pos)) return;
        world.setBlock(pos, block, 2);
    }


    /**
     * A wrapper for setting a block to air.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    public static void deleteBlock(WorldGenLevel world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (isProtectedBlock(world, pos)) return;
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
    }


    public static void deleteBlock(WorldGenLevel world, int x, int y, int z, int block) {
        BlockPos pos = new BlockPos(x, y, z);
        if (isProtectedBlock(world, pos)) return;
        registry.get(block).place(world, pos);
    }


    /**
     * Simply a wrapper for placing a chest.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     */
    public static void placeChest(WorldGenLevel world, int x, int y, int z) {
        placeBlock(world, x, y, z, Blocks.CHEST);
    }


    /**
     * This will place a spawner and set it to spawn the mob named.
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param mob
     */
    public static void placeSpawner(WorldGenLevel world, int x, int y, int z, String mob) {
        // Place spawner block
        BlockPos pos = new BlockPos(x, y, z);
        if (isProtectedBlock(world, pos)) return;
        if (!placeBlock(world, x, y, z, Blocks.SPAWNER)) return;
        final BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof SpawnerBlockEntity spawner) {
            EntityType<?> mobtype = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(mob));
            spawner.setEntityId(mobtype, RandomSource.create());
        } else {
            if((be != null)) {
                String error = "ERROR! Spawner placed at \"" + x + " " + y + " " + z
                        + "\" is actually " + Objects.requireNonNullElse(be.getClass().getSimpleName(), "(None)");
                DLDungeons.LOGGER.error(error);
            } else {
                String error = "ERROR! Spawner placed at \"" + x + " " + y + " " + z + " is actually NULL!!!"
                        + "  Actual block was " + world.getBlockState(pos).getBlock().getName() + ".";
                DLDungeons.LOGGER.error(error);
            }
        }
    }


    /**
     * True if the block Material is ground, grass, iron, sand, rock, or clay. This is
     * used to determine if building under a structure should stop because it has reached
     * the ground, so true really mean "stop building now."
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static boolean isGroundBlock(WorldGenLevel world, int x, int y, int z) {
        boolean output = false;
        BlockState bs = world.getBlockState(new BlockPos(x, y, z));
        // FIXME: This could probably be done better by creating my own tag holding the union of all thoes checked here!
        return (bs.is(Tags.Blocks.GRAVEL)
                || bs.is(Tags.Blocks.ORES)
                || bs.is(Tags.Blocks.GRAVEL)
                || bs.is(Tags.Blocks.COBBLESTONE)
                || bs.is(Tags.Blocks.OBSIDIAN)
                || bs.is(BlockTags.DIRT)
                || bs.is(BlockTags.SAND)
                || bs.is(Tags.Blocks.SAND)
                || bs.is(Tags.Blocks.STONE)
                || bs.is(Tags.Blocks.SANDSTONE)
                || bs.is(BlockTags.TERRACOTTA)
                // Failsafe, it can never go into the void, or become an infinite loop
                || (y < 0));
    }


    /************************************************************************************/
    /*                         BASIC OVERIDEN METHODS FROM OBJECT                       */
    /************************************************************************************/


    /**
     * Returns true if the other object is a DBlock the holds the same block with
     * the same meta-data.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof RegisteredBlock)) return false;
        return (block.equals(((RegisteredBlock) other).block));
    }


    /**
     * Returns a hash code derived from the block id and meta data; it will only produce
     * the same hash code if these are both equal, that is, equal hash codes implies equals()
     * is true.
     */
    @Override
    public int hashCode() {
        return block.hashCode();
    }


    @Override
    public void place(WorldGenLevel world, BlockPos pos) {
        block.place(world, pos);
    }


}
