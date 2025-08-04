package org.thecelestialworkshop.celestisynth.common.entity.skillcast;

import org.thecelestialworkshop.celestisynth.common.entity.base.EffectControllerEntity;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.KeresShadow;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.KeresSlash;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class SkillCastKeresSlashWave extends EffectControllerEntity {
    public int lifespan = 15;
    public boolean hasMultishot = false;

    public SkillCastKeresSlashWave(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        Player player = getOwnerUUID() == null ? null : level().getPlayerByUUID(getOwnerUUID());

        if (player == null) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }

        this.moveTo(player.position());

        if (lifespan > 0) {
            player.playSound(CSSoundEvents.SLASH_WATER.get(), 0.1F, (float) (1.5F + (player.getRandom().nextDouble() * 0.5)));
        }

        for (int i = 0; i < 22.5; i++) {
            Vec3 particleDir = Vec3.ZERO.add(Mth.sin(i), Mth.cos(i), 0).scale(0.9);
            Vec3 rotated = particleDir.xRot(-player.getXRot() * Mth.DEG_TO_RAD).yRot(-player.getYRot() * Mth.DEG_TO_RAD);
            ParticleUtil.sendParticle(level(), CSParticleTypes.KERES_OMEN.get(), player.getEyePosition().add(player.getLookAngle().scale(2)), rotated);
        }

        if (!level().isClientSide) {
            List<Float> angles = new ArrayList<>();
            angles.add(0F);
            if (this.hasMultishot) {
                angles.add(10F);
                angles.add(-10F);
            }
            for (float angle : angles) {
                KeresSlash slash = new KeresSlash(CSEntityTypes.KERES_SLASH.get(), player, level());
                Vec3 vec31 = player.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(angle * Mth.DEG_TO_RAD, vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = player.getViewVector(1.0F);
                Vector3f shootAngle = vec3.toVector3f().rotate(quaternionf);
                slash.setRoll((float) (random.nextGaussian() * 360));
                slash.moveTo(this.position().add(0, 1, 0));
                slash.baseDamage = this.damage;
                slash.shoot(shootAngle.x, shootAngle.y, shootAngle.z, 6F, 0);
                level().addFreshEntity(slash);
            }

            if (random.nextInt(hasMultishot ? 2 : 3) == 0) {
                KeresShadow shadow = new KeresShadow(CSEntityTypes.KERES_SHADOW.get(), player, level());
                shadow.moveTo(player.getX(), shadow.getY() - 1, player.getZ());
                shadow.shootFromRotation(player, (float) (level().random.nextGaussian() * 180), -15 - (float) (level().random.nextDouble() * 75), 0, 1F, 0);
                shadow.setDeltaMovement(level().random.nextGaussian() * 0.25, 0.4, level().random.nextGaussian() * 0.25);
                shadow.damage = this.damage * 2;
                level().addFreshEntity(shadow);
            }
        }

        if (tickCount > lifespan) {
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
