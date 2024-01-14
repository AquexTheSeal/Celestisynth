package com.aqutheseal.celestisynth.api.skill;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.network.chat.Component;

public interface ISkillCategory {

    String getCategoryName();
    Component getCategoryDescription();

    ObjectLinkedOpenHashSet<AbstractSkillTree> getAvailableSkillTrees();
}
