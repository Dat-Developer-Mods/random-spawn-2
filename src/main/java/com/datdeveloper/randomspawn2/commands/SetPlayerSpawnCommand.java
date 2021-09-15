package com.datdeveloper.randomspawn2.commands;

import com.datdeveloper.randomspawn2.Util;
import com.demmodders.datmoddingapi.util.DemConstants;
import com.demmodders.datmoddingapi.util.Permissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

public class SetPlayerSpawnCommand extends CommandBase {
    @Override
    public String getName() {
        return "randomsetplayerspawn";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return DemConstants.TextColour.COMMAND + "/setplayerspawn [player] " + DemConstants.TextColour.INFO + " - Sets the spawn point of the given player in the dimension you're currently in";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("setplayerspawn");
        return aliases;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (!(sender instanceof EntityPlayerMP)) {
            sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "You cannot set a player's spawn from the console"));
            return;
        } else if (!Permissions.checkPermission(sender, "datrandomteleport.rspawn.setplayerspawn", getRequiredPermissionLevel())) {
            sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "You don't have permission to do that"));
            return;
        }

        EntityPlayerMP targetPlayer;
        // Ensure it's either being called by a player, or on a player
        if (args.length == 0) {
            targetPlayer = ((EntityPlayerMP) sender);
        } else {
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(args[0]);
            if (player != null) {
                targetPlayer = player;
            } else {
                sender.sendMessage(new TextComponentString(DemConstants.TextColour.ERROR + "Random spawn cannot find that player"));
                return;
            }
        }



        // Set the spawn in config and update the file
        targetPlayer.setSpawnDimension(((EntityPlayerMP) sender).dimension);
        targetPlayer.setSpawnChunk(sender.getPosition(), true, ((EntityPlayerMP) sender).dimension);
        Util.setPlayerSpawn(targetPlayer, sender.getPosition(), ((EntityPlayerMP) sender).dimension);
        sender.sendMessage(new TextComponentString(DemConstants.TextColour.INFO + "Set " + (args.length == 0 ? "your spawn" : "the spawn of " + args[0]) + " to your current location: X=" + sender.getPosition().getX() + "Y=" + sender.getPosition().getY() + " Z=" + sender.getPosition().getZ()));
    }
}
