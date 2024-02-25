package com.aqutheseal.celestisynth.common.entity.helper.skinset;

import com.aqutheseal.celestisynth.common.entity.helper.CSSkinSet;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualAnimation;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualModel;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.util.SkinUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class FrostboundSlashSkinSet extends CSSkinSet {
    public static final CSVisualType FROSTBOUND_SLASH = new CSVisualType("frostbound_slash", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 2.5, false, true, true);
    public static final CSVisualType FROSTBOUND_SLASH_INVERTED = new CSVisualType("frostbound_slash_inverted", "frostbound_slash", CSVisualModel.FLAT_INVERTED, CSVisualAnimation.SPIN, 0, 0, 2.5, false, true, true);
    public static final CSVisualType FROSTBOUND_SLASH_LARGE = new CSVisualType("frostbound_slash_large", "frostbound_slash", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 3.5, false, true, true);

    public FrostboundSlashSkinSet(ItemStack stack) {
        super(stack);
    }

    public static FrostboundSlashSkinSet of(ItemStack stack) {
        return new FrostboundSlashSkinSet(stack);
    }

    public CSVisualType frozenSlashEffect() {
        if (SkinUtil.isUsingAquaSkin(stack)) {
            return CSVisualTypes.FROSTBOUND_SLASH_SEABR.get();
        } else {
            return CSVisualTypes.FROSTBOUND_SLASH.get();
        }
    }

    public CSVisualType frozenSlashInvertEffect() {
        if (SkinUtil.isUsingAquaSkin(stack)) {
            return CSVisualTypes.FROSTBOUND_SLASH_INVERTED_SEABR.get();
        } else {
            return CSVisualTypes.FROSTBOUND_SLASH_INVERTED.get();
        }
    }

    public CSVisualType frozenSlashLargeEffect() {
        if (SkinUtil.isUsingAquaSkin(stack)) {
            return CSVisualTypes.FROSTBOUND_SLASH_LARGE_SEABR.get();
        } else {
            return CSVisualTypes.FROSTBOUND_SLASH_LARGE.get();
        }
    }

    public SimpleParticleType frozenSlashParticle() {
        if (SkinUtil.isUsingAquaSkin(stack)) {
            return ParticleTypes.CLOUD;
        } else {
            return ParticleTypes.SNOWFLAKE;
        }
    }

    public SoundEvent frozenSlashSound() {
        if (SkinUtil.isUsingAquaSkin(stack)) {
            return SoundEvents.PLAYER_HURT_DROWN;
        } else {
            return CSSoundEvents.FROZEN_SLASH.get();
        }
    }
}
