package com.aqutheseal.celestisynth.api.skill;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public abstract class BaseSkillInstance implements ISkillInstance {

    @Override
    public abstract String getName();

    @Override
    public Component getDescription() {
        return Component.translatable("celestisynth.skill.description.default");
    }

    @Override
    public Component getCriterionDescription() {
        return Component.translatable("celestisynth.skill.description.criterion.default");
    }

    @Override
    public ResourceLocation getIcon() {
        return Celestisynth.prefix("skills/base/default.png");
    }

    @Nullable
    @Override
    public WeaponAttackInstance getAttack() {
        return null;
    }

    @Override
    public ISkillTier getTier() {
        return CelestisynthSkillTiers.BASIC;
    }

    @Nullable
    @Override
    public abstract AbstractSkillTree getSkillTree();

    @Override
    public final int getMinSkillLevel() {
        return 0;
    }

    @Override
    public int getMaxSkillLevel() {
        return 3;
    }

    @Override
    public abstract boolean isPassive();

    @Override
    public boolean getActivationConditions(Player owner) {
        return false;
    }

    @Override
    public boolean getDeactivationConditions(Player owner) {
        return false;
    }

    @Override
    public void onSkillLearned(Player owner) {

    }

    @Override
    public void onSkillUnlearned(Player owner) {

    }

    @Override
    public void onSkillLevelChanged(Player owner, boolean hasDecreased) {

    }

    @Override
    public void onSkillActivated(Player owner) {

    }

    @Override
    public void onSkillTick(Player owner) {

    }

    @Override
    public void onPassiveTick(Player owner) {

    }

    @Override
    public abstract void onSkillDeactivated(Player owner);

    @Override
    public abstract void setSkillLevel(int newSkillLevel);

    @Override
    public abstract void setTier(ISkillTier newTier);

    @Override
    public abstract void setAttack(@Nullable WeaponAttackInstance newAttack, boolean interruptCurrentAttack);

    @Override
    public abstract void setDescription(Component newDescription);

    @Override
    public CompoundTag serializeToNBT() {
        return null;
    }
}
