package com.aqutheseal.celestisynth.common.entity.skill;

import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.aqutheseal.celestisynth.api.entity.CSEffectEntity;
import com.aqutheseal.celestisynth.api.entity.EffectControllerEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.registry.*;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import com.aqutheseal.celestisynth.util.SkinUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class SkillCastFrostboundIceCast extends EffectControllerEntity {
    private static final EntityDataAccessor<Integer> CAST_LEVEL = SynchedEntityData.defineId(SkillCastFrostboundIceCast.class, EntityDataSerializers.INT);

    public SkillCastFrostboundIceCast(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItems.FROSTBOUND.get();
    }

    @Override
    public void tick() {
        UUID ownerUuid = getOwnerUuid();
        Player ownerPlayer = ownerUuid == null ? null : this.level().getPlayerByUUID(ownerUuid);
        Pair<SoundEvent, SoundEvent> sound;
        ParticleType<?> particle;
        CSVisualType impact;
        if (SkinUtil.getSkinIndex(getOriginItem()) == 1) {
            sound = Pair.of(CSSoundEvents.ICE_CAST.get(), SoundEvents.PLAYER_HURT_DROWN);
            particle = CSParticleTypes.WATER_DROP.get();
            impact = CSVisualTypes.FROSTBOUND_ICE_CAST_SEABR.get();
        } else {
            sound = Pair.of(CSSoundEvents.ICE_CAST.get(), SoundEvents.PLAYER_HURT_FREEZE);
            particle = ParticleTypes.SNOWFLAKE;
            impact = CSVisualTypes.FROSTBOUND_ICE_CAST.get();
        }
        if (tickCount == 1 && getCastLevel() > 0) {
            this.playSound(sound.getFirst());
            for (int i = 0; i < 360; i = i + 2) {
                double xI = Mth.sin(i) * 3;
                double zI = Mth.cos(i) * 3;
                ParticleUtil.sendParticles(level(), particle, getX() + xI, getY(), getZ() + zI, 1, -xI / 8, 0, -zI / 8);
            }
        }
        if (tickCount == 5) {
            if (getCastLevel() > 0) {
                if (!level().isClientSide()) {
                    SkillCastFrostboundIceCast frostboundIceCast = CSEntityTypes.FROSTBOUND_ICE_CAST.get().create(level());
                    float aX = this.getAngleX();
                    float aZ = this.getAngleZ();
                    int floorPos = getFloorPositionUnderPlayerYLevel(level(), blockPosition().offset((int) aX, 0, (int) aZ));
                    frostboundIceCast.setOwnerUuid(ownerUuid);
                    frostboundIceCast.setOriginItem(getOriginItem());
                    frostboundIceCast.setCastLevel(getCastLevel() - 1);
                    frostboundIceCast.setAngleX(aX);
                    frostboundIceCast.setAngleZ(aZ);
                    frostboundIceCast.moveTo(getX() + aX, floorPos + 2, getZ() + aZ);
                    level().addFreshEntity(frostboundIceCast);
                }
            }
        }
        if (tickCount == 20 && getCastLevel() > 0) {
            CSEffectEntity.createInstance(ownerPlayer, this, impact, 0, 0.5, 0);
            for (int i = 0; i < 360; i = i + 4) {
                double xI = Mth.sin(i) * 3;
                double zI = Mth.cos(i) * 3;
                ParticleUtil.sendParticles(level(), particle, getX(), getY() - 1, getZ(), 1, xI / 10, 0.3, zI / 10);
            }
            double range = 1.0;
            List<Entity> entities = level().getEntitiesOfClass(Entity.class, new AABB(getX() + range, getY() + (range * 3), getZ() + range, getX() - range, getY(), getZ() - range));
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != ownerPlayer && target.isAlive()) {
                        fromInterfaceWeapon().initiateAbilityAttack(ownerPlayer, target, (float) (double) CSConfigManager.COMMON.frostboundShiftSkillDmg.get(), AttackHurtTypes.NO_KB);
                        target.setDeltaMovement(0, 0.05, 0);
                        CSEntityCapabilityProvider.get(target).ifPresent(data -> {
                            data.setFrostbound(100);
                        });
                        target.playSound(sound.getSecond());
                    }
                }
            }
            shakeScreensForNearbyPlayers(ownerPlayer, level(), 15, 20, 15, 0.02F);
        }

        if (tickCount == 60) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CAST_LEVEL, 0);
    }

    public void setCastLevel(int castLevel) {
        this.entityData.set(CAST_LEVEL, castLevel);
    }

    public int getCastLevel() {
        return this.entityData.get(CAST_LEVEL);
    }
}
