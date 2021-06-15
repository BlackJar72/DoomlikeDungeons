package jaredbgreat.dldungeons.builder;


import java.util.List;
import java.util.Random;

import jaredbgreat.dldungeons.api.DLDEvent;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.util.cache.Coords;
import jaredbgreat.dldungeons.util.cache.WeakCache;
import jaredbgreat.dldungeons.util.debug.DebugOut;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;


// TODO: Everything, once the dungeons themselves are back!


public class Builder {
	private static final WeakCache<Dungeon> DUNGEON_CACHE = new WeakCache<>();	
	
	private static boolean debugPole = false;
	
	public static void buildDungeonChunk(final int cx, final int cz, final Coords dc, final ISeedReader world) throws Throwable {
		Dungeon dungeon = DUNGEON_CACHE.get(dc);
		if(dungeon == null) {
			dungeon = new Dungeon(world, dc);
			DUNGEON_CACHE.add(dungeon);
		}
		if((dungeon != null) && (dungeon.map != null)) {
			dungeon.map.buildInChunk(dungeon, cx, cz);
		}
	}
	
	
	public static void buildDungeonsChunk(final int cx, final int cz, final List<Coords> dcs, final ISeedReader world) throws Throwable {
		for(Coords dc : dcs) {
			buildDungeonChunk(cx, cz, dc, world);
		}
	}
	
}
