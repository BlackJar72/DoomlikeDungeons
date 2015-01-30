package jaredbgreat.dldungeons.themes;

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
