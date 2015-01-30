package jaredbgreat.dldungeons.builder;


/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/	


import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.planner.Dungeon;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import static jaredbgreat.dldungeons.builder.DBlock.*;


public class Builder {
	
	private static boolean debugPole = false;
	

	public static void placeDungeon(Random random, int chunkX, int chunkZ, World world) throws Throwable {	
		if(world.isRemote) return; // Do not perform world-gen on the client!z
		DoomlikeDungeons.profiler.startTask("Create Dungeons");
		Dungeon dungeon = new Dungeon(random, world.getBiomeGenForCoords((chunkX * 16), (chunkZ * 16)), 
									  world, chunkX, chunkZ);

		System.out.println("[DLDUNGONS] Running Builder.buildDungeon; building dungeon");
		buildDungeon(dungeon);
		System.out.println("[DLDUNGONS] Dungeon should be built now!");
		//if(true) debuggingPole(world, chunkX, chunkZ, dungeon);
		dungeon.finalize();
		dungeon = null;
		DoomlikeDungeons.profiler.endTask("Create Dungeons");
	}
	

	public static void placeDungeon(Random random, int chunkX, int chunkZ, World world,
						IChunkProvider chunkGenerator, IChunkProvider chunkProvider) throws Throwable {	
		if(world.isRemote) return; // Do not perform world-gen on the client!
		DoomlikeDungeons.profiler.startTask("Create Dungeons");
		Dungeon dungeon = new Dungeon(random, world.getBiomeGenForCoords((chunkX * 16), (chunkZ * 16)), 
									  world, chunkX, chunkZ);
		
		System.out.println("[DLDUNGONS] Running Builder.buildDungeon; building dungeon");
		if(debugPole) debuggingPole(world, chunkX, chunkZ, dungeon);
		buildDungeon(dungeon);
		System.out.println("[DLDUNGONS] Dungeon should be built now!");
		dungeon.finalize();
		dungeon = null;
		DoomlikeDungeons.profiler.endTask("Create Dungeons");
	}
	
	
	public static void buildDungeon(Dungeon dungeon /*TODO: Parameters*/) {
		//System.out.println("[DLDUNGONS] Inside Builder.placeDungeon; building dungeon");
		dungeon.map.build(dungeon);
	}
	
	
	public static void debuggingPole(World world, int chunkX, int chunkZ, Dungeon dungeon) {
		//Pointless stand-in for generation testing, and to help find dungeons later
		int x = (chunkX * 16) + 8;
		int z = (chunkZ * 16) + 8;
		for(int y = -16; y <= 241; y++) placeBlock(world, x, y, z, quartz);
		for(int i = -dungeon.size.radius; i <= dungeon.size.radius; i++) {
			placeBlock(world, x - dungeon.size.radius, 80, z + i, lapis);
			placeBlock(world, x + dungeon.size.radius, 80, z + i, lapis);
			placeBlock(world, x + i, 80, z - dungeon.size.radius, lapis);
			placeBlock(world, x + i, 80, z + dungeon.size.radius, lapis);
		}		
	}
	
	
	public static void setDebugPole(boolean val) {
		debugPole = val;
	}
}
