package jaredbgreat.dldungeons.builder;

import jaredbgreat.dldungeons.Info;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.world.World;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class BlockFamily implements IBlockPlacer {
	private static final Map<String, BlockFamily> FAMILIES = new HashMap<>();	
	
	private final String name;
	private final IBlockPlacer[] blocks;
	private	static Random random;	
	
	
	private BlockFamily(String name, List<IBlockPlacer> theBlocks) {
		this.name = name;
		blocks = theBlocks.toArray(new IBlockPlacer[theBlocks.size()]);
		random = new Random();
	}
	
	
	public static void setRadnom(Random r) {
		random = r;
	}
	
	
	public String getName() {
		return name;
	}

	
	@Override
	public void placeNoMeta(World world, int x, int y, int z) {
		blocks[random.nextInt(blocks.length)].placeNoMeta(world, x, y, z);
	}

	
	@Override
	public void place(World world, int x, int y, int z) {
		blocks[random.nextInt(blocks.length)].place(world, x, y, z);
	}
	
	
	/**
	 * Returns true if the other object is a DBlock the holds the same block with 
	 * the same meta-data. 
	 */
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof BlockFamily)) return false; 
		return (name.equals(((BlockFamily)other).name));
	}
	
	
	/**
	 * Returns a hash code derived from the block id and meta data; it will only produce 
	 * the same hash code if these are both equal, that is, equal hash codes implies equals() 
	 * is true. 
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	
	public static BlockFamily makeBlockFamily(String json) {
		JsonParser parser = new JsonParser();
		JsonObject data = parser.parse(json).getAsJsonObject();		
		String name;
		List<IBlockPlacer> blocks = new LinkedList<>(); 
		if(data.has("name")) {
			name = "$" + data.get("name").getAsString();
		} else {
			throw new RuntimeException(Info.OLD_ID + " tried to load BlockFamily with no Name!");
		}
		if(data.has("blocks")) {
			data.get("blocks").getAsJsonArray().forEach(new Consumer<JsonElement>(){
				@Override
				public void accept(JsonElement t) {
					int id = RegisteredBlock.add(t.getAsString());
					blocks.add(RegisteredBlock.getPlacer(id));
				}});;
		} else {
			throw new RuntimeException(Info.OLD_ID + " tried to load BlockFamily with no blocks!");			
		}
		if(blocks.isEmpty()) {
			throw new RuntimeException(Info.OLD_ID + " tried to load BlockFamily empty block list!");			
		}		
		BlockFamily out = new BlockFamily(name, blocks); 
		FAMILIES.put(name, out);
		return out;
	}
	
	
	public static BlockFamily getBlockFamily(String name) {
		return FAMILIES.get(name);
	}

}
