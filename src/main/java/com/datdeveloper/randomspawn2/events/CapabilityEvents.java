package com.datdeveloper.randomspawn2.events;

import com.datdeveloper.randomspawn2.Randomspawn2;
import com.datdeveloper.randomspawn2.capability.RespawnProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Randomspawn2.MODID, bus =  Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvents {
    public static final ResourceLocation RESPAWN_CAPABILITY = new ResourceLocation(Randomspawn2.MODID, "respawn");

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof ServerPlayer player)) return;

        if (player.getCapability(RespawnProvider.RESPAWN_HANDLER).isPresent()) return;


        event.addCapability(RESPAWN_CAPABILITY, new RespawnProvider());
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(RespawnProvider.RESPAWN_HANDLER).ifPresent(old -> {
            event.getEntity().getCapability(RespawnProvider.RESPAWN_HANDLER).ifPresent(newStore -> {
                newStore.copyFrom(old);
            });
        });
        event.getOriginal().invalidateCaps();
    }
}
