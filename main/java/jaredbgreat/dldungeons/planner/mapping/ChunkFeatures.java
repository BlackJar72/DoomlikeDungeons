package jaredbgreat.dldungeons.planner.mapping;

import java.util.ArrayList;
import java.util.List;

import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;

/**
 * A class to store information such as chest, spawner, and exit information, instead of 
 * (or in addition to) Room, so that dungeons may be build one chunk's worth at a time.
 * 
 * @author jared
 *
 */
public class ChunkFeatures {
	boolean     hasEntrance;		
	final List<Spawner> spawners;
	final List<BasicChest> chests;
	
	
	public ChunkFeatures() {
		spawners = new ArrayList<>();
		chests   = new ArrayList<>();
	}


}
