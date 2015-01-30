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


import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class AbstractEntrance {
	
	protected static final Block ladder 
			= (Block)Block.getBlockFromName("ladder");
	protected static final Block stairSlab 
			= (Block)Block.getBlockFromName("stone_slab");	
	
	int x, y, z;
	
	public AbstractEntrance(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public abstract void build(Dungeon dungeon, World world);
}
