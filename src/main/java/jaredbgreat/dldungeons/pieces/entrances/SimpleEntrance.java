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

public class SimpleEntrance extends AbstractEntrance {

	public SimpleEntrance(int x, int z) {
		super(x, z);
	}

	@Override
	public void build(Dungeon dungeon, WorldGenLevel world, int shiftX, int shiftZ) {
		int wx = x + shiftX;
		int wz = z + shiftZ;
		int bottom = dungeon.map.floorY[x][z];
		int top = world.getMaxBuildHeight();
		while(!RegisteredBlock.isGroundBlock(world, wx, top, wz)) top--;
		top++;
		int side = dungeon.random.nextInt(4);
		BlockState ladder;
		switch (side) {
			case 0:
				ladder = LADDER.defaultBlockState()
						.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST);
				for(int i = bottom; i <= top; i++) {
					RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					RegisteredBlock.placeBlock(world, wx + 1, i, wz, ladder, 5, 3);
				}
				break;
			case 1:
				ladder = LADDER.defaultBlockState()
						.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
				for(int i = bottom; i <= top; i++) {
					RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					RegisteredBlock.placeBlock(world, wx, i, wz + 1, ladder, 3, 3);
				}
				break;
			case 2:
				ladder = LADDER.defaultBlockState()
						.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
				for(int i = bottom; i <= top; i++) {
					RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					RegisteredBlock.placeBlock(world, wx - 1, i, wz, ladder, 4, 3);
				}
				break;
			case 3:
			default:
				ladder = LADDER.defaultBlockState()
						.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
				for(int i = bottom; i <= top; i++) {
					RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					RegisteredBlock.placeBlock(world, wx, i, wz - 1, ladder, 2, 3);
				}
				break;
		}
		
	}

}
