package jaredbgreat.dldungeons.pieces.entrances;


/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
