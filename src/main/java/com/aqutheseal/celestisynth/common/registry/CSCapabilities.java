package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.aqutheseal.celestisynth.common.capabilities.CSItemStackCapabilityProvider;

public class CSCapabilities {

    public static void registerCapabilities() {
        CSEntityCapabilityProvider.register();
        CSItemStackCapabilityProvider.register();
    }
}
