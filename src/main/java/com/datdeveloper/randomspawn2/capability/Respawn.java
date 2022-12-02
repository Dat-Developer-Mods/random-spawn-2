package com.datdeveloper.randomspawn2.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.HashMap;
import java.util.Map;

@AutoRegisterCapability
public class Respawn {
    Map<ResourceKey<Level>, BlockPos> respawnPoints = new HashMap<>();
    boolean bedReset;

    public Map<ResourceKey<Level>, BlockPos> getRespawnPoints() {
        return respawnPoints;
    }

    public void setRespawnPoints(Map<ResourceKey<Level>, BlockPos> respawnPoints) {
        this.respawnPoints = respawnPoints;
    }

    public BlockPos getRespawnForLevel(ResourceKey<Level> level) {
        return this.respawnPoints.get(level);
    }

    public void setRespawnPointForLevel(ResourceKey<Level> level, BlockPos respawnPoint) {
        this.respawnPoints.put(level, respawnPoint);
    }

    public boolean isBedReset() {
        return bedReset;
    }

    public void setBedReset(boolean bedReset) {
        this.bedReset = bedReset;
    }

    public void copyFrom(Respawn oldRespawn) {
        respawnPoints = new HashMap<>(oldRespawn.respawnPoints);
        bedReset = oldRespawn.bedReset;
    }
}
