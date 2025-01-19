package jaredbgreat.dldungeons.pieces.entrances;

import com.mojang.serialization.Codec;
import jaredbgreat.dldungeons.planner.Dungeon;
import net.minecraft.world.level.WorldGenLevel;

public enum EntranceType {
    SIMPLE (new SimpleFactory()),
    STAIR (new StairFactory()),
    ROOM (new TopRoomFactory()),
    NONE (new ClosedFactory());

    public final IEntranceFactory factory;


    public static final Codec<EntranceType> CODEC = Codec.INT.xmap(ordinal -> values()[ordinal], Enum::ordinal);


    EntranceType(IEntranceFactory factory) {
        this.factory = factory;
    }


    public interface IEntranceFactory {
        AbstractEntrance makeEntrance(int realX, int realZ);
    }


    public static class SimpleFactory implements IEntranceFactory {
        @Override
        public AbstractEntrance makeEntrance(int realX, int realZ) {
            return new SimpleEntrance(realX, realZ);
        }
    }


    public static class StairFactory implements IEntranceFactory {
        @Override
        public AbstractEntrance makeEntrance(int realX, int realZ) {
            return new SpiralStair(realX, realZ);
        }
    }


    public static class TopRoomFactory implements IEntranceFactory {
        @Override
        public AbstractEntrance makeEntrance(int realX, int realZ) {
            return new TopRoom(realX, realZ);
        }
    }


    public static class ClosedFactory implements IEntranceFactory {
        @Override
        public AbstractEntrance makeEntrance(int realX, int realZ) {
            return new NoEntrance(0, 0);
        }
    }


    public static class NoEntrance extends AbstractEntrance {
        public NoEntrance(int x, int z) { super(x, z); }
        @Override
        public void build(Dungeon dungeon, WorldGenLevel world, int shiftX, int shiftZ) {/* DO NOTHING*/}
    }

}
