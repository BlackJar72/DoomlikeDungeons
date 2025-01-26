package jaredbgreat.dldungeons.pieces;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import jaredbgreat.dldungeons.rooms.Room;

import java.util.List;

/**
 * Represents a spawner that should be placed, including its location
 * in xyz coordinates and the name of the mob to be spawned.
 * 
 * @author Jared Blackburn
 *
 */
public final class Spawner {

	public static final Codec<Spawner> CODEC = RecordCodecBuilder.create(builder -> builder
			.group(Codec.INT.listOf().fieldOf("data").forGetter(spawner -> List.of(
							spawner.x,
							spawner.y,
							spawner.z,
							spawner.room,
							spawner.level)),
					Codec.STRING.fieldOf("mob").forGetter(spawner -> spawner.mob))
			.apply(builder, (data, mob) -> {
				final Spawner spawner = new Spawner();

				spawner.x = data.get(0);
				spawner.y = data.get(1);
				spawner.z = data.get(2);
				spawner.room = data.get(3);
				spawner.level = data.get(4);
				spawner.mob = mob;
				return spawner;
			}));
	
	private int x, y, z, room, level;
	private String mob;

	private Spawner() {}
	
	public Spawner(int x, int y, int z, int room, int level, String mob) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.room = room;
		this.level = level;
		this.mob = mob;
	}


	/**
	 * Test to see if the selected location is occupied by a spawner.
	 * Used to make sure chests are not assigned the same locations.
	 *
	 * @param ox other X
	 * @param oy other Y
	 * @param oz other Z
	 * @return
	 */
	public boolean isLocation(int ox, int oy, int oz) {
		return ((ox == x) && (oy == y) && (oz == z));
	}
	
	
	public int getX() {
		return x;
	}
	
	
	public int getY() {
		return y;
	}
	
	
	public int getZ() {
		return z;
	}
	
	
	public int getRoom() {
		return room;
	}
	
	
	public int getLevel() {
		return level;
	}
	
	
	public String getMob() {
		return mob;
	}
}
