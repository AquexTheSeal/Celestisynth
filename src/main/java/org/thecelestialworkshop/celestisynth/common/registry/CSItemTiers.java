package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class CSItemTiers {
    public static final Tier CELESTIAL = TierSortingRegistry.registerTier(
            new ForgeTier(5, 2550, 9.0F, 4.0F, 15, CSTags.Blocks.NEEDS_CELESTIAL_TOOL, () ->
            Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get())),
            Celestisynth.prefix("celestial"), List.of(Tiers.NETHERITE), List.of()
    );
}
