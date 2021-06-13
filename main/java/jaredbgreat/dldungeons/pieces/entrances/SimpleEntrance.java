package jaredbgreat.dldungeons.pieces.entrances;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.world.ISeedReader;

public class SimpleEntrance extends AbstractEntrance {

	public SimpleEntrance(int x, int z) {
		super(x, z);
	}

	@Override
	public void build(Dungeon dungeon, ISeedReader world) {
		int wx = x + (dungeon.map.chunkX * 16) - (dungeon.map.room.length / 2) + 8;
		int wz = z + (dungeon.map.chunkZ * 16) - (dungeon.map.room.length / 2) + 8;
		int bottom = dungeon.map.floorY[x][z];
		int top = world.getHeight();
		while(!RegisteredBlock.isGroundBlock(world, wx, top, wz)) top--;
		top++;
		int side = dungeon.random.nextInt(4);
		switch (side) {
			case 0:
				for(int i = bottom; i <= top; i++) {
					RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					RegisteredBlock.placeBlock(world, wx + 1, i, wz, LADDER, 5, 3);
				}
				break;
			case 1:
				for(int i = bottom; i <= top; i++) {
					RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					RegisteredBlock.placeBlock(world, wx, i, wz + 1, LADDER, 3, 3);
				}
				break;
			case 2:
				for(int i = bottom; i <= top; i++) {
					RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					RegisteredBlock.placeBlock(world, wx - 1, i, wz, LADDER, 4, 3);
				}
				break;
			case 3:
				for(int i = bottom; i <= top; i++) {
					RegisteredBlock.place(world, wx, i, wz, dungeon.wallBlock1);
					RegisteredBlock.placeBlock(world, wx, i, wz - 1, LADDER, 2, 3);
				}
				break;
		}
		
	}

}
