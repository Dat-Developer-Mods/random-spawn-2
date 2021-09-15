package com.datdeveloper.randomspawn2.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import scala.Int;

import javax.annotation.Nullable;
import java.util.HashMap;

public class RespawnStorage implements Capability.IStorage<IRespawn> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IRespawn> capability, IRespawn instance, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("lastTeleport", instance.getLastTeleport());
        tag.setBoolean("bedReset", instance.isBedReset());

        NBTTagCompound map = new NBTTagCompound();
        for (int key : instance.getSpawn().keySet()) {
            BlockPos pos = instance.getSpawn(key);
            NBTTagCompound tagPos = new NBTTagCompound();
            tagPos.setInteger("x", pos.getX());
            tagPos.setInteger("y", pos.getY());
            tagPos.setInteger("z", pos.getZ());
            map.setTag(String.valueOf(key), tagPos);
        }

        tag.setTag("spawn", map);
        return tag;
    }

    @Override
    public void readNBT(Capability<IRespawn> capability, IRespawn instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        instance.setLastTeleport(tag.getLong("lastTeleport"));

        instance.setBedReset(tag.getBoolean("bedReset"));

        HashMap<Integer, BlockPos> posMap = new HashMap<>();
        NBTTagCompound map = tag.getCompoundTag("spawn");

        for (String key : map.getKeySet()) {
            int realKey = Integer.parseInt(key);
            NBTTagCompound blockPos = map.getCompoundTag(key);

            posMap.put(realKey, new BlockPos(blockPos.getInteger("x"), blockPos.getInteger("y"), blockPos.getInteger("z")));
        }

        instance.setSpawn(posMap);
    }
}
