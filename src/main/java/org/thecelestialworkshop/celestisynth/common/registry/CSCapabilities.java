package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;

public class CSCapabilities {

    public static void registerCapabilities() {
        CSEntityCapabilityProvider.register();
    }
}
