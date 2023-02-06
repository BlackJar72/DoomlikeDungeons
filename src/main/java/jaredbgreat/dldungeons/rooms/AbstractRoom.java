package jaredbgreat.dldungeons.rooms;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.planner.Dungeon;



/**
 * The base type for all rooms, this determines some general 
 * room characteristics and provides factory methods for creating 
 * rooms the types of which are randomly chosen based on dungeon 
 * wide theme-derived variables. 
 */
public abstract class AbstractRoom /*extends Shape*/ {


	public int airBlock;
	public int wallBlock1;
	public int floorBlock;
	public int cielingBlock;
	public int fenceBlock;
	public int pillarBlock;
	public int liquidBlock;
	public int caveBlock;
	
	public boolean degenerate;			// Leave air blocks in walls / ceiling/
	public boolean degenerateFloors;	// Even leave air blocks in floor?!?
	public boolean sky;					// Does the room have a ceiling? / Is it outdoorsy?
	public boolean fenced;
	
	
	/**
	 * The constructor for the null room (room 0, representing areas 
	 * that are not part of the dungeon).  It should never be used to 
	 * generate a "normal" room of any type.  It is only protected 
	 * (rather than private) because the null room needs to be instantiated 
	 * to as a place holder in the room lists.
	 */
	protected AbstractRoom() {
		wallBlock1   = 0;
		floorBlock   = 0;
		cielingBlock = 0;
		fenceBlock   = 0;
		pillarBlock  = 0;
		liquidBlock  = 0;
	}
	
	
	/**
	 * The super constructor for creating real rooms.  It is still protected 
	 * as no public constructor should exist for rooms as the are provided 
	 * by the makeRoom methods.
	 * 
	 * @param dungeon
	 * @param previous
	 */
	protected AbstractRoom(Dungeon dungeon, AbstractRoom previous) {
		if((previous != null) && !dungeon.variability.use(dungeon.random)) {
			airBlock     = previous.airBlock;
			wallBlock1   = previous.wallBlock1;
			floorBlock   = previous.floorBlock;
			cielingBlock = previous.cielingBlock;
			fenceBlock   = previous.fenceBlock;
			pillarBlock  = previous.pillarBlock;
			liquidBlock  = previous.liquidBlock;
			caveBlock    = previous.caveBlock;
		} else if(dungeon.variability.use(dungeon.random)) {
			if(dungeon.variability.use(dungeon.random)) {
				airBlock   = dungeon.theme.air[dungeon.random.nextInt(dungeon.theme.air.length)];
				wallBlock1 = dungeon.theme.walls[dungeon.random.nextInt(dungeon.theme.walls.length)];
				floorBlock = dungeon.theme.floors[dungeon.random.nextInt(dungeon.theme.floors.length)];
				cielingBlock = dungeon.theme.ceilings[dungeon.random.nextInt(dungeon.theme.ceilings.length)];
				fenceBlock = dungeon.theme.fencing[dungeon.random.nextInt(dungeon.theme.fencing.length)];
				pillarBlock = dungeon.theme.pillarBlock[dungeon.random.nextInt(dungeon.theme.pillarBlock.length)];
				liquidBlock = dungeon.theme.liquid[dungeon.random.nextInt(dungeon.theme.liquid.length)];
				caveBlock   = dungeon.theme.caveWalls[dungeon.random.nextInt(dungeon.theme.caveWalls.length)];
			} else {
				if(!dungeon.variability.use(dungeon.random)) 
					airBlock = dungeon.airBlock;
				else airBlock = dungeon.theme.air[dungeon.random.nextInt(dungeon.theme.air.length)];
				if(!dungeon.variability.use(dungeon.random)) 
					wallBlock1 = dungeon.wallBlock1;
				else wallBlock1 = dungeon.theme.walls[dungeon.random.nextInt(dungeon.theme.walls.length)];
				if(!dungeon.variability.use(dungeon.random)) 
					floorBlock = dungeon.floorBlock;
				else floorBlock = dungeon.theme.floors[dungeon.random.nextInt(dungeon.theme.floors.length)];
				if(!dungeon.variability.use(dungeon.random)) 
					cielingBlock = dungeon.cielingBlock;
				else cielingBlock = dungeon.theme.ceilings[dungeon.random.nextInt(dungeon.theme.ceilings.length)];
				if(!dungeon.variability.use(dungeon.random)) 
					fenceBlock = dungeon.fenceBlock;
				else fenceBlock = dungeon.theme.fencing[dungeon.random.nextInt(dungeon.theme.fencing.length)];
				if(!dungeon.variability.use(dungeon.random)) 
					pillarBlock = dungeon.cornerBlock;
				else pillarBlock = dungeon.theme.pillarBlock[dungeon.random.nextInt(dungeon.theme.pillarBlock.length)];
				if(!dungeon.variability.use(dungeon.random)) 
					liquidBlock = dungeon.liquidBlock;
				else liquidBlock = dungeon.theme.liquid[dungeon.random.nextInt(dungeon.theme.liquid.length)];
				if(!dungeon.variability.use(dungeon.random)) 
					caveBlock = dungeon.caveBlock;
				else caveBlock = dungeon.theme.caveWalls[dungeon.random.nextInt(dungeon.theme.caveWalls.length)];
			}
		} else {
			airBlock     = dungeon.airBlock;
			wallBlock1   = dungeon.wallBlock1;
			floorBlock   = dungeon.floorBlock;
			cielingBlock = dungeon.cielingBlock;
			fenceBlock   = dungeon.fenceBlock;
			pillarBlock  = dungeon.cornerBlock;
			liquidBlock  = dungeon.liquidBlock;
			caveBlock    = dungeon.caveBlock;
		}
		if(dungeon.outside.value > 0)
			sky = dungeon.outside.use(dungeon.random);
		if(dungeon.degeneracy.value > 0) {
			degenerate = dungeon.degeneracy.use(dungeon.random);
			degenerateFloors = 
					(degenerate && dungeon.degeneracy.use(dungeon.random) && dungeon.random.nextBoolean());
		}
		if(sky) fenced = dungeon.fences.use(dungeon.random);
		else fenced = false;
	}
	
	
	/**
	 * A factory method for creating rooms of a random type.  The 
	 * actual type will be based on dungeon-wide theme-derived 
	 * variables.
	 * 
	 * @param beginX
	 * @param endX
	 * @param beginZ
	 * @param endZ
	 * @param floorY
	 * @param ceilY
	 * @param dungeon
	 * @param parent
	 * @param previous
	 * @return
	 */
	public static Room makeRoom(int beginX, int endX, int beginZ, int endZ, int floorY, int ceilY, 
			Dungeon dungeon, Room parent, Room previous) {		
		RoomType type = RoomType.ROOM;
		if(dungeon.naturals.use(dungeon.random)) {
			type = RoomType.CAVE;
		}
		switch(type) {
		case CAVE: {
			Cave base = new Cave(beginX, endX, beginZ, endZ, floorY, ceilY, 
					dungeon, parent, previous);
			return base.plan(dungeon, parent);
		}
		case ROOM:
		default: {
			Room base = new Room(beginX, endX, beginZ, endZ, floorY, ceilY, 
				dungeon, parent, previous);
			return base.plan(dungeon, parent);
		}
		}
	}
	
	
	/**
	 * A factory method for creating rooms of a specified type.  Specifying 
	 * and unimplemented type will result in a default room of type ROOM / 
	 * class Room -- that is, a basic room built from shape primitives.
	 * 
	 * This is not currently used, but is kept for possible expansion.
	 * 
	 * @param beginX
	 * @param endX
	 * @param beginZ
	 * @param endZ
	 * @param floorY
	 * @param ceilY
	 * @param dungeon
	 * @param parent
	 * @param previous
	 * @param type
	 * @return
	 */
	public static Room makeRoom(int beginX, int endX, int beginZ, int endZ, int floorY, int ceilY, 
			Dungeon dungeon, Room parent, Room previous, RoomType type) {	
		switch(type) {
			case CAVE: {
				Cave base = new Cave(beginX, endX, beginZ, endZ, floorY, ceilY, 
					dungeon, parent, previous);
				return base.plan(dungeon, parent);
			}
			case ROOM:
			default: {
				Room base = new Room(beginX, endX, beginZ, endZ, floorY, ceilY, 
						dungeon, parent, previous);
				return base.plan(dungeon, parent);
			}
		}
	}
}