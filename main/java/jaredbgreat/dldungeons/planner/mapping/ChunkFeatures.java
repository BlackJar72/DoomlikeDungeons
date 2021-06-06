package jaredbgreat.dldungeons.planner.mapping;

import java.util.ArrayList;
import java.util.List;

import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraftforge.common.MinecraftForge;

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
	
	
	public void addSpawner(Spawner spawner) {
		spawners.add(spawner);
	}
	
	
	public void addChest(BasicChest chest) {
		chests.add(chest);
	}
	
	
	/**
	 * This add all the chest and spawners to the room. 
	 * 
	 * @param room
	 */
	public void addTileEntites(Dungeon dungeon, MapMatrix map, int shiftX, int shiftZ) {
		for(Spawner  spawner : spawners) {
				RegisteredBlock.placeSpawner(map.world, 
									shiftX + spawner.getX(), 
									spawner.getY(), 
									shiftZ + spawner.getZ(), 
									spawner.getMob());
		}
		for(BasicChest  chest : chests) {
			RegisteredBlock.placeChest(map.world, shiftX + chest.mx, chest.my, shiftZ + chest.mz);
			chest.place(map.world, shiftX + chest.mx, chest.my, shiftZ + chest.mz, dungeon.random);
		}
	}


}
