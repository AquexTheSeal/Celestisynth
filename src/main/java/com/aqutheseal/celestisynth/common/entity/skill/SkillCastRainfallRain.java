package com.aqutheseal.celestisynth.common.entity.skill;

import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.base.EffectControllerEntity;
import com.aqutheseal.celestisynth.common.entity.projectile.RainfallArrow;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

/// TODO: 1/10/2024 Add "TARGET" entity variable
public class SkillCastRainfallRain extends EffectControllerEntity {
    public BlockPos targetPos = null;

    public SkillCastRainfallRain(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItems.RAINFALL_SERENITY.get();
    }

    @Override
    public void tick() {
        super.tick();

        if (targetPos == null) remove(RemovalReason.DISCARDED);

        UUID ownerUuid = getOwnerUuid();
        Player ownerPlayer = ownerUuid == null ? null : this.level().getPlayerByUUID(ownerUuid);

        if (tickCount == 1) {
            CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.RAINFALL_RAIN.get(), 0, 0, 0);
        }

        if (tickCount >= 20 && tickCount <= 40 && tickCount % 2 == 0) {
            level().playSound(null, targetPos.getX(), targetPos.getY(), targetPos.getZ(), CSSoundEvents.CS_LASER_SHOOT.get(), SoundSource.PLAYERS, 0.4f, 0.5F + random.nextFloat());

            double rx = -5 + random.nextInt(10);
            double rz = -5 + random.nextInt(10);

            BlockPos floor = getFloor(targetPos.getX() + rx, targetPos.getZ() + rz);

            double finalDistX = floor.getX() - getX();
            double finalDistZ = floor.getZ() - getZ();
            double finalDistY = floor.getY() - getY();

            if (!level().isClientSide) {
                RainfallArrow rainfallArrow = new RainfallArrow(level(), ownerPlayer);

                rainfallArrow.setOwner(ownerPlayer);
                rainfallArrow.moveTo(position());
                rainfallArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                rainfallArrow.setOrigin(blockPosition());
                rainfallArrow.setPierceLevel((byte) 3);
                rainfallArrow.setBaseDamage(CSConfigManager.COMMON.rainfallSerenityQuasarArrowDmg.get());
                rainfallArrow.setImbueQuasar(false);

                rainfallArrow.shoot(finalDistX, finalDistY, finalDistZ, 3.0F, 0);
                level().addFreshEntity(rainfallArrow);

                Vec3 from = new Vec3(getX(), getY(), getZ());
                Vec3 to = new Vec3(floor.getX(), floor.getY(), floor.getZ());
                rainfallArrow.createLaser(from, to, false, false);
            }
        }

        if (tickCount >= 50) {
            int amount = 125;
            float expansionMultiplier = 1F;

            for (int i = 0; i < amount; i++) {
                float offX = (-0.5f + random.nextFloat()) * expansionMultiplier;
                float offY = (-0.5f + random.nextFloat()) * expansionMultiplier;
                float offZ = (-0.5f + random.nextFloat()) * expansionMultiplier;
                ParticleUtil.sendParticles(level(), ParticleTypes.SCULK_SOUL, getX(), getY(), getZ(), 0, offX, offY, offZ);
            }

            remove(RemovalReason.DISCARDED);
        }
    }

    public BlockPos getFloor(double x, double z) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, getY(), z);
        do {
            mutablePos.move(Direction.DOWN);
        } while (mutablePos.getY() > level().getMinBuildHeight() && level().getBlockState(mutablePos).isPathfindable(level(), mutablePos, PathComputationType.LAND));
        return new BlockPos(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ());
    }


    @Override
    public void remove(RemovalReason pReason) {
        double range = 12;
        List<Entity> surroundingEntities = level().getEntitiesOfClass(Entity.class, new AABB(getX() + range, getY() + range, getZ() + range, getX() - range, getY() - range, getZ() - range));
        for (Entity entityBatch : surroundingEntities) {
            if (entityBatch instanceof CSEffectEntity effect) {
                if (effect.getToFollow() == this) effect.remove(RemovalReason.DISCARDED);
            }
        }
        super.remove(pReason);
    }
}
