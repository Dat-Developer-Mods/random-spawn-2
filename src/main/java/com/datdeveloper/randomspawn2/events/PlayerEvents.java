package com.datdeveloper.randomspawn2.events;

import com.datdeveloper.randomspawn2.Randomspawn2;
import com.datdeveloper.randomspawn2.capability.RespawnProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Randomspawn2.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    /**
     * If the player has logged in for the first time, give them a random spawn
     */
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        /*
            TODO:   As CapabilityEvents#attachCapability will run before this executes, this will always return true
                    We need to instead check that the respawn point is undefined, then we can set it
         */

        if (event.getEntity().getCapability(RespawnProvider.RESPAWN_HANDLER).isPresent()) return;


    }

    /**
     * Checks if the player's bed is still valid, and corrects it to the random spawn point if not
     */
    @SubscribeEvent
    public static void playerDeath(LivingDeathEvent event) {
        /*
            TODO:   Check the spawn anchor/bed using Player#findRespawnPositionAndUseSpawnBlock
                    Set the player's spawn back to their stored spawn point if not
                    If the config is set to always randomise, then use this resetting point to randomise it again

         */
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;


    }
}
