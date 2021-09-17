package jaredbgreat.dldungeons.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;

import jaredbgreat.dldungeons.util.debug.Logging;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.registries.ForgeRegistries;

public final class RegisteredBlock extends AbstractBlock {
	private final String id;   // The name
	private final IBlockPlacer block;
	
	public static final Block quartz  = Blocks.QUARTZ_BLOCK;
	public static final Block lapis   = Blocks.LAPIS_BLOCK;
	public static final Block water   = Blocks.WATER;
	public static final Block air     = Blocks.AIR;
	
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
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(in));
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
		DBlock maybe = DBlock.makeDBlock(id);
		if(maybe == null) {
			// FXIME
			maybe = DBlock.makeDBlock("minecraft:hay_block");
		}
		
		if(((BlockState) maybe.getContents()).getBlock().getRegistryName().toString().contains("minecraft:air") && !id.contains("minecraft:air")) { 
			// FXIME
			maybe = DBlock.makeDBlock("minecraft:hay_block");
			String error = "[DLDUNGEONS] ERROR! Block read as \"" + id 
					+ "\" parsed into an air block!";
			Logging.logError(error);
			//throw new NoSuchElementException(error);
		}
		block = maybe;
	}
	
	
	public static void listDBlocks(File listsDir) {
		ArrayList<String> namel = new ArrayList<>();
		names.forEach( (name) -> namel.add(name) ) ;
		Collections.sort(namel);
		File itemlist = new File(listsDir.toString() + File.separator + "DBlocks.txt");
		if(itemlist.exists()) itemlist.delete(); 
		try {
			final BufferedWriter outstream 
					= new BufferedWriter(new FileWriter(itemlist.toString()));
			
			for(String name : namel) {
				outstream.write(name);
				outstream.newLine();
			}
			
			if(outstream != null) outstream.close();
		} catch (IOException e) {
			System.err.println("Error: Could not write file blocks.txt");
			e.printStackTrace();
		}		
	}
	
	
	
/**
 * Construct a dungeon block using a theme format from version 1.7 of the mod
 * or newer.
 * 
 * @param id
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
	public static void place(ISeedReader world, int x, int y, int z, int block) {
		if(!isProtectedBlock(world, x, y, z)) 
				registry.get(block).place(world, x, y, z);
	}
	
	
	/**
	 * Turns a string labeling the block into a DBlock and adds it to the registry if 
	 * its not already present.  It will return the new DBlocks registry index for 
	 * use as an internal id.
	 * 
	 * This is for use with mod versions newer that 1.7.
	 * 
	 * 
	 * @param id
	 * @param version
	 * @return
	 * @throws NoSuchElementException
	 */
	public static int add(String id) throws NoSuchElementException {
		if(id.startsWith("$")) {
			return add(BlockFamily.getBlockFamily(id));
		}
		RegisteredBlock block = new RegisteredBlock(id);
		if(!registry.contains(block)) {
			registry.add(block);
		}
		return registry.indexOf(block);
	}
	
	
	/**
	 * Turns a string labeling the block into a DBlock and adds it to the registry if 
	 * its not already present.  It will return the new DBlocks registry index for 
	 * use as an internal id.
	 * 
	 * This is for use with mod versions newer that 1.7.
	 * 
	 * 
	 * @param id
	 * @param version
	 * @return
	 * @throws NoSuchElementException
	 */
	public static int add(BlockFamily family) throws NoSuchElementException {
		RegisteredBlock blocks = new RegisteredBlock(family);
		if(!registry.contains(blocks)) {
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
	public static boolean placeBlock(ISeedReader world, int x, int y, int z, Block block) {
		if(isProtectedBlock(world, x, y, z)) return false;
		BlockPos pos = new BlockPos(x, y, z);
		//if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceBlock(world, pos, block))) return false;
		world.setBlock(pos, block.defaultBlockState(), 3);
		return true;
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
	public static void placeBlock(ISeedReader world, int x, int y, int z, Block block, int a, int b) {
		if(isProtectedBlock(world, x, y, z)) return; 
		world.setBlock(new BlockPos(x, y, z), block.defaultBlockState(), 3);
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
	public static void placeBlock(ISeedReader world, int x, int y, int z, BlockState block) {
		if(isProtectedBlock(world, x, y, z)) return; 
		world.setBlock(new BlockPos(x, y, z), block, 3);
	}
	
	
	/**
	 * A wrapper for setting a block to air.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void deleteBlock(ISeedReader world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		world.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
	}
	
	
	/**
	 * Almost a wrapper for setting the block to air in world, but will alternately set
	 * blocks to water instead if a dungeons is flooded.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param flooded
	 */
	public static void deleteBlock(ISeedReader world, int x, int y, int z, boolean flooded) {
		if(isProtectedBlock(world, x, y, z)) return;
		if(flooded) placeBlock(world, x, y, z, water); 
		else world.removeBlock(new BlockPos(x, y, z), false);
	}
	
	
	public static void deleteBlock(ISeedReader world, int x, int y, int z, int block) {
		if(isProtectedBlock(world, x, y, z)) return;
		if(block > 0) registry.get(block).place(world, x, y, z); 
		else registry.get(block).place(world, x, y, z);;
	}
	
	
	/**
	 * Simply a wrapper for placing a chest.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void placeChest(ISeedReader world, int x, int y, int z) {
		placeBlock(world, x, y, z, chest);		
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
	public static void placeSpawner(ISeedReader world, int x, int y, int z, String mob) {
		// Place spawner block
		BlockPos pos = new BlockPos(x, y, z);
		//if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.BeforePlaceSpawner(world, pos, mob))) return;
		if(isProtectedBlock(world, x, y, z)) return;
		if(!placeBlock(world, x, y, z, spawner)) return;
		MobSpawnerTileEntity theSpawner = (MobSpawnerTileEntity)world.getBlockEntity(pos);
		
		// Set up spawner logic
		AbstractSpawner logic = theSpawner.getSpawner();		
		CompoundNBT spawnData = new CompoundNBT();
	    spawnData.putString("id", mob);
	    logic.setNextSpawnData(new WeightedSpawnerEntity(1, spawnData));
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
	public static boolean isGroundBlock(ISeedReader world, int x, int y, int z) {
		BlockState bs = world.getBlockState(new BlockPos(x, y, z));
		Material mat = bs.getMaterial();
		return 	   (mat == Material.GRASS) 
				|| (mat == Material.METAL) 
				|| (mat == Material.DIRT) 
				|| (mat == Material.SAND) 
				|| (mat == Material.STONE) 
				|| (mat == Material.CLAY
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
		if(!(other instanceof RegisteredBlock)) return false; 
		return (block.equals(((RegisteredBlock)other).block));
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
	public void placeNoMeta(ISeedReader world, int x, int y, int z) {
		block.placeNoMeta(world, x, y, z);
	}


	@Override
	public void place(ISeedReader world, int x, int y, int z) {
		block.place(world, x, y, z);
	}


	@Override
	public Object getContents() {
		return block;
	}
}
