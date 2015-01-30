package jaredbgreat.dldungeons.themes;


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


import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;

public class BlockList extends ArrayList<Block> {

	public BlockList() {
		super();		
	}
	
	
	public BlockList(Block[] blocks) {
		super();
		add(blocks);
	}
	
	
	public Block choose(Random random) {
		return this.get(random.nextInt(this.size()));
	}
	
	
	public void add(Block[] blocks) {
		for(int i = 0; i < blocks.length; i++) {
			add(blocks[i]);
		}
	}
}
