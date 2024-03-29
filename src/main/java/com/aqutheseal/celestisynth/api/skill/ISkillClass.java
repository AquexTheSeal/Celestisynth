package com.aqutheseal.celestisynth.api.skill;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.network.chat.Component;

public interface ISkillClass {

    Component getClassName();
    Component getClassDescription();

    ObjectLinkedOpenHashSet<AbstractSkillTree> getAvailableSkillTrees();
}
