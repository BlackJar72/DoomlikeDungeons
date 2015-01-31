package jaredbgreat.dldungeons.themes;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
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
