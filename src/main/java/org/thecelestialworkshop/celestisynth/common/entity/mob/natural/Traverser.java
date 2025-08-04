package org.thecelestialworkshop.celestisynth.common.entity.mob.natural;

import org.thecelestialworkshop.celestisynth.common.entity.base.FixedMovesetEntity;
import org.thecelestialworkshop.celestisynth.common.entity.base.MonolithSummonedEntity;
import org.thecelestialworkshop.celestisynth.common.entity.goals.ActionStoppableMoveToTargetGoal;
import org.thecelestialworkshop.celestisynth.common.entity.goals.AnimatedMeleeGoal;
import org.thecelestialworkshop.celestisynth.common.entity.goals.traverser.TraverserJumpGoal;
import org.thecelestialworkshop.celestisynth.common.entity.goals.traverser.TraverserStunnedGoal;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.KeresSlash;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Traverser extends Monster implements GeoEntity, FixedMovesetEntity, MonolithSummonedEntity {
    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(Traverser.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(Traverser.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public StarMonolith monolith;

    public static final int ACTION_MELEE = 1;
    public static final int ACTION_MELEE_1 = 2;
    public static final int ACTION_MELEE_2 = 3;
    public static final int ACTION_JUMP = 4;
    public static final int ACTION_STUNNED = 5;
    public static final int ACTION_SPAWNED = 6;
    public int nextAttack;
    public boolean enableMeleeSlashing;

    public Traverser(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        nextAttack = ACTION_MELEE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TraverserStunnedGoal(this));
        this.goalSelector.addGoal(2, new AnimatedMeleeGoal<>(this, ACTION_MELEE_1, 20, 10, this.getBbWidth() + 2.5,
                mob -> this.nextAttack == ACTION_MELEE_1 && mob.getAction() != ACTION_STUNNED, mob -> mob.getAction() != ACTION_STUNNED)
        );
        this.goalSelector.addGoal(2, new AnimatedMeleeGoal<>(this, ACTION_MELEE, 20, 3, this.getBbWidth() + 1,
                mob -> this.nextAttack == ACTION_MELEE && mob.getAction() != ACTION_STUNNED, mob -> mob.getAction() != ACTION_STUNNED)
        );
        this.goalSelector.addGoal(2, new AnimatedMeleeGoal<>(this, ACTION_MELEE_2, 20, 13, this.getBbWidth() + 2,
                mob -> this.nextAttack == ACTION_MELEE_2 && mob.getAction() != ACTION_STUNNED, mob -> mob.getAction() != ACTION_STUNNED)
        );
        this.goalSelector.addGoal(3, new TraverserJumpGoal(this));
        this.goalSelector.addGoal(4, new ActionStoppableMoveToTargetGoal<>(this, 1.5, true, this.getBbWidth() + 1));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, entity -> !(entity instanceof Traverser) && !(entity instanceof StarMonolith)));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this)).setAlertOthers(Traverser.class));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.FLYING_SPEED, 1.0F);
    }

    public double getAttackReachInBlocks() {
        return getBbWidth() + 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (getAction() == ACTION_SPAWNED) {
            if (tickCount >= 40) {
                this.resetAction();
            } else {
                if (tickCount % 5 == 0) {
                    for (int i = 0; i < 22.5; i++) {
                        Vec3 particleDir = Vec3.ZERO.add(Mth.sin(i) * 1.2, 0, Mth.cos(i) * 1.2);
                        ParticleUtil.sendParticle(level(), CSParticleTypes.KERES_OMEN.get(), position().add(particleDir), new Vec3(0, 1, 0));
                    }
                }
            }
        }
        if (getAction() == Traverser.ACTION_MELEE) {
            if (this.getAnimationTick() >= 2 && getAnimationTick() <= 8) {
                for (int i = 0; i < 10; i++) {
                    ParticleUtil.sendParticle(level(), CSParticleTypes.KERES_ASH.get(), position().add(random.nextGaussian() * 0.1F, random.nextGaussian() * 0.1F, random.nextGaussian() * 0.1F), new Vec3(0, 0.2, 0));
                }
            }
            if (this.getAnimationTick() >= 10 && this.getAnimationTick() <= 15 && this.enableMeleeSlashing) {
                throwSlash();
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        playSound(CSSoundEvents.SLASH_WATER.get(), 0.2F, 1.2F);
        loopAttack();
        if (getAction() == Traverser.ACTION_MELEE) {
            Vec3 retreatVector = position().subtract(pEntity.position()).normalize().scale(2);
            this.setDeltaMovement(retreatVector.x(), getDeltaMovement().y(), retreatVector.z());
            playSound(SoundEvents.WITHER_SHOOT, 0.6F, 0.5F);
            this.enableMeleeSlashing = flag;
        } else {
            this.enableMeleeSlashing = false;
        }

        if (flag) {
            this.heal((float) (0.35F + random.nextGaussian() * 0.25F));
        }

        Vec3 retreatVector = position().subtract(pEntity.position()).normalize().scale(0.5 + (random.nextGaussian() * 0.2));
        if (getAction() == Traverser.ACTION_MELEE_2) {
            this.setDeltaMovement(retreatVector.reverse().x(), getDeltaMovement().y(), retreatVector.reverse().z());
        }

        if (getAction() == Traverser.ACTION_MELEE_1) {
            this.setDeltaMovement(retreatVector.x(), getDeltaMovement().y(), retreatVector.z());
        }
        return flag;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.getAction() == ACTION_SPAWNED && !pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        if (getAction() == Traverser.ACTION_MELEE) {
            if (getAnimationTick() >= 5 && getAnimationTick() <= 20) {
                if (level() instanceof ServerLevel server) {
                    server.playSeededSound(null, this, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(CSSoundEvents.SWORD_CLASH.get()), SoundSource.NEUTRAL, 0.2F, 1.0F, server.random.nextLong());
                }
                for (int i = 0; i < 22.5; i++) {
                    Vec3 particleDir = Vec3.ZERO.add(Mth.sin(i), Mth.cos(i), 0).scale(0.9);
                    Vec3 rotated = particleDir.xRot(-getXRot() * Mth.DEG_TO_RAD).yRot(-getYRot() * Mth.DEG_TO_RAD);
                    ParticleUtil.sendParticle(level(), CSParticleTypes.KERES_OMEN.get(), position().add(0, 0.75, 0).add(getLookAngle().scale(2)), rotated);
                }
                pAmount *= 0.5F;
                this.resetAction();
            }
        }
        boolean flag = super.hurt(pSource, pAmount);
        if (pSource.is(DamageTypeTags.IS_FALL) || pSource.is(DamageTypeTags.IS_FIRE)) {
            flag = false;
        }
        if (flag) {
            loopAttack();
            if (hasNoAction()) {
                this.setAction(ACTION_JUMP);
            }
        }
        return flag;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        super.playStepSound(pPos, pState);
        this.playSound(CSSoundEvents.TRAVERSER_STEP.get(), 0.2F, 1.3F);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return CSSoundEvents.TRAVERSER_STEP.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return CSSoundEvents.TRAVERSER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CSSoundEvents.TRAVERSER_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    public float getVoicePitch() {
        return 1.3F + (random.nextFloat() * 0.3F);
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || getAction() == ACTION_SPAWNED;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.playSound(CSSoundEvents.SWORD_SWING_FIRE.get(), 0.2F, 1.0f);
    }

    public void loopAttack() {
        switch (getAction()) {
            case ACTION_MELEE -> nextAttack = ACTION_MELEE_1;
            case ACTION_MELEE_1 -> nextAttack = ACTION_MELEE_2;
            case ACTION_MELEE_2 -> nextAttack = ACTION_MELEE;
        }
    }
   
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 2, (state) -> {
            if (getAction() == ACTION_SPAWNED) {
                state.setControllerSpeed(1);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.traverser.spawn"));
            }

            if (getAction() == ACTION_STUNNED) {
                state.setControllerSpeed(1);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.traverser.stunned"));
            }

            if (getAction() == ACTION_JUMP) {
                state.setControllerSpeed(1);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.traverser.jump"));
            }

            if (getAction() == ACTION_MELEE_2) {
                state.setControllerSpeed(1);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.traverser.melee3"));
            }

            if (getAction() == ACTION_MELEE_1) {
                state.setControllerSpeed(1);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.traverser.melee2"));
            }

            if (getAction() == ACTION_MELEE) {
                state.setControllerSpeed(1);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.traverser.melee"));
            }

            if (state.isMoving()) {
                state.setControllerSpeed(2);
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.traverser.walk"));
            }

            state.setControllerSpeed(1);
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.traverser.idle"));
        }));
    }

    public void throwSlash() {
        if (this.getTarget() != null) {
            this.playSound(CSSoundEvents.SLASH_WATER.get(), 0.2F, 1.0F);
            KeresSlash slash = new KeresSlash(CSEntityTypes.KERES_SLASH.get(), this, this.level());
            slash.moveTo(this.position().add(0, 0.5, 0));
            slash.baseDamage = 0.2F;
            slash.setRoll((float) (this.getRandom().nextGaussian() * 360));
            slash.shoot(this.getTarget().getX() - this.getX(), this.getTarget().getY() - this.getY(), this.getTarget().getZ() - this.getZ(), 2F, 3);
            this.level().addFreshEntity(slash);
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
        this.resetAction();
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        this.resetAction();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("monolith")) {
            Entity entity = level().getEntity(pCompound.getInt("monolith"));
            this.setMonolith(entity instanceof StarMonolith starMonolith ? starMonolith : null);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("monolith", this.getMonolith() != null ? this.getMonolith().getId() : 0);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void setAction(int action) {
        this.entityData.set(ACTION, action);
    }

    @Override
    public int getAction() {
        return this.entityData.get(ACTION);
    }

    @Override
    public int getAnimationTick() {
        return this.entityData.get(ANIMATION_TICK);
    }

    @Override
    public void setAnimationTick(int tick) {
        this.entityData.set(ANIMATION_TICK, tick);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ACTION, 0);
        this.entityData.define(ANIMATION_TICK, 0);
    }

    @Override
    public void setMonolith(@Nullable StarMonolith monolith) {
        this.monolith = monolith;
    }

    @Nullable
    @Override
    public StarMonolith getMonolith() {
        return this.monolith;
    }
}
