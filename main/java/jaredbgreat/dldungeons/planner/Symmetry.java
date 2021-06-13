package jaredbgreat.dldungeons.planner;


/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	


import jaredbgreat.dldungeons.pieces.Shapes;


/**
 * And enumeration of possible symmetries for rooms, including:
 * 
 * <ol>
 * <li>NONE: asymmetric</li>
 * <li>X: single line parallel with the x-axis</li>
 * <li>Z: single line parallel with the z-axis</li>
 * <li>XZ: two lines, parallel with the x-axis and z-axis</li>
 * <li>TR1: a diagonal line (transpose symmetry)</li>
 * <li>TR2: a diagonal line (transpose symmetry)</li>
 * <li>R: Rotational symmetry, based on a 180 degree rotation the center of the room</li>
 * <li>SW: Swirled symmetry, rotating 90 degrees around the center of the room</li>
 * </ol>
 * 
 * @author Jared Blackburn
 *
 */
public enum Symmetry {

	NONE (0, 0, false, false, false),
	X    (1, 1, true, false, false),
	Z    (2, 1, false, true, false),
	XZ   (3, 2, true, true, false),
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
	
	
	/**
	 * Returns a Symmetry constant based on the dungeons degree of
	 * symmetry; used to get the symmetry type for individual rooms. 
	 * 
	 * @param dungeon
	 * @return a Symmetry constant repressenting a type of semmetry
	 */
	public static Symmetry getSymmetry(Dungeon dungeon) {
		int num = 0;
		if(dungeon.symmetry.use(dungeon.random)) num += 1;
		if(dungeon.symmetry.use(dungeon.random)) num += 1;
		if(num == 0) return NONE;
		int which = dungeon.random.nextInt(4 / num);
		if(num == 1) switch (which) {
			case 0: return X;
			case 1: return Z;
			case 2: 
				if(dungeon.random.nextBoolean()) {
					return TR1;
				} else {
					return TR2;					
				}
			case 3: return R;
			default: return NONE;
		} else switch (which) {
			case 0: return XZ;
			case 1: return SW;
			default: return NONE;
		}		
	}
	
}