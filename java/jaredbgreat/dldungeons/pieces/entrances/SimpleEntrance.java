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


import jaredbgreat.dldungeons.builder.Builder;
import jaredbgreat.dldungeons.builder.DBlock;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class SimpleEntrance extends AbstractEntrance {

	public SimpleEntrance(int x, int y, int z) {
		super(x, y, z);
	}

	@Override
	public void build(Dungeon dungeon, World world) {
		int wx = x + (dungeon.map.chunkX * 16) - (dungeon.map.room.length / 2) + 8;
		int wz = z + (dungeon.map.chunkZ * 16) - (dungeon.map.room.length / 2) + 8;
		int bottom = dungeon.map.floorY[x][z];
		int top = world.getHeightValue(wx, wz);
		while(!DBlock.isGroundBlock(world, wx, top, wz)) top--;
		top++;
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
		
	}

}
