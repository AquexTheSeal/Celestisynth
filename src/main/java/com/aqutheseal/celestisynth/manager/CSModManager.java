package com.aqutheseal.celestisynth.manager;

import net.minecraftforge.eventbus.api.IEventBus;

public final class CSModManager {

    public static void registerAll(IEventBus modBus, IEventBus forgeBus) {
        CSConfigManager.registerConfigs();
        CSRegistryManager.registerRegistries(modBus);
        CSEventManager.registerEvents(modBus, forgeBus);
    }
}
