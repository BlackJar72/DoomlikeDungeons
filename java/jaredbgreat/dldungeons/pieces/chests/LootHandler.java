package jaredbgreat.dldungeons.pieces.chests;

import java.util.HashMap;
import java.util.Map;

public class LootHandler {
	private static LootHandler handler;
	private final Map<String, LootCategory> categories;
	
	private LootHandler() {
		categories = new HashMap<>();
	}
	
	
	public static LootHandler getLootHandler() {
		if(handler == null) {
			handler = new LootHandler();
		}
		return handler;
	}
	
	
	/**
	 * Creates and registers a new LootCategory using name as an identifier.
	 * 
	 * @param name
	 * @return
	 */
	public LootCategory createCategory(String name) {
		if(categories.containsKey(name)) {
			System.err.println("[DLDUNGEONS] Warning: Trying to create Loot Category" 
					+ name + " more than once!");
			return categories.get(name);
		}
		LootListSet listset = new LootListSet();
		LootCategory category = new LootCategory(listset);
		categories.put(name, category);
		return category;
	}
	
	
	public LootCategory getCategory(String name) {
		return categories.get(name);
	}

}
