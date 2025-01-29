package jaredbgreat.dldungeons.planner.mapping;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.Chest;
import jaredbgreat.dldungeons.pieces.entrances.AbstractEntrance;
import jaredbgreat.dldungeons.pieces.entrances.Entrance;
import jaredbgreat.dldungeons.pieces.entrances.EntranceType;
import jaredbgreat.dldungeons.pieces.entrances.SimpleEntrance;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.rooms.RoomList;
import jaredbgreat.dldungeons.themes.Sizes;
import jaredbgreat.dldungeons.util.cache.Coords;
import net.minecraft.world.level.WorldGenLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A class to store information such as chest, spawner, and exit information, instead of 
 * (or in addition to) Room, so that dungeons may be build one chunk's worth at a time.
 * 
 * @author jared
 *
 */
public class ChunkFeatures {

	private static final Entrance NO_ENTRANCE = new Entrance(0, 0, EntranceType.NONE);

	Entrance entrance;
	final List<Spawner> spawners;
	final List<Chest> chests;

	//public static final Codec<ChunkFeatures> CODEC = Spawner.CODEC.listOf().xmap(ChunkFeatures::new,
	//		spawners -> spawners.subList(1, spawners.size())
	//);

	/*
	public static final Codec<ChunkFeatures> CODEC = RecordCodecBuilder.create(builder -> builder
			.group(
					Spawner.CODEC.listOf().xmap(ArrayList::new, spawners -> spawners.subList(0, spawners.size()),
					Spawner.CODEC.listOf().xmap(ArrayList::new,chests -> chests.subList(0, chests.size())
			)
			.apply(builder, (spawners, chests) -> {

			}));
*/

	public static final Codec<ChunkFeatures> CODEC = RecordCodecBuilder.create(builder -> builder
			.group(
					Spawner.CODEC.listOf().fieldOf("spawners").forGetter(chunkFeatures -> chunkFeatures.spawners),
					Chest.CODEC.listOf().fieldOf("chests").forGetter(chunkFeatures -> chunkFeatures.chests),
					Entrance.CODEC.optionalFieldOf("entrance").forGetter(chunkFeatures
							-> Optional.ofNullable(chunkFeatures.entrance))
			)
			.apply(builder, (spawners, chests, entrance) -> {
				ChunkFeatures features = new ChunkFeatures(spawners, chests);
				features.entrance = entrance.orElse(null);;
				return features;
			}));


	private ChunkFeatures(List<Spawner> spawners, List<Chest> chests) {
			this.spawners = spawners;
			this.chests = chests;
			this.entrance = NO_ENTRANCE;
	}



	public ChunkFeatures() {
		spawners = new ArrayList<>();
		chests   = new ArrayList<>();
		entrance = NO_ENTRANCE;
	}
	
	
	public void addSpawner(Spawner spawner) {
		spawners.add(spawner);
	}
	
	
	public void addChest(Chest chest) {
		chests.add(chest);
	}
	
	
	public void addEntrance(Entrance entrance) {
		this.entrance = entrance;
	}
	
	
	/**
	 * This add all the chest and spawners to the room. 
	 * 
	 */
	public void buildTileEntites(WorldGenLevel world, Dungeon dungeon, int shiftX, int shiftZ) {
		for(Chest  chest : chests) {
			RegisteredBlock.placeChest(world, shiftX + chest.getMX(), chest.getMY(), shiftZ + chest.getMZ());
			chest.place(world, shiftX + chest.getMX(), chest.getMY(), shiftZ + chest.getMZ(), dungeon.random, dungeon.lootCat);
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
	private void buildEntrance(Dungeon dungeon, WorldGenLevel world, int shiftX, int shiftZ) {
		//System.out.println("Might build and entrance...");
		if((entrance != null) && (entrance.getType() != EntranceType.NONE)) {
			AbstractEntrance wayIn = entrance.getType().factory.makeEntrance(entrance.getX(), entrance.getZ());
			wayIn.avoidChunkEdges(shiftX, shiftZ);
			wayIn.build(dungeon, world, shiftX, shiftZ);
		}
		//System.out.println(" \t ...Build and entrance!");
	}
	
	
	public void buildFeatures(Dungeon dungeon, int shiftX, int shiftZ, WorldGenLevel world) {
		buildTileEntites(world, dungeon, shiftX, shiftZ);
		buildEntrance(dungeon, world, shiftX, shiftZ);
	}


}
