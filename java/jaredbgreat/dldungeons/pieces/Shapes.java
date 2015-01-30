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


import jaredbgreat.dldungeons.planner.Symmetry;

import java.util.EnumSet;
import java.util.Random;

import static jaredbgreat.dldungeons.pieces.Shape.*;


public enum Shapes {

	X (1, 1, xgroup),
	L (2, 2, lgroup),
	O (3, 3, ogroup),
	T (3, 3, tgroup),
	F (4, 5, fgroup),
	E (4, 5, egroup),
	I (3, 3, igroup), 
	C (3, 3, cgroup), 
	U (3, 3, ugroup),
	S (5, 5, sgroup);
	
	
	public final int minx;
	public final int miny;
	public final Shape[] family;
	
	
	Shapes(int minx, int miny, Shape[] family) {
		this.minx = minx;
		this.miny = miny;
		this.family = family;
	}
	
	
	public static Shapes wholeShape(Symmetry sym, Random random) {
		switch (sym) {
		case NONE:
			return sym.allPart[random.nextInt(sym.allPart.length)];
		case R:
		case SW:
			return sym.rotatedPart[random.nextInt(sym.rotatedPart.length)];
		case TR1:
		case TR2:
			return sym.transPart[random.nextInt(sym.transPart.length)];
		case X:
			return sym.xsymmetricPart[random.nextInt(sym.xsymmetricPart.length)];
		case XY:
			return sym.xysymmetricPart[random.nextInt(sym.xysymmetricPart.length)];
		case Y:
			return sym.ysymmetricPart[random.nextInt(sym.ysymmetricPart.length)];
		default:
			return sym.allPart[random.nextInt(sym.allPart.length)];		
		}
	}
	
}
