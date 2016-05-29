package jaredbgreat.dldungeons.pieces;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	


import jaredbgreat.dldungeons.planner.Symmetry;

import java.util.EnumSet;
import java.util.Random;

import static jaredbgreat.dldungeons.pieces.Shape.*;


/**
 * Families of shapes, each representing a single shape in each of its rotations.
 * 
 * Each enumeration constant has an associates Shape area from the shape class
 * allows with minimum dimensions to hold the shape without loose details to 
 * rounding.
 * 
 * This class also helps find a shape for a given symmetry class.
 * 
 * @author Jared Blackburn
 *
 */
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
	
	
	/**
	 * Will return a random shape that fits a given symmetry.
	 * 
	 * @param sym
	 * @param random
	 * @return
	 */
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
		case XZ:
			return sym.xysymmetricPart[random.nextInt(sym.xysymmetricPart.length)];
		case Z:
			return sym.ysymmetricPart[random.nextInt(sym.ysymmetricPart.length)];
		default:
			return sym.allPart[random.nextInt(sym.allPart.length)];		
		}
	}
	
}
