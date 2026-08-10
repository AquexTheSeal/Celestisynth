package org.thecelestialworkshop.celestisynth.manager;

import net.neoforged.bus.api.IEventBus;

public final class CSModManager {

    public static void registerAll(IEventBus modBus, IEventBus forgeBus) {
        CSConfigManager.registerConfigs();
        CSRegistryManager.registerRegistries(modBus);
        CSEventManager.registerEvents(modBus, forgeBus);
    }
}
