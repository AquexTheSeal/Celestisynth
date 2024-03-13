package com.aqutheseal.celestisynth.common.entity.helper.skinset;

import com.aqutheseal.celestisynth.common.entity.helper.CSSkinSet;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualAnimation;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualModel;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.item.weapons.FrostboundItem;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.util.SkinUtil;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FrostboundSlashSkinSet extends CSSkinSet {
    public static final CSVisualType FROSTBOUND_SLASH = new CSVisualType("frostbound_slash", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 2.5, false, true, true);
    public static final CSVisualType FROSTBOUND_SLASH_INVERTED = new CSVisualType("frostbound_slash_inverted", "frostbound_slash", CSVisualModel.FLAT_INVERTED, CSVisualAnimation.SPIN, 0, 0, 2.5, false, true, true);
    public static final CSVisualType FROSTBOUND_SLASH_LARGE = new CSVisualType("frostbound_slash_large", "frostbound_slash", CSVisualModel.FLAT, CSVisualAnimation.SPIN, 0, 0, 3.5, false, true, true);
    public static final CSVisualType FROSTBOUND_IMPACT_CRACK = new CSVisualType("frostbound_impact_crack", CSVisualModel.FLAT, CSVisualAnimation.noAnimWithLifespan(20), 0, 0, 2.5, false, true, false);
    public static final CSVisualType FROSTBOUND_ICE_CAST = new CSVisualType("frostbound_ice_cast", CSVisualModel.WALL_CROSS, FrostboundItem.SPECIAL_ICE_CAST, 0, 0, 1, false, true, false);
    public static final CSVisualType FROSTBOUND_SHARD_PULSE = new CSVisualType("frostbound_shard_pulse", CSVisualModel.FLAT_VERTICAL_FRONTFACE, CSVisualAnimation.SPIN_EXPAND, 0, 0, 1, false, true, false);

    public FrostboundSlashSkinSet(ItemStack stack, Player player) {
        super(stack, player);
    }

    public static FrostboundSlashSkinSet of(ItemStack stack, Player player) {
        return new FrostboundSlashSkinSet(stack, player);
    }

    public CSVisualType frozenShardPulseEffect() {
        if (SkinUtil.getSkinIndex(stack) == 1) {
            return CSVisualTypes.FROSTBOUND_SHARD_PULSE_SEABR.get();
        } else {
            return CSVisualTypes.FROSTBOUND_SHARD_PULSE.get();
        }
    }

    public SoundEvent frozenShardPulseSound() {
        if (SkinUtil.getSkinIndex(stack) == 1) {
            return CSSoundEvents.WATER_CAST.get();
        } else {
            return SoundEvents.BLAZE_SHOOT;
        }
    }
}
