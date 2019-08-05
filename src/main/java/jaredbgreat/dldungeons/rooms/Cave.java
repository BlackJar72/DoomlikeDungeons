package jaredbgreat.dldungeons.rooms;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.pieces.Doorway;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.AStar;


/**
 * This class represents alternative caves created by cellular automata 
 * with a "2 1/2 D" architecture.
 * 
 * The algorithm uses is based on one originally created for Rogue-like games
 * and a variation is also use by the Oblige level generator for creating 
 * "natural" areas in Doom levels.
 * 
 *  Googling "cellular automata caves" will produce many relevant results.
 * 
 * @author Jared Blackburn (JaredBGreat)
 */
public class Cave extends Room {
	private int xSize, zSize, layers;
	private int[][][] cells;	
	private int[][]   scratchpad;
	
	private int sum;
	
	
	public Cave(int beginX, int endX, int beginZ, int endZ, int floorY,
			int ceilY, Dungeon dungeon, Room parent, Room previous) {
		super(beginX, endX, beginZ, endZ, floorY, ceilY, dungeon, previous, previous);
		this.degenerate = true;
	}
	
	
	/**
	 * Plans the room.  Chests and spawner are added in the same way 
	 * as for basic rooms, but walls and variation in floor / ceiling
	 * height are determined by cellular automata to produce a more
	 * cave-like layout.
	 */
	@Override
	public Room plan(Dungeon dungeon, Room parent) {
		xSize = endX - beginX;
		zSize = endZ - beginZ;
		layers = dungeon.verticle.ordinal();{
			if(layers < 1) layers = 1;
		}
		
		cells = new int[layers][xSize][zSize];
		
		for(int i = 0; i < xSize; i++) {
			for(int j = 0; j < zSize; j++) {
				for(int k = 0; k < layers; k++) {
					cells[k][i][j] = dungeon.random.nextInt(2);
				}
			}
		}
		cells[0] = layerConvert1(cells[0], 5 + dungeon.random.nextInt(2));
		for(int i = 0; i < xSize; i++) {
			for(int j = 0; j < zSize; j++) {
				if(cells[0][i][j] == 1) {
					dungeon.map.isWall[i + beginX][j  + beginZ] = true;
				}
			}
		}
		int tmpFY = floorY;
		int tmpCY = ceilY;
		for(int k = 1; k < layers; k++) {
			cells[k] = layerConvert2(cells[k], k-1, 4 + dungeon.random.nextInt(3));
			tmpFY += (dungeon.random.nextInt(3) - 1);
			tmpCY += (dungeon.random.nextInt(3) - 1);
			if((tmpCY - tmpFY) < 3) {
					tmpCY = tmpFY + 3;
			}
			for(int i = 0; i < xSize; i++) {
				for(int j = 0; j < zSize; j++) {
					if(cells[k][i][j] == 0) {
						dungeon.map.floorY[i + beginX][j  + beginZ] = (byte)tmpFY;
						dungeon.map.nFloorY[i + beginX][j  + beginZ] = (byte)tmpFY;
						dungeon.map.ceilY[i + beginX][j  + beginZ] = (byte)tmpCY;
						dungeon.map.nCeilY[i + beginX][j  + beginZ] = (byte)tmpCY;
					}
				}
			}
		} 
		if(hasEntrance) {
			for(int i = (int)realX -2; i < ((int)realX + 2); i++)
					for(int j = (int)realZ - 2; j < ((int)realZ + 2); j++) {
						dungeon.map.floorY[i][j] = (byte)floorY;
						dungeon.map.hasLiquid[i][j] = false;
						dungeon.map.isWall[i][j] = false;
					}
		}
		if(parent == null) {
			addSpawners(dungeon);
		}
		if(dungeon.naturals.use(dungeon.random) || !dungeon.variability.use(dungeon.random)) {
			for(int i = beginX; i < endX; i++) {
				for(int j = beginZ; j < endZ; j++) {				
					dungeon.map.wall[i][j] = caveBlock;				
					dungeon.map.floor[i][j] = caveBlock;				
					dungeon.map.ceiling[i][j] = caveBlock;
				}
			}
		}
		return this;
	}
	
	
	/**
	 * Use cellular automata to find area that should be walls.
	 * 
	 * @param layer
	 * @param thresshold
	 * @return
	 */
	private int[][] layerConvert1(int[][] layer, int thresshold) {
		makeScratchpad();
		for(int i = xSize - 2; i > 0; i--) {
			for(int j = zSize - 2; j > 0; j--) {
				processCell(layer, i, j, thresshold);
			}
		}
		return scratchpad;
	}
	
	
	/**
	 * Use cellular automata to determine areas of height variation (floor 
	 * and ceiling) between the walls.
	 * 
	 * @param layer
	 * @param down
	 * @param thresshold
	 * @return
	 */
	private int[][] layerConvert2(int[][] layer, int down, int thresshold) {
		makeScratchpad();
		for(int i = xSize - 2; i > 0; i--) {
			for(int j = zSize - 2; j > 0; j--) {
				if(cells[down][i][j] == 1) {
					layer[i][j] = 1;
				}
			}
		}
		for(int i = xSize - 2; i > 0; i--) {
			for(int j = zSize - 2; j > 0; j--) {
				processCell(layer, i, j, thresshold);
			}
		}
		return scratchpad;
	}
	
	
	/**
	 * This process the cells for a tile, convert it to one or zero based on 
	 * the sum of it and its neighbors.  Note that most implementation do not
	 * consider the initial value of the cell itself, but I get good results 
	 * doing so and it simplifies the code, so I do here.
	 * 
	 * @param layer
	 * @param x
	 * @param z
	 * @param thresshold
	 */
	private void processCell(int[][] layer, int x, int z, int thresshold) {
		sum = 0;
		for(int i = x-1; i <= x+1; i++) {
			for(int j = z-1; j <= z+1; j++ ) {
				sum += layer[i][j];
			}
		}
		if(sum >= thresshold) {
			scratchpad[x][z] = 1;
		} else {
			scratchpad[x][z] = 0;
		}
	}
	
	
	/**
	 * Initialize the classes internal (read, private) scratchpad for a 
	 * new round of cellular automata processing.
	 */
	private void makeScratchpad() {
		scratchpad = new int[xSize][zSize];
		for(int i = 0; i < xSize; i++) {
			scratchpad[i][0] = 1; 
			scratchpad[i][zSize - 1] = 1; 
		}
		for(int i = 0; i < zSize; i++) {
			scratchpad[0][1] = 1; 
			scratchpad[xSize - 1][i] = 1; 
		}
	}

}
