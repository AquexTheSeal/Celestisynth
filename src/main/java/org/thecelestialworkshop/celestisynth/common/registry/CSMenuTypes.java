package org.thecelestialworkshop.celestisynth.common.registry;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.client.gui.starlitfactory.StarlitFactoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CSMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Celestisynth.MODID);

    public static final Supplier<MenuType<StarlitFactoryMenu>> STARLIT_FACTORY = MENU_TYPES.register("starlit_factory", () -> new MenuType<>(StarlitFactoryMenu::new, null));
}
