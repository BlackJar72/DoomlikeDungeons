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


import java.util.Random;

public enum Degrees implements Autoselectable {
	
	
	NONE	( 0),
	FEW 	( 1),
	SOME	( 3),
	PLENTY	( 5),
	HEAPS	( 9),
	ALL		(10);
	
	
	public final int value;
	public static final int scale = 10;
	
	
	Degrees(int val) {
		value = val;
	}
	
	
	public boolean use(Random random) {
		return(random.nextInt(scale) < value);
	}	
}
