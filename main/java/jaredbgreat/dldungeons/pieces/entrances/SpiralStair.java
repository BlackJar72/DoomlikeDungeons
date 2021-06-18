package jaredbgreat.dldungeons.pieces.entrances;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.block.SlabBlock;
import net.minecraft.state.properties.SlabType;
import net.minecraft.world.ISeedReader;


public class SpiralStair extends AbstractEntrance {

	
	public SpiralStair(int x, int z) {
		super(x, z);
	}

	
	@Override
	public void build(Dungeon dungeon, ISeedReader world) {
		int wx = x + (dungeon.map.chunkX * 16) - (dungeon.map.room.length / 2) + 8;
		int wz = z + (dungeon.map.chunkZ * 16) - (dungeon.map.room.length / 2) + 8;
		int bottom = dungeon.map.floorY[x][z];
		int top = world.getHeight();
		//int top = world.getChunkFromChunkCoords(wx / 16, wz / 16).getHeight(wx, wz);
		while(!RegisteredBlock.isGroundBlock(world, wx, top, wz)) top--;
		top++;
		int side = dungeon.random.nextInt(4);
		for(int i = bottom; i < top; i++) {
			int sx, sz;
			RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
			switch (side) {
			case 0:
				RegisteredBlock.placeBlock(world, wx+1, i, wz,   STAIR_SLAB1);
				RegisteredBlock.placeBlock(world, wx+1, i, wz+1, STAIR_SLAB2);
				// Empty space
				RegisteredBlock.deleteBlock(world, wx,    i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1,  i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1,  i,   wz);
				RegisteredBlock.deleteBlock(world, wx-1,  i, wz-1);
				RegisteredBlock.deleteBlock(world, wx,    i, wz-1);
				RegisteredBlock.deleteBlock(world, wx+1,  i, wz-1);
				break;
			case 1:
				RegisteredBlock.placeBlock(world, wx,   i, wz+1, STAIR_SLAB1);
				RegisteredBlock.placeBlock(world, wx-1, i, wz+1, STAIR_SLAB2);
				// Empty space
				RegisteredBlock.deleteBlock(world, wx+1, i,   wz);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1, i,   wz);
				RegisteredBlock.deleteBlock(world, wx-1, i, wz-1);
				RegisteredBlock.deleteBlock(world, wx,   i, wz-1);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz-1);
				break;
			case 2:
				RegisteredBlock.placeBlock(world, wx-1, i, wz,   STAIR_SLAB1);
				RegisteredBlock.placeBlock(world, wx-1, i, wz-1, STAIR_SLAB2);
				// Empty space
				RegisteredBlock.deleteBlock(world, wx+1, i,   wz);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz+1);
				RegisteredBlock.deleteBlock(world, wx,   i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1, i, wz+1);
				RegisteredBlock.deleteBlock(world, wx,   i, wz-1);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz-1);
				break;
			case 3:
				RegisteredBlock.placeBlock(world, wx,   i, wz-1, STAIR_SLAB1);
				RegisteredBlock.placeBlock(world, wx+1, i, wz-1, STAIR_SLAB2);
				// Empty space
				RegisteredBlock.deleteBlock(world, wx+1, i,   wz);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz+1);
				RegisteredBlock.deleteBlock(world, wx,   i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1, i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1, i,   wz);
				RegisteredBlock.deleteBlock(world, wx-1, i, wz-1);
				break;
			}
			side = (side + 1) % 4; 
		}		
	}

}
