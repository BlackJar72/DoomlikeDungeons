package jaredbgreat.dldungeons;


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


import static jaredbgreat.dldungeons.builder.Builder.placeDungeon;

import java.util.HashSet;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;


public class GenerationHandler implements IWorldGenerator {
	
	private static int frequency;
	private static int factor = 6;
	private static int minXZ;
	private static Random mrand;
//	private static boolean noEnd;
	private static HashSet<Integer> dimensions;
	
	public GenerationHandler() {
		GameRegistry.registerWorldGenerator(this, 100);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
						IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		// Prevent bad spawns
		if(world.isRemote) return; // Do not perform world-gen on the client!
		if((ConfigHandler.obeyRule && !world.getWorldInfo().isMapFeaturesEnabled())
				|| !ConfigHandler.naturalSpawn) return;  // Second part is redundant for safety
//		if((world.getBiomeGenForCoords(chunkX * 16, chunkZ * 16) == BiomeGenBase.sky) && noEnd) return;
		boolean blockedBiome = false;
		Type[] types = BiomeDictionary.getTypesForBiome((world.getBiomeGenForCoords(chunkX * 16, chunkZ * 16)));
		for(Type type : types) {			
			blockedBiome = ConfigHandler.biomeExclusions.contains(type) || blockedBiome;
			//System.out.println("[DLDUNGONS] biome type " + type.toString() +", blockedBiome = " + blockedBiome);
		}
		if(blockedBiome) return;
		if((dimensions.contains(Integer.valueOf(world.provider.dimensionId)) != ConfigHandler.positiveDims)) return;
		if((Math.abs(chunkX - (world.getSpawnPoint().posX / 16)) < minXZ) 
				|| (Math.abs(chunkZ - (world.getSpawnPoint().posZ / 16)) < minXZ)) return;
		
		//Get some numbers		
		//DoomlikeDungeons.profiler.startTask("Checking for Dungeon");
		int xseed = (int)(world.getSeed()  % (1 << 31)) + world.provider.dimensionId;
		int zseed = (int)(world.getSeed()  / (1 << 31)) + world.provider.dimensionId;
		//mrand = new Random(xseed + zseed + (2027 * (long)(chunkX / factor)) + (1987 * (long)(chunkZ / factor)));
		mrand = new Random(xseed 
				+ (2027 * (long)(chunkX / factor)) 
				+ (1987 * (long)(chunkZ / factor)));
		int xrand = mrand.nextInt();
		int zrand = mrand.nextInt();
		int xuse = ((chunkX + xseed + xrand) % factor);
		int zuse = ((chunkZ + zseed + zrand) % factor);
		
		if((xuse == 0) && (zuse == 0)) {
			try {
				placeDungeon(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			} catch (Throwable e) {
				System.err.println("[DLDUNGEONS] Danger!  Failed to finalize a dungeon after building!");
				e.printStackTrace();
			} // Put a Dungeon in the areaS
		}
		//DoomlikeDungeons.profiler.endTask("Checking for Dungeon");
	}	
	
	
	public static void setFrequency(int freqScale) {
		if((freqScale % 2) == 0) factor = 2;
		else factor = 3;
		System.out.println("freqScale = " + freqScale);
		factor = (1 << (freqScale / 2)) * factor;
		System.out.println("factor = " + factor);
	}
	
	
	public static void setMinXZ(int value) {
		minXZ = value;		
	}
	
	
//	public static void setEnd(boolean value) {
//		noEnd = value;		
//	}
	
	
	public static void setDimensions(int[] value) {
		dimensions = new HashSet<Integer>();
		dimensions.clear();
		for(int i = 0; i < value.length; i++) {
			dimensions.add(Integer.valueOf(value[i]));
		}
	}
	
	
	public static void addDimension(int value) {
		dimensions.add(Integer.valueOf(value));
	}
	
	
	public static void subDimension(int value) {
		dimensions.remove(Integer.valueOf(value));
	}
	
	
}
