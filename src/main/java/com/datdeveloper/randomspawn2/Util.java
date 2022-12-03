package com.datdeveloper.randomspawn2;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;

public class Util {
    static int radius =  5000;
    /**
     * The random source for generating spawn points
     */
    static Random random = new Random();

    /**
     * Generates a random coordinate for the level to use as the player's spawn
     * @param level The level the spawn is in
     * @return A random position on the surface for the player to spawn at
     */
    public static BlockPos generateRandomCoordinate(Level level){
        int x = random.nextInt(radius * 2) - radius;
        int z = random.nextInt(radius * 2) - radius;

        // TODO: This might not work for unloaded chunks, we made need to use WORLD_SURFACE_WG
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

        return new BlockPos(x, y, z);
    }
}
