package jaredbgreat.dldungeons.planner.mapping;


/*
 * Doomlike Dungeons by is licensed the MIT License
 * Copyright (c) 2014-2018 Jared Blackburn
 */

import jaredbgreat.dldungeons.builder.BlockFamily;
import jaredbgreat.dldungeons.builder.RegisteredBlock;
import jaredbgreat.dldungeons.pieces.Spawner;
import jaredbgreat.dldungeons.pieces.chests.Chest;
import jaredbgreat.dldungeons.pieces.entrances.AbstractEntrance;
import jaredbgreat.dldungeons.pieces.entrances.Entrance;
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

    /**
     * Convert a 2d chunk features to lists.
     */
    private List<List<ChunkFeatures>> featuresToList() {
        return Arrays.stream(features).toList()
                .stream()
                .map(arr -> {
                    List<ChunkFeatures> list = new ArrayList<>();
                    for (ChunkFeatures f : arr) {
                        list.add(f);
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
                            bytesToList(matrix.nFloorY),
                            bytesToList(matrix.boolBits)
                    )),
                    Codec.INT.listOf().listOf().listOf().fieldOf("blocksAndRooms").forGetter(matrix -> List.of(
                            intsToList(matrix.room)
                    )),
                    ChunkFeatures.CODEC.listOf().listOf().fieldOf("features").forGetter(matrix -> matrix.featuresToList())
            )
            .apply(builder, (size, coords, heights, blocksAndRooms, features) -> {
                final int[] actualCoords = coords.toArray();
                final MapMatrix matrix = new MapMatrix(Sizes.values()[size], new Coords(actualCoords[0], actualCoords[1], 0));


                iterateListOfLists(heights.get(0), (value, x, y) -> matrix.ceilY[x][y] = value);
                iterateListOfLists(heights.get(1), (value, x, y) -> matrix.floorY[x][y] = value);
                iterateListOfLists(heights.get(2), (value, x, y) -> matrix.nCeilY[x][y] = value);
                iterateListOfLists(heights.get(3), (value, x, y) -> matrix.nFloorY[x][y] = value);
                iterateListOfLists(heights.get(4), (value, x, y) -> matrix.boolBits[x][y] = value);

                iterateListOfLists(blocksAndRooms.get(0), (value, x, y) -> matrix.room[x][y] = value);

                iterateListOfLists(features, (value, x, y) -> matrix.features[x][y] = value);

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

    public byte[][] boolBits;

    // The room id (index of the room in the dungeons main RoomList)
    public int[][] room;

    //The A* scratch pad
    public Step nodedge[][];
    public boolean astared[][];

    public ChunkFeatures[][] features;


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
        boolBits = new byte[size.width][size.width];
        room = new int[size.width][size.width];
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


    public void addChest(Chest chest) {
        features[chest.getMX() / 16][chest.getMZ() / 16].addChest(chest);
    }


    public void addEntrance(Entrance entrance) {
        features[entrance.getX() / 16][entrance.getZ() / 16].addEntrance(entrance);
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

        int airBlock, wallBlock1, floorBlock, cielingBlock, fenceBlock, pillarBlock, liquidBlock, caveBlock;

        int sx = (cx0 - lowCX) * 16, ex = sx + 16;
        int sz = (cz0 - lowCZ) * 16, ez = sz + 16;

        for (int i = sx; i < ex; i++)
            for (int j = sz; j < ez; j++) {
                if (room[i][j] != 0) {
                    Room theRoom = dungeon.rooms.get(room[i][j]);


                    // Debugging code; should not normally run
                    if (drawFlyingMap) {
                        if (isAStar(i, j)) {
                            RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ + j, Blocks.LAPIS_BLOCK);
                        } else if (isDoor(i, j)) {
                            RegisteredBlock.placeBlock(world, shiftX + i, 96, shiftZ + j, Blocks.STONE_SLAB);
                        } else if (isWall(i, j)) {
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
                                RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, theRoom.wallBlock1);
                    if ((nFloorY[i][j] > floorY[i][j]) && (floorY[i][j] > 0))
                        for (int k = floorY[i][j]; k < nFloorY[i][j]; k++)
                            if (noLowDegenerate(theRoom, shiftX + i, k, shiftZ + j, i, j, world))
                                RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, theRoom.wallBlock1);

                    if (noLowDegenerate(theRoom, shiftX + i, floorY[i][j] - 1, shiftZ + j, i, j, world)) {
                        RegisteredBlock.place(world, shiftX + i, floorY[i][j] - 1, shiftZ + j, theRoom.floorBlock);
                        if (dungeon.theme.buildFoundation) { // Will become default, due to large caverns
                            below = nFloorY[i][j] < floorY[i][j] ? nFloorY[i][j] - 1 : floorY[i][j] - 2;
                            while (!RegisteredBlock.isGroundBlock(world, shiftX + i, below, shiftZ + j)) {
                                RegisteredBlock.place(world, shiftX + i, below, shiftZ + j, dungeon.floorBlock);
                                below--;
                                if (below < -60) break;
                            }
                        }
                    }

                    // Upper parts of the room
                    if (!theRoom.sky
                            && noHighDegenerate(theRoom, shiftX + i, ceilY[i][j] + 1, shiftZ + j, world))
                        RegisteredBlock.place(world, shiftX + i, ceilY[i][j] + 1, shiftZ + j, theRoom.cielingBlock);

                    for (int k = roomBottom(i, j); k <= ceilY[i][j]; k++)
                        if (!(isWall(i, j) || isPillar(i, j))) {
                            RegisteredBlock.deleteBlock(world, shiftX + i, k, shiftZ + j,
                                    theRoom.airBlock);
                        } else if(isWall(i, j) && noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j, world))
                            RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, theRoom.wallBlock1);
                        else if(noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j, world))
                            RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, theRoom.pillarBlock);
                    for (int k = nCeilY[i][j]; k < ceilY[i][j]; k++)
                        if (noHighDegenerate(theRoom, shiftX + i, k, shiftZ + j, world))
                            RegisteredBlock.place(world, shiftX + i, k, shiftZ + j, theRoom.wallBlock1);
                    if (isFence(i, j))
                        RegisteredBlock.place(world, shiftX + i, floorY[i][j], shiftZ + j, theRoom.fenceBlock);

                    if (isDoor(i, j)) {
                        RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j], shiftZ + j, theRoom.airBlock);
                        RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 1, shiftZ + j, theRoom.airBlock);
                        RegisteredBlock.deleteBlock(world, shiftX + i, floorY[i][j] + 2, shiftZ + j, theRoom.airBlock);
                        // This should make doors over liquids more even
                        if(isLiquid(i, j)) {
                            RegisteredBlock.deleteBlock(world, shiftX + i, dungeon.baseHeight + 1, shiftZ + j, theRoom.airBlock);
                            RegisteredBlock.deleteBlock(world, shiftX + i, dungeon.baseHeight + 2, shiftZ + j, theRoom.airBlock);
                        }
                    }

                    // Liquids
                    if (isLiquid(i, j) && (!isWall(i, j) || isDoor(i, j)) && (floorY[i][j] < dungeon.baseHeight)
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
                && !isAStar(i, j));
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
        if (isWall(i, j) && !isDoor(i, j)) b--;
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


//********************************************************************************************************************//
//                                   HANDLE BOOLEANS AS BIT FLAGS BELOW HERE                                          //
//********************************************************************************************************************//


    static final byte bWall   =  1;
    static final byte bPillar =  2;
    static final byte bFence  =  4;
    static final byte bLiquid =  8;
    static final byte bDoor   = 16;
    static final byte bAStar  = 32;

    static final byte nWall    =  ~bWall;
    static final byte nPillar  =  ~bPillar;
    static final byte nFence   =  ~bFence;
    static final byte nLiquid  =  ~bLiquid;
    static final byte nDoor    =  ~bDoor;
    static final byte nAStar   =  ~bAStar;

    // Is Wall
    public boolean isWall(int x, int z) {
        return (boolBits[x][z] & bWall) > 0;
    }

    public void setWall(int x, int z) {
        boolBits[x][z] |= bWall;
    }

    public void unsetWall(int x, int z) {
        boolBits[x][z] &= nWall;
    }

    public void setWall(int x, int z, Boolean value) {
        if(value) boolBits[x][z] |= bWall;
        else boolBits[x][z] &= nWall;
    }

    // Is Pillar
    public boolean isPillar(int x, int z) {
        return (boolBits[x][z] & bPillar) > 0;
    }

    public void setPillar(int x, int z) {
        boolBits[x][z] |= bPillar;
    }

    public void unsetPillar(int x, int z) {
        boolBits[x][z] &= nPillar;
    }

    public void setPillar(int x, int z, Boolean value) {
        if(value) boolBits[x][z] |= bPillar;
        else boolBits[x][z] &= nPillar;
    }

    // Is Fence
    public boolean isFence(int x, int z) {
        return (boolBits[x][z] & bFence) > 0;
    }

    public void setFence(int x, int z) {
        boolBits[x][z] |= bFence;
    }

    public void unsetFence(int x, int z) {
        boolBits[x][z] &= nFence;
    }

    public void setFence(int x, int z, Boolean value) {
        if(value) boolBits[x][z] |= bFence;
        else boolBits[x][z] &= nFence;
    }

    // Is Liquid
    public boolean isLiquid(int x, int z) {
        return (boolBits[x][z] & bLiquid) > 0;
    }

    public void setLiquid(int x, int z) {
        boolBits[x][z] |= bLiquid;
    }

    public void unsetLiquid(int x, int z) {
        boolBits[x][z] &= nLiquid;
    }

    public void setLiquid(int x, int z, Boolean value) {
        if(value) boolBits[x][z] |= bLiquid;
        else boolBits[x][z] &= nLiquid;
    }

    // Is Door
    public boolean isDoor(int x, int z) {
        return (boolBits[x][z] & bDoor) > 0;
    }

    public void setDoor(int x, int z) {
        boolBits[x][z] |= bDoor;
    }

    public void unsetDoor(int x, int z) {
        boolBits[x][z] &= nDoor;
    }

    public void setDoor(int x, int z, Boolean value) {
        if(value) boolBits[x][z] |= bDoor;
        else boolBits[x][z] &= nDoor;
    }

    // Is A* Marked
    public boolean isAStar(int x, int z) {
        return (boolBits[x][z] & bAStar) > 0;
    }

    public void setAStar(int x, int z) {
        boolBits[x][z] |= bAStar;
    }

    public void unsetAStar(int x, int z) {
        boolBits[x][z] &= nAStar;
    }

    public void setAStar(int x, int z, Boolean value) {
        if(value) boolBits[x][z] |= bAStar;
        else boolBits[x][z] &= nAStar;
    }




}
