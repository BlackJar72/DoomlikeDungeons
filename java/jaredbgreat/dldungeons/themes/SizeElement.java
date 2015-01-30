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

public class SizeElement implements Autoselecting {
	
	private Sizes tiny, small, medium, large, huge;
	private int prob1, prob2, prob3, prob4, prob5;
	private int probScale;
	
	
	public SizeElement (int prob1,
						int prob2,
						int prob3,
						int prob4,
						int prob5) {
		this.tiny 	= Sizes.TINY;
		this.prob1 	= prob1;
		this.small 	= Sizes.SMALL;
		this.prob2 	= prob2;
		this.medium = Sizes.MEDIUM;
		this.prob3 	= prob3;
		this.large  = Sizes.LARGE;
		this.prob4 	= prob4;
		this.huge 	= Sizes.HUGE;
		this.prob5 	= prob5;
		probScale = prob1 + prob2 + prob3 + prob4 + prob5;
	}
	
	public Sizes select(Random random) {
		int roll = random.nextInt(probScale);
		if(roll < prob1) return Sizes.TINY;
		else roll -= prob1;
		if(roll < prob2) return Sizes.SMALL;
		else roll -= prob2;
		if(roll < prob3) return Sizes.MEDIUM;
		else roll -= prob3;
		if(roll < prob4) return Sizes.LARGE;
		else roll -= prob4;
		if(roll < prob5) return Sizes.HUGE;
		else return Sizes.MEDIUM;
	}
}
