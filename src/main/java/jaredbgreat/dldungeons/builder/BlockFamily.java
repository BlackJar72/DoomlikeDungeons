package jaredbgreat.dldungeons.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import jaredbgreat.dldungeons.pieces.chests.LootCategory;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.ChunkFeatures;
import jaredbgreat.dldungeons.planner.mapping.MapMatrix;
import jaredbgreat.dldungeons.themes.Sizes;
import jaredbgreat.dldungeons.themes.Theme;
import jaredbgreat.dldungeons.util.cache.Coords;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class BlockFamily implements IBlockPlacer {
	private static final Map<String, BlockFamily> FAMILIES = new ConcurrentHashMap<>();


	// FIXME! HELP!!! I need to reload the RegisteredBlocks first to preserve their order, but this to have the BlockFamilies to register!!!
	public static final Codec<BlockFamily> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(
					Codec.STRING.fieldOf("name").forGetter(family -> family.name),
					Codec.STRING.listOf().fieldOf("blocks").forGetter((family
							-> BlockSaveHandler.stringsToList(family.blocks)))
			)
			.apply(instance, (name, blocks) -> {
				List<String> names = blocks;
				final BlockFamily family = new BlockFamily(name, List.of(new IBlockPlacer[names.size()]));

				for(int i = 0; i < family.blocks.length; i++) {
					family.blocks[i] = RegisteredBlock.get(RegisteredBlock.add(names.get(i)));
				}

				return family;
			}));



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
	

	@Override
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


	public static List<BlockFamily> getFamiliesAsList() {
		ArrayList<BlockFamily> output = new ArrayList<>(FAMILIES.size());
        output.addAll(FAMILIES.values());
		return output;
	}

}
