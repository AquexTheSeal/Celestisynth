package com.aqutheseal.celestisynth.events;

import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import com.aqutheseal.celestisynth.registry.CSRecipeRegistry;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.List;

/**
 * Attempt 1 to fix server sync problem
 *
 * @see net.minecraft.stats.RecipeBookSettings#read(FriendlyByteBuf)
 * @see net.minecraft.stats.RecipeBookSettings#write(FriendlyByteBuf)
 */
public final class CSRecipeBookTypeEvent {
    public static final RecipeBookType CELESTIAL_CRAFTING = RecipeBookType.create("celestial_crafting");

    /**
     * Because {@link net.minecraft.client.RecipeBookCategories} is marked as it SHOULD ONLY BE called in client, not in server/common
     */
    public static void registerEvent(RegisterRecipeBookCategoriesEvent event) {
        final RecipeBookCategories celestialCraftingSearch = RecipeBookCategories.create("celestial_crafting", new ItemStack(CSItemRegistry.CELESTIAL_CORE.get()));
        final RecipeBookCategories celestialWeapons = RecipeBookCategories.create("celestial_weapons", new ItemStack(CSItemRegistry.SUPERNAL_NETHERITE_INGOT.get()));
        final var celestialList = List.of(celestialWeapons, celestialCraftingSearch);

        event.registerBookCategories(CELESTIAL_CRAFTING, celestialList);
        event.registerRecipeCategoryFinder(CSRecipeRegistry.CELESTIAL_CRAFTING_TYPE.get(), recipe -> celestialWeapons);
        event.registerAggregateCategory(celestialCraftingSearch, celestialList);
    }
}