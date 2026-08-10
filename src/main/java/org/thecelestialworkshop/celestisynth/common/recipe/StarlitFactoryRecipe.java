package org.thecelestialworkshop.celestisynth.common.recipe;

import org.thecelestialworkshop.celestisynth.common.registry.CSRecipeTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class StarlitFactoryRecipe implements Recipe<StarlitFactoryRecipeInput> {
    protected final Ingredient baseMaterial;
    protected final Ingredient baseMaterial1;
    protected final Ingredient baseMaterial2;
    protected final Ingredient supportingMaterial;
    protected final Ingredient supportingMaterial1;
    protected final Ingredient supportingMaterial2;
    protected final ItemStack result;
    protected final int forgeTime;

    public StarlitFactoryRecipe(Ingredient baseMaterial, Ingredient baseMaterial1, Ingredient baseMaterial2, Ingredient supportingMaterial, Ingredient supportingMaterial1, Ingredient supportingMaterial2, ItemStack result, int forgeTime) {
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

    @Override
    public boolean matches(StarlitFactoryRecipeInput pInv, Level pLevel) {
        return
                baseMaterial.test(!pInv.getItem(0).isEmpty() ? pInv.getItem(0) : ItemStack.EMPTY) &&
                        baseMaterial1.test(!pInv.getItem(1).isEmpty() ? pInv.getItem(1) : ItemStack.EMPTY) &&
                        baseMaterial2.test(!pInv.getItem(2).isEmpty() ? pInv.getItem(2) : ItemStack.EMPTY) &&
                supportingMaterial.test(!pInv.getItem(3).isEmpty() ? pInv.getItem(3) : ItemStack.EMPTY) &&
                        supportingMaterial1.test(!pInv.getItem(4).isEmpty() ? pInv.getItem(4) : ItemStack.EMPTY) &&
                        supportingMaterial2.test(!pInv.getItem(5).isEmpty() ? pInv.getItem(5) : ItemStack.EMPTY)
                ;
    }

    @Override
    public ItemStack assemble(StarlitFactoryRecipeInput pContainer, HolderLookup.Provider pRegistries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
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

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
    }

    public int getForgeTime() {
        return this.forgeTime;
    }

    @Override
    public RecipeType<?> getType() {
        return CSRecipeTypes.STARLIT_FACTORY_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<StarlitFactoryRecipe> {
        public static final int DEFAULT_FORGING_TIME = 200;

        public static final MapCodec<StarlitFactoryRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("core_material").forGetter(r -> r.baseMaterial),
                Ingredient.CODEC.fieldOf("supporting_core_material").forGetter(r -> r.baseMaterial1),
                Ingredient.CODEC.fieldOf("extra_core_material").forGetter(r -> r.baseMaterial2),
                Ingredient.CODEC.fieldOf("supporting_material_top").forGetter(r -> r.supportingMaterial),
                Ingredient.CODEC.fieldOf("supporting_material_middle").forGetter(r -> r.supportingMaterial1),
                Ingredient.CODEC.fieldOf("supporting_material_bottom").forGetter(r -> r.supportingMaterial2),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
                Codec.INT.optionalFieldOf("forging_time", DEFAULT_FORGING_TIME).forGetter(r -> r.forgeTime)
        ).apply(instance, StarlitFactoryRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, StarlitFactoryRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        private static StarlitFactoryRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            Ingredient baseMaterial = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            Ingredient baseMaterial1 = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            Ingredient baseMaterial2 = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            Ingredient supportingMaterial = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            Ingredient supportingMaterial1 = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            Ingredient supportingMaterial2 = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(pBuffer);
            int forgingTime = pBuffer.readVarInt();
            return new StarlitFactoryRecipe(baseMaterial, baseMaterial1, baseMaterial2, supportingMaterial, supportingMaterial1, supportingMaterial2, result, forgingTime);
        }

        private static void toNetwork(RegistryFriendlyByteBuf pBuffer, StarlitFactoryRecipe pRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.baseMaterial);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.baseMaterial1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.baseMaterial2);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.supportingMaterial);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.supportingMaterial1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.supportingMaterial2);
            ItemStack.STREAM_CODEC.encode(pBuffer, pRecipe.result);
            pBuffer.writeVarInt(pRecipe.forgeTime);
        }

        @Override
        public MapCodec<StarlitFactoryRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StarlitFactoryRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
