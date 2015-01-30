package jaredbgreat.dldungeons;


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

public enum Difficulty {
	
	NONE  (0, 0, 0, 0, 0, false, "No spawners."),
	BABY  (3, 0, 0, 0, 0, false, "Baby mode."),
	NOOB  (4, 1, 1, 1, 1, false, "Not too hard."),
	NORM  (5, 2, 1, 1, 2, false, "Normal difficulty."),
	HARD  (6, 3, 2, 2, 3,  true, "Super violent."),
	NUTS  (7, 5, 2, 2, 4,  true, "Insane horror!");
	
	
	public final int spawners;
	public final int promote;
	public final int maxlev;
	public final int nodelev;
	public final int bosslev;
	public final boolean entrancemobs;
	public final String label;
	
	
	private Difficulty(int spawners, int promote, int mobmax, int nodelev,
			int bosslev, boolean entrancemobs, String label) {
		this.spawners = spawners;
		this.promote = promote;
		this.maxlev = mobmax;
		this.nodelev = nodelev;
		this.bosslev = bosslev;
		this.entrancemobs = entrancemobs;
		this.label = label;
	}
	
	
	public boolean addmob(Random random) {
		return(random.nextInt(10) < spawners);
	}
	
	
	public boolean multimob(Random random) {
		return(random.nextInt(20) < (spawners + promote));
	}
	
	
	public boolean promote(Random random) {
		return(random.nextInt(10) < promote);
	}
	
	
	public int moblevel(Random random) {
		int lev = 0;
		boolean pr = true;
		while(pr && (lev < maxlev)) {
			if(random.nextInt(10) < promote) {
				lev++;
			} else pr = false;
		}
		return lev;
	}
	
	
	public int nodelevel(Random random) {
		int lev = nodelev;
		boolean pr = true;
		while(pr && (lev < bosslev)) {
			if(random.nextInt(10) < promote) {
				lev++;
			} else pr = false;
		}
		return lev;
	}	
}


