package org.thecelestialworkshop.celestisynth.common.entity.projectile;

import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import org.thecelestialworkshop.celestisynth.common.registry.CSDamageSources;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class FrostboundShard extends ThrowableProjectile {
    public FrostboundShard(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public FrostboundShard(EntityType<? extends ThrowableProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public FrostboundShard(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        for (int i = 1; i < 5; i++) {
            double xx = random.nextGaussian() * 0.02;
            double yy = random.nextGaussian() * 0.02;
            double zz = random.nextGaussian() * 0.02;
            ParticleUtil.sendParticle(level(), ParticleTypes.SNOWFLAKE, getX(), getY() + 1, getZ(), xx, yy, zz);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof LivingEntity target && pResult.getEntity() != this.getOwner()) {
            CSEntityCapabilityProvider.get(target).ifPresent(data -> data.setFrostbound(data.getFrostbound() + 20));
            if (getOwner() instanceof Player player) {
                target.hurt(CSDamageSources.instance(level()).rapidPlayerAttack(player), 2F);
            } else {
                target.hurt(CSDamageSources.instance(level()).rapidPlayerAttack(), 2F);
            }
            for (int i = 1; i < 65; i++) {
                playSound(SoundEvents.PLAYER_HURT_FREEZE, 0.15F, 0.7F + random.nextFloat());
                double xx = random.nextGaussian() * 0.08;
                double yy = random.nextGaussian() * 0.08;
                double zz = random.nextGaussian() * 0.08;
                ParticleUtil.sendParticle(level(), ParticleTypes.SNOWFLAKE, getX(), getY(), getZ(), xx, yy, zz);
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }
}
