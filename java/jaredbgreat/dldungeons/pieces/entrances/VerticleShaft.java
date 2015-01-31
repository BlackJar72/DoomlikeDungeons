package jaredbgreat.dldungeons.pieces.entrances;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.world.World;


public class VerticleShaft extends AbstractEntrance {

	
	public VerticleShaft(int x, int y, int z) {
		super(x, y, z);
	}
	

	@Override
	public void build(Dungeon dungeon, World world) {
		int wx = x + (dungeon.map.chunkX * 16) - (dungeon.map.room.length / 2) + 8;
		int wz = z + (dungeon.map.chunkZ * 16) - (dungeon.map.room.length / 2) + 8;
		int bottom[] = new int[4];
		int top[] = new int[4];
		int side = dungeon.random.nextInt(4);
		bottom[0] = dungeon.map.floorY[x][z];
		top[0]    = world.getHeightValue(wx, wz);
		switch (side) {
			case 0:
				bottom[1] = dungeon.map.floorY[x+1][z];
				top[1]    = world.getHeightValue(wx+1, wz);
				bottom[2] = dungeon.map.floorY[x+1][z+1];
				top[2]    = world.getHeightValue(wx+1, wz+1);
				bottom[3] = dungeon.map.floorY[x][z+1];
				top[4]    = world.getHeightValue(wx, wz+1);
				break;
			case 1:
				bottom[1] = dungeon.map.floorY[x-1][z];
				top[1]    = world.getHeightValue(wx-1, wz);
				bottom[2] = dungeon.map.floorY[x-1][z+1];
				top[2]    = world.getHeightValue(wx-1, wz+1);
				bottom[3] = dungeon.map.floorY[x][z+1];
				top[4]    = world.getHeightValue(wx, wz+1);
				break;
			case 2:
				bottom[1] = dungeon.map.floorY[x+1][z];
				top[1]    = world.getHeightValue(wx+1, wz);
				bottom[2] = dungeon.map.floorY[x+1][z-1];
				top[2]    = world.getHeightValue(wx+1, wz-1);
				bottom[3] = dungeon.map.floorY[x][z-1];
				top[4]    = world.getHeightValue(wx, wz-1);
				break;
			case 3:
				bottom[1] = dungeon.map.floorY[x-1][z];
				top[1]    = world.getHeightValue(wx-1, wz);
				bottom[2] = dungeon.map.floorY[x-1][z-1];
				top[2]    = world.getHeightValue(wx-1, wz-1);
				bottom[3] = dungeon.map.floorY[x][z-1];
				top[4]    = world.getHeightValue(wx, wz-1);
				break;
		}
		int i;
		for(i = bottom[0]; i < top[0]; i++) DBlock.deleteBlock(world, wx, i, wz);
		for(i = bottom[1]; i < top[1]; i++) DBlock.deleteBlock(world, wx, i, wz);
		for(i = bottom[2]; i < top[2]; i++) DBlock.deleteBlock(world, wx, i, wz);
		for(i = bottom[3]; i < top[3]; i++) DBlock.deleteBlock(world, wx, i, wz);
	}	
}
