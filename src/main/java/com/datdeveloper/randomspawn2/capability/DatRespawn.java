package com.datdeveloper.randomspawn2.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.HashMap;
import java.util.Map;

/**
 * A capability for storing the respawn position in the player
 */
@AutoRegisterCapability
public class DatRespawn {
    /**
     * The spawn point for the player
     * TODO: Change this to just one respawn point
     */
    Map<ResourceKey<Level>, BlockPos> respawnPoints = new HashMap<>();
    /**
     * Whether the bed is still valid
     * Needed to pass data between the death event and respawn event
     * TODO: Consider if this is still required
     */
    boolean bedReset;

    /**
     * Get the map of respawnPoints
     * @return the map of respawn points
     */
    public Map<ResourceKey<Level>, BlockPos> getRespawnPoints() {
        return respawnPoints;
    }

    /**
     * Set the map of respawn points
     * @param respawnPoints The new map of respawn points
     */
    public void setRespawnPoints(Map<ResourceKey<Level>, BlockPos> respawnPoints) {
        this.respawnPoints = respawnPoints;
    }

    /**
     * Get the respawn point for the level
     * TODO: The stuff about switching to just one level and respawn point
     * @param level The level key to get the spawn point of
     * @return The player's spawn point for the given level
     */
    public BlockPos getRespawnForLevel(ResourceKey<Level> level) {
        return this.respawnPoints.get(level);
    }

    /**
     * Set the respawn point for the given level
     * @param level The Level key to
     * @param respawnPoint The new respawn point for the level
     */
    public void setRespawnPointForLevel(ResourceKey<Level> level, BlockPos respawnPoint) {
        this.respawnPoints.put(level, respawnPoint);
    }

    /**
     * Check if the bed has been reset
     * @return whether the bed has been reset
     */
    public boolean isBedReset() {
        return bedReset;
    }

    /**
     * Set if the bed has been reset
     * @param bedReset whether the bed has been reset
     */
    public void setBedReset(boolean bedReset) {
        this.bedReset = bedReset;
    }

    /**
     * Copy the data from the old DatRespawnObject
     * @param oldDatRespawn The old DatRespawn object to copy from
     */
    public void copyFrom(DatRespawn oldDatRespawn) {
        respawnPoints = new HashMap<>(oldDatRespawn.respawnPoints);
        bedReset = oldDatRespawn.bedReset;
    }
}
