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
import net.minecraft.block.state.BlockState;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DBlock {
	String id;   // The name
	Block block; // The Minecraft block
	int meta;	 // The blocks meta-data	
	
	public static final Block spawner = (Block)Block.getBlockFromName("mob_spawner");
	public static final Block chest   = (Block)Block.getBlockFromName("chest");
	public static final Block portal1 = (Block)Block.getBlockFromName("end_portal_frame");
	public static final Block portal2 = (Block)Block.getBlockFromName("end_portal");
	public static final Block quartz  = (Block)Block.getBlockFromName("quartz_block");
	public static final Block lapis   = (Block)Block.getBlockFromName("lapis_block");
	public static final Block water   = (Block)Block.getBlockFromName("water");
	public static final Block air     = (Block)Block.getBlockFromName("air");
	
	public static final int chestid = Block.getIdFromBlock(chest);	
	public static final int spawnerid = Block.getIdFromBlock(spawner);	
	public static final int portal1id = Block.getIdFromBlock(portal1);	
	public static final int portal2id = Block.getIdFromBlock(portal2);
	
	public static final ArrayList<DBlock> registry = new ArrayList<DBlock>();
	
	
	public DBlock(String id) {
		this.id = id;
		meta  = 0;
		StringTokenizer nums = new StringTokenizer(id, "({[]})");
		block = (Block)Block.getBlockFromName(nums.nextToken());
		if(block == null) {
			Logging.LogError("[DLDUNGEONS] ERROR! Block read as \"" + id 
					+ "\" was was not in registry (returned null).");
		}
		if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
		//System.out.println("[DLD] Contructed Block " + block + " from "  + id);
	}
	
	
	public DBlock(String id, float version) throws NoSuchElementException {
		//System.out.println("[DLDUNGEONS] Loading block " + id + " as " + version);
		this.id = id;
		meta  = 0;
		if(version < 1.7) {
			StringTokenizer nums = new StringTokenizer(id, "({[]})");
			block = (Block)Block.getBlockFromName(nums.nextToken());
			if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
		} else {
			StringTokenizer nums = new StringTokenizer(id, ":({[]})");
			String modid = nums.nextToken();
			if(modid.toLowerCase().equals("minecraft") || modid.toLowerCase().equals("vanilla")) {
				block = (Block)Block.getBlockFromName(nums.nextToken());
				if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
			} else {
				block = Block.getBlockFromItem(GameRegistry.findItem(modid, nums.nextToken()));
				if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());	
			}			
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
		// instead of elsewhere.
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
		if(!isProtectedBlock(world, x, y, z))
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
		Material mat = ((Block)world.getChunkFromChunkCoords(x / 16, z / 16).getBlock(x, y, z)).getMaterial();
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
		// Not sure this would work much less what really would!
		return id.hashCode();
	}
}
