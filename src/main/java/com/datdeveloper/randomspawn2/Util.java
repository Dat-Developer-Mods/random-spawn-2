package com.datdeveloper.randomspawn2;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;

public class Util {
    static int radius =  5000;
    static Random random = new Random();


    public static BlockPos generateRandomCoordinate(Level level){

        int x = random.nextInt(radius * 2) - radius;
        int z = random.nextInt(radius * 2) - radius;
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

        return new BlockPos(x, y, z);
    }
}
