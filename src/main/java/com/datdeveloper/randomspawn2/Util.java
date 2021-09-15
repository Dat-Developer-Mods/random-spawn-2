package com.datdeveloper.randomspawn2;

import com.datdeveloper.randomspawn2.capability.IRespawn;
import com.datdeveloper.randomspawn2.capability.RespawnProvider;
import com.datdeveloper.randomspawn2.returncodes.ETeleportResult;
import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.datmoddingapi.util.DatTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Util {
    private static final Random random = new Random();

    public static int getValidSpawnDimension(EntityPlayerMP player, int dimension) {
        final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        final World world = server.getWorld(dimension);
        if (world == null)
        {
            dimension = player.getSpawnDimension();
        }
        else if (!world.provider.canRespawnHere())
        {
            dimension = world.provider.getRespawnDimension(player);
        }
        if (server.getWorld(dimension) == null) dimension = 0;
        return dimension;
    }

    public static ETeleportResult teleportPlayer(EntityPlayerMP player, int dimension) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = server.getWorld(dimension);

        if (world == null) {
            return ETeleportResult.NOTEXIST;
        } else if (!world.provider.canRespawnHere()) {
            return ETeleportResult.NOTALLOWED;
        }

        BlockPos spawnPos = getSpawnPosForDimension(player, dimension);

        if (player.dimension == dimension) {
            player.connection.setPlayerLocation(spawnPos.getX() +.5f, spawnPos.getY(), spawnPos.getZ()+.5f, 0, 0);
        } else {
            player.changeDimension(dimension, new DatTeleporter(new Location(dimension, spawnPos.getX() + .5D, spawnPos.getY(), spawnPos.getZ() + .5D, 0, 0)));
        }

        return ETeleportResult.OK;
    }

    public static BlockPos getSpawnPosForDimension(EntityPlayerMP player,  int dimension) {
        int theDimension = getValidSpawnDimension(player, dimension);
        if (theDimension != dimension) {
            return null;
        }

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        BlockPos spawnPos = player.getBedLocation(dimension);
        if (spawnPos != null) {
            spawnPos = EntityPlayer.getBedSpawnLocation(server.getWorld(dimension), spawnPos, player.isSpawnForced(dimension));

            if (spawnPos == null) {
                IRespawn respawn = player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);
                spawnPos = respawn.getSpawn(dimension);
            }
        }

        if (spawnPos == null) {
            if (isDimensionExcluded(dimension)) {
                spawnPos = server.getWorld(dimension).getSpawnPoint();
            } else {
                spawnPos = generateSpawnPos(dimension);
                setPlayerSpawn(player, spawnPos, dimension);
            }
        }

        return spawnPos;
    }

    public static void setPlayerSpawn(EntityPlayerMP player, BlockPos spawnPos, int dimension) {
        IRespawn respawn = player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);
        respawn.putSpawn(dimension, spawnPos);
        player.setSpawnChunk(spawnPos, true, dimension);
    }

    /**
     * Algorithm stolen from net.minecraft.world.WorldServer
     * @see net.minecraft.world.WorldServer#createSpawnPosition(WorldSettings)
     * Idea stolen from Iberia
     * @see <a href="https://github.com/rockhymas/iberia/blob/master/src/main/java/com/gibraltar/iberia/challenge/SpawnChallenge.java">Iberia</a>
     * @param dimension The mension to generate a spawn position in
     * @return A randomised spawn point
     */
    @SuppressWarnings("JavadocReference")
    public static BlockPos generateSpawnPos(int dimension) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = server.getWorld(dimension);
        BlockPos startingPos = generateStarterCoords();

        BiomeProvider biomeprovider = world.provider.getBiomeProvider();
        List<Biome> list = biomeprovider.getBiomesToSpawnIn();
        Random random = new Random(world.provider.getSeed());
        BlockPos blockpos = biomeprovider.findBiomePosition(startingPos.getX(), startingPos.getZ(), 256, list, random);
        int i;
        int j = world.provider.getAverageGroundLevel();
        int k;
        if (blockpos != null) {
            i = blockpos.getX();
            k = blockpos.getZ();
        } else {
            RandomSpawn2.LOGGER.warn("Unable to find spawn biome");

            i = startingPos.getX();
            k = startingPos.getZ();
        }

        int l = 0;

        while(!world.provider.canCoordinateBeSpawn(i, k)) {
            i += random.nextInt(64) - random.nextInt(64);
            k += random.nextInt(64) - random.nextInt(64);
            ++l;
            if (l == 1000) {
                break;
            }
        }

        return world.getTopSolidOrLiquidBlock(new BlockPos(i, j, k));
    }

    public static BlockPos generateStarterCoords() {
        int x, z;

        x = random.nextInt(RandomConfig.spawnDistanceX);
        z = random.nextInt(RandomConfig.spawnDistanceZ);
        if (random.nextBoolean()) x *= -1;
        if (random.nextBoolean()) z *= -1;

        return new BlockPos(x + RandomConfig.spawnX, 0, z + RandomConfig.spawnZ);
    }

    public static void randomiseSpawnPoint(EntityPlayerMP player, int dimension) {
        Util.setPlayerSpawn(player, Util.generateSpawnPos(dimension), dimension);
    }

    public static boolean isDimensionExcluded(int dimension) {
        return Arrays.stream(RandomConfig.exclusions).anyMatch(dim -> dim == dimension);
    }

    public static BlockPos getSafeBlockPos(BlockPos blockPos, int dimension) {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);

        if (!world.getBlockState(blockPos.down()).getMaterial().blocksMovement()) {
            BlockPos test = blockPos.down();
            while (test.getY() > blockPos.getY() - 6) {
                if (world.getBlockState(test).getMaterial().blocksMovement()) return test;
                test = test.down();
            }

            return null;
        }

        return blockPos;
    }
}
