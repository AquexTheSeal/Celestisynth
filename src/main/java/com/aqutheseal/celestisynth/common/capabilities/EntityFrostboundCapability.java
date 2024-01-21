package com.aqutheseal.celestisynth.common.capabilities;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import dev._100media.capabilitysyncer.core.LivingEntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.simple.SimpleChannel;

public class EntityFrostboundCapability extends LivingEntityCapability {
    public static String ID = "cs.frostbound";
    private int frostBound;

    protected EntityFrostboundCapability(LivingEntity entity) {
        super(entity);
    }

    public int getFrostbound() {
        return frostBound;
    }

    public void setFrostbound(int value) {
        this.frostBound = value;
        this.updateTracking();
    }

    public void decreaseFrostbound() {
        this.frostBound = Math.max(frostBound - 1, 0);
        this.updateTracking();
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("frostbound", this.frostBound);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt.contains("frostbound", Tag.TAG_INT)) {
            this.frostBound = nbt.getInt("frostbound");
        }
    }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.livingEntity.getId(), Celestisynth.prefix(ID), this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return CSNetworkManager.INSTANCE;
    }
}
