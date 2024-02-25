package com.aqutheseal.celestisynth.common.capabilities;

import dev._100media.capabilitysyncer.core.ItemStackCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class CSItemStackCapability extends ItemStackCapability implements CSCapabilityHelper {
    public static final String ID = "celestisynthItemStackCapabilities";

    public static final String AQUA_SKIN = "cs.aquaSkin";
    private boolean aquaSkin;

    protected CSItemStackCapability(ItemStack itemStack) {
        super(itemStack);
    }

    public boolean usingAquaSkin() {
        return aquaSkin;
    }

    public void useAquaSkin(boolean value) {
        aquaSkin = value;
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean(AQUA_SKIN, this.aquaSkin);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        this.aquaSkin = nbt.getBoolean(AQUA_SKIN);
    }
}
