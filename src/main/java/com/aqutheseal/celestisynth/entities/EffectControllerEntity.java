package com.aqutheseal.celestisynth.entities;

import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
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

    public EffectControllerEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    SoundEvent[] BASE_WEAPON_EFFECTS = {
            CSSoundRegistry.CS_SWORD_SWING.get(),
            CSSoundRegistry.CS_SWORD_SWING_FIRE.get(),
            CSSoundRegistry.CS_AIR_SWING.get(),
            CSSoundRegistry.CS_SWORD_CLASH.get(),
            CSSoundRegistry.CS_FIRE_SHOOT.get(),
            CSSoundRegistry.CS_IMPACT_HIT.get()
    };

    public void playRandomBladeSound(Entity entity, int length) {
        SoundEvent randomSound = BASE_WEAPON_EFFECTS[new Random().nextInt(length)];
        entity.playSound(randomSound, 0.35F, 0.5F + new Random().nextFloat());
    }

    @Override
    public void tick() {
        super.tick();
        UUID uuid = this.getOwnerUuid();
        Player player = uuid == null ? null : this.getLevel().getPlayerByUUID(uuid);

        if (player == null || player.isDeadOrDying()) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public abstract Item getCorrespondingItem();

    public CSWeapon fromInterfaceWeapon() {
        if (getCorrespondingItem() instanceof CSWeapon interfaceApplied) {
            return interfaceApplied;
        } else {
            throw new IllegalStateException("Item [" + getCorrespondingItem() + "] is an invalid Celestisynth weapon for [" + getDisplayName() + "].");
        }
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
    protected void readAdditionalSaveData(CompoundTag tag) {
        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerUuid(uuid);
            } catch (Throwable throwable) {
                throw new IllegalStateException("...Crescentia-Ranged got no goddamn owner.");
            }
        }
        setAngleX(tag.getFloat("cs.angleX"));
        setAngleY(tag.getFloat("cs.angleY"));
        setAngleZ(tag.getFloat("cs.angleZ"));
        setAddAngleX(tag.getFloat("cs.angleAddX"));
        setAddAngleY(tag.getFloat("cs.angleAddY"));
        setAddAngleZ(tag.getFloat("cs.angleAddZ"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (this.getOwnerUuid() != null) {
            tag.putUUID("Owner", getOwnerUuid());
        }
        tag.putFloat("cs.angleX", getAngleX());
        tag.putFloat("cs.angleY", getAngleY());
        tag.putFloat("cs.angleZ", getAngleZ());
        tag.putFloat("cs.angleAddX", getAddAngleX());
        tag.putFloat("cs.angleAddY", getAddAngleY());
        tag.putFloat("cs.angleAddZ", getAddAngleZ());
    }

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
