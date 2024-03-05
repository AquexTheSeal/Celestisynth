package com.aqutheseal.celestisynth.common.entity.base;

import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class EffectControllerEntity extends Entity implements CSWeaponUtil {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<ItemStack> ORIGIN_ITEM = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Float> ANGLE_X = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_Y = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_Z = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_X = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_Y = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_Z = SynchedEntityData.defineId(EffectControllerEntity.class, EntityDataSerializers.FLOAT);
    public static final SoundEvent[] BASE_WEAPON_EFFECTS = {
            CSSoundEvents.SWORD_SWING.get(),
            CSSoundEvents.SWORD_SWING_FIRE.get(),
            CSSoundEvents.AIR_SWING.get(),
            CSSoundEvents.SWORD_CLASH.get(),
            CSSoundEvents.FIRE_SHOOT.get(),
            CSSoundEvents.IMPACT_HIT.get()
    };

    public EffectControllerEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        UUID ownerUuid = getOwnerUuid();
        Player ownerPlayer = ownerUuid == null ? null : level().getPlayerByUUID(ownerUuid);
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
        this.entityData.define(ORIGIN_ITEM, ItemStack.EMPTY);
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

    public int getDisappearRange() {
        return 64;
    }

    @Override
    public void remove(RemovalReason pReason) {
        double range = getDisappearRange();
        List<Entity> surroundingEntities = level().getEntitiesOfClass(Entity.class, new AABB(getX() + range, getY() + range, getZ() + range, getX() - range, getY() - range, getZ() - range));
        for (Entity entityBatch : surroundingEntities) {
            if (entityBatch instanceof CSEffectEntity effect) {
                if (effect.getToFollow() == this) effect.remove(RemovalReason.DISCARDED);
            }
        }
        super.remove(pReason);
    }

    public void setOwnerUuid(@Nullable UUID ownerUuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(ownerUuid));
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    public void setOriginItem(ItemStack stack) {
        this.entityData.set(ORIGIN_ITEM, stack);
    }

    public ItemStack getOriginItem() {
        return this.entityData.get(ORIGIN_ITEM);
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
