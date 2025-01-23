package jaredbgreat.dldungeons.pieces.chests;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */	

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LootHandler {
	private static final LootHandler handler = new LootHandler();;
	private final Map<String, LootCategory> categories;
	
	private LootHandler() {
		categories = new ConcurrentHashMap<>();
	}
	
	
	public static LootHandler getLootHandler() {
		return handler;
	}
	
	
	/**
	 * Creates and registers a new LootCategory using name as an identifier.
	 * 
	 * @param name
	 * @return
	 */
	public LootCategory createCategory(String name) {
		LootListSet listset = new LootListSet();
		LootCategory category = new LootCategory(listset, name);
		categories.put(name, category);
		return category;
	}
	




	public LootCategory getCategory(String name) {
		return categories.get(name);
	}

}
