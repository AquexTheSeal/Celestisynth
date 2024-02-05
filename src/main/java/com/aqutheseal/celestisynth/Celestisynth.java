package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.common.events.CSRecipeBookSetupEvents;
import com.aqutheseal.celestisynth.common.registry.CSTags;
import com.aqutheseal.celestisynth.manager.CSModManager;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.Locale;

@Mod(Celestisynth.MODID)
public class Celestisynth {
    public static final String MODID = "celestisynth";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Celestisynth() {
        GeckoLib.initialize();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        // Don't change
        CSTags.init();
        CSRecipeBookSetupEvents.init();
        modEventBus.addListener(CSRecipeBookSetupEvents::registerEvent);

        if (modEventBus != null && forgeEventBus != null)  CSModManager.registerAll(modEventBus, forgeEventBus);
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MODID, path.toLowerCase(Locale.ROOT));
    }
}
