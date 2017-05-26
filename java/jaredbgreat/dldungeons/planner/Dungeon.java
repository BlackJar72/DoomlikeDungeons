package jaredbgreat.dldungeons.planner;

import jaredbgreat.dldungeons.themes.BiomeSets;
import jaredbgreat.dldungeons.themes.Theme;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Dungeon {
	private int numLevels;
	private Level[] levels;
	
	
	public Dungeon(int n, Random random, Biome biome, World world, 
			int chunkX, int chunkZ) throws Throwable {		
		numLevels = n;
		Theme theme = BiomeSets.getTheme(biome, random);
		if(theme == null) return;
		levels = new Level[n];
	}

}
