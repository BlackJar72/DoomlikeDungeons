package jaredbgreat.dldungeons.builder;

import com.github.xyroc.dldungeons.DLDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class RegisteredBlock extends AbstractBlock {
    private final String id;   // The name
    private final IBlockPlacer block;

    public static final Block quartz = Blocks.QUARTZ_BLOCK;
    public static final Block lapis = Blocks.LAPIS_BLOCK;
    public static final Block water = Blocks.WATER;
    public static final Block air = Blocks.AIR;

    // All blocks, complete with meta-data used by the mod
    public static final ArrayList<RegisteredBlock> registry = new ArrayList<RegisteredBlock>();

    private static HashSet<String> names = new HashSet<>();

    /**
     * Gets the item named by the string "in" -- hacky, but might work
     * for now, hopefully....
     *
     * @param in
     * @return
     */
    private static Item getItem(String in) {
        return Items.AIR;
    }


    /**
     * Construct a dungeon block using a theme format from version 1.7 of the mod
     * or newer.
     *
     * @param id
     */
    private RegisteredBlock(String id) throws NoSuchElementException {
        names.add(id);
        this.id = id;
        block = DBlock.makeDBlock(id);
        if (block.toString().contains("minecraft:air")
                && !id.contains("minecraft:air")) {
            String error = "[DLDUNGEONS] ERROR! Block read as \"" + id
                    + "\" parsed into an air block!";
            throw new NoSuchElementException(error);
        }
    }


    public static void listDBlocks(File listsDir) {
        ArrayList<String> namel = new ArrayList<>();
        names.forEach((name) -> namel.add(name));
        Collections.sort(namel);
        File itemlist = new File(listsDir.toString() + File.separator + "DBlocks.txt");
        if (itemlist.exists()) itemlist.delete();
        try {
            final BufferedWriter outstream
                    = new BufferedWriter(new FileWriter(itemlist.toString()));

            for (String name : namel) {
                outstream.write(name);
                outstream.newLine();
            }

            if (outstream != null) outstream.close();
        } catch (IOException e) {
            System.err.println("Error: Could not write file blocks.txt");
            e.printStackTrace();
        }
    }


    /**
     * Construct a dungeon block using a theme format from version 1.7 of the mod
     * or newer.
     */
    private RegisteredBlock(BlockFamily family) throws NoSuchElementException {
        this.id = family.getName();
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
        }/* else {
            world.getChunk(pos).markPosForPostprocessing(pos);
        }*/
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
     * This is for use with mod versions newer that 1.7.
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


    @Override
    public Object getContents() {
        return block;
    }
}
