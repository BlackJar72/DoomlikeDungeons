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
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class DBlock {
	int id; 	// The Minecraft block ID
	int meta;	// The blocks meta-data	

	public static final int chest    = Block.chest.blockID;
	public static final int spawner  = Block.mobSpawner.blockID;
	public static final int portal1  = Block.endPortalFrame.blockID;
	public static final int portal2  = Block.endPortal.blockID;
	
	public static final ArrayList<DBlock> registry = new ArrayList<DBlock>();
	
	
	public DBlock(int id, int meta) {
		this.id = id;
		this.meta = meta;
	}
	
	
	public void placeNoMeta(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(!isProtectedBlock(world, x, y, z)) 
			world.setBlock(x, y, z, id);
	}
	
	
	public void place(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(!isProtectedBlock(world, x, y, z)) 
			world.setBlock(x, y, z, id, meta, 2);
	}
	
	
	/************************************************************************************/
	/*                STATIC UTILITIES BELOW (non-static methods above)                 */
	/************************************************************************************/
	
	
	public static void place(World world, int x, int y, int z, int block) {
		if(!isProtectedBlock(world, x, y, z)) 
				registry.get(block).place(world, x, y, z);
	}
	
	
	public static int add(int id, int meta) {
		DBlock block = new DBlock(id, meta);
		if(!registry.contains(block)) registry.add(block);
		return registry.indexOf(block);
	}
	
	public static void placeBlock(World world, int x, int y, int z, int block) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(!isProtectedBlock(world, x, y, z)) 
			world.setBlock(x, y, z, block);
	}
	
	
	public static void placeBlock(World world, int x, int y, int z, int block, int a, int b) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(!isProtectedBlock(world, x, y, z)) 
			world.setBlock(x, y, z, block, a, b);
	}
	
	
	public static void deleteBlock(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewere. 
		// world.setBlock(x, y, z, 0);
		if(!isProtectedBlock(world, x, y, z))
			world.setBlockToAir(x, y, z);
	}
	
	
	public static void deleteBlock(World world, int x, int y, int z, boolean flooded) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewere. 
		// world.setBlock(x, y, z, 0);
		if(!isProtectedBlock(world, x, y, z)) {
			if(flooded) world.setBlock(x, y, z, 9);
			else world.setBlockToAir(x, y, z);
		}
	}
	
	
	public static void placeChest(World world, int x, int y, int z) {
		if(!isProtectedBlock(world, x, y, z))
			world.setBlock(x, y, z, chest, 0, 2);		
	}
	
	
	public static void placeSpawner(World world, int x, int y, int z, String mob) {
		if(isProtectedBlock(world, x, y, z)) return;
		placeBlock(world, x, y, z, Block.mobSpawner.blockID);
		TileEntityMobSpawner spawner = (TileEntityMobSpawner)world.getBlockTileEntity(x, y, z);
		spawner.getSpawnerLogic().setMobID(mob);
		//world.setBlockMetadataWithNotify(x,y,z, 15, 7);
	}
	
	
	public static boolean isGroundBlock(World world, int x, int y, int z) {
		Material mat = world.getBlockMaterial(x, y, z);
		return (mat == Material.grass) || (mat == Material.ground) || (mat == Material.sand) 
				|| (mat == Material.rock) || (mat == Material.coral) || (mat == Material.craftedSnow) 
				|| (mat == Material.clay);
	}
	
	
	public static boolean isProtectedBlock(World world, int x, int y, int z) {
		int block = world.getBlockId(x, y, z);
		return (block == chest || block == spawner  
				|| block == portal1 || block == portal2);
	}
	
	
	/************************************************************************************/
	/*                         BASIC OVERIDEN METHODS FROM OBJECT                       */
	/************************************************************************************/
	
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof DBlock)) return false; 
		return ((hashCode() == ((DBlock)other).hashCode()));
	}
	
	
	@Override
	public int hashCode() {
		return (id << 4) + meta;
	}
}
