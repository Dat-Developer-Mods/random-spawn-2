package com.datdeveloper.randomspawn2.events;

import com.datdeveloper.randomspawn2.Randomspawn2;
import com.datdeveloper.randomspawn2.capability.DatRespawn;
import com.datdeveloper.randomspawn2.capability.RespawnProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.minecraft.world.entity.player.Player.findRespawnPositionAndUseSpawnBlock;

@Mod.EventBusSubscriber(modid = Randomspawn2.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    /**
     * If the player has logged in for the first time, give them a random spawn
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getCapability(RespawnProvider.RESPAWN_HANDLER).ifPresent((datRespawn -> {
            if (datRespawn.getRespawnPos() != null) return;

            ServerPlayer player = (ServerPlayer) event.getEntity();

            BlockPos respawnPos = DatRespawn.generateRandomCoordinate(player.getLevel());

            player.setRespawnPosition(player.getLevel().dimension(), respawnPos, 0.f, true, false);
            datRespawn.setRespawnLevel(player.getLevel().dimension());
            datRespawn.setRespawnPos(respawnPos);

            player.teleportTo(respawnPos.getX(), respawnPos.getY(), respawnPos.getZ());
        }));
    }

    /**
     * Checks if the player's bed is still valid, and corrects it to the random spawn point if not
     */
    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        //noinspection ConstantValue
        if (serverPlayer.getRespawnPosition() != null && serverPlayer.getRespawnDimension() != null) return;

        event.getEntity().getCapability(RespawnProvider.RESPAWN_HANDLER).ifPresent((datRespawn -> {
            ServerLevel level = serverPlayer.getServer().getLevel(datRespawn.getRespawnLevel());
            Optional<Vec3> spawnPos = Player.findRespawnPositionAndUseSpawnBlock(level, datRespawn.getRespawnPos(), 0.f, true, event.isEndConquered());

            if (spawnPos.isEmpty())
                datRespawn.setRespawnPos(DatRespawn.generateRandomCoordinate(level));

            serverPlayer.setRespawnPosition(datRespawn.getRespawnLevel(), datRespawn.getRespawnPos(), 0.f, true, false);
            serverPlayer.teleportTo(level, datRespawn.getRespawnPos().getX(), datRespawn.getRespawnPos().getY(), datRespawn.getRespawnPos().getZ(), 0.0f, 0.0f);
        }));

        System.out.println("");
    }
}
