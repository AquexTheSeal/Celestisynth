package com.aqutheseal.celestisynth.common.entity.base;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.entity.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
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

public class CSEffect extends Entity implements IAnimatable {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> TYPE_ID = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ANIMATION_ID = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FRAME_LEVEL = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SET_ROT_X = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SET_ROT_Z = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> CUSTOMIZABLE_SIZE = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.FLOAT);
    private static final AnimationBuilder DEFAULT_ANIMATION = new AnimationBuilder().addAnimation("animation.cs_effect.spin", ILoopType.EDefaultLoopTypes.LOOP);
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final AnimationController<CSEffect> mainController = new AnimationController<>(this, "maincsecontroller", 0, this::predicate);
    private CSEffectTypes lockedInEffect;
    private Entity toFollow;
    private int lifespan;
    private int frameTimer;

    public CSEffect(EntityType<? extends CSEffect> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public CSEffectTypes getEffectType() {
        if (lockedInEffect == null) {
            for (CSEffectTypes candidEffectType : CSEffectTypes.values()) {
                if (candidEffectType.getName().equalsIgnoreCase(getTypeID())) {
                    setLockedInEffect(candidEffectType);

                    return candidEffectType;
                }
            }
        } else return lockedInEffect;

        return getDefaultEffect();
    }

    public void setEffectType(CSEffectTypes getEffectType) {
        this.setTypeID(getEffectType.getName());
    }

    public String getTypeID() {
        return this.entityData.get(TYPE_ID);
    }

    public void setTypeID(String value) {
        this.entityData.set(TYPE_ID, value);
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

    public void setLockedInEffect(CSEffectTypes lockedInEffect) {
        this.lockedInEffect = lockedInEffect;
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
    public static CSEffect getEffectInstance(Player owner, @Nullable Entity toFollow, CSEffectTypes effectTypes, double offsetX, double offsetY, double offsetZ) {
        if (owner == null) return null;

        CSEffect slash = CSEntityTypes.CS_EFFECT.get().create(owner.level);
        slash.setEffectType(effectTypes);
        slash.setAnimationID(effectTypes.getAnimation().getAnimName());
        if (toFollow != null) slash.moveTo(toFollow.getX() + offsetX, (toFollow.getY() - 1.5) + offsetY, toFollow.getZ() + offsetZ);
        else slash.moveTo(owner.getX()  + offsetX, (owner.getY() - 1.5) + offsetY, owner.getZ() + offsetZ);

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

    public static void createInstance(Player owner, @Nullable Entity toFollow, CSEffectTypes effectTypes) {
       createInstance(owner, toFollow, effectTypes, 0, 0, 0);
    }

    public static void createInstance(Player owner, @Nullable Entity toFollow, CSEffectTypes effectTypes, double xOffset, double yOffset, double zOffset) {
        if (owner == null) return;

        CSEffect slash = getEffectInstance(owner, toFollow, effectTypes, xOffset, yOffset, zOffset);
        slash.setOwnerUuid(owner.getUUID());
        slash.setToFollow(toFollow);

        owner.level.addFreshEntity(slash);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(mainController);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getAnimationID() != null) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(getAnimationID(), ILoopType.EDefaultLoopTypes.LOOP));
        } else {
            Celestisynth.LOGGER.warn("EffectType for CSEffect is null!");
            event.getController().setAnimation(DEFAULT_ANIMATION);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        if (getOwnerUuid() == null) {
            remove(RemovalReason.DISCARDED);
            Celestisynth.LOGGER.debug("Removed CSEffect with no owner!");
        }

        frameTimer++;

        if (frameTimer >= (getEffectType().getFramesSpeed())) {
            setFrameLevel(getFrameLevel() == getEffectType().getFrames() ? 1 : getFrameLevel() + 1);

            frameTimer = 0;
        }

        if (toFollow != null) {
            if (getEffectType() == CSEffectTypes.AQUAFLORA_FLOWER_BIND) {
                calculateCustomizableSize((float) (toFollow.getBbWidth() - (toFollow.getBbWidth() / 4.5)), -getCustomizableSize());
            }
        }

        lifespan++;

        if (lifespan >= getEffectType().getAnimation().getLifespan()) {
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
        this.entityData.define(TYPE_ID, "none");
        this.entityData.define(ANIMATION_ID, "none");
        this.entityData.define(FRAME_LEVEL, 1);
        this.entityData.define(SET_ROT_X, 0);
        this.entityData.define(SET_ROT_Z, 0);
        this.entityData.define(CUSTOMIZABLE_SIZE, 1F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        UUID potentialOwnerUUID = compoundNBT.hasUUID("Owner") ? compoundNBT.getUUID("Owner") : compoundNBT.getString("Owner") != null ? OldUsersConverter.convertMobOwnerIfNecessary(getServer(), compoundNBT.getString("Owner")) : null;

        if (potentialOwnerUUID != null) setOwnerUuid(potentialOwnerUUID);
        else Celestisynth.LOGGER.warn("Missing Owner UUID for CSEffect! Crashes/unexpected behaviour may occur if the owner is retrieved at any point. Attempting to remove entity from level...");

        setLifespan(compoundNBT.getInt("lifespan"));
        setTypeID(compoundNBT.getString("typeId"));
        setAnimationID(compoundNBT.getString("animationID"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        if (getOwnerUuid() != null) compoundNBT.putUUID("Owner", getOwnerUuid());

        compoundNBT.putInt("lifespan", lifespan);
        compoundNBT.putString("typeId", getTypeID());
        compoundNBT.putString("animationId", getAnimationID());
    }

    public CSEffectTypes getDefaultEffect() {
        return CSEffectTypes.SOLARIS_BLITZ;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
