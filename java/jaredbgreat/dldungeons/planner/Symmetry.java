package jaredbgreat.dldungeons.planner;


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


import jaredbgreat.dldungeons.pieces.Shape;
import jaredbgreat.dldungeons.pieces.Shapes;

import java.util.EnumSet;



public enum Symmetry {

	NONE (0, 0, false, false, false),
	X    (1, 1, true, false, false),
	Y    (2, 1, false, true, false),
	XY   (3, 2, true, true, false),
	TR1  (4, 1, false, false, true),
	TR2  (5, 1, false, false, true),
	R    (6, 1, false, false, true),
	SW   (7, 2, true, true, false);
	
	
	public final int ID;
	public final int level;
	public final boolean halfX;
	public final boolean halfZ;
	public final boolean doubler;
	
	
	public static final Shapes[] allshapes = {Shapes.X, Shapes.L, Shapes.O, Shapes.T, 
			Shapes.F, Shapes.E, Shapes.I, Shapes.C, Shapes.U, Shapes.S};
	public static final Shapes[] xsymmetics = {Shapes.X, Shapes.O, Shapes.E, Shapes.I, Shapes.C};
	public static final Shapes[] ysymmetics = {Shapes.X, Shapes.O, Shapes.T, Shapes.I, Shapes.C, 
			Shapes.U};
	public static final Shapes[] xysymmetrics = {Shapes.X, Shapes.O, Shapes.I, Shapes.C};
	public static final Shapes[] transshapes = {Shapes.X, Shapes.L, Shapes.O, Shapes.C, Shapes.U};
	public static final Shapes[] rotateds  = {Shapes.X, Shapes.O, Shapes.C, Shapes.S};
	
	
	public static final Shapes[] allPart = {Shapes.L, Shapes.O, Shapes.T, 
			Shapes.F, Shapes.E, Shapes.I, Shapes.C, Shapes.U, Shapes.S};
	public static final Shapes[] xsymmetricPart = {Shapes.O, Shapes.E, Shapes.I, Shapes.C};
	public static final Shapes[] ysymmetricPart = {Shapes.O, Shapes.T, Shapes.I, Shapes.C, Shapes.U};;
	public static final Shapes[] xysymmetricPart = {Shapes.O, Shapes.I, Shapes.C};
	public static final Shapes[] transPart = {Shapes.L, Shapes.O, Shapes.C, Shapes.U};
	public static final Shapes[] rotatedPart = {Shapes.X, Shapes.O, Shapes.C, Shapes.S};
	
	

	public static final Shapes[] allSolids = {Shapes.X, Shapes.L, Shapes.T, 
			Shapes.F, Shapes.E, Shapes.I, Shapes.C, Shapes.U, Shapes.S};
	public static final Shapes[] xsymmetricSolid = {Shapes.X, Shapes.E, Shapes.I, Shapes.C};
	public static final Shapes[] ysymmetricSolid = {Shapes.X, Shapes.T, Shapes.I, Shapes.C, Shapes.U};
	public static final Shapes[] xysymmetricSolid = {Shapes.X, Shapes.I, Shapes.C};
	public static final Shapes[] transSolid = {Shapes.X, Shapes.L, Shapes.C, Shapes.U};
	public static final Shapes[] rotatedSolid = {Shapes.X, Shapes.C, Shapes.S};
	
	
	
	Symmetry(int ID, int level, boolean halfX, boolean halfZ, boolean doubler) {
		this.ID = ID;
		this.level = level;
		this.halfX = halfX;
		this.halfZ = halfZ;
		this.doubler = doubler;
	}
	
	
	public static Symmetry getSymmetry(Dungeon dungeon) {
		int num = 0;
		if(dungeon.symmetry.use(dungeon.random)) num += 1;
		if(dungeon.symmetry.use(dungeon.random)) num += 1;
		if(num == 0) return NONE;
		int which = dungeon.random.nextInt(4 / num);
		if(num == 1) switch (which) {
			case 0: return X;
			case 1: return Y;
			case 2: return TR1;
			case 3: return R;
			default: return NONE;
		} else switch (which) {
			case 0: return XY;
			case 1: return SW;
			default: return NONE;
		}		
	}
	
}