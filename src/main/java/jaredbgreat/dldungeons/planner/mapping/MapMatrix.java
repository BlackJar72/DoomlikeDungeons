package jaredbgreat.dldungeons.planner.mapping;


/*
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */

import jaredbgreat.dldungeons.builder.BlockFamily;
import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.BasicChest;
import jaredbgreat.dldungeons.pieces.entrances.AbstractEntrance;
import jaredbgreat.dldungeons.planner.Dungeon;
import jaredbgreat.dldungeons.planner.astar.Step;
import jaredbgreat.dldungeons.rooms.Room;
import jaredbgreat.dldungeons.themes.Sizes;
import jaredbgreat.dldungeons.themes.ThemeFlags;
import jaredbgreat.dldungeons.util.cache.Coords;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A two dimensional map of the dungeon, including heights, blocks, and
 * certain features such as fall and doorways, and pathfinding data.
 * <p>
 * This map also includes the method for building itself into the actual
 * world, converting the 2 1/2 d mapping into blocks.
 *
 * @author Jared Blackburn
 */
public class MapMatrix {

    /**
     * Convert a 2d array of bytes to a list of lists.
     */
    private static List<List<Byte>> bytesToList(byte[][] array) {
        return Arrays.stream(array).toList()
                .stream()
                .map(arr -> {
                    List<Byte> list = new ArrayList<>();
                    for (byte b : arr) {
                        list.add(b);
                    }
                    return list;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convert a 2d array of ints to a list of lists.
     */
    private static List<List<Integer>> intsToList(int[][] array) {
        return Arrays.stream(array).toList()
                .stream()
                .map(arr -> {
                    List<Integer> list = new ArrayList<>();
                    for (int b : arr) {
                        list.add(b);
                    }
                    return list;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convert a 2d array of booleans to a list of lists.
     */
    private static List<List<Boolean>> booleansToList(boolean[][] array) {
        return Arrays.stream(array).toList()
                .stream()
                .map(arr -> {
                    List<Boolean> list = new ArrayList<>();
                    for (boolean b : arr) {
                        list.add(b);
                    }
                    return list;
                })
                .collect(Collectors.toList());
    }

    private interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }

    private static <T> void iterateListOfLists(List<List<T>> list, TriConsumer<T, Integer, Integer> consumer) {
        for (int outer = 0; outer < list.size(); outer++) {
            final List<T> innerList = list.get(outer);
            for (int inner = 0; inner < innerList.size(); inner++) {
                consumer.accept(innerList.get(inner), outer, inner);
            }
        }
    }

    public static final Codec<MapMatrix> CODEC = RecordCodecBuilder.create(builder -> builder
            .group(Codec.INT.fieldOf("size").forGetter(matrix -> matrix.size.ordinal()),
                    Codec.INT_STREAM.fieldOf("coords").forGetter(matrix -> IntStream.of(
                            matrix.chunkX,
                            matrix.chunkZ
                    )),
                    Codec.BYTE.listOf().listOf().listOf().fieldOf("heightmaps").forGetter(matrix -> List.of(
                            bytesToList(matrix.ceilY),
                            bytesToList(matrix.floorY),
                            bytesToList(matrix.nCeilY),
                            bytesToList(matrix.nFloorY)
                    )),
                    Codec.INT.listOf().listOf().listOf().fieldOf("blocksAndRooms").forGetter(matrix -> List.of(
                            intsToList(matrix.ceiling),
                            intsToList(matrix.wall),
                            intsToList(matrix.floor),
                            intsToList(matrix.room)
                    )),
                    Codec.BOOL.listOf().listOf().listOf().fieldOf("booleans").forGetter(matrix -> List.of(
                            booleansToList(matrix.isWall),
                            booleansToList(matrix.isFence),
                            booleansToList(matrix.hasLiquid),
                            booleansToList(matrix.isDoor)
                    )))
            .apply(builder, (size, coords, heights, blocksAndRooms, booleans) -> {
                final int[] actualCoords = coords.toArray();
                final MapMatrix matrix = new MapMatrix(Sizes.values()[size], new Coords(actualCoords[0], actualCoords[1], 0));

                iterateListOfLists(heights.get(0), (value, x, y) -> matrix.ceilY[x][y] = value);
                iterateListOfLists(heights.get(1), (value, x, y) -> matrix.floorY[x][y] = value);
                iterateListOfLists(heights.get(2), (value, x, y) -> matrix.nCeilY[x][y] = value);
                iterateListOfLists(heights.get(3), (value, x, y) -> matrix.nFloorY[x][y] = value);

                iterateListOfLists(blocksAndRooms.get(0), (value, x, y) -> matrix.ceiling[x][y] = value);
                iterateListOfLists(blocksAndRooms.get(1), (value, x, y) -> matrix.wall[x][y] = value);
                iterateListOfLists(blocksAndRooms.get(2), (value, x, y) -> matrix.floor[x][y] = value);
                iterateListOfLists(blocksAndRooms.get(3), (value, x, y) -> matrix.room[x][y] = value);

                iterateListOfLists(booleans.get(0), (value, x, y) -> matrix.isWall[x][y] = value);
                iterateListOfLists(booleans.get(1), (value, x, y) -> matrix.isFence[x][y] = value);
                iterateListOfLists(booleans.get(2), (value, x, y) -> matrix.hasLiquid[x][y] = value);
                iterateListOfLists(booleans.get(3), (value, x, y) -> matrix.isDoor[x][y] = value);

                return matrix;
            }));

    private static boolean drawFlyingMap = false;

    private final Sizes size;
    public final int chunkX, chunkZ, origenX, origenZ, lowCX, lowCZ, shiftX, shiftZ;

    // map of heights to build at
    public byte[][] ceilY;        // Ceiling height
    public byte[][] floorY;        // Floor Height
    public byte[][] nCeilY;        // Height of Neighboring Ceiling
    public byte[][] nFloorY;    // Height of Neighboring Floor

    // Blocks referenced against the DBlock.registry
    public int[][] ceiling;
    public int[][] wall;
    public int[][] floor;

    // The room id (index of the room in the dungeons main RoomList)
    public int[][] room;

    // Is it a wall?
    public boolean[][] isWall;        // Is this coordinate occupied by a wall?
    public boolean[][] isFence;        // Is this coordinate occupied by a wall?
    public boolean[][] hasLiquid;    // Is floor covered by a liquid block?
    public boolean[][] isDoor;        // Is there a door here?

    //The A* scratch pad
    public Step nodedge[][];
    public boolean astared[][];

    public final ChunkFeatures[][] features;


    public MapMatrix(Sizes size, Coords coords) {
        this.size = size;
        chunkX = coords.getX();
        chunkZ = coords.getZ();
        lowCX = chunkX - size.chunkRadius;
        lowCZ = chunkZ - size.chunkRadius;
        origenX = (chunkX * 16) - (size.width / 2) + 8;
        origenZ = (chunkZ * 16) - (size.width / 2) + 8;
        ceilY = new byte[size.width][size.width];
        floorY = new byte[size.width][size.width];
        nCeilY = new byte[size.width][size.width];
        nFloorY = new byte[size.width][size.width];
        room = new int[size.width][size.width];
        ceiling = new int[size.width][size.width];
        wall = new int[size.width][size.width];
        floor = new int[size.width][size.width];
        isWall = new boolean[size.width][size.width];
        isFence = new boolean[size.width][size.width];
        hasLiquid = new boolean[size.width][size.width];
        isDoor = new boolean[size.width][size.width];
        nodedge = new Step[size.width][size.width];
        astared = new boolean[size.width][size.width];
        features = new ChunkFeatures[size.chunkWidth][size.chunkWidth];
        for (int i = 0; i < size.chunkWidth; i++)
            for (int j = 0; j < size.chunkWidth; j++) {
                features[i][j] = new ChunkFeatures();
            }
        shiftX = (chunkX * 16) - (room.length / 2) + 16;
        shiftZ = (chunkZ * 16) - (room.length / 2) + 16;
    }


    public void addSpawner(Spawner spawner) {
        features[spawner.getX() / 16][spawner.getZ() / 16].addSpawner(spawner);
    }


    public void addChest(BasicChest chest) {
        features[chest.mx / 16][chest.mz / 16].addChest(chest);
    }


    public void addEntrance(AbstractEntrance entrance) {
        features[entrance.x / 16][entrance.z / 16].addEntrance(entrance);
    }


    public void buildByChunks(Dungeon dungeon, WorldGenLevel world) {
        for (int i = lowCX, i0 = 0; i < (lowCX + dungeon.size.chunkWidth); i++, i0++)
            for (int j = lowCZ, j0 = 0; j < (lowCZ + dungeon.size.chunkWidth); j++, j0++) {
                buildInChunk(dungeon, i, j, world);
            }
    }


    /**
     * This will build the dungeon into the world, transforming the information
     * mapped here in 2D arrays into the finished 3D structure in the Minecraft
     * world.
     *
     * @param dungeon
     */
    public void buildInChunk(Dungeon dungeon, int cx0, int cz0, WorldGenLevel world) {
        int cx1 = cx0 - lowCX, cz1 = cz0 - lowCZ;
        if ((cx1 < 0) || (cx1 >= dungeon.size.chunkWidth) || (cz1 < 0) || (cz1 >= dungeon.size.chunkWidth)) {
            return;
        }

        BlockFamily.setRadnom(world.getRandom());
        int below;
        boolean flooded = dungeon.theme.flags.contains(ThemeFlags.WATER);

        int sx = (cx0 - lowCX) * 16, ex = sx + 16;
        int sz = (cz0 - lowCZ) * 16, ez = sz + 16;

        for (int i = sx; i < ex; i++)
            for (int j = sz; j < ez; j++) {
                if (room[i][j] != 0) {
                    Room theRoom = dungeon.rooms.get(room[i][j]);

                    // Debugging code; should not normally run
                    if (drawFlyingMap) {
                        if (astared[i][j]) {
                            RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ + j, Blocks.LAPIS_BLOCK);
                        } else if (isDoor[i][j]) {
                            RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ + j, Blocks.STONE_SLAB);
                        } else if (isWall[i][j]) {
                            RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ + j, Blocks.GOLD_BLOCK);
                        } else {
                            RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ + j, Blocks.GLASS);
                        }
                    }


                    // Fix bad heights
                    if (nFloorY[i][j] < 1) {
                        nFloorY[i][j] = (byte) dungeon.baseHeight;
                    }
                    if (floorY[i][j] < 1) {
                        floorY[i][j] = (byte) dungeon.baseHeight;
                    }

                    // Lower parts of the room
                    if ((nFloorY[i][j] < floorY[i][j]) && (nFloorY[i][j] > 0))
                        for (int k = nFloorY[i][j]; k < floorY[i][j]; k++)
                            if (noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j, world))
                                RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
                    if ((nFloorY[i][j] > floorY[i][j]) && (floorY[i][j] > 0))
                        for (int k = floorY[i][j]; k < nFloorY[i][j]; k++)
                            if (noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j, world))
                                RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);

                    if (noLowDegenerate(theRoom, shiftX + i, floorY[i][j] - 1, shiftZ + j, i, j, world)) {
                        RegisteredBlock.place(world, shiftX + i, floorY[i][j] - 1, shiftZ + j, floor[i][j]);
                        if (dungeon.theme.buildFoundation) {
                            below = nFloorY[i][j] < floorY[i][j] ? nFloorY[i][j] - 1 : floorY[i][j] - 2;
                            while (!RegisteredBlock.isGroundBlock(world, shiftX + i, below, shiftZ + j)) {
                                RegisteredBlock.place(world, shiftX + i, below, shiftZ + j, dungeon.floorBlock);
                                below--;
                                if (below < 1) break;
                            }
                        }
                    }

                    // Upper parts of the room
                    if (!theRoom.sky
                            && noHighDegenerate(theRoom, shiftX + i, ceilY[i][j] + 1, shiftZ + j, world))
                        RegisteredBlock.place(world, shiftX + i, ceilY[i][j] + 1, shiftZ + j, ceiling[i][j]);

                    for (int k = roomBottom(i, j); k <= ceilY[i][j]; k++)
                        if (!isWall[i][j]) {
                            RegisteredBlock.deleteBlock(world, shiftX + i, k, shiftZ + j,
                                    theRoom.airBlock);
                        } else if (noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j, world))
                            RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
                    for (int k = nCeilY[i][j]; k < ceilY[i][j]; k++)
                        if (noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j, world))
                            RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, wall[i][j]);
                    if (isFence[i][j])
                        RegisteredBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, theRoom.fenceBlock);

                    if (isDoor[i][j]) {
                        RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j], shiftZ + j, flooded);
                        RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 1, shiftZ + j, flooded);
                        RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 2, shiftZ + j, flooded);
                    }

                    // Liquids
                    if (hasLiquid[i][j] && (!isWall[i][j] && !isDoor[i][j])
                            && !world.getBlockState(new BlockPos(shiftX + i, floorY[i][j] - 1, shiftZ + j)).isAir())
                        RegisteredBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, theRoom.liquidBlock);
                }
            }

        features[cx1][cz1].buildFeatures(dungeon, shiftX, shiftZ, world);
    }


    /**
     * Returns true if a block should be placed in those coordinates; that is
     * the block is not air or the room is not degenerate.
     * <p>
     * This is for use with wall and ceiling blocks; for floor blocks use
     * noLowDegenerate.
     *
     * @param theRoom
     * @param x       world x coordinate
     * @param y       world y coordinate
     * @param z       world z coordinate
     * @return if the block should be placed here.
     */
    private boolean noHighDegenerate(Room theRoom, int x, int y, int z, WorldGenLevel world) {
        return !(theRoom.degenerate && world.getBlockState(new BlockPos(x, y, z)).isAir());
    }


    /**
     * Returns true if a floor block should be placed here.  This will be true
     * if the block is not air, if the room does not have degenerate floors, or
     * is part of a main path through the room.
     *
     * @param theRoom
     * @param x       world x coordinate
     * @param y       world y coordinate
     * @param z       world z coordinate
     * @param i       dungeon x coordinate
     * @param j       dungeon z coordinate
     * @return
     * @return if the block should be placed here.
     */
    private boolean noLowDegenerate(Room theRoom, int x, int y, int z, int i, int j, WorldGenLevel world) {
        return !(theRoom.degenerateFloors
                && world.getBlockState(new BlockPos(x, y, z)).isAir()
                && !astared[i][j]);
    }


    /**
     * The lowest height to place air or wall; walls may
     * go one block lower.
     *
     * @param i dungeon x coordinate
     * @param j dungeon z coordinate
     * @return lowest height to place a wall or air/water block.
     */
    private int roomBottom(int i, int j) {
        int b = floorY[i][j];
        if (isWall[i][j] && !isDoor[i][j]) b--;
        return b;
    }


    /**
     * Sets whether the flying debug map should be drawn.
     *
     * @param value
     */
    public static void setDrawFlyingMap(boolean value) {
        drawFlyingMap = value;
    }

}
