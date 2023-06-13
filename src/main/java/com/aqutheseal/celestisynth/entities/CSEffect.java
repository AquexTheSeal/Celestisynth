package com.aqutheseal.celestisynth.entities;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class CSEffect extends Entity implements GeoEntity {
    public CSEffect(EntityType<? extends CSEffect> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.noCulling = true;
    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<String> TYPE_ID = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> FRAME_LEVEL = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SET_ROT_X = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SET_ROT_Z = SynchedEntityData.defineId(CSEffect.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public Player owner;
    public Entity toFollow;
    public int lifespan;
    public int frameTimer;

    public CSEffectTypes getEffectType() {
        for (CSEffectTypes candids : CSEffectTypes.values()) {
            if (candids.getName().equals(this.getTypeID())) {
                return candids;
            }
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
        CSEffect slash = CSEntityRegistry.CS_EFFECT.get().create(owner.level);

        if (toFollow != null) {
            slash.moveTo(toFollow.getX() + offsetX, (toFollow.getY() - 1.5) + offsetY, toFollow.getZ() + offsetZ);
        } else {
            slash.moveTo(owner.getX()  + offsetX, (owner.getY() - 1.5) + offsetY, owner.getZ() + offsetZ);
        }

        slash.setOwnerUuid(owner.getUUID());
        slash.setToFollow(toFollow);
        slash.setEffectType(effectTypes);
        slash.setRandomRotation();

        float offsetRot = -10 + owner.getRandom().nextInt(10);

        slash.setYRot(owner.getYRot() + offsetRot);
        slash.yRotO = slash.getYRot();
        slash.setRot(slash.getYRot() + offsetRot, slash.getXRot());

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
        CSEffect slash = getEffectInstance(owner, toFollow, effectTypes, xOffset, yOffset, zOffset);
        slash.setOwnerUuid(owner.getUUID());
        owner.level.addFreshEntity(slash);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<?> state) {
        if (getEffectType() != null) {
            state.getController().setAnimation(RawAnimation.begin().thenPlayAndHold(getEffectType().getAnimation().getAnimationString()));
        } else {
            Celestisynth.LOGGER.warn("EffectType for CSEffect is null!");
            state.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("animation.cs_effect.spin"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        if (getOwnerUuid() == null) {
            this.remove(RemovalReason.DISCARDED);
        }

        ++lifespan;
        if (lifespan >= this.getEffectType().getAnimation().getLifespan()) {
            this.setLifespan(0);
            this.remove(RemovalReason.DISCARDED);
        }
        ++frameTimer;
        if (frameTimer >= (this.getEffectType().getFramesSpeed() - 1)) {
            this.setFrameLevel(this.getFrameLevel() == this.getEffectType().getFrames() ? 1 : this.getFrameLevel() + 1);
            frameTimer = 0;
        }
        super.tick();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(TYPE_ID, "none");
        this.entityData.define(FRAME_LEVEL, 1);
        this.entityData.define(SET_ROT_X, 0);
        this.entityData.define(SET_ROT_Z, 0);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        if (this.getOwnerUuid() != null) {
            compoundNBT.putUUID("Owner", getOwnerUuid());
        }
        compoundNBT.putInt("lifespan", this.lifespan);
        compoundNBT.putString("typeId", this.getTypeID());
    }

    public CSEffectTypes getDefaultEffect() {
        return CSEffectTypes.SOLARIS_BLITZ;
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
