package com.aqutheseal.celestisynth.common.entity.base;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class CSEffectEntity extends Entity implements IAnimatable {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(CSEffectEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> VISUAL_ID = SynchedEntityData.defineId(CSEffectEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ANIMATION_ID = SynchedEntityData.defineId(CSEffectEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FRAME_LEVEL = SynchedEntityData.defineId(CSEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SET_ROT_X = SynchedEntityData.defineId(CSEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SET_ROT_Z = SynchedEntityData.defineId(CSEffectEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> CUSTOMIZABLE_SIZE = SynchedEntityData.defineId(CSEffectEntity.class, EntityDataSerializers.FLOAT);
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final AnimationBuilder DEFAULT_ANIMATION = new AnimationBuilder().addAnimation("animation.cs_effect.spin", ILoopType.EDefaultLoopTypes.LOOP);
    private Entity toFollow;
    private int lifespan;
    private int frameTimer;
    private final AnimationController<CSEffectEntity> mainController = new AnimationController<>(this, "main_controller", 2, this::mainPredicate);

    public CSEffectEntity(EntityType<? extends CSEffectEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    private <E extends IAnimatable> PlayState mainPredicate(AnimationEvent<E> event) {
        if (getAnimationID() != null && getVisualType().getAnimation() != null) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(getAnimationID(), ILoopType.EDefaultLoopTypes.LOOP));

            if (event.getController().getAnimationState().equals(AnimationState.Stopped)) event.getController().markNeedsReload();
        } else {
            Celestisynth.LOGGER.warn("Animation is null!");
            event.getController().setAnimation(DEFAULT_ANIMATION);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(mainController);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public CSVisualType getVisualType() {
        if (getVisualID() != null && !getVisualID().equals("none")) {
            for (RegistryObject<CSVisualType> visual : CSVisualTypes.VISUALS.getEntries()) {
                if (getVisualID().equals(visual.get().getName())) {
                    return visual.get();
                }
            }
        }
        return getDefaultVisual();
    }

    public void setVisualType(CSVisualType getEffectType) {
        setVisualID(getEffectType.getName());
    }

    public String getVisualID() {
        return this.entityData.get(VISUAL_ID);
    }

    public void setVisualID(String value) {
        this.entityData.set(VISUAL_ID, value);
    }

    public String getAnimationID() {
        return this.entityData.get(ANIMATION_ID);
    }

    public void setAnimationID(String value) {
        this.entityData.set(ANIMATION_ID, value);
    }

    public int getFrameLevel() {
        return this.entityData.get(FRAME_LEVEL);
    }

    public void setFrameLevel(int value) {
        this.entityData.set(FRAME_LEVEL, value);
    }

    public void setRotationX(int rotationX) {
        this.entityData.set(SET_ROT_X, rotationX);
    }

    public void setRotationZ(int rotationZ) {
        this.entityData.set(SET_ROT_Z, rotationZ);
    }

    public int getRotationX() {
        return this.entityData.get(SET_ROT_X);
    }

    public int getRotationZ() {
        return this.entityData.get(SET_ROT_Z);
    }

    public float getCustomizableSize() {
        return this.entityData.get(CUSTOMIZABLE_SIZE);
    }

    public void setCustomizableSize(float size) {
        this.entityData.set(CUSTOMIZABLE_SIZE, size);
    }

    public void setOwnerUuid(@Nullable UUID ownerUuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(ownerUuid));
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    public Entity getToFollow() {
        return this.toFollow;
    }

    public void setToFollow(Entity livingEntity) {
        this.toFollow = livingEntity;
    }

    public void setLifespan(int value) {
        lifespan = value;
    }

    @Nullable
    public static CSEffectEntity getEffectInstance(Player owner, @Nullable Entity toFollow, CSVisualType visual, double offsetX, double offsetY, double offsetZ) {
        if (owner == null) return null;

        CSEffectEntity slash = CSEntityTypes.CS_EFFECT.get().create(owner.level);
        slash.setVisualID(visual.getName());
        slash.setAnimationID(visual.getAnimation().getAnimName());

        if (toFollow != null) {
            slash.moveTo(toFollow.getX() + offsetX, (toFollow.getY() - 1.5) + offsetY, toFollow.getZ() + offsetZ);
        } else {
            slash.moveTo(owner.getX()  + offsetX, (owner.getY() - 1.5) + offsetY, owner.getZ() + offsetZ);
        }
        slash.setOwnerUuid(owner.getUUID());
        slash.setToFollow(toFollow);

        slash.setRandomRotation();
        slash.setXRot(owner.getXRot());
        slash.xRotO = slash.getXRot();
        slash.setYRot(owner.getYRot());
        slash.yRotO = slash.getYRot();
        slash.setRot(slash.getYRot(), slash.getXRot());

        return slash;
    }

    public void setRandomRotation() {
        int rotationX = random.nextInt(360);
        int rotationZ = random.nextInt(360);

        setRotationX(rotationX);
        setRotationZ(rotationZ);
    }

    public static void createInstance(Player owner, @Nullable Entity toFollow, CSVisualType effectTypes) {
       createInstance(owner, toFollow, effectTypes, 0, 0, 0);
    }

    public static void createInstance(Player owner, @Nullable Entity toFollow, CSVisualType effectTypes, double xOffset, double yOffset, double zOffset) {
        if (owner == null) return;

        CSEffectEntity slash = getEffectInstance(owner, toFollow, effectTypes, xOffset, yOffset, zOffset);
        slash.setOwnerUuid(owner.getUUID());
        slash.setToFollow(toFollow);

        owner.level.addFreshEntity(slash);
    }

    @Override
    public void tick() {
        if (getOwnerUuid() == null) {
            remove(RemovalReason.DISCARDED);
            Celestisynth.LOGGER.debug("Removed CSEffect with no owner!");
        }

        frameTimer++;

        if (frameTimer >= (getVisualType().getFramesSpeed())) {
            setFrameLevel(getFrameLevel() == getVisualType().getFrames() ? 1 : getFrameLevel() + 1);

            frameTimer = 0;
        }

        if (toFollow != null) {
            if (getVisualType() == CSVisualTypes.AQUAFLORA_FLOWER_BIND.get()) {
                calculateCustomizableSize((float) (toFollow.getBbWidth() - (toFollow.getBbWidth() / 4.5)), -getCustomizableSize());
            }
        }

        lifespan++;

        if (lifespan >= getVisualType().getAnimation().getLifespan()) {
            setLifespan(0);
            remove(RemovalReason.DISCARDED);
        }

        super.tick();
    }

    public void calculateCustomizableSize(float size, double yOff) {
        setYRot(toFollow.getYRot());

        this.yRotO = getYRot();

        setRot(getYRot(), getXRot());
        setCustomizableSize(size);
        moveTo(toFollow.getX(), toFollow.getY() + yOff, toFollow.getZ());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(VISUAL_ID, "none");
        this.entityData.define(ANIMATION_ID, "none");
        this.entityData.define(FRAME_LEVEL, 1);
        this.entityData.define(SET_ROT_X, 0);
        this.entityData.define(SET_ROT_Z, 0);
        this.entityData.define(CUSTOMIZABLE_SIZE, 1F);
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
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        UUID potentialOwnerUUID = compoundNBT.hasUUID("Owner") ? compoundNBT.getUUID("Owner") : compoundNBT.getString("Owner") != null ? OldUsersConverter.convertMobOwnerIfNecessary(getServer(), compoundNBT.getString("Owner")) : null;

        if (potentialOwnerUUID != null) setOwnerUuid(potentialOwnerUUID);
        else Celestisynth.LOGGER.warn("Missing Owner UUID for CSEffect! Crashes/unexpected behaviour may occur if the owner is retrieved at any point. Attempting to remove entity from level...");

        setLifespan(compoundNBT.getInt("lifespan"));
        setVisualID(compoundNBT.getString("visualID"));
        setAnimationID(compoundNBT.getString("animID"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        if (getOwnerUuid() != null) compoundNBT.putUUID("Owner", getOwnerUuid());

        compoundNBT.putInt("lifespan", lifespan);
        compoundNBT.putString("visualID", getVisualID());
        compoundNBT.putString("animID", getAnimationID());
    }
    **/

    public CSVisualType getDefaultVisual() {
        return CSVisualTypes.SOLARIS_BLITZ.get();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}