package org.thecelestialworkshop.celestisynth.common.recipe;

import org.thecelestialworkshop.celestisynth.common.registry.CSRecipeTypes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StarlitFactoryRecipe implements Recipe<Container> {
    protected final ResourceLocation id;
    protected final Ingredient baseMaterial;
    protected final Ingredient baseMaterial1;
    protected final Ingredient baseMaterial2;
    protected final Ingredient supportingMaterial;
    protected final Ingredient supportingMaterial1;
    protected final Ingredient supportingMaterial2;
    protected final ItemStack result;
    protected final int forgeTime;

    public StarlitFactoryRecipe(ResourceLocation id, Ingredient baseMaterial, Ingredient baseMaterial1, Ingredient baseMaterial2, Ingredient supportingMaterial, Ingredient supportingMaterial1, Ingredient supportingMaterial2, ItemStack result, int forgeTime) {
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
    public RecipeSerializer<?> getSerializer() {
        return CSRecipeTypes.STARLIT_FACTORY.get();
    }

    public boolean matches(Container pInv, Level pLevel) {
        return
                baseMaterial.test(!pInv.getItem(0).isEmpty() ? pInv.getItem(0) : ItemStack.EMPTY) &&
                        baseMaterial1.test(!pInv.getItem(1).isEmpty() ? pInv.getItem(1) : ItemStack.EMPTY) &&
                        baseMaterial2.test(!pInv.getItem(2).isEmpty() ? pInv.getItem(2) : ItemStack.EMPTY) &&
                supportingMaterial.test(!pInv.getItem(3).isEmpty() ? pInv.getItem(3) : ItemStack.EMPTY) &&
                        supportingMaterial1.test(!pInv.getItem(4).isEmpty() ? pInv.getItem(4) : ItemStack.EMPTY) &&
                        supportingMaterial2.test(!pInv.getItem(5).isEmpty() ? pInv.getItem(5) : ItemStack.EMPTY)
                ;
    }

    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.baseMaterial);
        nonnulllist.add(this.baseMaterial1);
        nonnulllist.add(this.baseMaterial2);
        nonnulllist.add(this.supportingMaterial);
        nonnulllist.add(this.supportingMaterial1);
        nonnulllist.add(this.supportingMaterial2);
        return nonnulllist;
    }

    public ItemStack getResult() {
        return result;
    }

    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.result;
    }

    public int getForgeTime() {
        return this.forgeTime;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeType<?> getType() {
        return CSRecipeTypes.STARLIT_FACTORY_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<StarlitFactoryRecipe> {
        private final int defaultForgingTime;

        public Serializer() {
            this.defaultForgingTime = 200;
        }

        @Override
        public StarlitFactoryRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {

            // MATERIALS

            Ingredient baseMaterial = Ingredient.fromJson(this.extractJsonElement(pJson, "core_material"), true);
            Ingredient baseMaterial1 = Ingredient.fromJson(this.extractJsonElement(pJson, "supporting_core_material"), true);
            Ingredient baseMaterial2 = Ingredient.fromJson(this.extractJsonElement(pJson, "extra_core_material"), true);

            Ingredient supportingMaterial = Ingredient.fromJson(this.extractJsonElement(pJson, "supporting_material_top"), true);
            Ingredient supportingMaterial1 = Ingredient.fromJson(this.extractJsonElement(pJson, "supporting_material_middle"), true);
            Ingredient supportingMaterial2 = Ingredient.fromJson(this.extractJsonElement(pJson, "supporting_material_bottom"), true);

            // RESULT

            if (!pJson.has("result")) {
                throw new JsonSyntaxException("mfw you try'na make a recipe without a result item wtf are you trying to cook??");
            }

            ItemStack result;
            if (pJson.get("result").isJsonObject()) {
                result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
            } else {
                String resultItemFromJsonBrainrot = GsonHelper.getAsString(pJson, "result");
                ResourceLocation resourcelocation = new ResourceLocation(resultItemFromJsonBrainrot);
                result = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException(resultItemFromJsonBrainrot + " does NOT exist, that's not COOL AT ALL!!!")));
            }

            // FORGING TIME

            int forgingTime = GsonHelper.getAsInt(pJson, "forging_time", this.defaultForgingTime);

            return new StarlitFactoryRecipe(pRecipeId, baseMaterial, baseMaterial1, baseMaterial2, supportingMaterial, supportingMaterial1, supportingMaterial2, result, forgingTime);
        }

        @Override
        public @Nullable StarlitFactoryRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient baseMaterial = Ingredient.fromNetwork(pBuffer);
            Ingredient baseMaterial1 = Ingredient.fromNetwork(pBuffer);
            Ingredient baseMaterial2 = Ingredient.fromNetwork(pBuffer);
            Ingredient supportingMaterial = Ingredient.fromNetwork(pBuffer);
            Ingredient supportingMaterial1 = Ingredient.fromNetwork(pBuffer);
            Ingredient supportingMaterial2 = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int forgingTime = pBuffer.readVarInt();
            return new StarlitFactoryRecipe(pRecipeId, baseMaterial, baseMaterial1, baseMaterial2, supportingMaterial, supportingMaterial1, supportingMaterial2, result, forgingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, StarlitFactoryRecipe pRecipe) {
            pRecipe.baseMaterial.toNetwork(pBuffer);
            pRecipe.baseMaterial1.toNetwork(pBuffer);
            pRecipe.baseMaterial2.toNetwork(pBuffer);
            pRecipe.supportingMaterial.toNetwork(pBuffer);
            pRecipe.supportingMaterial1.toNetwork(pBuffer);
            pRecipe.supportingMaterial2.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.forgeTime);
        }

        public JsonElement extractJsonElement(JsonObject pJson, String memberName) {
            return GsonHelper.isArrayNode(pJson, memberName) ? GsonHelper.getAsJsonArray(pJson, memberName) : GsonHelper.getAsJsonObject(pJson, memberName);
        }
    }
}
