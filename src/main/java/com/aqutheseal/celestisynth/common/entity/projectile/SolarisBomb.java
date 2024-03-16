package com.aqutheseal.celestisynth.common.entity.projectile;

import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SolarisBomb extends ThrowableProjectile implements CSWeaponUtil {
    private static final EntityDataAccessor<Integer> LOOSE_TARGET = SynchedEntityData.defineId(SolarisBomb.class, EntityDataSerializers.INT);

    public SolarisBomb(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SolarisBomb(EntityType<? extends ThrowableProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public SolarisBomb(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (getOwner() != null) {
            ParticleUtil.sendParticle(level(), ParticleTypes.FLAME, getX(), getY() + 0.5, getZ(), random.nextGaussian() * 0.05, random.nextGaussian() * 0.05, random.nextGaussian() * 0.05);
            if (getLooseTarget() == null) {
                double plX = getOwner().getX() - (Mth.sin((float) tickCount / 8) * 7);
                double plY = getOwner().getY() + (Mth.sin((float) tickCount / 4) * 0.5);
                double plZ = getOwner().getZ() - (Mth.cos((float) tickCount / 8) * 7);
                Vec3 plPos = new Vec3(plX, plY + 1, plZ);
                this.setDeltaMovement(plPos.subtract(this.position()).scale(0.1F));
            }
            List<Projectile> projectiles = level().getEntitiesOfClass(Projectile.class, this.getBoundingBox()).stream().filter(projectile -> projectile.getOwner() != getOwner()).toList();
            if (!projectiles.isEmpty()) {
                for (Projectile targets : projectiles) {
                    targets.remove(RemovalReason.DISCARDED);
                    explodeFire();
                }
            }
        } else {
            remove(RemovalReason.DISCARDED);
        }
    }

    public static Stream<SolarisBomb> getAllBombsOwnedBy(LivingEntity owner, ServerLevel level) {
        return StreamSupport.stream(level.getAllEntities().spliterator(), false)
                .filter(mob -> mob instanceof SolarisBomb).map(SolarisBomb.class::cast)
                .filter(bomb -> bomb.getOwner() == owner);
    }

    public static void handleHurtEvent(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level() instanceof ServerLevel level) {
            if (event.getSource().getEntity() instanceof LivingEntity owner && !event.getSource().is(DamageTypeTags.BYPASSES_COOLDOWN)) {
                List<SolarisBomb> candids = getAllBombsOwnedBy(owner, level).toList();
                if (!candids.isEmpty()) {
                    SolarisBomb bomb = candids.get(entity.getRandom().nextInt(candids.size()));
                    bomb.setLooseTarget(entity);
                    bomb.setDeltaMovement(entity.position().subtract(bomb.position()).scale(0.1));
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        explodeFire();
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (getOwner() instanceof Player player) {
            if (pResult.getEntity() instanceof LivingEntity target && target != getOwner()) {
                initiateAbilityAttack(player, target, 5F, AttackHurtTypes.RAPID);
                target.setSecondsOnFire(4);
                if (target == getLooseTarget()) {
                    explodeFire();
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        explodeFire();
    }

    public void explodeFire() {
        this.playSound(SoundEvents.METAL_BREAK, 1.0F, 1.0F);
        for (int i = 0; i < 90; i++) {
            double xx = random.nextGaussian() * 0.05;
            double yy = random.nextGaussian() * 0.05;
            double zz = random.nextGaussian() * 0.05;
            ParticleUtil.sendParticle(level(), ParticleTypes.FLAME, getX(), getY() + 0.5, getZ(), xx, yy, zz);
            ParticleUtil.sendParticle(level(), ParticleTypes.CLOUD, getX(), getY() + 0.5, getZ(), xx / 2, yy / 2, zz / 2);
        }
        for (LivingEntity targets : level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.6, 1.6, 1.6))) {
            if (getOwner() instanceof Player player) {
                initiateAbilityAttack(player, targets, 2, AttackHurtTypes.RAPID_NO_KB);
                targets.setSecondsOnFire(2);
            }
        }
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public @Nullable LivingEntity getLooseTarget() {
        int data = entityData.get(LOOSE_TARGET);
        if (data == 0) {
            return null;
        }
        Entity target = level().getEntity(entityData.get(LOOSE_TARGET));
        if (target instanceof LivingEntity living) {
            return living;
        } else {
            return null;
        }
    }

    public void setLooseTarget(LivingEntity target) {
        entityData.set(LOOSE_TARGET, target.getId());
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(LOOSE_TARGET, 0);
    }
}
