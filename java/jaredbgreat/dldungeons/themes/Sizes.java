package jaredbgreat.dldungeons.themes;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */		


/**
 * An enumeration of dungeon size categories.
 * 
 * @author Jared Blackburn
 *
 */
public enum Sizes implements Autoselectable {
	TINY 	(80,	39,		42,	12,	2,	2),
	SMALL 	(96,	47,		50,	16,	3,	2),
	MEDIUM	(112,	55,		58,	20,	4,	2),
	LARGE	(144,	71,		82,	22,	5,	3),
	HUGE	(176,	87,    112,	24,	6,	4);
	
	public final int width;		// Distance across the dungeon zone
	public final int radius;	// Square "radius" around center block (pre-calculated)
	public final int maxRooms;
	public final int maxRoomSize;
	public final int maxNodes;
	public final int minNodes;
	
	Sizes(int d, int r, int mr, int mrs, int maxn, int minn) {
		width = d;
		radius = r;
		maxRooms = mr;
		maxRoomSize = mrs;
		maxNodes = maxn;
		minNodes = minn;
	}
}
