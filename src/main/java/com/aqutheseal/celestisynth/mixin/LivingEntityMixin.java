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
    public int tagTimer;

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
    }

    @Inject(method = "defineSynchedData", at = @At(value = "HEAD"))
    public void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(PHANTOM_TAG_BY, Optional.empty());
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "HEAD"))
    protected void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setPhantomTagFrom(uuid);
            } catch (Throwable throwable) {
                throw new IllegalStateException("...Phantom Tag got no goddamn owner.");
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "HEAD"))
    protected void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getPhantomTagFrom() != null) {
            tag.putUUID("Owner", getPhantomTagFrom());
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
}
