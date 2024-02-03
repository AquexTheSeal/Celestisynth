package com.aqutheseal.celestisynth.api.skill;

import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface ISkillInstance {

    Component getName();

    Component getDescription();
    Component getCriterionDescription();

    ResourceLocation getIcon();

    @Nullable
    WeaponAttackInstance getAttack();

    ISkillTier getTier();

    @Nullable
    AbstractSkillTree getSkillTree();

    int getMinSkillLevel();
    int getMaxSkillLevel();

    int getSkillCooldown();

    boolean isPassive();
    boolean getActivationConditions(Player owner);
    boolean getDeactivationConditions(Player owner);

    void onSkillLearned(Player owner);
    void onSkillUnlearned(Player owner);
    void onSkillLevelChanged(Player owner, boolean hasDecreased);
    void onSkillActivated(Player owner);
    void onSkillTick(Player owner);
    void onPassiveTick(Player owner);
    void onSkillDeactivated(Player owner);
    void setSkillLevel(int newSkillLevel);
    void setTier(ISkillTier newTier);
    void setAttack(@Nullable WeaponAttackInstance newAttack, boolean interruptCurrentAttack);
    void setDescription(Component newDescription);

    CompoundTag serializeToNBT();
}
