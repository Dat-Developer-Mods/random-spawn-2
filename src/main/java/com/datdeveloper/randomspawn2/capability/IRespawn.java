package com.datdeveloper.randomspawn2.capability;

import net.minecraft.util.math.BlockPos;

import java.util.Map;

public interface IRespawn {
    long getLastTeleport();

    void setLastTeleport(long lastTeleport);

    boolean isBedReset();

    void setBedReset(boolean bedReset);

    BlockPos getSpawn(int dimension);

    void putSpawn(int dimension, BlockPos pos);

    Map<Integer, BlockPos> getSpawn();

    void setSpawn(Map<Integer, BlockPos> spawn);

    void set(IRespawn oldRespawn);
}
