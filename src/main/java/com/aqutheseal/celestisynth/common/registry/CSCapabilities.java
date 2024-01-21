package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.common.capabilities.EntityFrostboundProvider;

public class CSCapabilities {
    public static void registerCapabilities() {
        EntityFrostboundProvider.register();
    }
}
