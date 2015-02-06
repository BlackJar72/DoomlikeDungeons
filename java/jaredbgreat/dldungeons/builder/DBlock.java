package jaredbgreat.dldungeons.builder;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import java.util.ArrayList;
import java.util.StringTokenizer;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

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
	
	public static final ArrayList<DBlock> registry = new ArrayList<DBlock>();
	
	
	public DBlock(String id) {
		this.id = id;
		meta  = 0;
		StringTokenizer nums = new StringTokenizer(id, "({[]})");
		block = (Block)Block.getBlockFromName(nums.nextToken());
		if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
	}
	
	
	public DBlock(String id, float version) {
		this.id = id;
		meta  = 0;
		if(version < 1.7) {
			StringTokenizer nums = new StringTokenizer(id, "({[]})");
			block = (Block)Block.getBlockFromName(nums.nextToken());
			if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());
		} else {
			StringTokenizer nums = new StringTokenizer(id, ":({[]})");
			block = GameRegistry.findBlock(nums.nextToken(), nums.nextToken());
			if(nums.hasMoreElements()) meta = Integer.parseInt(nums.nextToken());			
		}
	}
	
	
	public void placeNoMeta(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(!isProtectedBlock(world, x, y, z)) 
			world.setBlock(x, y, z, block);
	}
	
	
	public void place(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(!isProtectedBlock(world, x, y, z)) 
			world.setBlock(x, y, z, block, meta, 2);
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
//			System.out.println("[DLDUNGEONS] Adding block " + id 
//					+ " to registry as number " + registry.indexOf(block));
		}
		return registry.indexOf(block);
	}	
	
	
	public static int add(String id, float version) {
		DBlock block = new DBlock(id, version);
		if(!registry.contains(block)) {
			registry.add(block);
//			System.out.println("[DLDUNGEONS] Adding block " + id 
//					+ " to registry as number " + registry.indexOf(block));
		}
		return registry.indexOf(block);
	}

	
	public static void placeBlock(World world, int x, int y, int z, Block block) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere. 
		if(isProtectedBlock(world, x, y, z)) return; 
		world.setBlock(x, y, z, block);
	}
	
	
	public static void placeBlock(World world, int x, int y, int z, Block block, int a, int b) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewhere.
		if(isProtectedBlock(world, x, y, z)) return; 
		world.setBlock(x, y, z, block, a, b);
	}
	
	
	public static void deleteBlock(World world, int x, int y, int z) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewere. 
		// world.setBlock(x, y, z, 0);
		if(isProtectedBlock(world, x, y, z)) return;
		world.setBlockToAir(x, y, z);
	}
	
	
	public static void deleteBlock(World world, int x, int y, int z, boolean flooded) {
		// This wrapper is a protection against possible changes in block representation,
		// e.g., abandoning the ID system, allowing any needed changes to be made here
		// instead of elsewere. 
		// world.setBlock(x, y, z, 0);
		if(isProtectedBlock(world, x, y, z)) return;
		if(flooded) world.setBlock(x, y, z, water); 
		else world.setBlockToAir(x, y, z);
	}
	
	
	public static void placeChest(World world, int x, int y, int z) {
		if(!isProtectedBlock(world, x, y, z))
			world.setBlock(x, y, z, chest, 0, 2);		
	}
	
	
	public static void placeSpawner(World world, int x, int y, int z, String mob) {
		if(isProtectedBlock(world, x, y, z)) return;
		placeBlock(world, x, y, z, spawner);
		TileEntityMobSpawner theSpawner = (TileEntityMobSpawner)world.getTileEntity(x, y, z);
		theSpawner.func_145881_a().setEntityName(mob);
		//world.setBlockMetadataWithNotify(x, y, z, 15, 7);
	}
	
	
	public static boolean isGroundBlock(World world, int x, int y, int z) {
		Material mat = ((Block)world.getBlock(x, y, z)).getMaterial();
		return 	   (mat == Material.grass) 
				|| (mat == Material.iron) 
				|| (mat == Material.ground) 
				|| (mat == Material.sand) 
				|| (mat == Material.rock) 
				|| (mat == Material.clay)
				|| (mat == Material.coral);
	}
	
	
	public static boolean isProtectedBlock(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return (block == chest || block == spawner  
				|| block == portal1 || block == portal2);
	}
	
	
	/************************************************************************************/
	/*                         BASIC OVERIDEN METHODS FROM OBJECT                       */
	/************************************************************************************/
	
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof DBlock)) return false; 
		return ((id == ((DBlock)other).id) && (meta == ((DBlock)other).meta));
	}
	
	
	@Override
	public int hashCode() {
		// Not sure this would work much less what really would!
		return (block.hashCode() << 4) + meta;
	}
}
