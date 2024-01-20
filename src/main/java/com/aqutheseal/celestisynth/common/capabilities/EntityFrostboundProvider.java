package com.aqutheseal.celestisynth.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityFrostboundProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<EntityFrostbound> ENTITY_FROSTBOUND = CapabilityManager.get(new CapabilityToken<EntityFrostbound>(){});

    private EntityFrostbound frostbound = null;
    private final LazyOptional<EntityFrostbound> optional = LazyOptional.of(this::createEntityFrostbound);

    private EntityFrostbound createEntityFrostbound() {
        if (frostbound == null) {
            this.frostbound = new EntityFrostbound();
        }
        return frostbound;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ENTITY_FROSTBOUND) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createEntityFrostbound().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
       createEntityFrostbound().loadNBTData(nbt);
    }
}
