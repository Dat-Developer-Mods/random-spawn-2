package com.datdeveloper.randomspawn2.commands;

import com.datdeveloper.randomspawn2.RandomConfig;
import com.datdeveloper.randomspawn2.Util;
import com.datdeveloper.randomspawn2.capability.RespawnProvider;
import com.datdeveloper.randomspawn2.returncodes.ETeleportResult;
import com.datmodder.datsimplecommands.utils.PlayerManager;
import com.demmodders.datmoddingapi.delayedexecution.delayedevents.DelayedTeleportEvent;
import com.demmodders.datmoddingapi.structures.Location;
import com.demmodders.datmoddingapi.util.DemConstants;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;

public class DelayedSpawnEvent extends DelayedTeleportEvent {
    public DelayedSpawnEvent(EntityPlayerMP Player, int Delay) {
        // We don't need to store a destination, the util class handles it for us
        super(null, Player, Delay);
    }

    @Override
    public void execute(){
        // Forge Essentials
        if (Loader.isModLoaded("forgeessentials")) {
            // PlayerInfo.get(player.getUniqueID()).setLastTeleportOrigin(new WarpPoint(player.dimension, player.posX, player.posY, player.posZ, player.cameraPitch, player.rotationYaw));
        } else if (Loader.isModLoaded("datsimplecommands")) {
            PlayerManager.getInstance().updatePlayerBackLocation(player.getUniqueID(), new Location(player.dimension, player.posX, player.posY, player.posZ, player.cameraPitch, player.rotationYaw));
        }

        // Teleport the player to their spawn
        int dimension;

        if (RandomConfig.forceSpawnDimension) {
            dimension = RandomConfig.spawnDimension;
        } else {
            dimension = player.getSpawnDimension();
        }

        ETeleportResult result = Util.teleportPlayer(player, dimension);

        switch (result){
            case OK:
                player.sendMessage(new TextComponentString(DemConstants.TextColour.INFO + "Teleporting"));

                // Set last teleport
                player.getCapability(RespawnProvider.RESPAWN_CAPABILITY, null).setLastTeleport(System.currentTimeMillis());
                break;
            case NOTALLOWED:
                player.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "You're not allowed to respawn there"));
                break;
            case NOTEXIST:
                player.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "The dimension you're trying to teleport to doesn't exist"));
                break;
        }
    }
}
