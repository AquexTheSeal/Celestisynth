package org.thecelestialworkshop.celestisynth;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.thecelestialworkshop.celestisynth.common.registry.CSTags;
import org.thecelestialworkshop.celestisynth.manager.CSModManager;
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

        if (modEventBus != null && forgeEventBus != null)  CSModManager.registerAll(modEventBus, forgeEventBus);
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(MODID, path.toLowerCase(Locale.ROOT));
    }
}
