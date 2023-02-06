package jaredbgreat.dldungeons.api;

import java.util.Random;

/* 
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 * 
 * This class (and all lines calling its methods elsewhere thus far) is by 
 * frodare (Charles Howard), late 2016.
 */	

import jaredbgreat.dldungeons.builder.IBlockPlacer;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.mapping.MapMatrix;
import jaredbgreat.dldungeons.rooms.Room;
import net.minecraft.block.Block;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class DLDEvent extends Event {

	public DLDEvent() {

	}

	@Cancelable
	public abstract static class Place extends DLDEvent {
		protected final ISeedReader seedReader;
		protected BlockPos pos;

		public Place(ISeedReader seedReader, BlockPos pos) {
			this.seedReader = seedReader;
			this.pos = pos;
		}

		public ISeedReader getISeedReader() {
			return seedReader;
		}

		public BlockPos getPos() {
			return pos;
		}

		public void setPos(BlockPos pos) {
			this.pos = pos;
		}

	}

	@Cancelable
	public static class PlaceDBlock extends Place {

		protected final IBlockPlacer block;

		public PlaceDBlock(ISeedReader seedReader, BlockPos pos, IBlockPlacer block) {
			super(seedReader, pos);
			this.block = block;
		}

		public IBlockPlacer getBlock() {
			return block;
		}

	}

	@Cancelable
	public static class PlaceBlock extends Place {

		protected final Block block;

		public PlaceBlock(ISeedReader seedReader, BlockPos pos, Block block) {
			super(seedReader, pos);
			this.block = block;
		}

		public Block getBlock() {
			return block;
		}

	}

	public static class DungeonRoom extends DLDEvent {
		protected final Dungeon dungeon;
		protected final Room room;

		public DungeonRoom(Dungeon dungeon, Room room) {
			this.dungeon = dungeon;
			this.room = room;
		}

		public Dungeon getDungeon() {
			return dungeon;
		}

		public Room getRoom() {
			return room;
		}

	}

	@Cancelable
	public static class AddChestBlocksToRoom extends DungeonRoom {
		public AddChestBlocksToRoom(Dungeon dungeon, Room room) {
			super(dungeon, room);
		}
	}

	@Cancelable
	public static class BeforePlaceSpawner extends DLDEvent {

		private final ISeedReader seedReader;
		private final BlockPos pos;
		private final String mob;

		public BeforePlaceSpawner(ISeedReader seedReader, BlockPos pos, String mob) {
			this.seedReader = seedReader;
			this.pos = pos;
			this.mob = mob;
		}

		public ISeedReader getISeedReader() {
			return seedReader;
		}

		public BlockPos getPos() {
			return pos;
		}

		public String getMob() {
			return mob;
		}

	}

	public static class AfterChestTileEntity extends DLDEvent {

		private final ISeedReader seedReader;
		private final ChestTileEntity contents;
		private final int which;
		private final int x;
		private final int y;
		private final int z;
		private final Random random;
		private final int level;

		public AfterChestTileEntity(ISeedReader seedReader, ChestTileEntity contents, int which, int x, int y, int z, Random random, int level) {
			this.seedReader = seedReader;
			this.contents = contents;
			this.which = which;
			this.x = x;
			this.y = y;
			this.z = z;
			this.random = random;
			this.level = level;
		}

		public ISeedReader getISeedReader() {
			return seedReader;
		}

		public ChestTileEntity getContents() {
			return contents;
		}

		public int getWhich() {
			return which;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}

		public Random getRandom() {
			return random;
		}

		public int getLevel() {
			return level;
		}

	}

	@Cancelable
	public static class AddTileEntitiesToRoom extends DungeonRoom {
		public AddTileEntitiesToRoom(Dungeon dungeon, Room room) {
			super(dungeon, room);
		}
	}

	@Cancelable
	public static class AddEntrance extends DungeonRoom {
		public AddEntrance(Dungeon dungeon, Room room) {
			super(dungeon, room);
		}
	}

	public static class BeforeBuild extends DLDEvent {
		protected final MapMatrix mapMatrix;
		protected final int shiftX;
		protected final int shiftZ;
		protected final boolean flooded;

		public BeforeBuild(MapMatrix mapMatrix, int shiftX, int shiftZ, boolean flooded) {
			this.mapMatrix = mapMatrix;
			this.shiftX = shiftX;
			this.shiftZ = shiftZ;
			this.flooded = flooded;
		}

		public MapMatrix getMapMatrix() {
			return mapMatrix;
		}

		public int getShiftX() {
			return shiftX;
		}

		public int getShiftZ() {
			return shiftZ;
		}

		public boolean isFlooded() {
			return flooded;
		}

	}

	public static class AfterBuild extends BeforeBuild {
		public AfterBuild(MapMatrix mapMatrix, int shiftX, int shiftZ, boolean flooded) {
			super(mapMatrix, shiftX, shiftZ, flooded);
		}
	}

	@Cancelable
	public abstract static class PlaceDungeon extends DLDEvent {

		protected final int chunkX;
		protected final int chunkZ;
		protected final ISeedReader seedReader;

		public PlaceDungeon(int chunkX, int chunkZ, ISeedReader seedReader) {
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
			this.seedReader = seedReader;
		}

		public int getChunkX() {
			return chunkX;
		}

		public int getChunkZ() {
			return chunkZ;
		}

		public ISeedReader getISeedReader() {
			return seedReader;
		}

	}

	@Cancelable
	public static class PlaceDungeonBegin extends PlaceDungeon {
		public PlaceDungeonBegin(int chunkX, int chunkZ, ISeedReader seedReader) {
			super(chunkX, chunkZ, seedReader);
		}
	}

	public static class PlaceDungeonFinish extends PlaceDungeon {
		private final Dungeon dungeon;

		public PlaceDungeonFinish(int chunkX, int chunkZ, ISeedReader seedReader, Dungeon dungeon) {
			super(chunkX, chunkZ, seedReader);
			this.dungeon = dungeon;
		}

		public Dungeon getDungeon() {
			return dungeon;
		}

	}

}