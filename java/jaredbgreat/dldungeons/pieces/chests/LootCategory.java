package jaredbgreat.dldungeons.pieces.chests;


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


import static jaredbgreat.dldungeons.pieces.chests.LootList.*;


public class LootCategory {
	
	public static final int LEVELS = 7; 	
	public LootList[] levels = new LootList[LEVELS];
	
	
	public LootCategory(LootList[] loots) {
		levels = loots;
	};
	
	
	public static LootCategory gear = new LootCategory(new 
			LootList[]{gear1, gear2, gear3, gear4, gear5, gear6, gear7});
	
	
	public static LootCategory heal = new LootCategory(new 
			LootList[]{heal1, heal2, heal3, heal4, heal5, heal6, heal7});
	
	
	public static LootCategory loot = new LootCategory(new 
			LootList[]{loot1, loot2, loot3, loot4, loot5, loot6, loot7});
	
}
