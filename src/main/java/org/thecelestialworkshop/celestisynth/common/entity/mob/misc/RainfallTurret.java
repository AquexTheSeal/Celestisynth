package org.thecelestialworkshop.celestisynth.common.entity.mob.misc;

import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.entity.base.SummonableEntity;
import org.thecelestialworkshop.celestisynth.common.entity.goals.CSLookAroundGoal;
import org.thecelestialworkshop.celestisynth.common.entity.goals.CSLookAtTargetGoal;
import org.thecelestialworkshop.celestisynth.common.entity.goals.CSOwnerAttackGoal;
import org.thecelestialworkshop.celestisynth.common.entity.goals.CSOwnerAttackedGoal;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.RainfallArrow;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import org.thecelestialworkshop.celestisynth.util.EntityUtil;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RainfallTurret extends SummonableEntity implements GeoEntity {
    private static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.defineId(RainfallTurret.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> X_SYNC_ROT = SynchedEntityData.defineId(RainfallTurret.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<CompoundTag> ITEM_DATA = SynchedEntityData.defineId(RainfallTurret.class, EntityDataSerializers.COMPOUND_TAG);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public int shootTime;

    public RainfallTurret(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            this.setXSyncedRot(this.getXRot());
        }
//        if (this.getTarget() != null) {
//            if (!EntityUtil.isValidTargetForOwnable(this, this.getTarget())) {
//                this.setTarget(null);
//            }
//        }
        this.setYBodyRot(0);
        this.yBodyRotO = 0;
        this.tickShooting();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CSLookAtTargetGoal(this));
        this.goalSelector.addGoal(3, new CSLookAroundGoal(this, entity -> entity.getTarget() == null));
        this.targetSelector.addGoal(3, new CSOwnerAttackedGoal<>(this));
        this.targetSelector.addGoal(4, new CSOwnerAttackGoal<>(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false, target -> EntityUtil.isValidTargetForOwnable(this, target)) {
            @Override
            protected AABB getTargetSearchArea(double pTargetDistance) {
                return this.mob.getBoundingBox().inflate(pTargetDistance, pTargetDistance / 2, pTargetDistance);
            }
        });
        super.registerGoals();
    }

    public void tickShooting() {
        double enchantmentAdjustment = (org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil.getStackEnchantmentLevel(createBowFromData(), Enchantments.QUICK_CHARGE) * 3.25) - (org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil.getStackEnchantmentLevel(createBowFromData(), Enchantments.PIERCING) * 3.5);
        int shootInterval = (int) (20 - Math.min(19, enchantmentAdjustment));
        if (this.getTarget() != null && !this.isRemoved() && this.getOwner() instanceof Player player) {
            this.shootTime++;
            if (this.shootTime > 0 && this.shootTime % shootInterval == 0) {
                this.entityData.set(SHOOTING, true);

                this.playSound(CSSoundEvents.LASER_SHOOT.get(), 0.2F, 0.2F + random.nextFloat());
                CSEffectEntity.createInstance(player, this, CSVisualTypes.RAINFALL_SHOOT.get(), this.getLookAngle().x() * 2, 0.5 + this.getLookAngle().y() * 2, this.getLookAngle().z() * 2);

                FloatArrayList angles = new FloatArrayList();
                angles.add(0);

                int multishot = org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil.getStackEnchantmentLevel(createBowFromData(), Enchantments.MULTISHOT);

                if (multishot > 0) {
                    for (int i = 0; i < multishot + 1; i++) {
                        angles.add(10 * i);
                        angles.add(-10 * i);
                    }
                }

                for (float angle : angles) {
                    if (!level().isClientSide) {
                        RainfallArrow rainfallArrow = new RainfallArrow(level(), this);
                        Vec3 vec31 = this.getUpVector(1.0F);
                        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(angle * Mth.DEG_TO_RAD, vec31.x, vec31.y, vec31.z);
                        Vec3 vec3 = this.getViewVector(1.0F);
                        Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);

                        rainfallArrow.setOwner(this);
                        rainfallArrow.turretSource = this;
                        rainfallArrow.moveTo(this.position().add(0, 1, 0));
                        rainfallArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        rainfallArrow.setOrigin(rainfallArrow.position().add(0, 0.5, 0));
                        rainfallArrow.setPierceLevel((byte) 3);
                        rainfallArrow.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 3.0F, 1.0F);
                        level().addFreshEntity(rainfallArrow);
                    }
                }
            }
        } else {
            this.shootTime = 0;
        }
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHOOTING, false);
        builder.define(X_SYNC_ROT, 0F);
        builder.define(ITEM_DATA, new CompoundTag());
    }

    public void setXSyncedRot(float value) {
        this.entityData.set(X_SYNC_ROT, value);
    }

    public float getXSyncedRot() {
        return this.entityData.get(X_SYNC_ROT);
    }

    public void setItemData(CompoundTag tag) {
        this.entityData.set(ITEM_DATA, tag);
    }

    public CompoundTag getItemData() {
        return this.entityData.get(ITEM_DATA);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("itemDataRF", getItemData());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setItemData(pCompound.getCompound("itemDataRF"));
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (pPlayer == this.getOwner() && pPlayer.isShiftKeyDown()) {
            pPlayer.addItem(this.createBowFromData());
            playSound(SoundEvents.ITEM_PICKUP);
            for (int i = 0; i < 16; i++) {
                ParticleUtil.sendParticle(level(), CSParticleTypes.RAINFALL_ENERGY_SMALL.get(),
                        position().add(level().random.nextGaussian() * 0.4, 0, level().random.nextGaussian() * 0.4),
                        Vec3.ZERO.add(0, level().random.nextDouble() * 0.65, 0));
            }
            this.remove(RemovalReason.DISCARDED);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(pPlayer, pHand);
    }

    public ItemStack createBowFromData() {
        if (this.getItemData() == null || this.getItemData().isEmpty()) {
            return new ItemStack(CSItems.RAINFALL_SERENITY.get());
        }
        return ItemStack.parse(this.level().registryAccess(), this.getItemData()).orElseGet(() -> new ItemStack(CSItems.RAINFALL_SERENITY.get()));
    }

    @Override
    protected void tickDeath() {
        this.destroy();
    }

    public void destroy() {
        if (!level().isClientSide) {
            level().playSound(null, this.getX(), this.getY(), this.getZ(), this.getDeathSound(), this.getSoundSource(), 1.0F, 2.0F);
            ((ServerLevel) level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ANDESITE.defaultBlockState()), this.getX(), this.getY(), this.getZ(),
                    50, this.getBbWidth(), this.getBbHeight(), this.getBbWidth(), 0.1
            );
            ItemEntity dataItem = new ItemEntity(level(), this.getX(), this.getY(), this.getZ(), this.createBowFromData());
            dataItem.push(0, 0.3, 0);
            dataItem.getItem().setDamageValue(dataItem.getItem().getMaxDamage() - 1);
            level().addFreshEntity(dataItem);
            this.remove(RemovalReason.KILLED);
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!level().isClientSide) {
            ((ServerLevel) level()).sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ANDESITE.defaultBlockState()), this.getX(), this.getY(), this.getZ(),
                    25, this.getBbWidth(), this.getBbHeight(), this.getBbWidth(), 0.05
            );
        }
        if (pSource.getDirectEntity() instanceof RainfallArrow arrow) {
            if (arrow.getOwner() == this.getOwner()) {
                return false;
            }
        }
        if (pSource.getEntity() instanceof Player entity) {
            this.setOwner(entity);
        }
        return super.hurt(pSource, pAmount);
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.rainfall_turret.idle"))));
    }

    public boolean isControlledByLocalInstance() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.ARMOR, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.FLYING_SPEED, 0.0);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source == this.damageSources().drown() || source == this.damageSources().inWall();
    }

    protected Vec3 getLeashOffset() {
        return new Vec3(0.0, this.getEyeHeight() - 1.0F, 0.0);
    }

    public void setDeltaMovement(Vec3 motionIn) {
        super.setDeltaMovement(Vec3.ZERO.add(0, -0.2, 0));
    }

    public void knockback(double strength, double x, double z) {
    }

    public boolean isPushedByFluid() {
        return false;
    }

    public boolean canDrownInFluidType(net.neoforged.neoforge.fluids.FluidType type) {
        return false;
    }

    public boolean isPushable() {
        return false;
    }

    protected void markHurt() {
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.GILDED_BLACKSTONE_BREAK;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BONE_BLOCK_BREAK;
    }
}
