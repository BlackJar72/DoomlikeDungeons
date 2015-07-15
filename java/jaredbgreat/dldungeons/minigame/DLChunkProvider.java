package jaredbgreat.dldungeons.minigame;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.SCATTERED_FEATURE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.STRONGHOLD;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.DUNGEON;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.MapGenScatteredFeature;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class DLChunkProvider  implements IChunkProvider/*extends ChunkProviderGenerate*/ {
    
    private World worldObj;
    private final boolean mapFeaturesEnabled;
    private BiomeGenBase[] biomesForGeneration;

    public DLChunkProvider(World par1World)
    {
        worldObj = par1World;
        mapFeaturesEnabled = false;
    }
    
    
    public Chunk loadChunk(int par1, int par2)
    {
        return this.provideChunk(par1, par2);
    } 
    
    
    public Chunk provideChunk(int par1, int par2)
    {
        byte[] abyte = new byte[32768];
        for(int i = 0; i < abyte.length; i++) abyte[i] = (byte)Block.bedrock.blockID;
        // TODO: replace next line (Mojang code) with GPL-able code
        biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
        Chunk chunk = new Chunk(this.worldObj, abyte, par1, par2);
        chunk.generateSkylightMap();
        return chunk;
    }

    
    public boolean chunkExists(int par1, int par2)
    {
        return true;
    }

    
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        return true;
    }

    
    public void saveExtraData() {}


    public boolean unloadQueuedChunks()
    {
        return false;
    }


    public boolean canSave()
    {
        return true;
    }

    
    public String makeString()
    {
        return "DungeonWorldBedrockSource";
    }

    
    //TODO: Re-write to give specific creature list (or empty list?)
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
    	// This is Mojang code, must be eliminated before release to remain GPL
        BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(par2, par4);
        return biomegenbase.getSpawnableList(par1EnumCreatureType);
    }
    
    
    public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5)
    {
        return null;
    }
    

    public int getLoadedChunkCount()
    {
        return 0;
    }
    

    public void recreateStructures(int par1, int par2) {/*Do Nothing!*/}

    
	@Override
	public void populate(IChunkProvider ichunkprovider, int i, int j) {/*Do Nothing!*/}
}
