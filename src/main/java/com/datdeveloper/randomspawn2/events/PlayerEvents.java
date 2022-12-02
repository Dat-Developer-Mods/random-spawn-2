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
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().getCapability(RespawnProvider.RESPAWN_HANDLER).isPresent()) return;


    }

    @SubscribeEvent
    public static void playerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;


    }
}
