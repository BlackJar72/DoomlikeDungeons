package jaredbgreat.dldungeons.pieces;

import java.util.Random;

import jaredbgreat.dldungeons.DoomlikeDungeons;
import jaredbgreat.dldungeons.structure.DungeonStructure;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DebugPole extends StructurePiece {
	BlockState quartz = Blocks.CHISELED_QUARTZ_BLOCK.getDefaultState();
	public int x, y, z;
	
	public DebugPole(TemplateManager tm, CompoundNBT nbt) {
		super(DungeonStructure.DEBUG_POLE, nbt);
		x = nbt.getInt("x");
		y = nbt.getInt("y");
		z = nbt.getInt("z");
		this.boundingBox = new MutableBoundingBox(x, y, z, x + 15, y + 15, z + 15);
	}
	
	
	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
	    tagCompound.putInt("x", x);
	    tagCompound.putInt("y", y);
	    tagCompound.putInt("z", z);
	}
	

	@Override
	public boolean addComponentParts(IWorld world, Random random, MutableBoundingBox structbb,
			ChunkPos pos) {
		for(int y = 16; y <= 241; y++) 
			world.setBlockState(new BlockPos(x, y, z), quartz, 3);
		return true;
	}
}
