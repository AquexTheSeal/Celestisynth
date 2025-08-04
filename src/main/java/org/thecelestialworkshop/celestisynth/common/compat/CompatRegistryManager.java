package org.thecelestialworkshop.celestisynth.common.compat;

import org.thecelestialworkshop.celestisynth.common.compat.spellbooks.ISSCompatItems;
import org.thecelestialworkshop.celestisynth.common.compat.spellbooks.ISSItemUtil;
import org.thecelestialworkshop.celestisynth.manager.CSIntegrationManager;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class CompatRegistryManager {

    public static void registerIntegratedRegistries(IEventBus modBus) {
        if (CSIntegrationManager.checkIronsSpellbooks()) {
            ISSCompatItems.SPELLBOOKS_ITEMS.register(modBus);
        }
    }

    public static void manageCompatAttributes(ItemAttributeModifierEvent event) {
        if (CSIntegrationManager.checkIronsSpellbooks()) {
            ISSItemUtil.manageCompatAttributes(event);
        }
    }
}
