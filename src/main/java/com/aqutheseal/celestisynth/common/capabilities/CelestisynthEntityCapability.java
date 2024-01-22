package com.aqutheseal.celestisynth.common.capabilities;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import dev._100media.capabilitysyncer.core.LivingEntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class CelestisynthEntityCapability extends LivingEntityCapability {
    public static final String ID = "celestisynthCapabilities";

    public static UUID emptyUUID = new UUID(0L, 0L);

    public static final String FROSTBOUND_ID = "cs.frostBound";
    public static final String PHANTOM_TAG_SOURCE_ID = "cs.phantomTagSource";
    public static final String PHANTOM_TAG_TIME_ID = "cs.phantomTagTime";

    private int frostBound;
    private @Nullable Player phantomTagSource;
    private int phantomTagTime;

    protected CelestisynthEntityCapability(LivingEntity entity) {
        super(entity);
    }

    // FROSTBOUND

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

    // PHANTOM TAG

    public void setTag(@Nonnull Player source, int time) {
        this.phantomTagSource = source;
        this.phantomTagTime = time;
        this.updateTracking();
    }

    public @Nullable Entity getTagSource() {
        return phantomTagSource;
    }

    public int getTagTime() {
        return phantomTagTime;
    }

    public void removeSource() {
        this.phantomTagSource = null;
        this.updateTracking();
    }

    public void decreaseTagTime() {
        this.phantomTagTime = Math.max(this.phantomTagTime - 1, 0);
        this.updateTracking();
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(ID, this.frostBound);

        nbt.putUUID(PHANTOM_TAG_SOURCE_ID, this.phantomTagSource != null ? this.phantomTagSource.getUUID() : emptyUUID);
        nbt.putInt(PHANTOM_TAG_TIME_ID, this.phantomTagTime);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt.contains(ID, Tag.TAG_INT)) {
            this.frostBound = nbt.getInt(FROSTBOUND_ID);
        }

        if (nbt.getUUID(PHANTOM_TAG_SOURCE_ID) != emptyUUID) this.phantomTagSource = entity.level.getPlayerByUUID(nbt.getUUID(PHANTOM_TAG_SOURCE_ID));
        else this.phantomTagSource = null;

        if (nbt.contains(ID, Tag.TAG_INT)) this.phantomTagTime = nbt.getInt(PHANTOM_TAG_TIME_ID);
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
