package jaredbgreat.dldungeons.planner.mapping;

import java.util.ArrayList;
import java.util.List;

import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.pieces.entrances.AbstractEntrance;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.world.World;

/**
 * A class to store information such as chest, spawner, and exit information, instead of 
 * (or in addition to) Room, so that dungeons may be build one chunk's worth at a time.
 * 
 * @author jared
 *
 */
public class ChunkFeatures {
	volatile AbstractEntrance entrance;		
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
	
	
	public void addEntrance(AbstractEntrance entrance) {
		this.entrance = entrance;
	}
	
	
	/**
	 * This add all the chest and spawners to the room. 
	 * 
	 * @param room
	 */
	public void buildTileEntites(Dungeon dungeon, MapMatrix map, int shiftX, int shiftZ) {
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
	
	
	/**
	 * This will added a physical entrance to all entrance nodes.
	 * 
	 * @param room
	 */
	private void buildEntrance(Dungeon dungeon, World world) {
		//System.out.println("Might build and entrance...");
		if(entrance != null) {
			entrance.build(dungeon, world);
			//System.out.println(" \t ...Build and entrance!");
		}
	}
	
	
	public void buildFeatures(Dungeon dungeon, MapMatrix map, int shiftX, int shiftZ, World world) {
		buildTileEntites(dungeon, map, shiftX, shiftZ);
		buildEntrance(dungeon, world);
	}


}
