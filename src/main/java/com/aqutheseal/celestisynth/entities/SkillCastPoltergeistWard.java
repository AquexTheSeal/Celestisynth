package com.aqutheseal.celestisynth.entities;

import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.CSUtilityFunctions;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SkillCastPoltergeistWard extends EffectControllerEntity {
    public SkillCastPoltergeistWard(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItemRegistry.POLTERGEIST.get();
    }

    @Override
    public void tick() {
        super.tick();
        UUID uuid = this.getOwnerUuid();
        Player player = uuid == null ? null : this.getLevel().getPlayerByUUID(uuid);
        double range = 8.5;
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(getX() + range, getY() + (range / 2), getZ() + range, getX() - range, getY() - (range / 2), getZ() - range));

        if (tickCount == 1) {
            CSEffect.createInstance(player, this, CSEffectTypes.POLTERGEIST_WARD_SUMMON, 0, 0.25, 0);
            CSEffect.createInstance(player, this, CSEffectTypes.POLTERGEIST_WARD, 0, 2, 0);
            CSEffect.createInstance(player, this, CSEffectTypes.POLTERGEIST_WARD_GROUND, 0, 0.65, 0);
        }

        if (tickCount % 20 == 0) {
            CSEffect.createInstance(player, this, CSEffectTypes.POLTERGEIST_WARD_ABSORB, 0, -1, 0);
            level.playSound(player, player.blockPosition(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.BLOCKS, 0.5F, 0.5F);
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target && entityBatch != player) {
                    target.hurt(DamageSource.indirectMagic(player, player), 1.5F);
                    target.setDeltaMovement((this.getX() - target.getX()) / 4, (this.getY() - target.getY()) / 4, (this.getZ() - target.getZ()) / 4);
                    target.hurtMarked = true;
                }
            }
        }

        if (tickCount >= 100) {
            int amount = 125;
            float expansionMultiplier = 1F;
            for (int i = 0; i < amount; i++) {
                Random random = new Random();
                float offX = (-0.5f + random.nextFloat()) * expansionMultiplier;
                float offY = (-0.5f + random.nextFloat()) * expansionMultiplier;
                float offZ = (-0.5f + random.nextFloat()) * expansionMultiplier;
                CSUtilityFunctions.sendParticles(level, ParticleTypes.END_ROD, getX(), getY(), getZ(), 0, offX, offY, offZ);
            }
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof CSEffect effect) {
                    if (effect.getToFollow() == this) {
                        effect.remove(RemovalReason.DISCARDED);
                    }
                }
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void remove(RemovalReason pReason) {
        double range = 12;
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(getX() + range, getY() + range, getZ() + range, getX() - range, getY() - range, getZ() - range));
        for (Entity entityBatch : entities) {
            if (entityBatch instanceof CSEffect effect) {
                if (effect.getToFollow() == this) {
                    effect.remove(RemovalReason.DISCARDED);
                }
            }
        }
        super.remove(pReason);
    }
}
