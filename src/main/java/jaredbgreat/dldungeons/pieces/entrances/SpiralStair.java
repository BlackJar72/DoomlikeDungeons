package jaredbgreat.dldungeons.pieces.entrances;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */

import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

public class SpiralStair extends AbstractEntrance {

	
	public SpiralStair(int x, int z) {
		super(x, z);
	}

	
	@Override
	public void build(Dungeon dungeon, WorldGenLevel world, int shiftX, int shiftZ) {
		int wx = x + shiftX;
		int wz = z + shiftZ;
		int bottom = dungeon.map.floorY[x][z];
		int top = world.getMaxBuildHeight();
		//int top = world.getChunkFromChunkCoords(wx / 16, wz / 16).getHeight(wx, wz);
		while(!RegisteredBlock.isGroundBlock(world, wx, top, wz)) top--;
		top++;
		int side = dungeon.random.nextInt(4);
		BlockState uslab = STAIR_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP);
		BlockState lslab = STAIR_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM);
		for(int i = bottom; i < top; i++) {
			int sx, sz;
			RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
			switch (side) {
			case 0:
				RegisteredBlock.placeBlock(world, wx+1, i, wz,   lslab, 0, 3);
				RegisteredBlock.placeBlock(world, wx+1, i, wz+1, uslab, 8, 3);
				// Empty space
				RegisteredBlock.deleteBlock(world, wx,    i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1,  i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1,  i,   wz);
				RegisteredBlock.deleteBlock(world, wx-1,  i, wz-1);
				RegisteredBlock.deleteBlock(world, wx,    i, wz-1);
				RegisteredBlock.deleteBlock(world, wx+1,  i, wz-1);
				break;
			case 1:
				RegisteredBlock.placeBlock(world, wx,   i, wz+1, lslab, 0, 3);
				RegisteredBlock.placeBlock(world, wx-1, i, wz+1, uslab, 8, 3);
				// Empty space
				RegisteredBlock.deleteBlock(world, wx+1, i,   wz);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1, i,   wz);
				RegisteredBlock.deleteBlock(world, wx-1, i, wz-1);
				RegisteredBlock.deleteBlock(world, wx,   i, wz-1);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz-1);
				break;
			case 2:
				RegisteredBlock.placeBlock(world, wx-1, i, wz,   lslab, 0, 3);
				RegisteredBlock.placeBlock(world, wx-1, i, wz-1, uslab, 8, 3);
				// Empty space
				RegisteredBlock.deleteBlock(world, wx+1, i,   wz);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz+1);
				RegisteredBlock.deleteBlock(world, wx,   i, wz+1);
				RegisteredBlock.deleteBlock(world, wx-1, i, wz+1);
				RegisteredBlock.deleteBlock(world, wx,   i, wz-1);
				RegisteredBlock.deleteBlock(world, wx+1, i, wz-1);
				break;
			case 3:
				RegisteredBlock.placeBlock(world, wx,   i, wz-1, lslab, 0, 3);
				RegisteredBlock.placeBlock(world, wx+1, i, wz-1, uslab, 8, 3);
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
