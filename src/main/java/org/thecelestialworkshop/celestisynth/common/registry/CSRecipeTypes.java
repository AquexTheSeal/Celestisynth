package org.thecelestialworkshop.celestisynth.common.registry;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.recipe.StarlitFactoryRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CSRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Celestisynth.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Celestisynth.MODID);

    public static final Supplier<RecipeType<StarlitFactoryRecipe>> STARLIT_FACTORY_TYPE = RECIPE_TYPES.register("starlit_factory_type", () -> new SimpleNamedRecipeType<>("starlit_factory"));
    public static final Supplier<RecipeSerializer<StarlitFactoryRecipe>> STARLIT_FACTORY = RECIPE_SERIALIZERS.register("starlit_factory", StarlitFactoryRecipe.Serializer::new);

    public record SimpleNamedRecipeType<T extends Recipe<?>>(String name) implements RecipeType<T> {
        @Override
        public String toString() {
            return name;
        }
    }
}
