package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.client.gui.celestialcrafting.CelestialCraftingMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Celestisynth.MODID);

    public static final RegistryObject<MenuType<CelestialCraftingMenu>> CELESTIAL_CRAFTING = MENU_TYPES.register("celestial_crafting", () -> new MenuType<>(CelestialCraftingMenu::new, null));
}
