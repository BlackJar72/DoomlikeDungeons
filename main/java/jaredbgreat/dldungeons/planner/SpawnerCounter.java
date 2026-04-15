package jaredbgreat.dldungeons.planner;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import jaredbgreat.dldungeons.ConfigHandler;
import jaredbgreat.dldungeons.pieces.Spawner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class SpawnerCounter {
	final ArrayList<Spawner> list;
	int dungeonSize;
	
	public SpawnerCounter() {
		list = new ArrayList<>();
		dungeonSize = 0;
	}
	
	
	public void addRoom(int size) {
		dungeonSize += size;
	}
	
	
	public void addSpawner(Spawner s) {
		list.add(s);
	}
	
	
	public void fixSpawners(Dungeon dungeon, Random random) {
		int targetNum = dungeonSize / ConfigHandler.difficulty.blocksPerSpawner;
		targetNum = (int) (((float)targetNum *
				(1.0f + (Math.max(-2f, Math.min(2f, (random.nextGaussian()))) / 10.0f))));
		int existing = list.size();
		if(existing <= targetNum) {
			return;
		}
		Collections.shuffle(list, new Random(random.nextLong()));
		for(int i = targetNum; i < existing; i++) {
			Spawner s = list.get(i);
			dungeon.rooms.get(s.getRoom()).spawners.remove(s);
		}
	}
}
