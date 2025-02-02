package jaredbgreat.dldungeons.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class BlockFamily implements IBlockPlacer {
	private static final Map<String, BlockFamily> FAMILIES = new ConcurrentHashMap<>();
	
	public final String name;
	private final IBlockPlacer[] blocks;
	private	static RandomSource random;
	
	
	private BlockFamily(String name, List<IBlockPlacer> theBlocks) {
		this.name = name;
		blocks = theBlocks.toArray(new IBlockPlacer[theBlocks.size()]);
		random = RandomSource.create();
	}
	
	
	public static void setRadnom(RandomSource r) {
		random = RandomSource.create(r.nextLong());
	}
	
	
	public String getName() {
		return name;
	}

	
	@Override
	public void place(WorldGenLevel world, BlockPos pos) {
		blocks[random.nextInt(blocks.length)].place(world, pos);
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
		JsonObject data = JsonParser.parseString(json).getAsJsonObject();
		String name;
		List<IBlockPlacer> blocks = new LinkedList<>(); 
		if(data.has("name")) {
			name = "$" + data.get("name").getAsString();
		} else {
			throw new RuntimeException("tried to load BlockFamily with no Name!");
		}
		if(data.has("blocks")) {
			data.get("blocks").getAsJsonArray().forEach(new Consumer<JsonElement>(){
				@Override
				public void accept(JsonElement t) {
					int id = RegisteredBlock.add(t.getAsString());
					blocks.add(RegisteredBlock.getPlacer(id));
				}});;
		} else {
			throw new RuntimeException("tried to load BlockFamily with no blocks!");
		}
		if(blocks.isEmpty()) {
			throw new RuntimeException("tried to load BlockFamily empty block list!");
		}		
		BlockFamily out = new BlockFamily(name, blocks); 
		FAMILIES.put(name, out);
		return out;
	}


	public boolean isEmpty() {
		return ((blocks == null) || blocks.length < 1);
	}
	
	
	public static BlockFamily getBlockFamily(String name) {
		return FAMILIES.get(name);
	}


	@Override
	public Object getContents() {
		return blocks;
	}

}
