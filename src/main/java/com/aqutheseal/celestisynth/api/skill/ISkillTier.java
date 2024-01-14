package com.aqutheseal.celestisynth.api.skill;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface ISkillTier {
    /**
     * Gets the name of this tier instance.
     *
     * @return The name of this tier instance.
     */
    String getTierName();

    /**
     * The formatted description of this tier instance.
     *
     * @return The formatted description of this tier instance.
     */
    Component getTierDescription();

    /**
     * The icon image's path of this tier instance.
     *
     * @return The path icon of this tier instance's icon image.
     */
    ResourceLocation getTierIcon();

    /**
     * The weight variable which decides this tier's position on the skill tree hierarchy. Higher weight values place
     * this tier below already present tiers.
     *
     * If 2 tier instances happen to share the same weight value, one of them will be selected and moved below automatically.
     *
     * @return This tier instance's weight.
     */
    int getWeight();
}
