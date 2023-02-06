package jaredbgreat.dldungeons.builder;


import java.util.List;

import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.util.cache.Coords;
import jaredbgreat.dldungeons.util.cache.WeakCache;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;


// TODO: Everything, once the dungeons themselves are back!


public class Builder {
	private static final WeakCache<Dungeon> DUNGEON_CACHE = new WeakCache<>();
	
	
	public static void buildDungeonChunk(final ChunkPos cp, final Coords dc, final ISeedReader world) throws Throwable {
		Dungeon dungeon = DUNGEON_CACHE.get(dc);
		if(dungeon == null) {
			dungeon = new Dungeon(world, dc);
			DUNGEON_CACHE.add(dungeon);
		}
		if((dungeon != null) && (dungeon.map != null)) {
			dungeon.map.buildInChunk(dungeon, cp);
		}
	}
	
	
	public static void buildDungeonsChunk(final ChunkPos cp, final List<Coords> dcs, final ISeedReader world) throws Throwable {
		for(Coords dc : dcs) {
			buildDungeonChunk(cp, dc, world);
		}
	}
	
}
