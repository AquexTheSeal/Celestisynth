package com.aqutheseal.celestisynth.common.recipe.celestialcrafting;

import com.aqutheseal.celestisynth.common.registry.CSRecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
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

    public static class Serializer implements RecipeSerializer<CelestialShapedRecipe> {
        public Serializer() {
        }

        @Override
        public CelestialShapedRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            ShapedRecipe fromJson = SHAPED_RECIPE.fromJson(pRecipeId, pJson);
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(pJson, "category", null), CraftingBookCategory.MISC);
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
            return new CelestialShapedRecipe(pRecipeId, fromJson.getGroup(), craftingbookcategory, fromJson.getWidth(), fromJson.getHeight(), fromJson.getIngredients(), itemstack, fromJson.showNotification());
        }

        @Override
        public CelestialShapedRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ShapedRecipe fromNetwork = SHAPED_RECIPE.fromNetwork(pRecipeId, pBuffer);
            CraftingBookCategory craftingbookcategory = pBuffer.readEnum(CraftingBookCategory.class);
            ItemStack itemstack = pBuffer.readItem();
            return new CelestialShapedRecipe(pRecipeId, fromNetwork.getGroup(), craftingbookcategory, fromNetwork.getWidth(), fromNetwork.getHeight(), fromNetwork.getIngredients(), itemstack, fromNetwork.showNotification());
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CelestialShapedRecipe pRecipe) {
            SHAPED_RECIPE.toNetwork(pBuffer, pRecipe);
        }
    }
}
