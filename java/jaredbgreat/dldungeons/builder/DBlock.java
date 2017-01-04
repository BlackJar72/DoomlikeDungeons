package jaredbgreat.dldungeons.builder;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.api.DLDEvent;

import jaredbgreat.dldungeons.debug.Logging;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class DBlock {
	private final String id;   // The name
	private final Block block; // The Minecraft block
	private final int meta;	   // The blocks meta-data
	
	// Block constants used by the mod for various purposes, usually for placement
	public static final Block spawner = (Block)Block.getBlockFromName("mob_spawner");
	public static final Block chest   = (Block)Block.getBlockFromName("chest");
	public static final Block portal1 = (Block)Block.getBlockFromName("end_portal_frame");
	public static final Block portal2 = (Block)Block.getBlockFromName("end_portal");
	public static final Block quartz  = (Block)Block.getBlockFromName("quartz_block");
	public static final Block lapis   = (Block)Block.getBlockFromName("lapis_block");
	public static final Block water   = (Block)Block.getBlockFromName("water");
	public static final Block air     = (Block)Block.getBlockFromName("air");
	
	// Block IDs, used for comparison to tell if the block is allowed to be replaced
	public static final int chestid   = Block.getIdFromBlock(chest);	
	public static final int spawnerid = Block.getIdFromBlock(spawner);	
	public static final int portal1id = Block.getIdFromBlock(portal1);	
	public static final int portal2id = Block.getIdFromBlock(portal2);
	
	// All blocks, complete with meta-data used by the mod
	public static final ArrayList<DBlock> registry = new ArrayList<DBlock>();
	
	
	/**
	 * Construct a dungeon block using an older theme format, from before
	 * version 1.7 of the mod.
	 * 
	 * @param id
	 */
	private DBlock(String id) {
		this.id = id;
		StringTokenizer nums = new StringTokenizer(id, "({[]})");
		block = (Block)Block.getBlockFromName(nums.nextToken());
		if(block == null) {
			Logging.LogError("[DLDUNGEONS] ERROR! Block read as \"" + id 
					+ "\" was was not in registry (returned null).");
		}
		if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
		else meta = 0;
	}
		
	
	/**
	 * Construct a dungeon block using a theme format from version 1.7 of the mod
	 * or newer.
	 * 
	 * @param id
	 */
	private DBlock(String id, float version) throws NoSuchElementException {
		this.id = id;
		if(version < 1.7) {
			StringTokenizer nums = new StringTokenizer(id, "({[]})");
			block = (Block)Block.getBlockFromName(nums.nextToken());
			if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
			else meta = 0;
		} else {
			StringTokenizer nums = new StringTokenizer(id, ":({[]})");
			String modid = nums.nextToken();
			if(modid.toLowerCase().equals("minecraft") || modid.toLowerCase().equals("vanilla")) {
				block = (Block)Block.getBlockFromName(nums.nextToken());
				if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
				else meta = 0;
			} else {
				block = Block.getBlockFromItem(GameRegistry.findItem(modid, nums.nextToken()));
				if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
				else meta = 0;	
			}			
		}
		if(block == null) {
			String error = "[DLDUNGEONS] ERROR! Block read as \"" + id 
					+ "\" was was not in registry (returned null).";
			Logging.LogError(error);
			throw new NoSuchElementException(error);
		}
		if(block == null) {
			String error = "[DLDUNGEONS] ERROR! Block read as \"" + id 
					+ "\" was was not in registry (returned null).";
			Logging.LogError(error);
			throw new NoSuchElementException(error);
		}
	}
	
	
	/**
	 * Places a the block into the world at the given coordinates and with meta-data 
	 * as zero.  This wrapping eases updating with block representation and method
	 * names change.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public void placeNoMeta(World world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		BlockPos pos = new BlockPos(x, y, z);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDBlock(world, pos, this))) return;
		world.setBlockState(new BlockPos(x, y, z), block.getDefaultState());
	}
	
	
	/**
	 * Places the block in the world, including its correct meta-data.  This wrapping allow
	 * for changes in block / state representation and method signatures to be easily
	 * adapted and for meta-blocks to easily be used in dungeons. 
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public void place(World world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		BlockPos pos = new BlockPos(x, y, z);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceDBlock(world, pos, this))) return;
		world.setBlockState(pos, block.getStateFromMeta(meta));
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
	public static void place(World world, int x, int y, int z, int block) {
		if(!isProtectedBlock(world, x, y, z)) 
				registry.get(block).place(world, x, y, z);
	}
	
	
	/**
	 * Turns a string labeling the block into a DBlock and adds it to the registry if 
	 * its not already present.  It will return the new DBlocks registry index for 
	 * use as an internal id.
	 * 
	 * This is for use with older theme formats from before mod version 1.7.
	 * 
	 * @param id
	 * @return The index of the block in the DBlock registry
	 */
	public static int add(String id) {
		DBlock block = new DBlock(id);
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
	public static int add(String id, float version) throws NoSuchElementException {
		DBlock block = new DBlock(id, version);
		if(!registry.contains(block)) {
			registry.add(block);
		}
		return registry.indexOf(block);
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
	public static boolean placeBlock(World world, int x, int y, int z, Block block) {
		if(isProtectedBlock(world, x, y, z)) return false;
		BlockPos pos = new BlockPos(x, y, z);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.PlaceBlock(world, pos, block))) return false;
		world.setBlockState(pos, block.getDefaultState());
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
	public static void placeBlock(World world, int x, int y, int z, Block block, int a, int b) {
		if(isProtectedBlock(world, x, y, z)) return; 
		world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(a));
	}
	
	
	/**
	 * A wrapper for setting a block to air.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void deleteBlock(World world, int x, int y, int z) {
		if(isProtectedBlock(world, x, y, z)) return;
		world.setBlockToAir(new BlockPos(x, y, z));
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
	public static void deleteBlock(World world, int x, int y, int z, boolean flooded) {
		if(isProtectedBlock(world, x, y, z)) return;
		if(flooded) placeBlock(world, x, y, z, water); 
		else world.setBlockToAir(new BlockPos(x, y, z));
	}
	
	
	/**
	 * Simply a wrapper for placing a chest.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void placeChest(World world, int x, int y, int z) {
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
	public static void placeSpawner(World world, int x, int y, int z, String mob) {
		BlockPos pos = new BlockPos(x, y, z);
		if (MinecraftForge.TERRAIN_GEN_BUS.post(new DLDEvent.BeforePlaceSpawner(world, pos, mob))) return;
		if(isProtectedBlock(world, x, y, z)) return;
		if(!placeBlock(world, x, y, z, spawner)) return;
		TileEntityMobSpawner theSpawner = (TileEntityMobSpawner)world.getTileEntity(pos);
		MobSpawnerBaseLogic logic = theSpawner.getSpawnerBaseLogic();
		logic.setEntityName(mob);
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
	public static boolean isGroundBlock(World world, int x, int y, int z) {
		IBlockState bs = world.getBlockState(new BlockPos(x, y, z));
		Material mat = bs.getMaterial();
		return 	   (mat == Material.GRASS) 
				|| (mat == Material.IRON) 
				|| (mat == Material.GROUND) 
				|| (mat == Material.SAND) 
				|| (mat == Material.ROCK) 
				|| (mat == Material.CLAY
				// Failsafe, it can never go into the void, or become an infinite loop
				|| (y < 0));
	}
	
	
	/**
	 * True if the block is one that should not be built over; specifically a chest,
	 * spawner, or any part of the End portal.
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static boolean isProtectedBlock(World world, int x, int y, int z) {
		int block = Block.getIdFromBlock(world.getBlockState(new BlockPos(x, y, z)).getBlock());
		return (block == chestid || block == spawnerid  
				|| block == portal1id || block == portal2id);
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
		if(!(other instanceof DBlock)) return false; 
		return ((id.hashCode() == ((DBlock)other).id.hashCode()));
	}
	
	
	/**
	 * Returns a hash code derived from the block id and meta data; it will only produce 
	 * the same hash code if these are both equal, that is, equal hash codes implies equals() 
	 * is true. 
	 */
	@Override
	public int hashCode() {
		int a = Block.getIdFromBlock(block);
		a = (a << 4) + meta;
		a += (a << 16);
		return a;
	}
}
