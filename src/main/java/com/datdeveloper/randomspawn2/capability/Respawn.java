package com.datdeveloper.randomspawn2.capability;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class Respawn implements IRespawn{
    private long lastTeleport = 0;
    private boolean bedReset;
    private Map<Integer, BlockPos> spawn = new HashMap<>();

    @Override
    public long getLastTeleport() {
        return lastTeleport;
    }

    @Override
    public void setLastTeleport(long lastTeleport) {
        this.lastTeleport = lastTeleport;
    }

    @Override
    public boolean isBedReset() {
        return bedReset;
    }

    @Override
    public void setBedReset(boolean bedReset) {
        this.bedReset = bedReset;
    }

    @Override
    public BlockPos getSpawn(int dimension) {
        return spawn.get(dimension);
    }

    @Override
    public void putSpawn(int dimension, BlockPos pos) {
        spawn.put(dimension, pos);
    }

    @Override
    public Map<Integer, BlockPos> getSpawn() {
        return spawn;
    }

    @Override
    public void setSpawn(Map<Integer, BlockPos> spawn) {
        this.spawn = spawn;
    }

    @Override
    public void set(IRespawn respawn) {
        this.lastTeleport = respawn.getLastTeleport();
        this.bedReset = respawn.isBedReset();
        this.spawn = new HashMap<>(respawn.getSpawn());
    }
}
