package jaredbgreat.dldungeons.builder;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.debug.Logging;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class DBlock {
	private final String id;   // The name
	private final Block block; // The Minecraft block
	private final int meta;	 // The blocks meta-data
	
	public static final Block spawner = Blocks.mob_spawner;
	public static final Block chest   = Blocks.chest;
	public static final Block portal1 = Blocks.end_portal_frame;
	public static final Block portal2 = Blocks.end_portal;
	public static final Block quartz  = Blocks.quartz_block;
	public static final Block lapis   = Blocks.lapis_block;
	public static final Block water   = Blocks.water;
	public static final Block air     = Blocks.air;
	
	public static final int chestid = Block.getIdFromBlock(chest);	
	public static final int spawnerid = Block.getIdFromBlock(spawner);	
	public static final int portal1id = Block.getIdFromBlock(portal1);	
	public static final int portal2id = Block.getIdFromBlock(portal2);
	
	public static final ArrayList<DBlock> registry = new ArrayList<DBlock>();
	
	
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
		//System.out.println("[DLD] Contructed Block " + block + " from "  + id);
	}
	
	
	private DBlock(String id, float version) throws NoSuchElementException {
		//System.out.println("[DLDUNGEONS] Loading block " + id + " as " + version);
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
	
	
	public void placeNoMeta(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		System.out.println(block);
		if(!isProtectedBlock(world, x, y, z)) 
			world.setBlockState(new BlockPos(x, y, z), block.getDefaultState());
	}
	
	
	public void place(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(!isProtectedBlock(world, x, y, z))
			world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(meta));
	}
	
	
	/************************************************************************************/
	/*                STATIC UTILITIES BELOW (non-static methods above)                 */
	/************************************************************************************/
	
	
	public static void place(World world, int x, int y, int z, int block) {
		if(!isProtectedBlock(world, x, y, z)) 
				registry.get(block).place(world, x, y, z);
	}
	
	
	public static int add(String id) {
		DBlock block = new DBlock(id);
		if(!registry.contains(block)) {
			registry.add(block);
		}
		//System.out.println("[DLD] Adding Block " + block + " from "  + id);
		return registry.indexOf(block);
	}	
	
	
	public static int add(String id, float version) throws NoSuchElementException {
		DBlock block = new DBlock(id, version);
		if(!registry.contains(block)) {
			registry.add(block);
		}
		return registry.indexOf(block);
	}

	
	public static void placeBlock(World world, int x, int y, int z, Block block) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(isProtectedBlock(world, x, y, z)) return;
		world.setBlockState(new BlockPos(x, y, z), block.getDefaultState());
	}
	
	
	public static void placeBlock(World world, int x, int y, int z, Block block, int a, int b) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		if(isProtectedBlock(world, x, y, z)) return; 
		world.setBlockState(new BlockPos(x, y, z), block.getStateFromMeta(a));
	}
	
	
	public static void deleteBlock(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewere. 
		// world.setBlock(x, y, z, 0);
		if(isProtectedBlock(world, x, y, z)) return;
		world.setBlockToAir(new BlockPos(x, y, z));
	}
	
	
	public static void deleteBlock(World world, int x, int y, int z, boolean flooded) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewere. 
		// world.setBlock(x, y, z, 0);
		if(isProtectedBlock(world, x, y, z)) return;
		if(flooded) placeBlock(world, x, y, z, water); 
		else world.setBlockToAir(new BlockPos(x, y, z));
	}
	
	
	public static void placeChest(World world, int x, int y, int z) {
		placeBlock(world, x, y, z, chest);		
	}
	
	
	public static void placeSpawner(World world, int x, int y, int z, String mob) {
		BlockPos pos = new BlockPos(x, y, z);
		if(isProtectedBlock(world, x, y, z)) return;
		placeBlock(world, x, y, z, spawner);
		TileEntityMobSpawner theSpawner = (TileEntityMobSpawner)world.getTileEntity(pos);
		MobSpawnerBaseLogic logic = theSpawner.getSpawnerBaseLogic();
		logic.setEntityName(mob);
	}
	
	
	public static boolean isGroundBlock(World world, int x, int y, int z) {
		IBlockState bs = world.getChunkFromChunkCoords(x / 16, z / 16).getBlockState(x, y, z);
		Material mat = bs.getMaterial();
		return 	   (mat == Material.grass) 
				|| (mat == Material.iron) 
				|| (mat == Material.ground) 
				|| (mat == Material.sand) 
				|| (mat == Material.rock) 
				|| (mat == Material.clay)
				|| (mat == Material.coral);
	}
	
	
	public static boolean isProtectedBlock(World world, int x, int y, int z) {
		int block = Block.getIdFromBlock(world.getBlockState(new BlockPos(x, y, z)).getBlock());
		return (block == chestid || block == spawnerid  
				|| block == portal1id || block == portal2id);

//		Block block = world.getBlock(x, y, z);
//		return (block == chest || block == spawner || 
//				block == portal1 || block == portal2 || 
//				block instanceof net.minecraft.block.BlockEndPortalFrame ||
//				block instanceof net.minecraft.block.BlockMobSpawner);
	}
	
	
	/************************************************************************************/
	/*                         BASIC OVERIDEN METHODS FROM OBJECT                       */
	/************************************************************************************/
	
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof DBlock)) return false; 
		return ((id.hashCode() == ((DBlock)other).id.hashCode()));
	}
	
	
	@Override
	public int hashCode() {
		int a = Block.getIdFromBlock(block);
		a = (a << 4) + meta;
		a += (a << 16);
		return a;
	}
}
