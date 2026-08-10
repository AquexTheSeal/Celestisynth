package org.thecelestialworkshop.celestisynth.common.capabilities;

import org.thecelestialworkshop.celestisynth.common.network.s2c.EntityCapabilitySyncPacket;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.INBTSerializable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Per-living-entity state, formerly a Forge capability backed by the
 * CapabilitySyncer library, now a NeoForge data attachment with a custom
 * tracking-sync packet.
 */
public class CSEntityCapability implements CSCapabilityHelper, INBTSerializable<CompoundTag> {
    public static final String ID = "celestisynthEntityCapabilities";

    public static final String FROSTBOUND_ID = "cs.frostBound";
    public static final String TRUE_INVISIBILITY_ID = "cs.trueInvisibility";
    public static final String PHANTOM_TAG_SOURCE_ID = "cs.phantomTagSource";
    public static final String PHANTOM_TAG_TIME_ID = "cs.phantomTagTime";
    public static final String QUASAR_IMBUE_SOURCE_ID = "cs.quasarImbueSource";
    public static final String QUASAR_IMBUE_TIME_ID = "cs.quasarImbueTime";

    protected final LivingEntity livingEntity;

    private int frostBound;
    private int trueInvisibility;
    private int bloodthirst;
    private @Nullable LivingEntity phantomTagSource;
    private int phantomTagTime;
    private @Nullable LivingEntity quasarImbueSource;
    private int quasarImbueTime;

    public CSEntityCapability(IAttachmentHolder holder) {
        this.livingEntity = (LivingEntity) holder;
    }

    public void updateTracking() {
        if (livingEntity.level() instanceof ServerLevel) {
            CSNetworkManager.sendToPlayersTrackingEntityAndSelf(new EntityCapabilitySyncPacket(livingEntity.getId(), writeSyncTag()), livingEntity);
        }
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

    // TRUE INVISIBILITY

    public int getTrueInvisibility() {
        return trueInvisibility;
    }

    public void setTrueInvisibility(int value) {
        if (this.trueInvisibility < value) {
            this.trueInvisibility = value;
            this.updateTracking();
        }
    }

    public void decreaseTrueInvisibility(int value) {
        this.trueInvisibility = Math.max(trueInvisibility - value, 0);
        this.updateTracking();
    }

    public void decreaseTrueInvisibility() {
        decreaseTrueInvisibility(1);
    }

    // PHANTOM TAG

    public void setPhantomTag(@NotNull LivingEntity source, int time) {
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
    }

    // QUASAR IMBUE

    public void setQuasarImbue(@NotNull LivingEntity source, int time) {
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
        }
    }

    public CompoundTag writeSyncTag() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt(FROSTBOUND_ID, this.frostBound);
        nbt.putInt(TRUE_INVISIBILITY_ID, this.trueInvisibility);

        nbt.putInt(PHANTOM_TAG_SOURCE_ID, this.phantomTagSource != null ? this.phantomTagSource.getId() : 0);
        nbt.putInt(PHANTOM_TAG_TIME_ID, this.phantomTagTime);

        nbt.putInt(QUASAR_IMBUE_SOURCE_ID, this.quasarImbueSource != null ? this.quasarImbueSource.getId() : 0);
        nbt.putInt(QUASAR_IMBUE_TIME_ID, this.quasarImbueTime);

        return nbt;
    }

    public void readSyncTag(CompoundTag nbt) {
        if (nbt.contains(FROSTBOUND_ID, Tag.TAG_INT)) {
            this.frostBound = nbt.getInt(FROSTBOUND_ID);
        }

        if (nbt.contains(TRUE_INVISIBILITY_ID, Tag.TAG_INT)) {
            this.trueInvisibility = nbt.getInt(TRUE_INVISIBILITY_ID);
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
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return writeSyncTag();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        readSyncTag(nbt);
    }
}
