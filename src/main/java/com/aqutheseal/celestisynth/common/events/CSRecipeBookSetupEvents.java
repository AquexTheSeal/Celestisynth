package com.aqutheseal.celestisynth.common.events;

import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSRecipeTypes;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.List;

/**
 * @author KhanhTypo
 */
public class CSRecipeBookSetupEvents {
    public static final RecipeBookType CELESTIAL_CRAFTING = RecipeBookType.create("celestial_crafting");

    public static void registerEvent(RegisterRecipeBookCategoriesEvent event) {
        final RecipeBookCategories celestialCraftingSearch = RecipeBookCategories.create("celestial_crafting", new ItemStack(CSItems.CELESTIAL_CORE.get()));
        final RecipeBookCategories celestialWeapons = RecipeBookCategories.create("celestial_weapons", new ItemStack(CSItems.SUPERNAL_NETHERITE_INGOT.get()));
        final var celestialList = List.of(celestialWeapons, celestialCraftingSearch);

        event.registerBookCategories(CELESTIAL_CRAFTING, celestialList);
        event.registerRecipeCategoryFinder(CSRecipeTypes.CELESTIAL_CRAFTING_TYPE.get(), recipe -> celestialWeapons);
        event.registerAggregateCategory(celestialCraftingSearch, celestialList);
    }

    /**
     * The static constants above must be called and assigned in both client and server side. Therefore, add the event listener above in the lamba
     * expression will ONLY makes both the static constants and the method be called IN CLIENT, but in server : this entier class, including methods and
     * static constants will be ignored -> sided mismatch -> server sync failed
     */
    public static void staticInit() {}
}
