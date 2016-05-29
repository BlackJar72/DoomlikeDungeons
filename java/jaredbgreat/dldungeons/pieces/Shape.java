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

import java.util.ArrayList;

import net.minecraft.world.World;
import static jaredbgreat.dldungeons.pieces.Rectangle.*;


/**
 * This class represents shape primitives from which dungeon architecture
 * is drawn.  Each shape is cut from a unit square centered on 0,0 in the 
 * xz plane and built from rectangles.  This allows for shapes to be easily
 * stretched to fit areas they are added.  In addition, they can be flipped
 * on either axis and come in pre-rotated variations, for easy transformation.
 * 
 * All shapes used by the mod are defined as static final members of the class,
 * as are groups varying only by rotation as are larger groups defined by 
 * symmetry.
 * 
 * @author Jared Blackburn
 *
 */
public class Shape {
	
	private final Rectangle[] rectangles;	
	
	public Shape(Rectangle[] rectangles) {
		this.rectangles = rectangles;
	}
	
	
	/**
	 * Add a pool of "liquid" in this shape to the dungeon.
	 * 
	 * @param dungeon
	 * @param room
	 * @param sx the x coordinate
	 * @param sz the z coordinate
	 * @param sdimx the length on x
	 * @param sdimz the length on z
	 * @param invertX
	 * @param invertZ
	 */
	public void drawLiquid(Dungeon dungeon, Room room, float sx, float sz, float sdimx, float sdimz, 
			boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawLiquid(dungeon, room, sx, sz, sdimx, sdimz, invertX, invertZ);
		}
	}
	
	
	/**
	 * Add a walkway (normal floor at normal height) through a pool of 
	 * previously added liquid.  This is mostly for use with whole room
	 * shapes.
	 * 
	 * @param dungeon
	 * @param room
	 * @param sx the x coordinate
	 * @param sz the z coordinate
	 * @param sdimx the length on x
	 * @param sdimz the length on z
	 * @param invertX
	 * @param invertZ
	 */
	public void drawWalkway(Dungeon dungeon, Room room, float sx, float sz, byte sdimx, byte sdimz, 
				boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawWalkway(dungeon, room, sx, sz, sdimx, sdimz, invertX, invertZ);
		}
	}
	
	
	/**
	 * Effectively remove an area of in the shape from the dungeon by filling it 
	 * with walls.
	 * 
	 * @param dungeon
	 * @param room
	 * @param sx the x coordinate
	 * @param sz the z coordinate
	 * @param sdimx the length on x
	 * @param sdimz the length on z
	 * @param invertX
	 * @param invertZ
	 */
	public void drawCutout(Dungeon dungeon, Room room, float sx, float sz, float sdimx, float sdimz, 
			boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawCutout(dungeon, room, sx, sz, sdimx, sdimz, invertX, invertZ);
		}
	}
	
	
	/**
	 * Removes walls from an area of this shape (cuts into the wall); this is
	 * mostly for use with whole room shapes.
	 * 
	 * @param dungeon
	 * @param room
	 * @param sx the x coordinate
	 * @param sz the z coordinate
	 * @param sdimx the length on x
	 * @param sdimz the length on z
	 * @param invertX
	 * @param invertZ
	 */
	public void drawCutin(Dungeon dungeon, Room room, 
					  float sx, float sz, byte sdimx, byte sdimz, boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawCutin(dungeon, room, sx, sz, sdimx, sdimz, invertX, invertZ);
		}
	}
	
	
	/**
	 * Adds a platform (or depression) of this shape.
	 * 
	 * @param dungeon
	 * @param room
	 * @param floorY the new floor height
	 * @param sx the x coordinate
	 * @param sz the z coordinate
	 * @param sdimx the length on x
	 * @param sdimz the length on z
	 * @param invertX
	 * @param invertZ
	 */
	public void drawPlatform(Dungeon dungeon, Room room, byte floorY, 
					  float sx, float sz, float sdimx, float sdimz, boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawPlatform(dungeon, room, floorY, sx, sz, sdimx, sdimz, invertX, invertZ);
		}
	}
	
	
	
	/***********************/
	/*       Shapes        */
	/***********************/
	
	// Rectangle (Works)
	public static final Shape simpleRect = new Shape(new Rectangle[]{simple});
	public static final Shape X = simpleRect;
	public static final Shape[] xgroup = {X, X, X, X};
	
	// 'L' (Works)
	public static final Shape L000 = new Shape(new Rectangle[]{lback000, lbottom000});
	public static final Shape L090 = new Shape(new Rectangle[]{lback090, lbottom090});
	public static final Shape L180 = new Shape(new Rectangle[]{lback180, lbottom180});
	public static final Shape L270 = new Shape(new Rectangle[]{lback270, lbottom270});
	public static final Shape[] lgroup = {L000, L090, L180, L270};
	
	// 'O' (Works)
	public static final Shape O    = new Shape(new Rectangle[]{obottom, oleft, oright, otop});
	public static final Shape[] ogroup = {O, O, O, O};

	// 'T' (I think its working -- not sure out its orientation)...
	public static final Shape T000 = new Shape(new Rectangle[]{ttop000, tbottom000});
	public static final Shape T090 = new Shape(new Rectangle[]{ttop090, tbottom090});
	public static final Shape T180 = new Shape(new Rectangle[]{ttop180, tbottom180});
	public static final Shape T270 = new Shape(new Rectangle[]{ttop270, tbottom270});
	public static final Shape[] tgroup = {T000, T090, T180, T270};
	
	// 'F' (Working)
	public static final Shape F000 = new Shape(new Rectangle[]{eback000, etop000, emiddle000});
	public static final Shape F090 = new Shape(new Rectangle[]{eback090, etop090, emiddle090});
	public static final Shape F180 = new Shape(new Rectangle[]{eback180, etop180, emiddle180});
	public static final Shape F270 = new Shape(new Rectangle[]{eback270, etop270, emiddle270});
	public static final Shape[] fgroup = {F000, F090, F180, F270};
	
	// 'E' (Works)
	public static final Shape E000 = new Shape(new Rectangle[]{eback000, etop000, emiddle000, ebottom000});
	public static final Shape E090 = new Shape(new Rectangle[]{eback090, etop090, emiddle090, ebottom090});
	public static final Shape E180 = new Shape(new Rectangle[]{eback180, etop180, emiddle180, ebottom180});
	public static final Shape E270 = new Shape(new Rectangle[]{eback270, etop270, emiddle270, ebottom270});
	public static final Shape[] egroup = {E000, E090, E180, E270};
	
	// 'I' / 'H' (Working)
	public static final Shape I000 = new Shape(new Rectangle[]{itop, imiddle, ibottom});
	public static final Shape I090 = new Shape(new Rectangle[]{ileft, imiddle, iright});
	public static final Shape I180 = I000;
	public static final Shape I270 = I090;
	public static final Shape[] igroup = {I000, I090, I180, I270};
	
	// Plus-shape (Works)
	public static final Shape CROSS = new Shape(new Rectangle[]{crosstop, crossmiddle, crossbottom});
	public static final Shape[] cgroup = {CROSS, CROSS, CROSS, CROSS};
	
	// 'U' (Works)
	public static final Shape U000  = new Shape(new Rectangle[]{uleft000, uright000, ubottom000});
	public static final Shape U090  = new Shape(new Rectangle[]{uleft090, uright090, ubottom090});
	public static final Shape U180  = new Shape(new Rectangle[]{uleft180, uright180, ubottom180});
	public static final Shape U270  = new Shape(new Rectangle[]{uleft270, uright270, ubottom270});
	public static final Shape[] ugroup = {U000, U090, U180, U270};
	
	// 'S' (Works)
	public static final Shape S000  = new Shape(new Rectangle[]{stop000, smiddle000, sbottom000, 
																sleft000, sright000});
	public static final Shape S090  = new Shape(new Rectangle[]{stop090, smiddle090, sbottom090, 
																sleft090, sright090});
	public static final Shape S180  = S000;
	public static final Shape S270  = S090;
	public static final Shape[] sgroup = {S000, S090, S180, S270};
	
	

	public static final Shape[][] allshapes = {xgroup, lgroup, ogroup, tgroup, fgroup, 
											   egroup, igroup, cgroup, ugroup, sgroup};

	public static final Shape[][] allSolids = {xgroup, lgroup, tgroup, fgroup, 
											   egroup, ugroup};
	
	
}
