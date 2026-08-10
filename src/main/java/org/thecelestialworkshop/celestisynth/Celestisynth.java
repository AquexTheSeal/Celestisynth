package org.thecelestialworkshop.celestisynth;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.thecelestialworkshop.celestisynth.common.registry.CSTags;
import org.thecelestialworkshop.celestisynth.manager.CSModManager;

import java.util.Locale;

@Mod(Celestisynth.MODID)
public class Celestisynth {
    public static final String MODID = "celestisynth";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Celestisynth(IEventBus modEventBus) {
        IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        // Don't change
        CSTags.init();

        CSModManager.registerAll(modEventBus, forgeEventBus);
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path.toLowerCase(Locale.ROOT));
    }
}
