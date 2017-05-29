package jaredbgreat.dldungeons.planner;

import jaredbgreat.dldungeons.themes.BiomeSets;
import jaredbgreat.dldungeons.themes.Theme;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class Dungeon {
	Level[] levels;
	
	public Dungeon(int n, Random random, Biome biome, World world, 
			int chunkX, int chunkZ) throws Throwable {
		levels = new Level[n];;
		if(n == 1) {
			levels[0] = new Level(random, biome, world, 
			chunkX, chunkZ);
		} else if (random.nextBoolean()) {
			Theme theme = BiomeSets.getTheme(biome, random);
			for(int i = 0; i < n; i++) {
				levels[i] = new Level(1 - i, random, theme, world, 
						chunkX, chunkZ, 10 + (i * 20));
			}
		} else {
			for(int i = 0; i < n; i++) {
				Theme theme = BiomeSets.getTheme(biome, random);
				levels[i] = new Level(1 - i, random, theme, world, 
						chunkX, chunkZ, 10 + (i * 20));
			}
		}
	}
	
	
	public Level[] getLevels() {
		return levels;
	}
	
	
	public Level getLevel(int n) {
		return levels[n];
	}
	
	
	public int getNumLevels() {
		return levels.length;
	}

}