package com.aqutheseal.celestisynth.entities;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvents;
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
    public CSEffect(EntityType<? extends CSEffect> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.noCulling = true;
    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> TYPE_ID = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> ANIMATION_ID = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FRAME_LEVEL = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SET_ROT_X = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SET_ROT_Z = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
     private static final EntityDataAccessor<Float> CUSTOMIZABLE_SIZE = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.FLOAT);
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public CSEffectTypes lockedInEffect;
    public Entity toFollow;
    public int lifespan;
    public int frameTimer;

    public CSEffectTypes getEffectType() {
        if (lockedInEffect == null) {
            for (CSEffectTypes candids : CSEffectTypes.values()) {
                if (candids.getName().equals(this.getTypeID())) {
                    lockedInEffect = candids;
                    return candids;
                }
            }
        } else {
            return lockedInEffect;
        }
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

    public static CSEffect getEffectInstance(Player owner, @Nullable Entity toFollow, CSEffectTypes effectTypes, double offsetX, double offsetY, double offsetZ) {
        if (owner == null) {
            return null;
        }
        CSEffect slash = CSEntityRegistry.CS_EFFECT.get().create(owner.level);
        slash.setEffectType(effectTypes);
        slash.setAnimationID(effectTypes.getAnimation().getAnimationString());
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
        this.entityData.set(SET_ROT_X, rotationX);
        this.entityData.set(SET_ROT_Z, rotationZ);
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
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
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
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cs_effect.spin", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        if (getOwnerUuid() == null) {
            this.remove(RemovalReason.DISCARDED);
        }

        ++frameTimer;
        if (frameTimer >= (this.getEffectType().getFramesSpeed())) {
            this.setFrameLevel(this.getFrameLevel() == this.getEffectType().getFrames() ? 1 : this.getFrameLevel() + 1);
            frameTimer = 0;
        }

        if (tickCount == 1) {
            if (getEffectType() == CSEffectTypes.POLTERGEIST_IMPACT_CRACK || getEffectType() == CSEffectTypes.POLTERGEIST_IMPACT_CRACK_LARGE) {
                playSound(SoundEvents.END_GATEWAY_SPAWN, 1.0F, 1.75F);
                playSound(CSSoundRegistry.CS_LOUD_IMPACT.get(), 1.5F, 1.0F);
            }
        }

        if (toFollow != null) {
            if (getEffectType() == CSEffectTypes.AQUAFLORA_FLOWER_BIND) {
                calculateCustomizableSize((float) (toFollow.getBbWidth() - (toFollow.getBbWidth() / 4.5)), -getCustomizableSize());
            }
        }

        ++lifespan;
        if (lifespan >= this.getEffectType().getAnimation().getLifespan()) {
            this.setLifespan(0);
            this.remove(RemovalReason.DISCARDED);
        }

        super.tick();
    }

    public void calculateCustomizableSize(float size, double yOff) {
        this.setYRot(toFollow.getYRot());
        this.yRotO = this.getYRot();
        this.setRot(this.getYRot(), this.getXRot());
        this.setCustomizableSize(size);
        this.moveTo(toFollow.getX(), toFollow.getY() + yOff, toFollow.getZ());
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
        UUID uuid;
        if (compoundNBT.hasUUID("Owner")) {
            uuid = compoundNBT.getUUID("Owner");
        } else {
            String s = compoundNBT.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerUuid(uuid);
            } catch (Throwable throwable) {
                throw new IllegalStateException("...Crescentia-Ranged got no goddamn owner.");
            }
        }
        this.setLifespan(compoundNBT.getInt("lifespan"));
        this.setTypeID(compoundNBT.getString("typeId"));
        this.setAnimationID(compoundNBT.getString("animationID"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        if (this.getOwnerUuid() != null) {
            compoundNBT.putUUID("Owner", getOwnerUuid());
        }
        compoundNBT.putInt("lifespan", this.lifespan);
        compoundNBT.putString("typeId", this.getTypeID());
        compoundNBT.putString("animationId", this.getAnimationID());
    }

    public CSEffectTypes getDefaultEffect() {
        return CSEffectTypes.SOLARIS_BLITZ;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
