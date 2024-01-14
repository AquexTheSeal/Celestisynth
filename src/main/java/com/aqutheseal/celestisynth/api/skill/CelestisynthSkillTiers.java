package com.aqutheseal.celestisynth.api.skill;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public enum CelestisynthSkillTiers implements ISkillTier {
    BASIC("basic", Component.translatable("celestisynth.skill.tier.basic"), Celestisynth.prefix("skills/tiers/basic.png"), 0),
    ADVANCED("advanced", Component.translatable("celestisynth.skill.tier.advanced"), Celestisynth.prefix("skills/tiers/advanced.png"), 1),
    EXPERT("expert", Component.translatable("celestisynth.skill.tier.expert"), Celestisynth.prefix("skills/tiers/expert.png"), 2);

    private final String tierName;
    private final Component tierDescription;
    private final ResourceLocation tierIcon;
    private final int weight;

    CelestisynthSkillTiers(String tierName, Component tierDescription, ResourceLocation tierIcon, int weight) {
        this.tierName = tierName;
        this.tierDescription = tierDescription;
        this.tierIcon = tierIcon;
        this.weight = weight;
    }

    @Override
    public String getTierName() {
        return tierName;
    }

    @Override
    public Component getTierDescription() {
        return tierDescription;
    }

    @Override
    public ResourceLocation getTierIcon() {
        return tierIcon;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
