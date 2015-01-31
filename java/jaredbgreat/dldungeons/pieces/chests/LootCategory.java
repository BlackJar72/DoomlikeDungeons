package jaredbgreat.dldungeons.pieces.chests;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
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
