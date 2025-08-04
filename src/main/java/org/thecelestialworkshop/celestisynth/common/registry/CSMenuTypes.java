package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.client.gui.starlitfactory.StarlitFactoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Celestisynth.MODID);

    public static final RegistryObject<MenuType<StarlitFactoryMenu>> STARLIT_FACTORY = MENU_TYPES.register("starlit_factory", () -> new MenuType<>(StarlitFactoryMenu::new, null));
}
