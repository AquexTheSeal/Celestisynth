package com.aqutheseal.celestisynth.common.entity.skill;

import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.base.EffectControllerEntity;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class SkillCastPoltergeistWard extends EffectControllerEntity {

    public SkillCastPoltergeistWard(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItems.POLTERGEIST.get();
    }

    @Override
    public void tick() {
        super.tick();

        UUID ownerUuid = getOwnerUuid();
        Player ownerPlayer = ownerUuid == null ? null : this.level().getPlayerByUUID(ownerUuid);
        double range = 8.5;
        List<Entity> surroundingEntities = level().getEntitiesOfClass(Entity.class, new AABB(getX() + range, getY() + (range / 2), getZ() + range, getX() - range, getY() - (range / 2), getZ() - range));

        if (tickCount == 1) {
            CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.POLTERGEIST_WARD_SUMMON.get(), 0, 0.25, 0);
            CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.POLTERGEIST_WARD.get(), 0, 2, 0);
            CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.POLTERGEIST_WARD_GROUND.get(), 0, 0.65, 0);
        }

        if (tickCount % 20 == 0) {
            CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.POLTERGEIST_WARD_ABSORB.get(), 0, -1, 0);
            level().playSound(ownerPlayer, ownerPlayer.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.BLOCKS, 0.5F, 0.5F);

            for (Entity entityBatch : surroundingEntities) {
                if (entityBatch instanceof LivingEntity target && entityBatch != ownerPlayer) {
                    target.hurt(damageSources().indirectMagic(ownerPlayer, ownerPlayer), 1.5F);
                    target.setDeltaMovement((this.getX() - target.getX()) / 4, (this.getY() - target.getY()) / 4, (this.getZ() - target.getZ()) / 4);
                    target.hurtMarked = true;
                }
            }
        }

        if (tickCount >= 100) {
            int amount = 125;
            float expansionMultiplier = 1F;

            for (int i = 0; i < amount; i++) {
                float offX = (-0.5f + random.nextFloat()) * expansionMultiplier;
                float offY = (-0.5f + random.nextFloat()) * expansionMultiplier;
                float offZ = (-0.5f + random.nextFloat()) * expansionMultiplier;

                ParticleUtil.sendParticles(level(), ParticleTypes.END_ROD, getX(), getY(), getZ(), 0, offX, offY, offZ);
            }
            for (Entity entityBatch : surroundingEntities) {
                if (entityBatch instanceof CSEffectEntity effect) {
                    if (effect.getToFollow() == this) effect.remove(RemovalReason.DISCARDED);
                }
            }

            remove(RemovalReason.DISCARDED);
        }
    }
}
