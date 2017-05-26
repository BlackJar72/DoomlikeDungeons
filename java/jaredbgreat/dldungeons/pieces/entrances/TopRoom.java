package jaredbgreat.dldungeons.pieces.entrances;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Level;
import net.minecraft.block.Block;
import net.minecraft.world.World;


/**
 * An entrance with a small, one-room building, either complete or as a ruin,
 * at the top.
 * 
 * @author Jared Blackburn
 *
 */
public class TopRoom extends AbstractEntrance {
	int wx, wz, bottom, top, xdim, ydim, zdim, ymod, xmin, xmax, zmin, zmax, below; 
	
	public TopRoom(int x, int z) {
		super(x, z);
		
	}

	
	@Override
	public void build(Level dungeon, World world) {
		//DoomlikeDungeons.profiler.startTask("Generating Top Room Numbers (TopRoom)");
		wx = x + (dungeon.map.chunkX * 16) - (dungeon.map.room.length / 2) + 8;
		wz = z + (dungeon.map.chunkZ * 16) - (dungeon.map.room.length / 2) + 8;
		bottom = dungeon.map.floorY[x][z];
		top = world.getActualHeight();
		while(!DBlock.isGroundBlock(world, wx, top, wz)) top--;
		xdim = dungeon.random.nextInt(7) + 6;
		zdim = dungeon.random.nextInt(7) + 6;
		ymod = (xdim <= zdim) ? (int) Math.sqrt(xdim) : (int) Math.sqrt(zdim);
		ydim = dungeon.random.nextInt((dungeon.verticle.value / 2) + (ymod / 2) + 2) + 2;
		
		xmin = wx - (xdim / 2);
		xmax = wx + (xdim / 2);
		zmin = wz - (zdim / 2);
		zmax = wz + (zdim / 2);
		//DoomlikeDungeons.profiler.endTask("Generating Top Room Numbers (TopRoom)");
		
		// Build a small building over the entrance
		if(dungeon.random.nextBoolean() || dungeon.degeneracy.use(dungeon.random)) 
			buildRuin(dungeon, world);
		else buidBuilding(dungeon, world);
			
		// Build the actual way in
		if(dungeon.random.nextBoolean()) buildLatter(world, dungeon); 
		else buildStair(world, dungeon);
	}
	

	
	
	//////////////////////////////////////////////////////////////////////////
	//                       Building The Upper Ruin                        //
	//////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Build a complete / intact building, consisting of one room in the 
	 * shape of a rectangular solid.  This will also fill in a any areas 
	 * between the room and ground so that it doesn't float.
	 * 
	 * @param dungeon
	 * @param world
	 */
	private void buidBuilding(Level dungeon, World world) {
		//DoomlikeDungeons.profiler.startTask("Generating Building (TopRoom)");
		//DoomlikeDungeons.profiler.startTask("Generating Floor (TopRoom)");
		for(int i = xmin + 1; i < xmax; i++) 
			for(int j = zmin + 1; j < zmax; j++) {
				DBlock.place(world, i, top, j, dungeon.floorBlock);
				for(int k = top + (ydim * 2); k >= top + 1; k--) 
					DBlock.deleteBlock(world, i, k, j);
				below = top - 1;
				while(!DBlock.isGroundBlock(world, i, below, j)) {
					DBlock.place(world, i, below, j, dungeon.floorBlock);
					below--;
				}
			}
		//DoomlikeDungeons.profiler.endTask("Generating Floor (TopRoom)");
		//DoomlikeDungeons.profiler.startTask("Generating X-Walls (TopRoom)");
		for(int i = xmin; i <= xmax; i++) {
			for(int j = top + ydim; j >= top; j--) { 
				DBlock.place(world, i, j, zmax, dungeon.wallBlock1);
				DBlock.place(world, i, j, zmin, dungeon.wallBlock1);
			}
			if(dungeon.random.nextInt(8) == 0)
				for(int j = top + 2; j > top; j--) {
					DBlock.deleteBlock(world, i, j, zmax);
					DBlock.deleteBlock(world, i, j, zmin);
				}
			below = top - 1;
			while(!DBlock.isGroundBlock(world, i, below, zmax)) {
				DBlock.place(world, i, below, zmax, dungeon.wallBlock1);
				below--;
			}
			below = top - 1;
			while(!DBlock.isGroundBlock(world, i, below, zmin)) {
				DBlock.place(world, i, below, zmin, dungeon.floorBlock);
				below--;
			}
		}
		//DoomlikeDungeons.profiler.endTask("Generating X-Walls (TopRoom)");
		//DoomlikeDungeons.profiler.startTask("Generating Z-Walls (TopRoom)");
		for(int i = zmin; i <= zmax; i++) {
			for(int j = top + ydim; j >= top; j--) { 
				DBlock.place(world, xmin, j, i, dungeon.wallBlock1);
				DBlock.place(world, xmax, j, i, dungeon.wallBlock1);
			}
			if(dungeon.random.nextInt(8) == 0)
				for(int j = top + 2; j > top; j--) {
					DBlock.deleteBlock(world, xmax, j, i);
					DBlock.deleteBlock(world, xmin, j, i);
				}
			below = top - 1;
			while(!DBlock.isGroundBlock(world, xmax, below, i)) {
				DBlock.place(world, xmax, below, i, dungeon.wallBlock1);
				below--;
			}
			below = top - 1;
			while(!DBlock.isGroundBlock(world, xmin, below, i)) {
				DBlock.place(world, xmin, below, i, dungeon.floorBlock);
				below--;
			}
		}
		//DoomlikeDungeons.profiler.endTask("Generating Z-Walls (TopRoom)");
		//DoomlikeDungeons.profiler.startTask("Generating Ceilding (TopRoom)");
		for(int i = xmin; i <= xmax; i++) 
			for(int j = zmin; j <= zmax; j++) {
				DBlock.place(world, i, top + ydim + 1, j, dungeon.cielingBlock);
			}
		//DoomlikeDungeons.profiler.endTask("Generating Ceilding (TopRoom)");
		//DoomlikeDungeons.profiler.endTask("Generating Building (TopRoom)");
	}
	
	
	/**
	 * This will build a small ruin at the dungeon entrance.  This will also 
	 * fill in a any areas between the room and ground so that it doesn't float.
	 * 
	 * @param dungeon
	 * @param world
	 */
	private void buildRuin(Level dungeon, World world) {
		//DoomlikeDungeons.profiler.startTask("Generating Ruin (TopRoom)");
		//DoomlikeDungeons.profiler.startTask("Generating Floor (TopRoom)");
		for(int i = xmin + 1; i < xmax; i++) 
			for(int j = zmin + 1; j < zmax; j++) {
				DBlock.place(world, i, top, j, dungeon.floorBlock);
				below = top - 1;
				while(!DBlock.isGroundBlock(world, i, below, j)) {
					DBlock.place(world, i, below, j, dungeon.floorBlock);
					below--;
				}
			}
		//DoomlikeDungeons.profiler.endTask("Generating Floor (TopRoom)");
		//DoomlikeDungeons.profiler.startTask("Generating X-Walls (TopRoom)");
		for(int i = xmin; i <= xmax; i++) {
			for(int j = top + ydim - dungeon.random.nextInt(3); j >= top; j--) { 
				DBlock.place(world, i, j, zmax, dungeon.wallBlock1);
				DBlock.place(world, i, j, zmin, dungeon.wallBlock1);
			}
			if(dungeon.random.nextInt(8) == 0)
				for(int j = top + 2; j > top; j--) {
					DBlock.deleteBlock(world, i, j, zmax);
					DBlock.deleteBlock(world, i, j, zmin);
				}
			below = top - 1;
			while(!DBlock.isGroundBlock(world, i, below, zmax)) {
				DBlock.place(world, i, below, zmax, dungeon.wallBlock1);
				below--;
			}
			below = top - 1;
			while(!DBlock.isGroundBlock(world, i, below, zmin)) {
				DBlock.place(world, i, below, zmin, dungeon.floorBlock);
				below--;
			}
		}
		//DoomlikeDungeons.profiler.endTask("Generating X-Walls (TopRoom)");
		//DoomlikeDungeons.profiler.startTask("Generating Z-Walls (TopRoom)");
		for(int i = zmin; i <= zmax; i++) {
			for(int j = top + ydim - dungeon.random.nextInt(3); j >= top; j--) { 
				DBlock.place(world, xmin, j, i, dungeon.wallBlock1);
				DBlock.place(world, xmax, j, i, dungeon.wallBlock1);
			}
			if(dungeon.random.nextInt(8) == 0)
				for(int j = top + 2; j > top; j--) {
					DBlock.deleteBlock(world, xmax, j, i);
					DBlock.deleteBlock(world, xmin, j, i);
				}
			below = top - 1;
			while(!DBlock.isGroundBlock(world, xmax, below, i)) {
				DBlock.place(world, xmax, below, i, dungeon.wallBlock1);
				below--;
			}
			below = top - 1;
			while(!DBlock.isGroundBlock(world, xmin, below, i)) {
				DBlock.place(world, xmin, below, i, dungeon.floorBlock);
				below--;
			}
		}
		//DoomlikeDungeons.profiler.endTask("Generating Z-Walls (TopRoom)");
		//DoomlikeDungeons.profiler.startTask("Generating Ruin (TopRoom)");
	}
	

	
	
	//////////////////////////////////////////////////////////////////////////
	//                       Building Way In Below                          //
	//////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Will build a column holding a ladder; this is the same as what's build
	 * by SimplEntrance, except that the column and ladder will reach to the
	 * top room's ceiling height.
	 * 
	 * @param world
	 * @param dungeon
	 */
	private void buildLatter(World world, Level dungeon) {
		//DoomlikeDungeons.profiler.startTask("Generating Latter (TopRoom)");
		top += ydim;
		int side = dungeon.random.nextInt(4);
		switch (side) {
			case 0:
				for(int i = bottom; i <= top; i++) {
					DBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					DBlock.placeBlock(world, wx + 1, i, wz, ladder, 5, 3);
				}
				break;
			case 1:
				for(int i = bottom; i <= top; i++) {
					DBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					DBlock.placeBlock(world, wx, i, wz + 1, ladder, 3, 3);
				}
				break;
			case 2:
				for(int i = bottom; i <= top; i++) {
					DBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					DBlock.placeBlock(world, wx - 1, i, wz, ladder, 4, 3);
				}
				break;
			case 3:
				for(int i = bottom; i <= top; i++) {
					DBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					DBlock.placeBlock(world, wx, i, wz - 1, ladder, 2, 3);
				}
				break;
		}	
		//DoomlikeDungeons.profiler.endTask("Generating Latter (TopRoom)");
	}
	
	
	/**
	 * This will build a spiral stair into the dungeon, the same as would have
	 * been built by the SpiralStair class.
	 * 
	 * @param world
	 * @param dungeon
	 */
	private void buildStair(World world, Level dungeon) {
		//DoomlikeDungeons.profiler.startTask("Generating Stair (TopRoom)");
		top++;
		int side = dungeon.random.nextInt(4);
		for(int i = bottom; i < top; i++) {
			int sx, sz;
			DBlock.place(world, wx, i, wz, dungeon.wallBlock1);
			switch (side) {
			case 0:
				DBlock.placeBlock(world, wx+1, i, wz,   stairSlab, 0, 3);
				DBlock.placeBlock(world, wx+1, i, wz+1, stairSlab, 8, 3);
				// Empty space
				DBlock.deleteBlock(world, wx,    i, wz+1);
				DBlock.deleteBlock(world, wx-1,  i, wz+1);
				DBlock.deleteBlock(world, wx-1,  i,   wz);
				DBlock.deleteBlock(world, wx-1,  i, wz-1);
				DBlock.deleteBlock(world, wx,    i, wz-1);
				DBlock.deleteBlock(world, wx+1,  i, wz-1);
				break;
			case 1:
				DBlock.placeBlock(world, wx,   i, wz+1, stairSlab, 0, 3);
				DBlock.placeBlock(world, wx-1, i, wz+1, stairSlab, 8, 3);
				// Empty space
				DBlock.deleteBlock(world, wx+1, i,   wz);
				DBlock.deleteBlock(world, wx+1, i, wz+1);
				DBlock.deleteBlock(world, wx-1, i,   wz);
				DBlock.deleteBlock(world, wx-1, i, wz-1);
				DBlock.deleteBlock(world, wx,   i, wz-1);
				DBlock.deleteBlock(world, wx+1, i, wz-1);
				break;
			case 2:
				DBlock.placeBlock(world, wx-1, i, wz,   stairSlab, 0, 3);
				DBlock.placeBlock(world, wx-1, i, wz-1, stairSlab, 8, 3);
				// Empty space
				DBlock.deleteBlock(world, wx+1, i,   wz);
				DBlock.deleteBlock(world, wx+1, i, wz+1);
				DBlock.deleteBlock(world, wx,   i, wz+1);
				DBlock.deleteBlock(world, wx-1, i, wz+1);
				DBlock.deleteBlock(world, wx,   i, wz-1);
				DBlock.deleteBlock(world, wx+1, i, wz-1);
				break;
			case 3:
				DBlock.placeBlock(world, wx,   i, wz-1, stairSlab, 0, 3);
				DBlock.placeBlock(world, wx+1, i, wz-1, stairSlab, 8, 3);
				// Empty space
				DBlock.deleteBlock(world, wx+1, i,   wz);
				DBlock.deleteBlock(world, wx+1, i, wz+1);
				DBlock.deleteBlock(world, wx,   i, wz+1);
				DBlock.deleteBlock(world, wx-1, i, wz+1);
				DBlock.deleteBlock(world, wx-1, i,   wz);
				DBlock.deleteBlock(world, wx-1, i, wz-1);
				break;
			}
			side = (side + 1) % 4; 
		}
		//DoomlikeDungeons.profiler.endTask("Generating Stair (TopRoom)");
	}
	
}
