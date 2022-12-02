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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class RespawnProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<Respawn> RESPAWN_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});

    private Respawn respawn = null;
    private final LazyOptional<Respawn> opt = LazyOptional.of(this::createRespawn);

    private Respawn createRespawn() {
        if (respawn == null) respawn = new Respawn();
        return respawn;
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

        nbt.putBoolean("bedReset", respawn.bedReset);

        ListTag map = new ListTag();
        Map<ResourceKey<Level>, BlockPos> respawnPoints = respawn.getRespawnPoints();
        for (ResourceKey<Level> levelKey : respawnPoints.keySet()) {
            CompoundTag store = new CompoundTag();

            CompoundTag level = new CompoundTag();
            level.putString("registry", levelKey.registry().toString());
            level.putString("location", levelKey.location().toString());
            store.put("level", level);

            CompoundTag pos = new CompoundTag();
            pos.putInt("x", respawnPoints.get(levelKey).getX());
            pos.putInt("y", respawnPoints.get(levelKey).getY());
            pos.putInt("z", respawnPoints.get(levelKey).getZ());
            store.put("pos", pos);

            map.add(store);
        }
        nbt.put("spawn", map);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createRespawn();

        respawn.bedReset = nbt.getBoolean("bedReset");

        for (Tag spawnRaw : nbt.getList("spawn", Tag.TAG_COMPOUND)) {
            CompoundTag spawn = (CompoundTag) spawnRaw;
            CompoundTag levelTag = spawn.getCompound("level");

            ResourceKey<Level> level = ResourceKey.create(ResourceKey.createRegistryKey(new ResourceLocation(levelTag.getString("registry"))), new ResourceLocation(levelTag.getString("location")));

            CompoundTag pos = spawn.getCompound("pos");
            BlockPos position = new BlockPos(pos.getInt("x"), pos.getInt("y"), pos.getInt("z"));
            respawn.setRespawnPointForLevel(level, position);
        }
    }
}
