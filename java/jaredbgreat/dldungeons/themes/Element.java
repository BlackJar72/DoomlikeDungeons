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

public class Element implements Autoselecting {
	
	private Degrees none, few, some, plenty, heaps, all;
	private int prob1, prob2, prob3, prob4, prob5, prob6;
	private int probScale;
	
	
	public Element (int prob1,
					int prob2,
					int prob3,
					int prob4,
					int prob5,
					int prob6) {
		this.none 	= Degrees.NONE;
		this.prob1 	= prob1;
		this.few 	= Degrees.FEW;
		this.prob2 	= prob2;
		this.some 	= Degrees.SOME;
		this.prob3 	= prob3;
		this.plenty = Degrees.PLENTY;
		this.prob4 	= prob4;
		this.heaps 	= Degrees.HEAPS;
		this.prob5 	= prob5;
		this.all 	= Degrees.ALL;
		this.prob6 	= prob6;
		probScale = prob1 + prob2 + prob3 + prob4 + prob5 + prob6;
	}
	
	
	public Degrees select(Random random) {
		int roll = random.nextInt(probScale);
		if(roll < prob1) return Degrees.NONE;
		else roll -= prob1;
		if(roll < prob2) return Degrees.FEW;
		else roll -= prob2;
		if(roll < prob3) return Degrees.SOME;
		else roll -= prob3;
		if(roll < prob4) return Degrees.PLENTY;
		else roll -= prob4;
		if(roll < prob5) return Degrees.HEAPS;
		else roll -= prob5;
		if(roll < prob6) return Degrees.ALL;
		else return Degrees.NONE;
	}
	
	
	public boolean never() {
		return    ((prob1 !=0) 
				&& (prob2 ==0) 
				&& (prob3 ==0) 
				&& (prob4 ==0) 
				&& (prob5 ==0) 
				&& (prob6 ==0));     
	}
	

}
