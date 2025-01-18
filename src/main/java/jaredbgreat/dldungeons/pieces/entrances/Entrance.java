package jaredbgreat.dldungeons.pieces.entrances;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class Entrance {
    private int x, z;
    private EntranceType type;


    public static final Codec<Entrance> CODEC = RecordCodecBuilder.create(builder -> builder
            .group(Codec.INT.listOf().fieldOf("coords").forGetter(entrance -> List.of(
                            entrance.x,
                            entrance.z)),
                    EntranceType.CODEC.fieldOf("type").forGetter(entrance -> entrance.type))
            .apply(builder, (coords, type) -> {
                final Entrance entrance = new Entrance();

                entrance.x = coords.get(0);
                entrance.z = coords.get(1);
                entrance.type = type;
                return entrance;
            }));


    private Entrance() {}


    public Entrance(int x, int z, EntranceType type) {
        this.x = x;
        this.z = z;
        this.type = type;
    }

    public int getX() { return x; }
    public int getZ() { return z; }
    public EntranceType getType() { return type; }
}
