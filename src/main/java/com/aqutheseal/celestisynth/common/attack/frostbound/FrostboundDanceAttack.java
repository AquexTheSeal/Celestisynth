package com.aqutheseal.celestisynth.common.attack.frostbound;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.aqutheseal.celestisynth.api.entity.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import com.aqutheseal.celestisynth.util.SkinUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FrostboundDanceAttack extends FrostboundAttack {
    public FrostboundDanceAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_FROSTBOUND_TRIPLE_SLASH;
    }

    @Override
    public int getCooldown() {
        return CSConfigManager.COMMON.frostboundSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 50;
    }

    @Override
    public boolean getCondition() {
        return !player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        player.setDeltaMovement(0, 0.75, 0);
        player.playSound(CSSoundEvents.HOP.get());
    }

    @Override
    public void tickAttack() {
        double xP = calculateXLook(player) * 1.5;
        double zP = calculateZLook(player) * 1.5;
        if (getTimerProgress() == 12) {
            performImpact(xP, zP);
            shootShard(4);
        }
        if (getTimerProgress() == 20) {
           performIceSlash(0, xP, zP);
            shootShard(3);
        } else if (getTimerProgress() == 30) {
            performIceSlash(1, xP, zP);
            shootShard(3);
        } else if (getTimerProgress() == 40) {
            performIceSlash(2, xP, zP);
            shootShard(6);
        }
    }

    public void performImpact(double xOffset, double zOffset) {
        Pair<SoundEvent, SoundEvent> sound;
        ParticleType<?> particle;
        CSVisualType impact;
        if (SkinUtil.getSkinIndex(stack) == 1) {
            sound = Pair.of(CSSoundEvents.GROUND_IMPACT_WATER.get(), SoundEvents.PLAYER_HURT_DROWN);
            particle = CSParticleTypes.WATER_DROP.get();
            impact = CSVisualTypes.FROSTBOUND_IMPACT_CRACK_SEABR.get();
        } else {
            sound = Pair.of(CSSoundEvents.SWORD_CLASH.get(), SoundEvents.PLAYER_HURT_FREEZE);
            particle = ParticleTypes.SNOWFLAKE;
            impact = CSVisualTypes.FROSTBOUND_IMPACT_CRACK.get();
        }
        BlockPos groundPos = this.getFloorPositionUnderPlayer(level, player.blockPosition());
        player.setDeltaMovement(0, (groundPos.getY() - player.getY()), 0);
        this.playSoundAt(level, SoundEvents.GLASS_BREAK, groundPos);
        this.playSoundAt(level, sound.getFirst(), groundPos);
        CSEffectEntity.createInstanceLockedPos(player, null, impact, player.getX() + xOffset, groundPos.getY() - 0.75, player.getZ() + zOffset);
        for (int i = 0; i < 360; i = i + 4) {
            double xI = xOffset + Mth.sin(i) * 3;
            double zI = zOffset + Mth.cos(i) * 3;
            ParticleUtil.sendParticles(level, particle, player.getX() + xI, groundPos.getY() + 1.5, player.getZ() + zI, 1, (xI / 5), 0, (zI / 5));
        }
        if (level.isClientSide()) shakeScreens(player, 5, 4, 0.0015F);
        for (Entity entity : iterateEntities(level, createAABB(groundPos.offset((int) xOffset, 1, (int) zOffset), 6, 3))) {
            if (entity instanceof LivingEntity target && entity != player) {
                initiateAbilityAttack(player, target, (float) (double) CSConfigManager.COMMON.frostboundSkillDmg.get() + getSharpnessValue(stack, 1.75F), AttackHurtTypes.NO_KB);
                CSEntityCapabilityProvider.get(target).ifPresent(data -> {
                    data.setFrostbound(100);
                });
                entity.playSound(sound.getSecond());
            }
        }
    }

    public void performIceSlash(int slashIndex, double xOffset, double zOffset) {
        Pair<SoundEvent, SoundEvent> sound;
        ParticleType<?> particle;
        CSVisualType slash;
        if (SkinUtil.getSkinIndex(stack) == 1) {
            sound = Pair.of(CSSoundEvents.SLASH_WATER.get(), SoundEvents.PLAYER_HURT_DROWN);
            particle = CSParticleTypes.WATER_DROP.get();
            slash = switch (slashIndex) {
                default -> CSVisualTypes.FROSTBOUND_SLASH_SEABR.get();
                case 1 -> CSVisualTypes.FROSTBOUND_SLASH_INVERTED_SEABR.get();
                case 2 -> CSVisualTypes.FROSTBOUND_SLASH_LARGE_SEABR.get();
            };
        } else {
            sound = Pair.of(CSSoundEvents.FROZEN_SLASH.get(), SoundEvents.PLAYER_HURT_DROWN);
            particle = ParticleTypes.SNOWFLAKE;
            slash = switch (slashIndex) {
                default -> CSVisualTypes.FROSTBOUND_SLASH.get();
                case 1 -> CSVisualTypes.FROSTBOUND_SLASH_INVERTED.get();
                case 2 -> CSVisualTypes.FROSTBOUND_SLASH_LARGE.get();
            };
        }
        this.playSoundAt(level, sound.getFirst(), player.blockPosition().offset((int) xOffset, 0, (int) zOffset));
        CSEffectEntity.createInstance(player, player, slash, xOffset, slashIndex == 2 ? 0.15 : 0.25, zOffset);
        for (int i = 0; i < 360; i = i + 4) {
            double sizeMult = slashIndex == 2 ? 1.5 : 0.5;
            double xI = xOffset + Mth.sin(i) * 3;
            double zI = zOffset + Mth.cos(i) * 3;
            ParticleUtil.sendParticle(level, particle, player.getX() + xI, player.getY() + 0.5, player.getZ() + zI, Mth.sin(i) * sizeMult, 0, Mth.cos(i) * sizeMult);
        }
        for (Entity entity : iterateEntities(level, createAABB(player.blockPosition().offset((int) xOffset, 1, (int) zOffset), slashIndex == 2 ? 8 : 5, 3))) {
            if (entity instanceof LivingEntity target && entity != player) {
                initiateAbilityAttack(player, target, 6 + getSharpnessValue(stack, 1.5F), AttackHurtTypes.REGULAR);
                CSEntityCapabilityProvider.get(target).ifPresent(data -> {
                    data.setFrostbound(60);
                });
                entity.playSound(sound.getSecond());
            }
        }
        player.setDeltaMovement(calculateXLook(player) * 1.5 , 0.25, calculateZLook(player) * 1.5);
    }

    @Override
    public void stopUsing() {
    }
}
