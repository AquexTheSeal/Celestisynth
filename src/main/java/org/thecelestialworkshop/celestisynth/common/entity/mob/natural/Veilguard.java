package org.thecelestialworkshop.celestisynth.common.entity.mob.natural;

import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.entity.base.FixedMovesetEntity;
import org.thecelestialworkshop.celestisynth.common.entity.goals.ActionStoppableMoveToTargetGoal;
import org.thecelestialworkshop.celestisynth.common.entity.goals.AnimatedMultiTriggerGoal;
import org.thecelestialworkshop.celestisynth.common.entity.goals.AnimatedTriggerGoal;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class Veilguard extends Monster implements GeoEntity, FixedMovesetEntity, CSWeaponUtil {
    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(Veilguard.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(Veilguard.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final int ACTION_MELEE = 1;
    public static final int ACTION_SMASH = 2;
    public static final int ACTION_RAGE = 3;
    public int nextAttack;

    public Veilguard(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        nextAttack = ACTION_MELEE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18F)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10)
                .add(Attributes.ARMOR, 20.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AnimatedTriggerGoal<>(this, ACTION_MELEE, 60, 50, this.getBbWidth() + 3,
                mob -> this.nextAttack == ACTION_MELEE, () -> this.drillPunch(false)));
        this.goalSelector.addGoal(1, new AnimatedTriggerGoal<>(this, ACTION_SMASH, 50, 25, this.getBbWidth() + 4,
                mob -> this.nextAttack == ACTION_SMASH, () -> this.smash(false)));
        this.goalSelector.addGoal(1, new AnimatedMultiTriggerGoal<>(this, ACTION_RAGE, 60, this.getBbWidth() + 4, mob -> this.nextAttack == ACTION_RAGE,
                new AnimatedMultiTriggerGoal.Triggerable(20, () -> this.drillPunch(true)),
                new AnimatedMultiTriggerGoal.Triggerable(25, () -> this.drillPunch(true)),
                new AnimatedMultiTriggerGoal.Triggerable(40, () -> this.smash(true))
                )
        );
        this.goalSelector.addGoal(2, new ActionStoppableMoveToTargetGoal<>(this, 1.0, true, this.getBbWidth() + 2));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Veilguard.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, entity -> !(entity instanceof Veilguard)));
    }

    public void loopAttack() {
        this.nextAttack = 1 + random.nextInt(3);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAction() == Veilguard.ACTION_MELEE && getAnimationTick() > 0 && getAnimationTick() < 45) {
            this.playSound(CSSoundEvents.HEARTBEAT.get(), 0.15F, 1.5F);
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8)).stream().filter((e) -> e != this).toList();
            for (LivingEntity target : targets) {
                if (!(target instanceof Player player && player.isCreative())) {
                    target.setDeltaMovement(target.getDeltaMovement().add(this.position().subtract(target.position()).normalize().scale(0.05)));
                }
            }
        }
    }

    public void smash(boolean isRage) {
        AABB aabb = new AABB(position().subtract(4, 2, 4), position().add(4, 2, 4)).move(0, 2, 0).move(getLookAngle().scale(3.5));
        List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, aabb).stream().filter((e) -> e != this).toList();
        this.playSound(CSSoundEvents.WIND_STRIKE.get(), 0.3F, 0.5F);
        this.playSound(CSSoundEvents.BASS_PULSE.get(), 0.3F, 1.5F);
        this.playSound(CSSoundEvents.GROUND_IMPACT_WATER.get(), 0.3F, 1.0F);
        for (int i = 0; i < 45; i++) {
            for (int j = 0; j < 5; j++) {
                Vec3 vec = getLookAngle().scale(3.5);
                ParticleUtil.sendParticle(level(), CSParticleTypes.RAINFALL_ENERGY_SMALL.get(),
                        this.position().add(0, 0.25, 0).add(vec.x(), 0, vec.z()).add(Mth.sin(i) * j, 0, Mth.cos(i) * j),
                        Vec3.ZERO.add(Mth.sin(i) * 0.05, 0.05, Mth.cos(i) * 0.05)
                );
                ParticleUtil.sendParticle(level(), ParticleTypes.POOF,
                        this.position().add(0, 0.25, 0).add(vec.x(), 0, vec.z()).add(Mth.sin(i) * j, 0, Mth.cos(i) * j),
                        Vec3.ZERO.add(Mth.sin(i) * 0.05, 0.05, Mth.cos(i) * 0.05)
                );
            }
        }
        for (LivingEntity target : targets) {
            if (this.doHurtTarget(target)) {
                target.setDeltaMovement(this.position().subtract(target.position()).normalize().scale(-3));
            }
        }
        if (isRage) {
            this.setDeltaMovement(this.getLookAngle().normalize().scale(-3.5).subtract(0, 3, 0));
        }
        this.loopAttack();
    }

    public void drillPunch(boolean isRage) {
        AABB aabb = new AABB(position().subtract(2, 2, 2), position().add(2, 2, 2)).move(0, 2, 0).move(getLookAngle().scale(3.5));
        List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, aabb).stream().filter((e) -> e != this).toList();
        this.playSound(CSSoundEvents.WIND_STRIKE.get(), 0.3F, 0.5F);
        if (!targets.isEmpty()) {
            this.playSound(CSSoundEvents.BASS_PULSE.get(), 0.3F, 1.5F);
            for (int i = 0; i < 90; i++) {
                ParticleUtil.sendParticle(level(), CSParticleTypes.RAINFALL_BEAM.get(), this.position().add(0, 2, 0).add(getLookAngle().scale(3.5)), Vec3.ZERO.add(random.nextGaussian() * 0.2, random.nextGaussian() * 0.2, random.nextGaussian() * 0.2));
            }
        }
        for (LivingEntity target : targets) {
            if (this.doHurtTarget(target)) {
                if (isRage) {
                    target.setDeltaMovement(this.position().subtract(target.position()).normalize().scale(-1.5));
                    target.invulnerableTime = 0;
                } else {
                    target.setDeltaMovement(this.position().subtract(target.position()).normalize().scale(-3));
                }
            }
        }
        if (isRage) {
            this.setDeltaMovement(this.getLookAngle().normalize().scale(1.25));
        } else {
            this.loopAttack();
        }
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (pEntity instanceof LivingEntity living) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), living.getMobType());
            if (this.getAction() == ACTION_MELEE) {
                f = f + (living.getHealth() * 0.2F);
            }
        }
        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            pEntity.setSecondsOnFire(i * 4);
        }
        boolean flag = pEntity.hurt(this.damageSources().mobAttack(this), f);
        if (flag) {
            this.doEnchantDamageEffects(this, pEntity);
            this.setLastHurtMob(pEntity);
        }
        return flag;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 5, (state) -> {
            if (getAction() == ACTION_RAGE) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.veilguard.rage"));
            }

            if (getAction() == ACTION_SMASH) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.veilguard.smash"));
            }

            if (getAction() == ACTION_MELEE) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.veilguard.attack"));
            }

            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.veilguard.walk"));
            }

            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.veilguard.idle"));
        }));
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
}
