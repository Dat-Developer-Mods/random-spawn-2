package com.datdeveloper.randomspawn2.events;

import com.datdeveloper.randomspawn2.RandomSpawn2;
import com.datdeveloper.randomspawn2.capability.IRespawn;
import com.datdeveloper.randomspawn2.capability.RespawnProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = RandomSpawn2.MOD_ID)
public class CapabilityHandler {
    public static final ResourceLocation RESPAWN_CAPABILITY = new ResourceLocation(RandomSpawn2.MOD_ID, "respawn");

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (!(event.getObject() instanceof EntityPlayer)) return;

        event.addCapability(RESPAWN_CAPABILITY, new RespawnProvider());
    }

    @SubscribeEvent
    public static void playerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone e) {
        if (e.isWasDeath()) {

            IRespawn respawn = e.getEntityPlayer().getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);
            IRespawn oldRespawn = e.getOriginal().getCapability(RespawnProvider.RESPAWN_CAPABILITY, null);

            respawn.set(oldRespawn);
        }
    }
}
