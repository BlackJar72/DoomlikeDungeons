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
	TINY 	(5,	 39,		42,	 12,	2,	2), //5  3  2
	SMALL 	(7,	 55,		50,	 16,	3,	2),		//7	 4  3
	MEDIUM	(9,  71,		58,	 20,	4,	2),		//9  5  4
	LARGE	(11, 87,		82,	 22,	5,	3),		//11 6  5
	HUGE	(13, 103,      112,  24,	6,	4);		//13 7  6
	
	public final int chunkWidth;
	public final int width;		// Distance across the dungeon zone
	public final int chunkRadius;
	public final int radius;	// Square "radius" around center block (pre-calculated)
	public final int maxRooms;
	public final int maxRoomSize;
	public final int maxNodes;
	public final int minNodes;
	
	Sizes(int d, int r, int mr, int mrs, int maxn, int minn) {
		chunkWidth = d;
		width = d * 16;
		chunkRadius = d / 2;
		radius = r;
		maxRooms = mr;
		maxRoomSize = mrs;
		maxNodes = maxn;
		minNodes = minn;
	}
}
