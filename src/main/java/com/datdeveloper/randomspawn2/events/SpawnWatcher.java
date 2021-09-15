package com.datdeveloper.randomspawn2.events;

import com.datdeveloper.randomspawn2.RandomConfig;
import com.datdeveloper.randomspawn2.RandomSpawn2;
import com.datdeveloper.randomspawn2.Util;
import com.datdeveloper.randomspawn2.capability.IRespawn;
import com.datdeveloper.randomspawn2.capability.RespawnProvider;
import com.demmodders.datmoddingapi.util.BlockPosUtil;
import com.demmodders.datmoddingapi.util.DemConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = RandomSpawn2.MOD_ID)
public class SpawnWatcher {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null).getSpawn(event.player.getSpawnDimension()) == null) {
            Util.randomiseSpawnPoint((EntityPlayerMP) event.player, event.player.getSpawnDimension());
            Util.teleportPlayer((EntityPlayerMP) event.player, event.player.getSpawnDimension());
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

        // Predict the next dimension
        int dimension = Util.getValidSpawnDimension(player, player.dimension);

        if (player.isSpawnForced(dimension)) {
            if (!RandomConfig.saveSpawn) Util.randomiseSpawnPoint(player, dimension);
        } else {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
            BlockPos spawn = EntityPlayer.getBedSpawnLocation(world, player.getBedLocation(dimension), player.forceSpawn);

            if (spawn == null) {
                IRespawn respawn = player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);
                respawn.setBedReset(true);

                if (RandomConfig.saveSpawn) {
                    player.setSpawnChunk(respawn.getSpawn(dimension), true, dimension);
                } else {
                    Util.randomiseSpawnPoint(player, dimension);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent e){
        if (!e.isEndConquered()) {
            IRespawn respawn = e.player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);
            if (respawn.isBedReset()) {
                ((EntityPlayerMP) e.player).connection.sendPacket(new SPacketChangeGameState(0, 0.0F));
                respawn.setBedReset(false);
            } else if (e.player.getBedLocation(e.player.dimension) == null) {
                Util.randomiseSpawnPoint((EntityPlayerMP) e.player, e.player.dimension);
                Util.teleportPlayer((EntityPlayerMP) e.player, e.player.dimension);
                e.player.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "or rather, your spawn was, thus has been reset"));
            } else if (RandomConfig.safeSpawn && e.player.isSpawnForced(e.player.dimension)) {
                BlockPos pos = Util.getSafeBlockPos(e.player.getPosition(), e.player.dimension);
                if (pos == null) {
                    Util.randomiseSpawnPoint((EntityPlayerMP) e.player, e.player.dimension);
                    Util.teleportPlayer((EntityPlayerMP) e.player, e.player.dimension);
                    e.player.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "Your spawn point is unavailable, resetting"));
                } else {
                    Util.setPlayerSpawn((EntityPlayerMP) e.player, pos, e.player.dimension);
                }
            }

            respawn.setLastTeleport(System.currentTimeMillis());
        }
    }
}
