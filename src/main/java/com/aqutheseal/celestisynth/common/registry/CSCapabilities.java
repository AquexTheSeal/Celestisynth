package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;

public class CSCapabilities {
    public static void registerCapabilities() {
        CSEntityCapabilityProvider.register();
    }
}
