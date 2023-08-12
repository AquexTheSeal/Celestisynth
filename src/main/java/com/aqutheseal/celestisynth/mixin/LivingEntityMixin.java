package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.LivingMixinSupport;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingMixinSupport {
    private static final EntityDataAccessor<Optional<UUID>> PHANTOM_TAG_BY = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> QUASAR_IMBUED_BY = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> QUASAR_TARGET = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    public int tagTimer;
    public int quasarTimer;
    public int quasarTargetTimer;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "aiStep", at = @At(value = "HEAD"))
    public void aiStep(CallbackInfo ci) {
        if (tagTimer > 0) {
            tagTimer--;
        } else {
            setPhantomTagger(null);
        }
        if (quasarTimer > 0) {
            quasarTimer--;
        } else {
            setQuasarImbued(null);
        }
    }

    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"))
    public void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(PHANTOM_TAG_BY, Optional.empty());
        this.entityData.define(QUASAR_IMBUED_BY, Optional.empty());
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "HEAD"))
    protected void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        readTag(tag, "PhantomTag", PHANTOM_TAG_BY);
        readTag(tag, "QuasarImbued", QUASAR_IMBUED_BY);
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "HEAD"))
    protected void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getPhantomTagFrom() != null) {
            tag.putUUID("QuasarImbued", getPhantomTagFrom());
        }
        if (this.getQuasarImbuedFrom() != null) {
            tag.putUUID("QuasarImbued", getQuasarImbuedFrom());
        }
    }

    public @Nullable Player getPhantomTagger() {
        if (getPhantomTagFrom() != null) {
            return level.getPlayerByUUID(getPhantomTagFrom());
        } else {
            return null;
        }
    }

    public void setPhantomTagger(@Nullable Player tagger) {
        if (tagger != null) {
            setPhantomTagFrom(tagger.getUUID());
        } else {
            setPhantomTagFrom(null);
        }
    }

    public void setPhantomTagFrom(@Nullable UUID phantomTagFrom) {
        this.entityData.set(PHANTOM_TAG_BY, Optional.ofNullable(phantomTagFrom));
        tagTimer = 100;
    }

    @Nullable
    public UUID getPhantomTagFrom() {
        return this.entityData.get(PHANTOM_TAG_BY).orElse(null);
    }

    public @Nullable Player getQuasarImbued() {
        if (getQuasarImbuedFrom() != null) {
            return level.getPlayerByUUID(getQuasarImbuedFrom());
        } else {
            return null;
        }
    }

    public void setQuasarImbued(@Nullable Player imbuedTo) {
        if (imbuedTo != null) {
            setQuasarImbuedFrom(imbuedTo.getUUID());
        } else {
            setQuasarImbuedFrom(null);
        }
    }

    public void setQuasarImbuedFrom(@Nullable UUID imbuedFrom) {
        this.entityData.set(QUASAR_IMBUED_BY, Optional.ofNullable(imbuedFrom));
        quasarTimer = 60;
    }

    @Nullable
    public UUID getQuasarImbuedFrom() {
        return this.entityData.get(QUASAR_IMBUED_BY).orElse(null);
    }

    private void readTag(CompoundTag tag, String tagName, EntityDataAccessor<Optional<UUID>> data) {
        UUID uuid;
        if (tag.hasUUID(tagName)) {
            uuid = tag.getUUID(tagName);
        } else {
            String s = tag.getString(tagName);
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.entityData.set(data, Optional.ofNullable(uuid));
            } catch (Throwable throwable) {
                throw new IllegalStateException("..." + tagName + " got no goddamn owner.");
            }
        }
    }
}
