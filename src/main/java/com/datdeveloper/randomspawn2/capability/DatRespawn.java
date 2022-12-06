package com.datdeveloper.randomspawn2.capability;

import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.Random;

/**
 * A capability for storing the respawn position in the player
 */
@AutoRegisterCapability
public class DatRespawn {
    static int radius =  5000;

    /**
     * The random source for generating spawn points
     */
    static Random random = new Random();

    /**
     * Position that the player respawns to
     */
    BlockPos respawnPos = null;

    /**
     * The level the player respawns to
     */
    ResourceKey<Level> respawnLevel;

    /**
     * Whether the bed is still valid
     * Needed to pass data between the death event and respawn event
     * TODO: Consider if this is still required
     */
    boolean bedReset;

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
     * Get the respawn point for the player
     * @return the respawn point for the player
     */
    public BlockPos getRespawnPos() {
        return respawnPos;
    }

    /**
     * Set the respawn point for the player
     * @param respawnPos The respawn point for the player
     */
    public void setRespawnPos(BlockPos respawnPos) {
        this.respawnPos = respawnPos;
    }

    /**
     * Get the level the player repsawns to
     * @return the level the player respawns to
     */
    public ResourceKey<Level> getRespawnLevel() {
        return respawnLevel;
    }

    /**
     * Set the level the player respawns to
     * @param respawnLevel The level the player respawns to
     */
    public void setRespawnLevel(ResourceKey<Level> respawnLevel) {
        this.respawnLevel = respawnLevel;
    }

    /**
     * Copy the data from the old DatRespawnObject
     * @param oldDatRespawn The old DatRespawn object to copy from
     */
    public void copyFrom(DatRespawn oldDatRespawn) {
        respawnPos = oldDatRespawn.respawnPos;
        respawnLevel = oldDatRespawn.respawnLevel;
        bedReset = oldDatRespawn.bedReset;
    }

    /**
     * Generates a random coordinate for the level to use as the player's spawn
     * @param level The level the spawn is in
     * @return A random position on the surface for the player to spawn at
     */
    public static BlockPos generateRandomCoordinate(ServerLevel level){
        int x = random.nextInt(radius * 2) - radius;
        int z = random.nextInt(radius * 2) - radius;

        return level.findClosestBiome3d((biomeHolder -> true), new BlockPos(x, 50, z), 6400, 32, 64).getFirst();
    }
}
