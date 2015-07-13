package jaredbgreat.dldungeons.rooms.naturals;


import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.AbstractRoom;
import jaredbgreat.dldungeons.rooms.Room;

/**
 * This class represents alternative caves created by cellular automata 
 * with a "2 1/2 D" architecture (similar to those created by Oblige).
 * 
 * @author Jared Blackburn (JaredBGreat)
 */
public class DldCave extends Room {
	private int xSize, zSize, layers;
	private int[][][] cells;	
	private int[][]   scratchpad;
	
	private int sum;
	
	
	public DldCave(int beginX, int endX, int beginZ, int endZ, int floorY,
			int ceilY, Dungeon dungeon, Room parent, Room previous) {
		super(beginX, endX, beginZ, endZ, floorY, ceilY, dungeon, previous, previous);
	}
	
	
	@Override
	public Room plan(Dungeon dungeon, Room parent) {
		xSize = endX - beginX;
		zSize = endZ - beginZ;
		layers = dungeon.verticle.ordinal();{
			if(layers < 1) layers = 1;
		}
		
		cells      = new int[layers][xSize][zSize];
		
		for(int i = 0; i < xSize; i++) {
			for(int j = 0; j < zSize; j++) {
				for(int k = 0; k < layers; k++) {
					cells[k][i][j] = dungeon.random.nextInt(2);
				}
			}
		}
		cells[0] = layerConvert1(cells[0], 5);
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
			cells[k] = layerConvert2(cells[k], k-1, 5 - dungeon.random.nextInt(2));
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
						dungeon.map.nFloorY[i + beginX][j  + beginZ] = (byte)tmpCY;
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
			addChests(dungeon);
		}
		return this;
	}
	
	
	private int[][] layerConvert1(int[][] layer, int thresshold) {
		makeScratchpad();
		for(int i = xSize - 2; i > 0; i--) {
			for(int j = zSize - 2; j > 0; j--) {
				processCell(layer, i, j, thresshold);
			}
		}
		return scratchpad;
	}
	
	
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
