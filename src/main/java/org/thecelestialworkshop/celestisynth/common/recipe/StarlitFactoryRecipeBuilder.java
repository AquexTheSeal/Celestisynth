package org.thecelestialworkshop.celestisynth.common.recipe;

import org.thecelestialworkshop.celestisynth.common.registry.CSRecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class StarlitFactoryRecipeBuilder implements RecipeBuilder {
    protected final Ingredient baseMaterial;
    protected final Ingredient baseMaterial1;
    protected final Ingredient baseMaterial2;
    protected final Ingredient supportingMaterial;
    protected final Ingredient supportingMaterial1;
    protected final Ingredient supportingMaterial2;
    protected final Item result;
    protected final int forgeTime;
    private final RecipeSerializer<? extends StarlitFactoryRecipe> serializer;

    public StarlitFactoryRecipeBuilder(Ingredient baseMaterial, Ingredient baseMaterial1, Ingredient baseMaterial2, Ingredient supportingMaterial, Ingredient supportingMaterial1, Ingredient supportingMaterial2, ItemLike result, int forgeTime) {
        this.baseMaterial = baseMaterial;
        this.baseMaterial1 = baseMaterial1;
        this.baseMaterial2 = baseMaterial2;
        this.supportingMaterial = supportingMaterial;
        this.supportingMaterial1 = supportingMaterial1;
        this.supportingMaterial2 = supportingMaterial2;
        this.result = result.asItem();
        this.forgeTime = forgeTime;
        this.serializer = CSRecipeTypes.STARLIT_FACTORY.get();
    }

    public static StarlitFactoryRecipeBuilder starlitFactory(Ingredient baseMaterial, Ingredient baseMaterial1, Ingredient baseMaterial2, Ingredient supportingMaterial, Ingredient supportingMaterial1, Ingredient supportingMaterial2, ItemLike result, int forgeTime) {
        return new StarlitFactoryRecipeBuilder(baseMaterial, baseMaterial1, baseMaterial2, supportingMaterial, supportingMaterial1, supportingMaterial2, result, forgeTime);
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, baseMaterial, baseMaterial1, baseMaterial2, supportingMaterial, supportingMaterial1, supportingMaterial2, result, forgeTime));
    }

    @Override
    public Item getResult() {
        return result;
    }

    static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        protected final Ingredient baseMaterial;
        protected final Ingredient baseMaterial1;
        protected final Ingredient baseMaterial2;
        protected final Ingredient supportingMaterial;
        protected final Ingredient supportingMaterial1;
        protected final Ingredient supportingMaterial2;
        protected final Item result;
        protected final int forgeTime;

        Result(ResourceLocation id, Ingredient baseMaterial, Ingredient baseMaterial1, Ingredient baseMaterial2, Ingredient supportingMaterial, Ingredient supportingMaterial1, Ingredient supportingMaterial2, Item result, int forgeTime) {
            this.id = id;
            this.baseMaterial = baseMaterial;
            this.baseMaterial1 = baseMaterial1;
            this.baseMaterial2 = baseMaterial2;
            this.supportingMaterial = supportingMaterial;
            this.supportingMaterial1 = supportingMaterial1;
            this.supportingMaterial2 = supportingMaterial2;
            this.result = result;
            this.forgeTime = forgeTime;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("core_material", this.baseMaterial.toJson());
            pJson.add("supporting_core_material", this.baseMaterial1.toJson());
            pJson.add("extra_core_material", this.baseMaterial2.toJson());

            pJson.add("supporting_material_top", this.supportingMaterial.toJson());
            pJson.add("supporting_material_middle", this.supportingMaterial1.toJson());
            pJson.add("supporting_material_bottom", this.supportingMaterial2.toJson());

            pJson.addProperty("result", BuiltInRegistries.ITEM.getKey(this.result).toString());

            pJson.addProperty("forging_time", this.forgeTime);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CSRecipeTypes.STARLIT_FACTORY.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

}
