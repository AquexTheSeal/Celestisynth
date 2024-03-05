package com.aqutheseal.celestisynth.common.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

public class CSRarityTypes {
    public static final Rarity CELESTIAL = Rarity.create("celestial", ChatFormatting.GOLD);
    public static final Rarity ETHEREAL = Rarity.create("ethereal", style -> style.withColor(0xb3f7ff).withBold(true).withUnderlined(true));
}
