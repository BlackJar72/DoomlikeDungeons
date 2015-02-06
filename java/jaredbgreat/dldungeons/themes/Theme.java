package jaredbgreat.dldungeons.themes;


/* 
 * This mod is the creation and copyright (c) 2015 
 * of Jared Blackburn (JaredBGreat).
 * 
 * It is licensed under the creative commons 4.0 attribution license: * 
 * https://creativecommons.org/licenses/by/4.0/legalcode
*/	



import jaredbgreat.dldungeons.builder.DBlock;

import java.util.ArrayList;
import java.util.EnumSet;

import net.minecraftforge.common.BiomeDictionary.Type;

public class Theme {
	
	public String name;
	public float version = 0f;
	
	public static Theme classic;
	public static Theme nether;
	public static Theme end;
	
	public EnumSet<Type> biomes = EnumSet.noneOf(Type.class);
	public EnumSet<Type> notIn  = EnumSet.noneOf(Type.class);
	
	public EnumSet<ThemeType> type  = EnumSet.noneOf(ThemeType.class);
	public EnumSet<ThemeFlags> flags = EnumSet.noneOf(ThemeFlags.class);
	
	public int minY;
	public int maxY;
	public boolean buildFoundation;
	
	public SizeElement sizes;
	
	public Element outside;		// Roofless, wall-less rooms (intend for outdoors in surface dungeons)
	public Element liquids;		// Quantity of water / lava pools
	public Element subrooms;	// Rooms budding of from the main one
	public Element islands;		// Rooms inside rooms	
	public Element pillars;		// Uh, pillars / columns, duh!
	public Element fences;		// Uh, fences, duh!
	public Element symmetry;	// How symmetrical rooms are (technically, chance of axis mirroring)
	public Element variability;	// Inconsistency, that chance of using a different style in some place
	public Element degeneracy;	// Chance of walls / ceilings not spawning over air blocks (idea taken from Greymerk)
	public Element complexity;	// Basically how many shape primitives to add
	public Element verticle;	// How many height change and how much height change
	public Element entrances;	// Ways in and out / number of entrance/exit nodes (others are destination / treasure nodes)
	
	public int[] walls;
	public int[] floors;
	public int[] ceilings;
	public int[] fencing;
	public int[] liquid;
	public int[] pillarBlock;
	
	public ArrayList<String> commonMobs;
	public ArrayList<String> hardMobs;
	public ArrayList<String> bruteMobs;
	public ArrayList<String> eliteMobs;
	public ArrayList<String> bossMobs;
	public ArrayList<String>[] allMobs;
	
	
	public Theme() {
		minY = 10;
		maxY = 50;
		buildFoundation = false;
		sizes = new SizeElement(2, 5, 10, 5, 1);	
		outside = new Element(25,  0,  0,  0,  0,  0);
		liquids = new Element(1,  30, 50, 20, 10,  0);
		subrooms = new Element(0, 5, 25, 60, 25,  5);
		islands = new Element(5,  50, 10, 50, 20,  0);	
		pillars = new Element(5,  30, 60, 40, 20,  0);
		symmetry = new Element(5,  15, 30, 75, 25,  0);
		variability = new Element(5,  10, 25, 75, 50, 25);
		degeneracy = new Element(50,  5, 15, 50, 10,  0);
		complexity = new Element(5,  10, 25, 75, 15,  0);
		verticle = new Element(5,  10, 25, 20, 10,  0);
		entrances = new Element(2,   5, 25, 50, 15,  3);
		fences = new Element(15, 25, 55, 15,  0, 0);
		walls = makeBlockList(new String[]{"Minecraft:stonebrick", "Minecraft:cobblestone", "Minecraft:sandstone", "Minecraft:brick_block"});
		floors = makeBlockList(new String[]{"Minecraft:stonebrick", "Minecraft:cobblestone", "Minecraft:dirt", "Minecraft:stone"});
		ceilings = makeBlockList(new String[]{"Minecraft:stonebrick", "Minecraft:cobblestone", "Minecraft:planks", "Minecraft:double_stone_slab"});
		fencing = makeBlockList(new String[]{"Minecraft:cobblestone_wall", "Minecraft:fence"});
		liquid = makeBlockList(new String[]{"Minecraft:water", "Minecraft:lava"});
		pillarBlock = makeBlockList(new String[]{"Minecraft:stonebrick", "Minecraft:cobblestone", "Minecraft:sandstone", "Minecraft:planks", "Minecraft:double_stone_slab"});
		String[] mobs  = new String[]{"Zombie", "Skeleton", "Spider"};
		String[] hard  = new String[]{"Creeper", "Enderman", "CaveSpider"};
		String[] brute = new String[]{"Witch"};
		String[] elite = new String[]{};
		String[] boss = new String[]{};
		commonMobs = new ArrayList<String>(mobs.length);
		hardMobs = new ArrayList<String>(hard.length);
		bruteMobs = new ArrayList<String>(brute.length);
		eliteMobs = new ArrayList<String>(0);
		bossMobs = new ArrayList<String>(0);
		for(int i = 0; i < mobs.length; i++) {
			this.commonMobs.add(mobs[i]);
		} for(int i = 0; i < hard.length; i++) {
			this.hardMobs.add(hard[i]);
		}
		bruteMobs.add("Witch");
		fixMobs();
	}
	
	
	/*--------------------------------------*
	 * 			ADD THEMES                   *
	 ---------------------------------------*/
	
	
	public static void addDefaultTheme() {
		String[] mobs;
		String[] hard;
		
		classic = new Theme();
		classic.type.add(ThemeType.DUNGEON);
		classic.type.add(ThemeType.URBAN);
		classic.type.add(ThemeType.TECH);
		
		nether = new Theme();
		nether.minY = 32;
		nether.maxY = 64;
		nether.buildFoundation = true;
		nether.walls = new int[]{112};
		nether.floors = new int[]{112};
		nether.ceilings = new int[]{112};
		nether.liquid = new int[]{11};
		nether.pillarBlock = new int[]{112, 49, 112, 49, 155};		
		nether.walls = makeBlockList(new String[]{"Minecraft:nether_brick"});
		nether.floors = makeBlockList(new String[]{"Minecraft:nether_brick"});
		nether.ceilings = makeBlockList(new String[]{"Minecraft:nether_brick"});
		nether.liquid = makeBlockList(new String[]{"Minecraft:lava"});
		nether.pillarBlock = makeBlockList(new String[]{"Minecraft:nether_brick", "Minecraft:obsidian", "Minecraft:nether_brick", "Minecraft:obsidian", "Minecraft:quartz_block"});
		nether.fencing = makeBlockList(new String[]{"Minecraft:nether_brick_fence"});
		mobs = new String[]{"Blaze", "LavaSlime", "PigZombie"};
		hard = new String[]{"Blaze", "Skeleton"};
		nether.commonMobs = new ArrayList<String>(mobs.length);
		nether.hardMobs = new ArrayList<String>(hard.length);
		for(int i = 0; i < mobs.length; i++) {
			nether.commonMobs.add(mobs[i]);
		} for(int i = 0; i < hard.length; i++) {
			nether.hardMobs.add(hard[i]);
		}
		nether.bossMobs.add("WitherBoss");
		nether.type.add(ThemeType.NETHER);
		nether.type.add(ThemeType.TECH);
		nether.fixMobs();
		
		end = new Theme();
		end.minY = 30;
		end.maxY = 60;
		end.walls = new int[]{121};
		end.floors = new int[]{121};
		end.ceilings = new int[]{121};
		end.liquid = new int[]{0};
		end.pillarBlock = new int[]{121, 49, 49, 49, 89};
		end.liquids = new Element(5, 0, 0, 0, 0, 0);
		end.verticle = new Element(0, 0, 5, 55, 15, 0);
		end.outside = new Element(5, 10, 15, 5, 0, 0);
		end.pillars = new Element(0, 5, 15, 10, 5, 0);
		end.degeneracy = new Element(10, 0, 0, 0, 0, 0);
		end.entrances = new Element(10, 0, 0, 0, 0, 0);
		end.commonMobs = new ArrayList<String>(mobs.length);
		end.hardMobs = new ArrayList<String>(hard.length);
		end.bruteMobs = new ArrayList<String>(hard.length);
		end.eliteMobs = new ArrayList<String>(hard.length);
		end.commonMobs.add("Enderman");
		end.hardMobs.add("Enderman");
		end.bruteMobs.add("Enderman");
		end.bossMobs.add("WitherBoss");
		end.type.add(ThemeType.END);
		end.type.add(ThemeType.TECH);
		end.fixMobs();
	}
	
	
	public void addMob(String mob, int level) {
		switch(level) {
		case 0:
			if(!commonMobs.contains(mob)) commonMobs.add(mob);
			return;
		case 1:
			if(!hardMobs.contains(mob)) hardMobs.add(mob);
			return;
		case 2:
			if(!bruteMobs.contains(mob)) bruteMobs.add(mob);
			return;
		case 3:
			if(!eliteMobs.contains(mob)) eliteMobs.add(mob);
			return;
		default:
			System.err.println("[DLDUNGEONS] Failed to add mob " + mob + " to theme " 
					+ name + ", illegal difficulty level " + level + " (use 0 to 3).");
			return;
		}
	}
	
	
	public void removeMob(String mob, int level) {
		switch(level) {
		case 0:
			while(commonMobs.contains(mob)) commonMobs.remove(mob);
			return;
		case 1:
			while(hardMobs.contains(mob)) hardMobs.remove(mob);
			return;
		case 2:
			while(bruteMobs.contains(mob)) bruteMobs.remove(mob);
			return;
		case 3:
			while(eliteMobs.contains(mob)) eliteMobs.remove(mob);
			return;
		default:
			System.err.println("[DLDUNGEONS] Failed to remove mob " + mob + " to theme " 
					+ name + ", illegal difficulty level " + level + " (use 0 to 3).");
			return;
		}
	}
	
	
	public void fixMobs() {
		allMobs = new ArrayList[5];
		allMobs[0] = commonMobs;
		allMobs[1] = hardMobs;
		allMobs[2] = bruteMobs;
		allMobs[3] = eliteMobs;
		allMobs[4] = bossMobs;
	}
	
	
	public void biomeRegister() {
		BiomeLists.registerTheme(this);
		BiomeLists.removeTheme(this);
	}
	
	
	@Override
	public String toString() {
		if(name == null) return "null";
		return name;
	}
	
	
	private static int makeDBlock(String block) {
		return DBlock.add(block);
	}
	
	
	private static int[] makeBlockList(String[] in) {
		int[] out = new int[in.length];
		for(int i = 0; i < in.length; i++) 
			out[i] = makeDBlock(in[i]);
		return out;
	}
	
}
