package org.thecelestialworkshop.celestisynth.common.entity.projectile;

import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.registry.CSMobEffects;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class KeresSlash extends ThrowableProjectile implements GeoEntity, CSWeaponUtil {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public float baseDamage = 2;

    private static final EntityDataAccessor<Float> ROLL = SynchedEntityData.defineId(KeresSlash.class, EntityDataSerializers.FLOAT);

    public KeresSlash(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public KeresSlash(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 40) {
            this.remove(RemovalReason.DISCARDED);
        }

        Vec3 particleDir = getDeltaMovement().normalize().reverse().scale(0.2);
        ParticleUtil.sendParticle(level(), CSParticleTypes.KERES_OMEN.get(), position().add(0, 1.25, 0), particleDir);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (this.getOwner() instanceof LivingEntity owner) {
            if (pResult.getEntity() instanceof LivingEntity target && target != owner) {
                target.addEffect(new MobEffectInstance(CSMobEffects.CURSEBANE, 100, 1));
                float damageCalculation = baseDamage + (target.getMaxHealth() * (baseDamage * 0.0035F));
                this.initiateAbilityAttack(owner, target, damageCalculation, damageSources().indirectMagic(this, this.getOwner()), AttackHurtTypes.RAPID_NO_KB);
                owner.heal(damageCalculation * 0.5F);
                for (int i = 0; i < 15; i++) {
                    Vec3 attackParticle = getDeltaMovement().normalize().xRot((float) random.nextGaussian() * 0.1F).yRot((float) random.nextGaussian() * 0.1F).zRot((float) random.nextGaussian() * 0.1F);
                    ParticleUtil.sendParticle(level(), ParticleTypes.POOF, position().add(0, 1.25, 0), attackParticle);
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.remove(RemovalReason.DISCARDED);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) -> {
            //
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.keres_slash.idle"));
        }));
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public float getRoll() {
        return this.entityData.get(ROLL);
    }

    public void setRoll(float roll) {
        this.entityData.set(ROLL, roll);
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        builder.define(ROLL, 0F);
    }
}
