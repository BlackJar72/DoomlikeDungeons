package jaredbgreat.dldungeons.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class BlockFamily implements IBlockPlacer {
	private static final Map<String, BlockFamily> FAMILIES = new ConcurrentHashMap<>();


	// FIXME! HELP!!! I need to reload the RegisteredBlocks first to preserve their order, but this to have the BlockFamilies to register!!!
	// Plan: Load them
	/**
	 * For converting to and from NBT; the system for reading JSON is already in place.
	 */
	public static final Codec<BlockFamily> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(
					Codec.STRING.fieldOf("name").forGetter(family -> family.name),
					Codec.STRING.listOf().fieldOf("blocks").forGetter((family
							-> BlockSaveHandler.stringsToList(family.blocks)))
			)
			.apply(instance, (name, blocks) -> {
				List<String> blockNames = blocks;
				BlockFamily family = FAMILIES.get(name);
				if(family == null) { // This really should not happen, but handling it just in case
					family = new BlockFamily(name, List.of(new IBlockPlacer[blockNames.size()]));
					for (int i = 0; i < family.blocks.length; i++) {
						family.blocks[i] = RegisteredBlock.get(RegisteredBlock.add(blockNames.get(i)));
					}
				} else {
					IBlockPlacer[] familyBlocks = new IBlockPlacer[blockNames.size()];
					for (int i = 0; i < family.blocks.length; i++) {
						familyBlocks[i] = RegisteredBlock.get(RegisteredBlock.add(blockNames.get(i)));
					}
					family.blocks = familyBlocks;
				}

				return family;
			}));



	public final String name;
	private volatile IBlockPlacer[] blocks;
	private	static RandomSource random;


	/**
	 * This constructor is used to create new block families from the JSON files from data packs.
	 *
	 * @param name
	 * @param theBlocks
	 */
	private BlockFamily(String name, List<IBlockPlacer> theBlocks) {
		this.name = name;
		blocks = theBlocks.toArray(new IBlockPlacer[theBlocks.size()]);
		random = RandomSource.create();
	}


	/**
	 * This is used to create empty block families that during loading from a save;
	 * these can then be populated with blocks when reading the from a data pack.
	 *
	 * @param name
	 */
	private BlockFamily(String name) {
		this.name = name;
		blocks = new IBlockPlacer[0];
		random = RandomSource.create();
	}


	/**
	 * Use to set the blocks for a block family that has been loaded from a save.
	 *
	 * @param theBlocks
	 */
	private void setBlocks(List<IBlockPlacer> theBlocks) {
		blocks = theBlocks.toArray(new IBlockPlacer[theBlocks.size()]);
	}


	/**
	 * Use to set the blocks for a block family that has been loaded from a save.
	 *
	 * @param theBlocks
	 */
	private void setBlocks(IBlockPlacer[] theBlocks) {
		blocks = theBlocks;
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
		BlockFamily out = FAMILIES.get(name);
		if(out == null) {
			out = new BlockFamily(name, blocks);
			FAMILIES.put(name, out);
		} else {
			out.setBlocks(blocks);
		}
		return out;
	}


	public static BlockFamily loadBlockFamily(String name) {
		BlockFamily family = FAMILIES.get(name);
		if(family == null) {
			family = new BlockFamily(name);
			FAMILIES.put(name, family);
		}
		return family;
	}


	public boolean isEmpty() {
		return ((blocks == null) || blocks.length < 1);
	}


	public static void addFromLoad(BlockFamily family) {
		BlockFamily existing = FAMILIES.get(family.name);
		if(existing ==  null) {
			FAMILIES.put(family.name, family);
		} else {
			existing.setBlocks(family.blocks);
		}
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
