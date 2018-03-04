package jaredbgreat.dldungeons.themes;

/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		


/**
 * An enumeration of dungeon size categories.
 * 
 * @author Jared Blackburn
 *
 */
public enum Sizes implements Autoselectable {
	TINY 	(80,	39,		42,	12,	2,	2),
	SMALL 	(112,	55,		50,	16,	3,	2),
	MEDIUM	(144,	71,		58,	20,	4,	2),
	LARGE	(176,	87,		82,	22,	5,	3),
	HUGE	(208,	103,   112,	24,	6,	4);
	
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
