package com.aqutheseal.celestisynth.common.capabilities;

import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import dev._100media.capabilitysyncer.core.LivingEntityCapability;
import dev._100media.capabilitysyncer.network.EntityCapabilityStatusPacket;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.simple.SimpleChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CSEntityCapability extends LivingEntityCapability implements CSCapabilityHelper {
    public static final String ID = "celestisynthCapabilities";

    public static final String FROSTBOUND_ID = "cs.frostBound";
    public static final String PHANTOM_TAG_SOURCE_ID = "cs.phantomTagSource";
    public static final String PHANTOM_TAG_TIME_ID = "cs.phantomTagTime";
    public static final String QUASAR_IMBUE_SOURCE_ID = "cs.quasarImbueSource";
    public static final String QUASAR_IMBUE_TIME_ID = "cs.quasarImbueTime";

    private int frostBound;
    private @Nullable LivingEntity phantomTagSource;
    private int phantomTagTime;
    private @Nullable LivingEntity quasarImbueSource;
    private int quasarImbueTime;

    protected CSEntityCapability(LivingEntity entity) {
        super(entity);
    }

    // FROSTBOUND

    public int getFrostbound() {
        return frostBound;
    }

    public void setFrostbound(int value) {
        if (this.frostBound < value) {
            this.frostBound = value;
            this.updateTracking();
        }
    }

    public void decreaseFrostbound(int value) {
        this.frostBound = Math.max(frostBound - value, 0);
        this.updateTracking();
    }

    public void decreaseFrostbound() {
       decreaseFrostbound(1);
    }

    // PHANTOM TAG

    public void setPhantomTag(@Nonnull LivingEntity source, int time) {
        this.phantomTagSource = source;
        this.updateTracking();
        this.phantomTagTime = time;
    }

    public @Nullable LivingEntity getPhantomTagSource() {
        return phantomTagSource;
    }

    public int getPhantomTagTime() {
        return phantomTagTime;
    }

    public void clearPhantomTag() {
        if (phantomTagSource != null) {
            this.phantomTagSource = null;
            this.updateTracking();
        }
    }

    public void decreasePhantomTagTime() {
        this.phantomTagTime = Math.max(this.phantomTagTime - 1, 0);
        //this.updateTracking();
    }

    // QUASAR IMBUE

    public void setQuasarImbue(@Nonnull LivingEntity source, int time) {
        this.quasarImbueSource = source;
        this.updateTracking();
        this.quasarImbueTime = time;
    }


    public @Nullable LivingEntity getQuasarImbueSource() {
        return quasarImbueSource;
    }

    public int getQuasarImbueTime() {
        return quasarImbueTime;
    }

    public void clearQuasarImbue() {
        if (quasarImbueSource != null) {
            this.quasarImbueSource = null;
            this.updateTracking();
        }
    }

    public void decreaseQuasarImbueTime() {
        if (quasarImbueTime > 0) {
            this.quasarImbueTime = this.quasarImbueTime - 1;
            //this.updateTracking();
        }
    }

    @Override
    public CompoundTag serializeNBT(boolean savingToDisk) {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt(FROSTBOUND_ID, this.frostBound);

        nbt.putInt(PHANTOM_TAG_SOURCE_ID, this.phantomTagSource != null ? this.phantomTagSource.getId() : 0);
        nbt.putInt(PHANTOM_TAG_TIME_ID, this.phantomTagTime);

        nbt.putInt(QUASAR_IMBUE_SOURCE_ID, this.quasarImbueSource != null ? this.quasarImbueSource.getId() : 0);
        nbt.putInt(QUASAR_IMBUE_TIME_ID, this.quasarImbueTime);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt, boolean readingFromDisk) {
        if (nbt.contains(FROSTBOUND_ID, Tag.TAG_INT)) {
            this.frostBound = nbt.getInt(FROSTBOUND_ID);
        }

        if (checkBoth(nbt, Tag.TAG_INT, PHANTOM_TAG_SOURCE_ID, PHANTOM_TAG_TIME_ID)) {
            this.phantomTagSource = getLivingFromWorld(livingEntity.level(), nbt.getInt(PHANTOM_TAG_SOURCE_ID));
            this.phantomTagTime = nbt.getInt(PHANTOM_TAG_TIME_ID);
        }

        if (checkBoth(nbt, Tag.TAG_INT, QUASAR_IMBUE_SOURCE_ID, QUASAR_IMBUE_TIME_ID)) {
            this.quasarImbueSource = getLivingFromWorld(livingEntity.level(), nbt.getInt(QUASAR_IMBUE_SOURCE_ID));
            this.quasarImbueTime = nbt.getInt(QUASAR_IMBUE_TIME_ID);
        }
    }

    @Override
    public EntityCapabilityStatusPacket createUpdatePacket() {
        return new SimpleEntityCapabilityStatusPacket(this.livingEntity.getId(), CSEntityCapabilityProvider.CS_ENTITY_CAP_RL, this);
    }

    @Override
    public SimpleChannel getNetworkChannel() {
        return CSNetworkManager.INSTANCE;
    }
}
