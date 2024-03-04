package com.aqutheseal.celestisynth.common.compat;

import com.aqutheseal.celestisynth.common.compat.spellbooks.ISSCompatItemRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

public class CompatRegistryManager {

    public static void registerIntegratedRegistries(IEventBus modBus) {
        if (CSCompatManager.checkIronsSpellbooks()) {
            ISSCompatItemRegistry.SPELLBOOKS_ITEMS.register(modBus);
        }
    }
}
