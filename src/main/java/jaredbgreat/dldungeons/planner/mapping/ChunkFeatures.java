package jaredbgreat.dldungeons.planner.mapping;


import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.pieces.entrances.AbstractEntrance;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.world.level.WorldGenLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to store information such as chest, spawner, and exit information, instead of 
 * (or in addition to) Room, so that dungeons may be build one chunk's worth at a time.
 * 
 * @author jared
 *
 */
public class ChunkFeatures {
	AbstractEntrance entrance;
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
	 */
	public void buildTileEntites(WorldGenLevel world, Dungeon dungeon, int shiftX, int shiftZ) {
		for(BasicChest  chest : chests) {
			RegisteredBlock.placeChest(world, shiftX + chest.mx, chest.my, shiftZ + chest.mz);
			chest.place(world, shiftX + chest.mx, chest.my, shiftZ + chest.mz, dungeon.random);
		}
		for(Spawner  spawner : spawners) {
				RegisteredBlock.placeSpawner(world,
									shiftX + spawner.getX(), 
									spawner.getY(), 
									shiftZ + spawner.getZ(), 
									spawner.getMob());
		}
	}
	
	
	/**
	 * This will added a physical entrance to all entrance nodes.
	 * 
	 */
	private void buildEntrance(Dungeon dungeon, WorldGenLevel world) {
		//System.out.println("Might build and entrance...");
		if(entrance != null) {
			entrance.build(dungeon, world);
			//System.out.println(" \t ...Build and entrance!");
		}
	}
	
	
	public void buildFeatures(Dungeon dungeon, int shiftX, int shiftZ, WorldGenLevel world) {
		buildTileEntites(world, dungeon, shiftX, shiftZ);
		buildEntrance(dungeon, world);
	}


}
