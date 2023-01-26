package com.datdeveloper.randomspawn2.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RespawnProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<DatRespawn> RESPAWN_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});

    private DatRespawn datRespawn = null;
    private final LazyOptional<DatRespawn> opt = LazyOptional.of(this::createRespawn);

    /**
     * Create an instance of respawn for this provider if it does not already exist
     * @return This respawn providers instance
     */
    private DatRespawn createRespawn() {
        if (datRespawn == null) datRespawn = new DatRespawn();
        return datRespawn;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == RESPAWN_HANDLER)  {
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        DatRespawn datRespawnInstance = createRespawn();

        nbt.putBoolean("bedReset", datRespawnInstance.bedReset);

        CompoundTag level = new CompoundTag();
        level.putString("registry", datRespawnInstance.respawnLevel.registry().toString());
        level.putString("location", datRespawnInstance.respawnLevel.location().toString());
        nbt.put("level", level);

        CompoundTag pos = new CompoundTag();
        pos.putInt("x", datRespawnInstance.respawnPos.getX());
        pos.putInt("y", datRespawnInstance.respawnPos.getY());
        pos.putInt("z", datRespawnInstance.respawnPos.getZ());
        nbt.put("pos", pos);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        DatRespawn datRespawnInstance = createRespawn();
        datRespawnInstance.bedReset = nbt.getBoolean("bedReset");

        CompoundTag levelTag = nbt.getCompound("level");

        datRespawnInstance.respawnLevel = ResourceKey.create(ResourceKey.createRegistryKey(new ResourceLocation(levelTag.getString("registry"))), new ResourceLocation(levelTag.getString("location")));

        CompoundTag pos = nbt.getCompound("pos");
        datRespawnInstance.respawnPos = new BlockPos(pos.getInt("x"), pos.getInt("y"), pos.getInt("z"));
    }
}
