package com.aqutheseal.celestisynth.api.skill;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public abstract class AbstractSkillTree {
    protected final Component treeName;
    protected final Component treeDescription;
    protected final ResourceLocation treeIconLoc;
    protected final ResourceLocation treeBgLoc;
    @Nullable
    protected final ISkillClass parentSkillClass;

    protected AbstractSkillTree(Component treeName, Component treeDescription, ResourceLocation treeIconLoc, ResourceLocation treeBgLoc, @Nullable ISkillClass parentSkillClass) {
        this.treeName = treeName;
        this.treeDescription = treeDescription;
        this.treeIconLoc = treeIconLoc;
        this.treeBgLoc = treeBgLoc;
        this.parentSkillClass = parentSkillClass;
    }
}
