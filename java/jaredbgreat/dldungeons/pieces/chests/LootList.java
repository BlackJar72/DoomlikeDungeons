package jaredbgreat.dldungeons.pieces.chests;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/		


import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import static jaredbgreat.dldungeons.pieces.chests.LootItem.*;


public class LootList extends ArrayList<LootItem>{
	ArrayList<LootItem> dummy = new ArrayList<LootItem>();
	
	public void add(Item item, int min, int max, int prob) {
		add(new LootItem(item, min, max));
	}
	
	
	public void add(Block item, int min, int max, int prob) {
		add(new LootItem(item, min, max));
	}
	
	
	public LootItem getLoot(Random random) {
		LootItem out;
		if(isEmpty()) return null;
		// Done with removal now to somewhat increase randomness
		if(dummy.isEmpty() || random.nextInt(size()) > dummy.size()) {
			dummy.clear();
			dummy.addAll(this);
		}
		int which = random.nextInt(dummy.size());
		out = dummy.get(which);
		dummy.remove(which);
		return out;
	}
	
	
	/*----------------------------------*/
	/*       Deafult Loots Below        */
	/*----------------------------------*/
	
	static LootList gear1 = new LootList();
	static LootList gear2 = new LootList();
	static LootList gear3 = new LootList();
	static LootList gear4 = new LootList();
	static LootList gear5 = new LootList();
	static LootList gear6 = new LootList();
	static LootList gear7 = new LootList();
	
	static LootList heal1 = new LootList();
	static LootList heal2 = new LootList();
	static LootList heal3 = new LootList();
	static LootList heal4 = new LootList();
	static LootList heal5 = new LootList();
	static LootList heal6 = new LootList();
	static LootList heal7 = new LootList();
	
	static LootList loot1 = new LootList();
	static LootList loot2 = new LootList();
	static LootList loot3 = new LootList();
	static LootList loot4 = new LootList();
	static LootList loot5 = new LootList();
	static LootList loot6 = new LootList();
	static LootList loot7 = new LootList();
	
	static LootList discs = new LootList();
	static LootList special = new LootList();
	
	
	public static void addDefaultLoot() {
		gear1.add(stoneSword);	
		gear1.add(leatherHat);
		gear1.add(leatherPants);
		gear1.add(leatherBoots);
		gear1.add(leatherChest);
		gear1.add(fewArrows);
		gear1.add(fewTorches);							

		gear2.add(stoneSword);
		gear2.add(ironSword);	
		gear2.add(bow);
		gear2.add(leatherHat);
		gear2.add(leatherPants);
		gear2.add(leatherBoots);
		gear2.add(leatherChest);
		gear2.add(ironHat);
		gear2.add(ironPants);
		gear2.add(ironBoots);
		gear2.add(ironChest);
		gear2.add(fewArrows);
		gear2.add(fewTorches);	
		
		gear3.add(ironSword);	
		gear3.add(bow);
		gear3.add(ironHat);
		gear3.add(ironPants);
		gear3.add(ironBoots);
		gear3.add(ironChest);
		gear3.add(fewArrows);
		gear3.add(fewTorches);
		gear3.add(someArrows);
		gear3.add(manyTorches);
		
		gear4.add(ironSword);
		gear4.add(diamondSword);	
		gear4.add(bow);
		gear4.add(ironHat);
		gear4.add(ironPants);
		gear4.add(ironBoots);
		gear4.add(ironChest);
		gear4.add(diamondHat);
		gear4.add(diamondPants);
		gear4.add(diamondBoots);
		gear4.add(diamondChest);
		gear4.add(fewArrows);
		gear4.add(fewTorches);
		gear4.add(someArrows);
		gear4.add(manyTorches);
		
		gear5.add(diamondSword);
		gear5.add(diamondHat);
		gear5.add(diamondPants);
		gear5.add(diamondBoots);
		gear5.add(diamondChest);
		gear5.add(fewArrows);
		gear5.add(fewTorches);
		gear5.add(someArrows);
		gear5.add(manyTorches);
		
		gear6.add(diamondSword);
		gear6.add(diamondHat);
		gear6.add(diamondPants);
		gear6.add(diamondBoots);
		gear6.add(diamondChest);
		gear6.add(manyArrows);
		gear6.add(someArrows);
		gear6.add(manyTorches);
		
		gear7.add(diamondSword);
		gear7.add(diamondHat);
		gear7.add(diamondPants);
		gear7.add(diamondBoots);
		gear7.add(diamondChest);
		gear7.add(manyArrows);
		gear7.add(manyTorches);
		
		heal1.add(someBread);
		
		heal2.add(someBread);
		heal2.add(someApples);
		heal2.add(someChicken);
		
		heal3.add(moreBread);
		heal3.add(someApples);
		heal2.add(someChicken);
		heal3.add(someSteak);
		heal3.add(goldApple);
		
		heal4.add(moreBread);
		heal4.add(someApples);
		heal4.add(moreApples);
		heal4.add(someChicken);
		heal4.add(moreChicken);
		heal4.add(someSteak);
		heal4.add(goldApple);
		
		heal5.add(moreBread);
		heal5.add(somePie);
		heal5.add(moreApples);
		heal5.add(someChicken);
		heal5.add(moreChicken);
		heal5.add(someSteak);
		heal5.add(moreSteak);
		heal5.add(goldApple);
		heal5.add(goldApples);
		
		heal6.add(somePie);
		heal6.add(moreApples);
		heal6.add(morePie);
		heal6.add(moreSteak);
		heal6.add(goldApple);
		heal6.add(goldApples);
		
		heal7.add(moreApples);
		heal7.add(moreSteak);
		heal7.add(goldApple);
		heal7.add(goldApples);
		
		loot1.add(someIron);
		loot1.add(oneGold);
		loot1.add(book);
		loot1.add(someApples);
		loot1.add(ironSword);
		
		loot2.add(book);
		loot2.add(someBooks);
		loot2.add(someIron);
		loot2.add(oneGold);
		loot2.add(someGold);
		
		loot3.add(someBooks);
		loot3.add(someGold);
		loot3.add(oneDiamond);
		loot3.add(moreIron);
		loot3.add(oneEmerald);
		
		loot4.add(goldApple);
		loot4.add(blazeRod);
		loot4.add(moreBooks);
		loot4.add(enderpearls);
		loot4.add(moreIron);
		loot4.add(someGold);
		loot4.add(oneDiamond);
		loot4.add(diamonds);
		loot4.add(oneEmerald);
		loot4.add(emeralds);
		
		loot5.add(moreBooks);
		loot5.add(blazeRod);
		loot5.add(goldApple);
		loot5.add(eyeOfEnder);
		loot5.add(enderpearls);
		loot5.add(moreIron);
		loot5.add(moreGold);
		loot5.add(diamonds);
		loot5.add(oneEmerald);
		loot5.add(emeralds);
		
		loot6.add(moreBooks);
		loot6.add(eyeOfEnder);
		loot6.add(enderpearls);
		loot6.add(goldApples);
		loot6.add(moreIron);
		loot6.add(moreGold);
		loot6.add(diamonds);
		loot6.add(emeralds);
		
		loot7.add(moreGold);
		loot7.add(diamonds);
		loot7.add(emeralds);
		loot7.add(eyeOfEnder);
		loot7.add(goldApple);
		
		special.add(discBlocks);
		special.add(discChirp);
		special.add(discFar);
		special.add(discMall);
		special.add(discMellohi);
		special.add(discStrad);
		special.add(discWard);
		special.add(disc11);
		special.add(discWait);
		special.add(netherstar);
		special.add(goldApples);
		
		addDiscs();
		TreasureChest.initSlots();
	}
	
	
	public static void addDiscs() {		
		discs.add(disc13);
		discs.add(discCat);
		discs.add(discBlocks);
		discs.add(discChirp);
		discs.add(discFar);
		discs.add(discMall);
		discs.add(discMellohi);
		discs.add(discStrad);
		discs.add(discWard);
		discs.add(disc11);
		discs.add(discWait);		
	}
	
	
	public static void addItem(LootItem item, String type, int level) {
		if(type.equals("gear")) {
			switch(level) {
				case 0:
				case 1:
					gear1.add(item);
					break;
				case 2:
					gear2.add(item);
					break;
				case 3:
					gear3.add(item);
					break;
				case 4:
					gear4.add(item);
					break;
				case 5:
					gear5.add(item);
					break;
				case 6:
					gear6.add(item);
					break;
				case 7:
					gear7.add(item);
					break;
				default:
					special.add(item);
					break;	
			}
		} else if(type.equals("heal")) {
				switch(level) {
				case 0:
				case 1:
					heal1.add(item);
					break;
				case 2:
					heal2.add(item);
					break;
				case 3:
					heal3.add(item);
					break;
				case 4:
					heal4.add(item);
					break;
				case 5:
					heal5.add(item);
					break;
				case 6:
					heal6.add(item);
					break;
				case 7:
					heal7.add(item);
					break;	
				default: 
					special.add(item);
					break;	
			}
		} else {
				switch(level) {
				case 0:
				case 1:
					loot1.add(item);
					break;
				case 2:
					loot2.add(item);
					break;
				case 3:
					loot3.add(item);
					break;
				case 4:
					loot4.add(item);
					break;
				case 5:
					loot5.add(item);
					break;
				case 6:
					loot6.add(item);
					break;
				case 7:
					loot7.add(item);
					break;
				default:
					special.add(item);
					break;	
			}
		}
	}
	
	
	
}
