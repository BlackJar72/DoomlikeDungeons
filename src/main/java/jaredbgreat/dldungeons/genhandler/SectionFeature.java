package jaredbgreat.dldungeons.genhandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.mojang.serialization.Codec;

import jaredbgreat.dldungeons.builder.Builder;
import jaredbgreat.dldungeons.themes.Sizes;
import jaredbgreat.dldungeons.util.cache.Coords;
import jaredbgreat.dldungeons.util.debug.Logging;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class SectionFeature  extends Feature<NoFeatureConfig> {
	private volatile static int factor = 6;
	private volatile static Random mrand;
	private static HashSet<Integer> dimensions;

	public SectionFeature(Codec<NoFeatureConfig> codec) {
		super(codec);
	}

	
	@Override
	public boolean place(ISeedReader sreader, ChunkGenerator chunkgen, Random random, final BlockPos pos,
			NoFeatureConfig noconfig) {
		int cx = pos.getX() / 16, cz = pos.getZ() / 16;	
		ChunkPos cp = new ChunkPos(pos);
		mrand = new Random(sreader.getSeed() 
				+ (2027 * (long)(cx / factor)) 
				+ (1987 * (long)(cz / factor)));		
		findDungeonsToBuild(sreader, cp, pos);
		return true;
	}
	
	
	private void findDungeonsToBuild(final ISeedReader sreader, final ChunkPos cp, final BlockPos pos) {
		long seed = sreader.getSeed();
		World world = sreader.getLevel();
		int dimension = world.dimension().location().hashCode();
		ArrayList<Coords> dungeonLocs = new ArrayList<>();
		int sx = cp.x - Sizes.HUGE.chunkRadius, ex = cp.x + Sizes.HUGE.chunkRadius;
		int sz = cp.z - Sizes.HUGE.chunkRadius, ez = cp.z + Sizes.HUGE.chunkRadius;
		for(int i = sx; i <= ex; i++) 
			for(int j = sz; j <= ez; j++) {
				if(isDungeonCenter(seed, i, j, dimension)) {
					dungeonLocs.add(new Coords(i, j, dimension));
				}
			}
		dungeonLocs.sort(Coords.HashCompare.C);
		try {
			Builder.buildDungeonsChunk(cp, dungeonLocs, sreader);
		} catch (Throwable e) {
			// Ignore this as an apparent false alarm, everything works
			e.printStackTrace();
			StringBuilder b = new StringBuilder(e.getLocalizedMessage());
			b.append("SectionFeature.findDungeonsToBuild(): Building in chunk at " + cp);
			b.append(" (blocks  ");
			b.append(cp.getMinBlockX() + "," + cp.getMinBlockZ());
			b.append(" to ");
			b.append(cp.getMaxBlockX() + "," + cp.getMinBlockZ() + "); ");
			b.append("Originally requested " + pos);
			b.append(System.lineSeparator());
			Logging.logError(b.toString());
		}
	}

	
	
	private boolean isDungeonCenter(long seed, int chunkX, int chunkZ, int dim) {		
		mrand = new Random(seed + dim
				+ (2027 * (long)(chunkX / factor)) 
				+ (1987 * (long)(chunkZ / factor)));
		int xrand = mrand.nextInt();
		int zrand = mrand.nextInt();
		int xuse = ((chunkX + xrand) % factor);
		int zuse = ((chunkZ + zrand) % factor);
		return (xuse == 0) && (zuse == 0);
	}
	
	
	
	

	
	
	
	/**
	 * Sets the frequency scale and converts into the 
	 * width (and height) of the areas for which dungeons
	 * are generated.
	 * 
	 * @param freqScale
	 */
	public static void setFrequency(int freqScale) {
		if((freqScale % 2) == 0) factor = 2;
		else factor = 3;
		factor = (factor << (freqScale / 2));
	}
	
	
	/**
	 * Sets the minimum number of chunks from spawn a dungeon center must be.
	 * 
	 * @param value
	 */
	public static void setMinXZ(int value) {}
	
	
	/**
	 * Sets list of dimension id's to check for dungeons being allowed.
	 * 
	 * @param value
	 */
	public static void setDimensions(int[] value) {
		dimensions = new HashSet<Integer>();
		for(int i = 0; i < value.length; i++) {
			dimensions.add(Integer.valueOf(value[i]));
		}
	}
	
	
	/**
	 * Add a dimension id to the list of dimension to consider when 
	 * check if a dungeon is allowed.  (What this means can vary.)
	 * 
	 * @param value
	 */
	public static void addDimension(int value) {
		dimensions.add(Integer.valueOf(value));
	}
	
	
	/**
	 * Remove a dimension id from the list of those to consider when potentially 
	 * placing a dungeons.  (What this means can vary.)
	 * 
	 * @param value
	 */
	public static void subDimension(int value) {
		dimensions.remove(Integer.valueOf(value));
	}

}
