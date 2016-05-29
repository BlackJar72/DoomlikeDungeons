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


/**
 * The building block for more complex shape-primitives represented 
 * by the Shape class, and thus the foundational class for all dungeon
 * architecture.
 * 
 * Each rectangle is, if used correctly, a sub-section of a unit square
 * centered on 0,0 in the xz plane.  This allows them, and thus shape 
 * built from them, to be stretched to a desired size and shape (rounded)
 * by multiplication by the desired dimensions and placed by adding the 
 * coordinates of the center block (which could very well be a fractional 
 * block.
 * 
 * All rectangles used are static final members of this class.  The class 
 * is also immutable.
 * 
 * @author Jared Blackburn
 *
 */
public class Rectangle {
	
	final float xdim, zdim, xcoord, zcoord;				// Width in each dimension and center coordinates
	
	
	public Rectangle(float xdim, float zdim, float xcoord, float zcoord) {
		this.xdim = xdim;
		this.zdim = zdim;
		this.xcoord = xcoord;
		this.zcoord = zcoord;
	}
	
	
	/**
	 * Add the rectangle to the dungeon as pool of "liquid."
	 * 
	 * @param dungeon
	 * @param room
	 * @param sx x coordinate
	 * @param sz z coordinate
	 * @param sdimx length on x
	 * @param sdimz length on z
	 * @param invertX
	 * @param invertZ
	 */
	public void drawLiquid(Dungeon dungeon, Room room, float sx, float sz, float sdimx, float sdimz, 
			boolean invertX, boolean invertZ) {
		int drop;
		if(dungeon.theme.flags.contains(ThemeFlags.SWAMPY)) drop = 1;
		else drop = 2;
		byte xbegin, xend, zbegin, zend;
		
		// Find actual beginning and ending coordinates
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
	
	
	/**
	 * Add the rectangle to as a walkway (normal floor) through a previously added pool.
	 * 
	 * @param dungeon
	 * @param room
	 * @param sx x coordinate
	 * @param sz z coordinate
	 * @param sdimx length in x 
	 * @param sdimz length in z
	 * @param invertX
	 * @param invertZ
	 */
	public void drawWalkway(Dungeon dungeon, Room room, float sx, float sz, byte sdimx, byte sdimz, 
				boolean invertX, boolean invertZ) {
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
					}
				//System.out.println("Rectangle has been drawn.");
		}
	
	
	/**
	 * Adds the rectangle as a section filled with walls from floor to ceiling (an 
	 * area cut out from the rooms actual space).
	 * 
	 * @param dungeon
	 * @param room
	 * @param sx x coordinate
	 * @param sz z coordinate
	 * @param sdimx length on x
	 * @param sdimz length on z
	 * @param invertX
	 * @param invertZ
	 */
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
	
	
	/**
	 * Add the rectangle as a area of empty space, that is, an area from which previously
	 * place walls have been removed.
	 * 
	 * @param dungeon
	 * @param room
	 * @param sx x coordinate
	 * @param sz z coordinate
	 * @param sdimx length in x
	 * @param sdimz length in z
	 * @param invertX
	 * @param invertZ
	 */
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
					}
		}
	
	
	/**
	 * This will add a platform built based on the rectangle and its specifications.
	 * The specifications include the scaling factors in x and z, coordinated to 
	 * place it, and if it should be inverted.  This technically resent the floor 
	 * height, and is thus also used to add depression be setting the floor to a lower
	 * height rather than higher.
	 * 
	 * @param dungeon
	 * @param room
	 * @param floorY The new floor height
	 * @param sx the x coordinate
	 * @param sz the z coordinate
	 * @param sdimx the length on x
	 * @param sdimz the length on z
	 * @param invertX
	 * @param invertZ
	 */
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
	
	
	public float getzdim() {
		return zdim;
	}
	
	
	public float getxcoord() {
		return xcoord;
	}
	
	
	public float getzcoord() {
		return zcoord;
	}
}
