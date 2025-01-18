package jaredbgreat.dldungeons.pieces.entrances;

import com.mojang.serialization.Codec;

public enum EntranceType {
    SIMPLE (new SimpleFactory()),
    STAIR (new StairFactory()),
    ROOM (new TopRoomFactory());

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

}
