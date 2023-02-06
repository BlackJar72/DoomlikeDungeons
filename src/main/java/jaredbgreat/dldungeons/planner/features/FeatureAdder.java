package jaredbgreat.dldungeons.planner.features;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.Degree;


/**
 * A base class for all room features.  This allows a chance for
 * a type of feature occurring to be store in a collection and sorted
 * to randomize the order of evaluation. 
 * 
 * @author Jared Blackburn
 *
 */
public abstract class FeatureAdder {
	
	/**
	 * The chance of placing the feature on a given use.
	 */
	Degree chance;

	public FeatureAdder(Degree chance) {
		this.chance = chance;
	}
	
	
	/**
	 * This will try to add the feature to the room, based on the features
	 * pre-defined degree of chance.  The actual feature is built by calling 
	 * a method back on the room.  It will return true if the room was 
	 * instructed to add the feature, and false otherwise; whether or not the 
	 * feature was actually added successfully is another matter.
	 * 
	 * @param dungeon
	 * @param room
	 * @return the result of chance.use()
	 */
	public boolean addFeature(Dungeon dungeon, Room room) {
		boolean built = chance.use(dungeon.random);
		if(built) buildFeature(dungeon, room);
		return built;	
	}
	
	
	/**
	 * This will actually build the feature into the room.
	 * 
	 * @param dungeon
	 * @param room
	 */
	public abstract void buildFeature(Dungeon dungeon, Room room);

}
