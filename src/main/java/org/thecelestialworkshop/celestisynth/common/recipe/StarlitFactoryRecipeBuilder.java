package org.thecelestialworkshop.celestisynth.common.recipe;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public class StarlitFactoryRecipeBuilder implements RecipeBuilder {
    protected final Ingredient baseMaterial;
    protected final Ingredient baseMaterial1;
    protected final Ingredient baseMaterial2;
    protected final Ingredient supportingMaterial;
    protected final Ingredient supportingMaterial1;
    protected final Ingredient supportingMaterial2;
    protected final Item result;
    protected final int forgeTime;

    public StarlitFactoryRecipeBuilder(Ingredient baseMaterial, Ingredient baseMaterial1, Ingredient baseMaterial2, Ingredient supportingMaterial, Ingredient supportingMaterial1, Ingredient supportingMaterial2, ItemLike result, int forgeTime) {
        this.baseMaterial = baseMaterial;
        this.baseMaterial1 = baseMaterial1;
        this.baseMaterial2 = baseMaterial2;
        this.supportingMaterial = supportingMaterial;
        this.supportingMaterial1 = supportingMaterial1;
        this.supportingMaterial2 = supportingMaterial2;
        this.result = result.asItem();
        this.forgeTime = forgeTime;
    }

    public static StarlitFactoryRecipeBuilder starlitFactory(Ingredient baseMaterial, Ingredient baseMaterial1, Ingredient baseMaterial2, Ingredient supportingMaterial, Ingredient supportingMaterial1, Ingredient supportingMaterial2, ItemLike result, int forgeTime) {
        return new StarlitFactoryRecipeBuilder(baseMaterial, baseMaterial1, baseMaterial2, supportingMaterial, supportingMaterial1, supportingMaterial2, result, forgeTime);
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pRecipeId) {
        pRecipeOutput.accept(pRecipeId, new StarlitFactoryRecipe(
                baseMaterial, baseMaterial1, baseMaterial2,
                supportingMaterial, supportingMaterial1, supportingMaterial2,
                new ItemStack(result), forgeTime
        ), null);
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, Criterion<?> pCriterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

}
