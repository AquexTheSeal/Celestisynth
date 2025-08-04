package org.thecelestialworkshop.celestisynth.manager;

import org.thecelestialworkshop.celestisynth.api.animation.player.CSAnimator;
import org.thecelestialworkshop.celestisynth.client.events.CSClientMiscEvents;
import org.thecelestialworkshop.celestisynth.client.events.CSClientSetupEvents;
import org.thecelestialworkshop.celestisynth.common.events.CSCommonMiscEvents;
import org.thecelestialworkshop.celestisynth.common.events.CSCommonSetupEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;

public final class CSEventManager {

    protected static void registerEvents(IEventBus modBus, IEventBus forgeBus) {
        registerClientEvents(modBus, forgeBus);
        registerCommonEvents(modBus, forgeBus);
        registerServerEvents(modBus, forgeBus);
    }

    private static void registerClientEvents(IEventBus modBus, IEventBus forgeBus) {
        if (FMLEnvironment.dist.isClient()) {
            modBus.register(CSClientSetupEvents.class);
            forgeBus.register(CSClientMiscEvents.class);
        }

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(CSAnimator::registerAnimationLayer));
    }

    private static void registerCommonEvents(IEventBus modBus, IEventBus forgeBus) {
        modBus.register(CSCommonSetupEvents.CSModSetupEvents.class);
        modBus.register(CSNetworkManager.class);

        forgeBus.register(CSCommonSetupEvents.CSForgeSetupEvents.class);
        forgeBus.register(CSCommonMiscEvents.class);
        forgeBus.register(CSCommandsManager.class);
    }

    private static void registerServerEvents(IEventBus modBus, IEventBus forgeBus) {
        if (FMLEnvironment.dist.isDedicatedServer()) {
        }
    }
}
