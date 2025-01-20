package jaredbgreat.dldungeons.planner.mapping;

public class BoolWrapper {
    private byte data;

    static final byte bWall   =  1;
    static final byte bFence  =  2;
    static final byte bLiquid =  4;
    static final byte bDoor   =  8;
    static final byte bAStar  = 16;

    static final byte nWall    =  ~bWall;
    static final byte nFence   =  ~bFence;
    static final byte nLiquid  =  ~bLiquid;
    static final byte nDoor    =  ~bDoor;
    static final byte nAStar   =  ~bAStar;


    public boolean isWall() {
        return (data & bWall) > 0;
    }

    public void setWall() {
        data |= bWall;
    }

    public void unsetWall() {
        data &= nWall;
    }

    public void setWall(Boolean value) {
        if(value) data |= bWall;
        else data &= nWall;
    }



}
