package com.aqutheseal.celestisynth.common.entity.base;

import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public abstract class EffectControllerEntity extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> ANGLE_X = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_Y = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_Z = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_X = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_Y = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_Z = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    public static final SoundEvent[] BASE_WEAPON_EFFECTS = {
            CSSoundEvents.CS_SWORD_SWING.get(),
            CSSoundEvents.CS_SWORD_SWING_FIRE.get(),
            CSSoundEvents.CS_AIR_SWING.get(),
            CSSoundEvents.CS_SWORD_CLASH.get(),
            CSSoundEvents.CS_FIRE_SHOOT.get(),
            CSSoundEvents.CS_IMPACT_HIT.get()
    };

    public EffectControllerEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void playRandomBladeSound(Entity entity, int length) {
        SoundEvent randomSound = BASE_WEAPON_EFFECTS[entity.level.getRandom().nextInt(length)];

        entity.playSound(randomSound, 0.35F, 0.5F + new Random().nextFloat());
    }

    @Override
    public void tick() {
        super.tick();

        UUID ownerUuid = getOwnerUuid();
        Player ownerPlayer = ownerUuid == null ? null : getLevel().getPlayerByUUID(ownerUuid);

        if (ownerPlayer == null || ownerPlayer.isDeadOrDying()) remove(RemovalReason.DISCARDED);
    }

    public abstract Item getCorrespondingItem();

    public CSWeapon fromInterfaceWeapon() {
        if (getCorrespondingItem() instanceof CSWeapon interfaceApplied) return interfaceApplied;
        else throw new IllegalStateException("Item [" + getCorrespondingItem() + "] is an invalid Celestisynth weapon for [" + getDisplayName() + "].");
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(ANGLE_X, 0F);
        this.entityData.define(ANGLE_Y, 0F);
        this.entityData.define(ANGLE_Z, 0F);
        this.entityData.define(ANGLE_ADD_X, 0F);
        this.entityData.define(ANGLE_ADD_Y, 0F);
        this.entityData.define(ANGLE_ADD_Z, 0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        this.remove(RemovalReason.DISCARDED);
    }

    /**
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        UUID potentialOwnerUUID = tag.hasUUID("Owner") ? tag.getUUID("Owner") : tag.getString("Owner") != null ? OldUsersConverter.convertMobOwnerIfNecessary(getServer(), tag.getString("Owner")) : null;

        if (potentialOwnerUUID != null) setOwnerUuid(potentialOwnerUUID);
        else Celestisynth.LOGGER.warn("Missing Owner UUID for CSControllerEntity! Crashes/unexpected behaviour may occur if the owner is retrieved at any point. Attempting to remove entity from level...");

        setAngleX(tag.getFloat("cs.angleX"));
        setAngleY(tag.getFloat("cs.angleY"));
        setAngleZ(tag.getFloat("cs.angleZ"));
        setAddAngleX(tag.getFloat("cs.angleAddX"));
        setAddAngleY(tag.getFloat("cs.angleAddY"));
        setAddAngleZ(tag.getFloat("cs.angleAddZ"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (getOwnerUuid() != null) tag.putUUID("Owner", getOwnerUuid());

        tag.putFloat("cs.angleX", getAngleX());
        tag.putFloat("cs.angleY", getAngleY());
        tag.putFloat("cs.angleZ", getAngleZ());
        tag.putFloat("cs.angleAddX", getAddAngleX());
        tag.putFloat("cs.angleAddY", getAddAngleY());
        tag.putFloat("cs.angleAddZ", getAddAngleZ());
    }
    **/

    public void setOwnerUuid(@Nullable UUID ownerUuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(ownerUuid));
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    public void setAngleX(float angleX) {
        this.entityData.set(ANGLE_X, angleX);
    }

    public float getAngleX() {
        return this.entityData.get(ANGLE_X);
    }

    public void setAngleY(float angleY) {
        this.entityData.set(ANGLE_Y, angleY);
    }

    public float getAngleY() {
        return this.entityData.get(ANGLE_Y);
    }

    public void setAngleZ(float angleZ) {
        this.entityData.set(ANGLE_Z, angleZ);
    }

    public float getAngleZ() {
        return this.entityData.get(ANGLE_Z);
    }

    public void setAddAngleX(float angleX) {
        this.entityData.set(ANGLE_ADD_X, angleX);
    }

    public float getAddAngleX() {
        return this.entityData.get(ANGLE_ADD_X);
    }

    public void setAddAngleY(float angleY) {
        this.entityData.set(ANGLE_ADD_Y, angleY);
    }

    public float getAddAngleY() {
        return this.entityData.get(ANGLE_ADD_Y);
    }

    public void setAddAngleZ(float angleZ) {
        this.entityData.set(ANGLE_ADD_Z, angleZ);
    }

    public float getAddAngleZ() {
        return this.entityData.get(ANGLE_ADD_Z);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
