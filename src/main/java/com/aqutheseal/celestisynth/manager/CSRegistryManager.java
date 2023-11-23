package com.aqutheseal.celestisynth.manager;

import com.aqutheseal.celestisynth.common.registry.*;
import net.minecraftforge.eventbus.api.IEventBus;

public final class CSRegistryManager {

    protected static void registerRegistries(IEventBus modBus) {
        CSEntityTypes.ENTITY_TYPES.register(modBus);
        CSItems.ITEMS.register(modBus);
        CSBlocks.BLOCKS.register(modBus);
        CSParticleTypes.PARTICLE_TYPES.register(modBus);
        CSBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modBus);
        CSSoundEvents.SOUND_EVENTS.register(modBus);
        CSFeatures.FEATURES.register(modBus);
        CSFeatures.CONFIGURED_FEATURES.register(modBus);
        CSFeatures.PLACED_FEATURES.register(modBus);
        CSRecipeTypes.RECIPE_TYPES.register(modBus);
        CSRecipeTypes.RECIPE_SERIALIZERS.register(modBus);
        CSMenuTypes.MENU_TYPES.register(modBus);
    }
}
