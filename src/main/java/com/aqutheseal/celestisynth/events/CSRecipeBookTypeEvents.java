package com.aqutheseal.celestisynth.events;

import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import com.aqutheseal.celestisynth.registry.CSRecipeRegistry;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.List;

/**
 * @author KhanhTypo
 */
public final class CSRecipeBookTypeEvents {
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

    /**
     * The static constants above must be called and assigned in both client and server side. Therefore, add the event listener above in the lambda
     * expression will ONLY make both the static constants and the method be called IN CLIENT, but in server : this entire class, including methods and
     * static constants will be ignored -> sided mismatch -> server sync failed
     */
    public static void staticInit() {}
}