package com.aqutheseal.celestisynth.common.capabilities;

import net.minecraft.nbt.CompoundTag;

public class EntityFrostbound {
    private int frostBound;

    public int getFrostbound() {
        return frostBound;
    }

    public void setFrostbound(int value) {
        this.frostBound = value;
    }

    public void decreaseFrostbound() {
        this.frostBound = Math.max(frostBound - 1, 0);
    }

    public void copyFrom(EntityFrostbound source) {
        this.frostBound = source.frostBound;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("thirst", frostBound);
    }

    public void loadNBTData(CompoundTag nbt) {
        frostBound = nbt.getInt("thirst");
    }
}
