package jaredbgreat.dldungeons.minigame;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public class DLWorldProvider extends WorldProvider {


    public void registerWorldChunkManager()
    {
    	// TODO: Make use DLWorldChunk Manager
        worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.ocean, 1.0F, 0.0F);
        isHellWorld = false;
        hasNoSky = true;
        dimensionId = -6; // Will make configurable later
    }
    
    
	@Override
	public String getDimensionName() {
		return "Dungeons";
	}

	
    public IChunkProvider createChunkGenerator()
    {
        return new DLChunkProvider(worldObj);
    }

}
