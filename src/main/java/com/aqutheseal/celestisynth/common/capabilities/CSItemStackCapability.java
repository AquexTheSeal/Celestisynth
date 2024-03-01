package com.aqutheseal.celestisynth.common.capabilities;

import dev._100media.capabilitysyncer.core.ItemStackCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class CSItemStackCapability extends ItemStackCapability implements CSCapabilityHelper {
    public static final String ID = "celestisynthItemStackCapabilities";

    public static final String SKIN_INDEX = "cs.aquaSkin";
    private int skinIndex;

    protected CSItemStackCapability(ItemStack itemStack) {
        super(itemStack);
    }

    public int getSkinIndex() {
        return skinIndex;
    }

    public void setSkinIndex(int value) {
        skinIndex = value;
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(SKIN_INDEX, this.skinIndex);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.skinIndex = nbt.getInt(SKIN_INDEX);
    }
}
