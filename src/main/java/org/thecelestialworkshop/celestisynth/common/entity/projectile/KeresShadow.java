package org.thecelestialworkshop.celestisynth.common.entity.projectile;

import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.registry.CSDamageSources;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.util.EntityUtil;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KeresShadow extends ThrowableProjectile implements CSWeaponUtil {
    private static final EntityDataAccessor<Integer> HOMING_TARGET = SynchedEntityData.defineId(KeresShadow.class, EntityDataSerializers.INT);
    public float damage;

    public KeresShadow(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public KeresShadow(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        double xx = random.nextGaussian() * 0.02;
        double yy = random.nextGaussian() * 0.02;
        double zz = random.nextGaussian() * 0.02;
        Vec3 offsetVector = this.getDeltaMovement().normalize().scale(4);
        ParticleUtil.sendParticle(level(), CSParticleTypes.KERES_ASH.get(), getX() + offsetVector.x(), getY() + offsetVector.y() + 1, getZ() + offsetVector.z(), xx, yy, zz);

        if (getHomingTarget() != null) {
            Vec3 positionAmends = getHomingTarget().position().subtract(this.position());
            double posAmendScale = 0.05D;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(positionAmends.normalize().scale(posAmendScale)));

            if (this.getBoundingBox().inflate(1).intersects(this.getHomingTarget().getBoundingBox())) {
                this.onHitEntity(new EntityHitResult(this.getHomingTarget()));
            }
        } else {
            TargetingConditions selectFreshTarget = TargetingConditions.forCombat().range(128.0D).ignoreLineOfSight().selector(living -> living != this.getOwner() && EntityUtil.isNotAPetOf(this.getOwner(), living));
            List<LivingEntity> targetList = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32), entity -> selectFreshTarget.test(null, entity));
            if (!targetList.isEmpty()) {
                this.setHomingTarget(targetList.get(random.nextInt(targetList.size())));
            } else {
                if (this.getOwner() instanceof LivingEntity owner) {
                    if (this.tickCount >= 20) {
                        this.setHomingTarget(owner);
                    }
                } else {
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof LivingEntity target) {
            if (this.getOwner() instanceof LivingEntity owner) {
                if (target == owner) {
                    owner.heal(1F);
                    if (owner instanceof Player player) {
                        player.getFoodData().eat(2, 0F);
                    }
                    owner.playSound(SoundEvents.AMETHYST_BLOCK_BREAK);
                } else {
                    target.hurt(CSDamageSources.instance(level()).rapidPlayerAttack(owner), damage);
                    target.playSound(SoundEvents.WITHER_BREAK_BLOCK, 0.2F, 1 + (random.nextFloat() * 0.5F));

                    KeresSlash slash = new KeresSlash(CSEntityTypes.KERES_SLASH.get(), owner, level());

                    double d0 = target.getX() - owner.getX();
                    double d1 = target.getY() - owner.getY();
                    double d2 = target.getZ() - owner.getZ();

                    slash.setRoll((float) (random.nextGaussian() * 360));
                    slash.moveTo(owner.position());
                    slash.baseDamage = damage / 2;
                    slash.shoot(d0, d1, d2, 2F, 0);
                    level().addFreshEntity(slash);

                    level().playSound(null, owner.blockPosition(), CSSoundEvents.SLASH_WATER.get(), SoundSource.PLAYERS, 0.02F, (float) (1.5F + (level().random.nextDouble() * 0.5)));
                }
                if (target == getHomingTarget()) {
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
    }

//    @Override
//    protected void onHitBlock(BlockHitResult pResult) {
//        super.onHitBlock(pResult);
//        this.remove(RemovalReason.DISCARDED);
//    }

    @Override
    public boolean isNoGravity() {
        return true;
    }


    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.remove(RemovalReason.DISCARDED);
    }

    public @Nullable LivingEntity getHomingTarget() {
        int data = entityData.get(HOMING_TARGET);
        if (data == 0) {
            return null;
        }
        Entity target = level().getEntity(entityData.get(HOMING_TARGET));
        if (target instanceof LivingEntity living) {
            return living;
        } else {
            return null;
        }
    }

    public void setHomingTarget(LivingEntity target) {
        entityData.set(HOMING_TARGET, target.getId());
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        builder.define(HOMING_TARGET, 0);
    }
}
