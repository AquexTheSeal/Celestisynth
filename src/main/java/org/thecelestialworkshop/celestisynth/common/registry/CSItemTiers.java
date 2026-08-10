package org.thecelestialworkshop.celestisynth.common.registry;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * TierSortingRegistry no longer exists in 1.21; tier ordering is expressed
 * through the incorrect-blocks tag instead.
 */
public class CSItemTiers {
    public static final Tier CELESTIAL = new SimpleTier(
            CSTags.Blocks.INCORRECT_FOR_CELESTIAL_TOOL, 2550, 9.0F, 4.0F, 15,
            () -> Ingredient.of(CSItems.CELESTIAL_CORE_HEATED.get())
    );
}
