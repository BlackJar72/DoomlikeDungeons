package jaredbgreat.dldungeons.pieces;


/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	


import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.rooms.Room;

import java.util.ArrayList;

import net.minecraft.world.World;
import static jaredbgreat.dldungeons.pieces.Rectangle.*;


public class Shape {
	
	Rectangle[] rectangles;	
	
	public Shape(Rectangle[] rectangles) {
		this.rectangles = rectangles;
	}
	
	
	/*
	 * I'm really not sure this is the best way to build shape primitives for room parts; it seemed 
	 * like a good idea but there are many complications I didn't originally think of.
	 */
	
	
	public void write(Dungeon dungeon, Room room, 
					  float sx, byte sy, float sz, byte height, byte sdimx, byte sdimz, 
					  boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.write(dungeon, room, sx, sy, sz, height, sdimx, sdimz, invertX, invertZ);
			// TODO: More...?
		}
	}
	
	
	public void drawLiquid(Dungeon dungeon, Room room, float sx, float sz, float sdimx, float sdimz, 
			boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawLiquid(dungeon, room, sx, sz, sdimx, sdimz, invertX, invertZ);
			// TODO: More...?
		}
	}
	
	
	public void drawWalkway(Dungeon dungeon, Room room, float sx, float sz, byte sdimx, byte sdimz, 
				boolean invertX, boolean invertZ) {
		//System.out.println("About to iterate rectangles; shape is " + this.getClass() + ".");
		for(Rectangle rect: rectangles) {
			//System.out.println("About add rectangle; rectangle is is " + rect.getClass() + ".");
			rect.drawWalkway(dungeon, room, sx, sz, sdimx, sdimz, invertX, invertZ);
			// TODO: More...?
		}
	}
	
	
	public void drawCutout(Dungeon dungeon, Room room, float sx, float sz, float sdimx, float sdimz, 
			boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawCutout(dungeon, room, sx, sz, sdimx, sdimz, invertX, invertZ);
			// TODO: More...?
		}
	}
	
	
	public void drawCutin(Dungeon dungeon, Room room, 
					  float sx, float sz, byte sdimx, byte sdimz, boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawCutin(dungeon, room, sx, sz, sdimx, sdimz, invertX, invertZ);
			// TODO: More...?
		}
	}
	
	
	public void drawPlatform(Dungeon dungeon, Room room, byte floorY, 
					  float sx, float sz, float sdimx, float sdimz, boolean invertX, boolean invertZ) {
		for(Rectangle rect: rectangles) {
			rect.drawPlatform(dungeon, room, floorY, sx, sz, sdimx, sdimz, invertX, invertZ);
			// TODO: More...?
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
	
	
	public static final Shape[][] assymmetrics = {fgroup};
	public static final Shape[][] xsymmetrics = {xgroup, ogroup, cgroup, ugroup, tgroup, igroup};
	public static final Shape[][] ysymmetrics = {xgroup, ogroup, cgroup, egroup, igroup};
	public static final Shape[][] xysymmetrics = {xgroup, ogroup, cgroup, igroup};
	public static final Shape[][] transshapes = {lgroup, xgroup, ogroup, cgroup};
	public static final Shape[][] rotateds = {sgroup, xgroup, ogroup, cgroup, igroup};
	public static final Shape[][] allshapes = {xgroup, lgroup, ogroup, tgroup, fgroup, 
											   egroup, igroup, cgroup, ugroup, sgroup};
	
	
	public static final Shape[][] assymmetricPart = {fgroup};
	public static final Shape[][] xsymmetricPart = {ogroup, cgroup, ugroup, tgroup, igroup};
	public static final Shape[][] ysymmetricPart = {ogroup, cgroup, egroup, igroup};
	public static final Shape[][] xysymmetricPart = {ogroup, cgroup, igroup};
	public static final Shape[][] transPart = {ogroup, cgroup, lgroup};
	public static final Shape[][] rotatedPart = {sgroup, ogroup, cgroup, igroup};
	public static final Shape[][] allPart = {lgroup, ogroup, tgroup, fgroup, 
											   egroup, igroup, cgroup, ugroup, sgroup};
	
	
	public static final Shape[][] assymmetricSolid = {fgroup};
	public static final Shape[][] xsymmetricSolid = {xgroup, ugroup, tgroup};
	public static final Shape[][] ysymmetricSolid = {xgroup, egroup};
	public static final Shape[][] xysymmetricSolid = {xgroup};
	public static final Shape[][] transSolid = {xgroup, lgroup};
	public static final Shape[][] rotatedSolid = {sgroup, xgroup};
	public static final Shape[][] allSolids = {xgroup, lgroup, tgroup, fgroup, 
											   egroup, ugroup};
	
		
	//TODO: Add a system of selecting some things by symmetry?  Or maybe just add to certain parts of the rooms 
}
