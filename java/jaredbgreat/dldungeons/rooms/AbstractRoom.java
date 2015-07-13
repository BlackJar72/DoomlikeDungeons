package jaredbgreat.dldungeons.rooms;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	

import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.naturals.DldCave;

import java.util.Random;

import net.minecraft.block.Block;

public abstract class AbstractRoom /*extends Shape*/ {


	public int wallBlock1;
	public int floorBlock;
	public int cielingBlock;
	public int fenceBlock;
	public int pillarBlock;
	public int liquidBlock;
	
	public boolean degenerate;			// Leave air blocks in walls / ceiling/
	public boolean degenerateFloors;	// Even leave air blocks in floor?!?
	public boolean sky;					// Does the room have a ceiling? / Is it outdoorsy?
	public boolean fenced;
	
	
	protected AbstractRoom() {
		wallBlock1   = 0;
		floorBlock   = 0;
		cielingBlock = 0;
		fenceBlock   = 0;
		pillarBlock  = 0;
		liquidBlock  = 0;
	}
	
	
	public AbstractRoom(Dungeon dungeon, AbstractRoom previous) {
		if((previous != null) && !dungeon.variability.use(dungeon.random)) {
			wallBlock1   = previous.wallBlock1;
			floorBlock   = previous.floorBlock;
			cielingBlock = previous.cielingBlock;
			fenceBlock   = previous.fenceBlock;
			pillarBlock  = previous.pillarBlock;
			liquidBlock  = previous.liquidBlock;
		} else if(dungeon.variability.use(dungeon.random)) {
			if(dungeon.variability.use(dungeon.random)) {
				wallBlock1 = dungeon.theme.walls[dungeon.random.nextInt(dungeon.theme.walls.length)];
				floorBlock = dungeon.theme.floors[dungeon.random.nextInt(dungeon.theme.floors.length)];
				cielingBlock = dungeon.theme.ceilings[dungeon.random.nextInt(dungeon.theme.ceilings.length)];
				fenceBlock = dungeon.theme.fencing[dungeon.random.nextInt(dungeon.theme.fencing.length)];
				pillarBlock = dungeon.theme.pillarBlock[dungeon.random.nextInt(dungeon.theme.pillarBlock.length)];
				liquidBlock = dungeon.theme.liquid[dungeon.random.nextInt(dungeon.theme.liquid.length)];
			} else {
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
			}
		} else {
			wallBlock1   = dungeon.wallBlock1;
			floorBlock   = dungeon.floorBlock;
			cielingBlock = dungeon.cielingBlock;
			fenceBlock   = dungeon.fenceBlock;
			pillarBlock  = dungeon.cornerBlock;
			liquidBlock  = dungeon.liquidBlock;
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
	
	
	public static Room makeRoom(int beginX, int endX, int beginZ, int endZ, int floorY, int ceilY, 
			Dungeon dungeon, Room parent, Room previous) {		
		RoomType type = RoomType.ROOM;
		if(dungeon.naturals.use(dungeon.random)) {
			type = RoomType.CAVE;
		}
		switch(type) {
		case CAVE: {
			DldCave base = new DldCave(beginX, endX, beginZ, endZ, floorY, ceilY, 
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
	
	
	public static Room makeRoom(int beginX, int endX, int beginZ, int endZ, int floorY, int ceilY, 
			Dungeon dungeon, Room parent, Room previous, RoomType type) {	
		switch(type) {
		case CAVE: {
			DldCave base = new DldCave(beginX, endX, beginZ, endZ, floorY, ceilY, 
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