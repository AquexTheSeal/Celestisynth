package com.aqutheseal.celestisynth.recipe;

import com.aqutheseal.celestisynth.registry.CSRecipeRegistry;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class CelestialShapedRecipe extends ShapedRecipe implements CelestialCraftingRecipe {

    public CelestialShapedRecipe(ResourceLocation pId, String pGroup, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult) {
        super(pId, pGroup, pWidth, pHeight, pRecipeItems, pResult);
    }

    public RecipeSerializer<?> getSerializer() {
        return CSRecipeRegistry.SHAPED_CELESTIAL_CRAFTING.get();
    }

    @Override
    public RecipeType<CelestialCraftingRecipe> getType() {
        return CSRecipeRegistry.CELESTIAL_CRAFTING_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CelestialShapedRecipe> {
        RecipeSerializer<ShapedRecipe> serializer = new ShapedRecipe.Serializer();
        public Serializer() {
        }

        @Override
        public CelestialShapedRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            ShapedRecipe fromJson = serializer.fromJson(pRecipeId, pJson);
            return new CelestialShapedRecipe(pRecipeId, fromJson.getGroup(), fromJson.getWidth(), fromJson.getHeight(), fromJson.getIngredients(), fromJson.getResultItem());
        }

        @Override
        public CelestialShapedRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ShapedRecipe fromNetwork = serializer.fromNetwork(pRecipeId, pBuffer);
            return new CelestialShapedRecipe(pRecipeId, fromNetwork.getGroup(), fromNetwork.getWidth(), fromNetwork.getHeight(), fromNetwork.getIngredients(), fromNetwork.getResultItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CelestialShapedRecipe pRecipe) {
            serializer.toNetwork(pBuffer, pRecipe);
        }
    }
}
