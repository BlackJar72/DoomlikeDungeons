package jaredbgreat.dldungeons.pieces.entrances;

import com.mojang.serialization.Codec;
import jaredbgreat.dldungeons.themes.Degree;

public enum EntranceType {
    SIMPLE,
    STAIR,
    ROOM;

    public static final Codec<EntranceType> CODEC = Codec.INT.xmap(ordinal -> values()[ordinal], Enum::ordinal);

}
