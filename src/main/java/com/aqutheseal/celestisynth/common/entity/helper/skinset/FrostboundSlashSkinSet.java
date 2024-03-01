package com.aqutheseal.celestisynth.common.entity.helper.skinset;

import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSSkinSet;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualAnimation;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualModel;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.util.SkinUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FrostboundSlashSkinSet extends CSSkinSet {
    public static final CSVisualType FROSTBOUND_SLASH = new CSVisualType("frostbound_slash", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 2.5, false, true, true);
    public static final CSVisualType FROSTBOUND_SLASH_INVERTED = new CSVisualType("frostbound_slash_inverted", "frostbound_slash", CSVisualModel.FLAT_INVERTED, CSVisualAnimation.SPIN, 0, 0, 2.5, false, true, true);
    public static final CSVisualType FROSTBOUND_SLASH_LARGE = new CSVisualType("frostbound_slash_large", "frostbound_slash", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 3.5, false, true, true);

    public FrostboundSlashSkinSet(ItemStack stack, Player player) {
        super(stack, player);
    }

    public static FrostboundSlashSkinSet of(ItemStack stack, Player player) {
        return new FrostboundSlashSkinSet(stack, player);
    }

    public CSVisualType frozenSlashEffect(double xP, double zP) {
        if (SkinUtil.getSkinIndex(stack) == 1) {
            CSEffectEntity.createInstance(player, player, CSVisualTypes.SOLARIS_BLITZ_SOUL.get(), xP, 0.25, zP);
            CSEffectEntity.createInstance(player, player, CSVisualTypes.SOLARIS_BLITZ_SOUL.get(), xP, 0.25, zP);
            return CSVisualTypes.FROSTBOUND_SLASH_SEABR.get();
        } else {
            return CSVisualTypes.FROSTBOUND_SLASH.get();
        }
    }

    public CSVisualType frozenSlashInvertEffect(double xP, double zP) {
        if (SkinUtil.getSkinIndex(stack) == 1) {
            return CSVisualTypes.FROSTBOUND_SLASH_INVERTED_SEABR.get();
        } else {
            return CSVisualTypes.FROSTBOUND_SLASH_INVERTED.get();
        }
    }

    public CSVisualType frozenSlashLargeEffect(double xP, double zP) {
        if (SkinUtil.getSkinIndex(stack) == 1) {
            return CSVisualTypes.FROSTBOUND_SLASH_LARGE_SEABR.get();
        } else {
            return CSVisualTypes.FROSTBOUND_SLASH_LARGE.get();
        }
    }

    public SimpleParticleType frozenSlashParticle() {
        if (SkinUtil.getSkinIndex(stack) == 1) {
            return CSParticleTypes.RAINFALL_BEAM.get();
        } else {
            return ParticleTypes.SNOWFLAKE;
        }
    }

    public SoundEvent frozenSlashSound() {
        if (SkinUtil.getSkinIndex(stack) == 1) {
            return SoundEvents.PLAYER_HURT_DROWN;
        } else {
            return CSSoundEvents.FROZEN_SLASH.get();
        }
    }
}
