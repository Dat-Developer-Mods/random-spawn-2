package com.datdeveloper.randomspawn2.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RespawnProvider implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(IRespawn.class)
    public static final Capability<IRespawn> RESPAWN_CAPABILITY = null;

    private IRespawn instance = RESPAWN_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == RESPAWN_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == RESPAWN_CAPABILITY ? RESPAWN_CAPABILITY.<T> cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
        return RESPAWN_CAPABILITY.getStorage().writeNBT(RESPAWN_CAPABILITY, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        RESPAWN_CAPABILITY.getStorage().readNBT(RESPAWN_CAPABILITY, this.instance, null, nbt);
    }
}
