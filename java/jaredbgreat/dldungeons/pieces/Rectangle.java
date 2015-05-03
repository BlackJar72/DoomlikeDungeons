package jaredbgreat.dldungeons.pieces;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import jaredbgreat.dldungeons.themes.ThemeType;


public class Rectangle {
	
	float xdim, zdim, xcoord, zcoord;				// Width in each dimension and center coordinates
	
	
	public Rectangle(float xdim, float zdim, float xcoord, float zcoord) {
		this.xdim = xdim;
		this.zdim = zdim;
		this.xcoord = xcoord;
		this.zcoord = zcoord;
	}
	
	
	public void scalex(float factor) {
		xdim *= factor;
	}
	
	
	public void scalez(float factor) {
		zdim *= factor;
	}	
	
	public void rotate() {
		float tmp = xdim;
		xdim = zdim;
		zdim = tmp;
	}
	
	/*
	 * I'm really not sure this is the best way to build shape primitives for room parts; it seemed 
	 * like a good idea but there are many complications I didn't originally think of.
	 */
	
	
	public void write(Dungeon dungeon, Room room, float sx, byte sy, float sz, 
				byte height, byte sdimx, byte sdimz, boolean invertX, boolean invertZ) {
		byte xbegin, xend, zbegin, zend;
		// This should probably be in a helper method
		if(invertX) {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
		} else {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
		}
		if(invertZ) {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
		} else {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
		}
		byte ceily  = (byte)(sy + height);	
			
		// Place floor, ceiling, and empty space
				for(byte k = zbegin; k < zend; k++) 
					for(byte j = xbegin; j < xend; j++) {
						if((j < 0) || (j >= dungeon.size.width) || (k < 0) || (k >= dungeon.size.width)) continue;
						if(dungeon.map.floorY[j][k] != 0) 
							dungeon.map.nFloorY[j][k] = dungeon.map.floorY[j][k];
						if(dungeon.map.ceilY[j][k] != 0) 
							dungeon.map.nCeilY[j][k] = dungeon.map.floorY[j][k]; 
						dungeon.map.ceilY[j][k]   = ceily;
						dungeon.map.floorY[j][k]  = sy;
						dungeon.map.wall[j][k]    = room.wallBlock1;
						dungeon.map.floor[j][k] = room.floorBlock;
						if(!room.sky) dungeon.map.ceiling[j][k] = room.cielingBlock;
					}
		
	}
	
	
	public void drawLiquid(Dungeon dungeon, Room room, float sx, float sz, float sdimx, float sdimz, 
			boolean invertX, boolean invertZ) {
		int drop;
		if(dungeon.theme.flags.contains(ThemeFlags.SWAMPY)) drop = 1;
		else drop = 2;
		byte xbegin, xend, zbegin, zend;
		if(invertX) {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
		} else {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
		}
		if(invertZ) {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
		} else {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
		}
			
		// Place floor, ceiling, and empty space
				for(byte k = zbegin; k < zend; k++) 
					for(byte j = xbegin; j < xend; j++) {
						if((j < 0) || (j >= dungeon.size.width) || (k < 0) || (k >= dungeon.size.width)) continue;
						if(dungeon.map.room[j][k] != room.id) continue;
						if(dungeon.map.floorY[j][k] > room.floorY) dungeon.map.floorY[j][k] = (byte)room.floorY;
						dungeon.map.floorY[j][k] = (byte)(room.floorY - drop);
						dungeon.map.hasLiquid[j][k] = true;
						byte nFloorY = dungeon.map.nFloorY[j][k] < dungeon.map.floorY[j][k] ? 
								dungeon.map.nFloorY[j][k] : dungeon.map.floorY[j][k];
						dungeon.map.nFloorY[j][k] = nFloorY;
					}
		}
	
	
	public void drawWalkway(Dungeon dungeon, Room room, float sx, float sz, byte sdimx, byte sdimz, 
				boolean invertX, boolean invertZ) {
		// To be used after setting whole room to hasLiquid
		byte xbegin, xend, zbegin, zend;
		int drop;
		if(dungeon.theme.type.contains(ThemeType.SWAMP)) drop = 1;
		else drop = 2;
		//System.out.println("Writing rectange.");
		if(invertX) {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
		} else {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
		}
		if(invertZ) {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
		} else {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
		}
				for(byte k = zbegin; k < zend; k++) 
					for(byte j = xbegin; j < xend; j++) {
						if((j < 0) || (j >= dungeon.size.width) || (k < 0) || (k >= dungeon.size.width)) continue;
						byte nFloorY = dungeon.map.nFloorY[j][k] < dungeon.map.floorY[j][k] ? 
								dungeon.map.nFloorY[j][k] : dungeon.map.floorY[j][k];
						dungeon.map.floorY[j][k] += drop;
						dungeon.map.hasLiquid[j][k] = false;
						dungeon.map.nFloorY[j][k] = nFloorY;
						//dungeon.map.extraDebugBuilding(j, k);
					}
				//System.out.println("Rectangle has been drawn.");
		}
	
	
	public void drawCutout(Dungeon dungeon, Room room, float sx, float sz, float sdimx, float sdimz, 
			boolean invertX, boolean invertZ) {
		byte xbegin, xend, zbegin, zend;
		if(invertX) {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
		} else {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
		}
		if(invertZ) {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
		} else {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
		}
				for(byte k = zbegin; k < zend; k++) 
					for(byte j = xbegin; j < xend; j++) {
						if((j < 0) || (j >= dungeon.size.width) || (k < 0) || (k >= dungeon.size.width)) continue;
						if(dungeon.map.room[j][k] != room.id) continue;
						dungeon.map.isWall[j][k] = true;
					}
		}
	
	
	public void drawCutin(Dungeon dungeon, Room room, float sx, float sz, byte sdimx, byte sdimz, 
			boolean invertX, boolean invertZ) {
		byte xbegin, xend, zbegin, zend;
		if(invertX) {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
		} else {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
		}
		if(invertZ) {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
		} else {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
		}
				for(byte k = zbegin; k < zend; k++) 
					for(byte j = xbegin; j < xend; j++) {
						if((j < 0) || (j >= dungeon.size.width) || (k < 0) || (k >= dungeon.size.width)) continue;
						dungeon.map.isWall[j][k] = false;
						//dungeon.map.extraDebugBuilding(j, k);
					}
		}
	
	
	public void drawPlatform(Dungeon dungeon, Room room, byte floorY, float sx, float sz, float sdimx, float sdimz, 
			boolean invertX, boolean invertZ) {
		byte xbegin, xend, zbegin, zend;
		if(invertX) {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) - (xcoord * sdimx));
		} else {
			xbegin = (byte)(sx - ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
			xend   = (byte)(sx + ((sdimx * xdim) / 2.0f) + (xcoord * sdimx));
		}
		if(invertZ) {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) - (zcoord * sdimz));
		} else {
			zbegin = (byte)(sz - ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
			zend   = (byte)(sz + ((sdimz * zdim) / 2.0f) + (zcoord * sdimz));
		}
				for(byte k = zbegin; k < zend; k++) 
					for(byte j = xbegin; j < xend; j++) {
						if((j < 0) || (j >= dungeon.size.width) || (k < 0) || (k >= dungeon.size.width)) continue;
						if(dungeon.map.room[j][k] != room.id) continue;
						dungeon.map.floorY[j][k] = floorY;
						dungeon.map.hasLiquid[j][k] = false;
						//dungeon.map.extraDebugBuilding(j, k);
					}
		}
		
	
	
	
	
	/***********************/
	/*     Rectangles      */
	/***********************/

	
	// Rectangle (Works)	
	public static final Rectangle simple      = new Rectangle(1.0f, 1.0f, 0.0f, 0.0f);
	
	// 'L' (Works)
	public static final Rectangle lback000    = new Rectangle(0.5f,   1.0f, -0.25f,    0.0f);
	public static final Rectangle lbottom000  = new Rectangle(0.5f,   0.5f,  0.25f,  -0.25f);
	public static final Rectangle lback090    = new Rectangle(1.0f,   0.5f,   0.0f,  -0.25f);
	public static final Rectangle lbottom090  = new Rectangle(0.5f,   0.5f,  0.25f,   0.25f);
	public static final Rectangle lback270    = new Rectangle(0.5f,   1.0f,  0.25f,    0.0f);
	public static final Rectangle lbottom270  = new Rectangle(0.5f,   0.5f, -0.25f,   0.25f);
	public static final Rectangle lback180    = new Rectangle(1.0f,   0.5f,   0.0f,  -0.25f);
	public static final Rectangle lbottom180  = new Rectangle(0.5f,   0.5f,  0.25f,   0.25f);

	// 'O' (Works)	
	public static final Rectangle oleft       = new Rectangle(0.3333333333f, 1.0f, -0.3333333333f, 0.0f);
	public static final Rectangle oright      = new Rectangle(0.3333333333f, 1.0f,  0.3333333333f, 0.0f);
	public static final Rectangle obottom     = new Rectangle(0.3333333333f, 0.3333333333f, 0.0f, -0.3333333333f);
	public static final Rectangle otop        = new Rectangle(0.3333333333f, 0.3333333333f, 0.0f,  0.3333333333f);

	// 'T' (I think its working -- not sure out its orientation)...
	public static final Rectangle ttop000     = new Rectangle(1.0f,          0.3333333333f,  0.0f,  0.3333333333f);
	public static final Rectangle tbottom000  = new Rectangle(0.3333333333f, 0.6666666667f,  0.0f, -0.1666666667f);
	public static final Rectangle ttop090     = new Rectangle(0.3333333333f, 1.0f,          -0.3333333333f,  0.0f);
	public static final Rectangle tbottom090  = new Rectangle(0.6666666667f, 0.3333333333f,  0.1666666667f,  0.0f);
	public static final Rectangle ttop180     = new Rectangle(1.0f, 0.3333333333f,           0.0f, -0.3333333333f);
	public static final Rectangle tbottom180  = new Rectangle(0.3333333333f, 0.6666666667f,  0.0f,  0.1666666667f);
	public static final Rectangle ttop270     = new Rectangle(0.3333333333f, 1.0f,           0.3333333333f,  0.0f);
	public static final Rectangle tbottom270  = new Rectangle(0.6666666667f, 0.3333333333f, -0.1666666667f,  0.0f);

	// 'E' / 'F' (Working)
	public static final Rectangle eback000    = new Rectangle(0.3333333333f, 1.0f, -0.3333333333f,  0.0f);
	public static final Rectangle etop000     = new Rectangle(0.6666666667f, 0.2f,  0.1666666667f,  0.4f);
	public static final Rectangle emiddle000  = new Rectangle(0.6666666667f, 0.2f,  0.1666666667f,  0.0f);
	public static final Rectangle ebottom000  = new Rectangle(0.6666666667f, 0.2f,  0.1666666667f, -0.4f);
	public static final Rectangle eback090    = new Rectangle(1.0f, 0.3333333333f,  0.0f,  -0.3333333333f);
	public static final Rectangle etop090     = new Rectangle(0.2f, 0.6666666667f, -0.4f,   0.1666666667f);
	public static final Rectangle emiddle090  = new Rectangle(0.2f, 0.6666666667f,  0.0f,   0.1666666667f);
	public static final Rectangle ebottom090  = new Rectangle(0.2f, 0.6666666667f,  0.4f,   0.1666666667f);	
	public static final Rectangle eback180    = new Rectangle(0.3333333333f, 1.0f,  0.3333333333f,   0.0f);
	public static final Rectangle etop180     = new Rectangle(0.6666666667f, 0.2f, -0.1666666667f,   0.4f);
	public static final Rectangle emiddle180  = new Rectangle(0.6666666667f, 0.2f, -0.1666666667f,   0.0f);
	public static final Rectangle ebottom180  = new Rectangle(0.6666666667f, 0.2f, -0.1666666667f,  -0.4f);
	public static final Rectangle eback270    = new Rectangle(1.0f, 0.3333333333f,  0.0f,   0.3333333333f);
	public static final Rectangle etop270     = new Rectangle(0.2f, 0.6666666667f,  0.4f,  -0.1666666667f);
	public static final Rectangle emiddle270  = new Rectangle(0.2f, 0.6666666667f,  0.0f,  -0.1666666667f);
	public static final Rectangle ebottom270  = new Rectangle(0.2f, 0.6666666667f, -0.4f,  -0.1666666667f);

	// 'I' / 'H' (Working)
	public static final Rectangle itop        = new Rectangle(1.0f, 0.3333333333f, 0.0f,  0.3333333333f);
	public static final Rectangle imiddle     = new Rectangle(0.3333333333f, 0.3333333333f,  0.0f, 0.0f);
	public static final Rectangle ibottom     = new Rectangle(1.0f, 0.3333333333f, 0.0f, -0.3333333333f);
	public static final Rectangle iright      = new Rectangle(0.3333333333f, 1.0f,  0.3333333333f, 0.0f);
	public static final Rectangle ileft       = new Rectangle(0.3333333333f, 1.0f, -0.3333333333f, 0.0f);
	
	// Plus-shape (Works)
	public static final Rectangle crosstop    = new Rectangle(0.3333333333f, 0.3333333333f, 0.0f,  0.3333333333f);
	public static final Rectangle crossmiddle = new Rectangle(1.0f,          0.3333333333f, 0.0f,           0.0f);
	public static final Rectangle crossbottom = new Rectangle(0.3333333333f, 0.3333333333f, 0.0f, -0.3333333333f);
	
	// 'U' (Works)
	public static final Rectangle uleft000    = new Rectangle(0.3333333333f,  0.6666666667f, 
															 -0.3333333333f,  0.1666666667f);	
	public static final Rectangle uright000   = new Rectangle(0.3333333333f,  0.6666666667f,  
															  0.3333333333f,  0.1666666667f);
	public static final Rectangle ubottom000  = new Rectangle(1.0f,           0.3333333333f,  
															  0.0f,          -0.3333333333f);
	public static final Rectangle uleft090    = new Rectangle(0.6666666667f,  0.3333333333f,  
															  0.1666666667f, -0.3333333333f);	
	public static final Rectangle uright090   = new Rectangle(0.6666666667f,  0.3333333333f,  
															  0.1666666667f,  0.3333333333f);
	public static final Rectangle ubottom090  = new Rectangle(0.3333333333f,  1.0f,            
															 -0.3333333333f,  0.0f);
	public static final Rectangle uright180   = new Rectangle(0.3333333333f,  0.6666666667f,  
															  0.3333333333f, -0.1666666667f);	
	public static final Rectangle uleft180    = new Rectangle(0.3333333333f,  0.6666666667f,  
															 -0.3333333333f, -0.1666666667f);
	public static final Rectangle ubottom180  = new Rectangle(1.0f,           0.3333333333f,  
															  0.0f,           0.3333333333f);
	public static final Rectangle uright270   = new Rectangle(0.6666666667f,  0.3333333333f, 
															 -0.1666666667f, -0.3333333333f);	
	public static final Rectangle uleft270    = new Rectangle(0.6666666667f,  0.3333333333f,  
															 -0.1666666667f,  0.3333333333f);
	public static final Rectangle ubottom270  = new Rectangle(0.3333333333f,  1.0f,             
															  0.333333333f,   0.0f);
	
	// 'S' (Working)
	public static final Rectangle stop000     = new Rectangle(1.0f, 0.2f,  0.0f, -0.4f);
	public static final Rectangle smiddle000  = new Rectangle(1.0f, 0.2f,  0.0f,  0.0f);
	public static final Rectangle sbottom000  = new Rectangle(1.0f, 0.2f,  0.0f,  0.4f);
	public static final Rectangle sleft000    = new Rectangle(0.2f, 0.2f, -0.4f,  0.2f);
	public static final Rectangle sright000   = new Rectangle(0.2f, 0.2f,  0.4f, -0.2f);
	public static final Rectangle stop090     = new Rectangle(0.2f, 1.0f, -0.4f,  0.0f);
	public static final Rectangle smiddle090  = new Rectangle(0.2f, 1.0f,  0.0f,  0.0f);
	public static final Rectangle sbottom090  = new Rectangle(0.2f, 1.0f,  0.4f,  0.0f);
	public static final Rectangle sleft090    = new Rectangle(0.2f, 0.2f, -0.2f,  0.4f);
	public static final Rectangle sright090   = new Rectangle(0.2f, 0.2f,  0.2f, -0.4f);
	
	
	
	/***********************/
	/* Getters and Setters */
	/***********************/
	
	
	public float getxdim() {
		return xdim;
	}
	
	
	public void setxdim(float val) {
		xdim = val;
	}
	
	
	public float getzdim() {
		return zdim;
	}
	
	
	public void setzdim(float val) {
		zdim = val;
	}
	
	
	public float getxcoord() {
		return xcoord;
	}
	
	
	public void setxcoord(float val) {
		xcoord = val;
	}
	
	
	public float getzcoord() {
		return zcoord;
	}
	
	
	public void setzcoord(float val) {
		zdim = val;
	}
}
