package com.aqutheseal.celestisynth.common.recipe.celestialcrafting;

import com.aqutheseal.celestisynth.common.registry.CSRecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class CelestialShapedRecipe extends ShapedRecipe implements CelestialCraftingRecipe {

    public CelestialShapedRecipe(ResourceLocation pId, String pGroup, CraftingBookCategory pCategory, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult, boolean pShowNotification) {
        super(pId, pGroup, pCategory, pWidth, pHeight, pRecipeItems, pResult, pShowNotification);
    }

    public CelestialShapedRecipe(ResourceLocation pId, String pGroup, CraftingBookCategory pCategory, int pWidth, int pHeight, NonNullList<Ingredient> pRecipeItems, ItemStack pResult) {
        super(pId, pGroup, pCategory, pWidth, pHeight, pRecipeItems, pResult);
    }

    public RecipeSerializer<?> getSerializer() {
        return CSRecipeTypes.SHAPED_CELESTIAL_CRAFTING.get();
    }

    @Override
    public RecipeType<CelestialCraftingRecipe> getType() {
        return CSRecipeTypes.CELESTIAL_CRAFTING_TYPE.get();
    }

    @Override
    public CraftingBookCategory category() {
        return super.category();
    }

    public static class Serializer implements RecipeSerializer<CelestialShapedRecipe> {
        final RegistryAccess registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);

        public Serializer() {
        }

        @Override
        public CelestialShapedRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            ShapedRecipe fromJson = SHAPED_RECIPE.fromJson(pRecipeId, pJson);
            return new CelestialShapedRecipe(pRecipeId, fromJson.getGroup(), fromJson.category(), fromJson.getWidth(), fromJson.getHeight(), fromJson.getIngredients(), fromJson.getResultItem(registryAccess), fromJson.showNotification());
        }

        @Override
        public CelestialShapedRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ShapedRecipe fromNetwork = SHAPED_RECIPE.fromNetwork(pRecipeId, pBuffer);
            return new CelestialShapedRecipe(pRecipeId, fromNetwork.getGroup(), fromNetwork.category(), fromNetwork.getWidth(), fromNetwork.getHeight(), fromNetwork.getIngredients(), fromNetwork.getResultItem(registryAccess), fromNetwork.showNotification());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CelestialShapedRecipe pRecipe) {
            SHAPED_RECIPE.toNetwork(pBuffer, pRecipe);
        }
    }
}
