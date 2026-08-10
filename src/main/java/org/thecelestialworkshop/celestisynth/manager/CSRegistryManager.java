package org.thecelestialworkshop.celestisynth.manager;

import org.thecelestialworkshop.celestisynth.common.registry.*;
import net.neoforged.bus.api.IEventBus;

public final class CSRegistryManager {

    protected static void registerRegistries(IEventBus modBus) {
        CSCapabilities.ATTACHMENT_TYPES.register(modBus);
        CSDataComponents.DATA_COMPONENTS.register(modBus);
        org.thecelestialworkshop.celestisynth.api.item.CSArmorMaterials.ARMOR_MATERIALS.register(modBus);
        CSAttributes.ATTRIBUTES.register(modBus);
        CSEntityTypes.ENTITY_TYPES.register(modBus);
        CSItems.ITEMS.register(modBus);
        CSBlocks.BLOCKS.register(modBus);
        CSParticleTypes.PARTICLE_TYPES.register(modBus);
        CSBlockEntityTypes.BLOCK_ENTITY_TYPES.register(modBus);
        CSSoundEvents.SOUND_EVENTS.register(modBus);
        CSFeatures.FEATURES.register(modBus);
        CSRecipeTypes.RECIPE_TYPES.register(modBus);
        CSRecipeTypes.RECIPE_SERIALIZERS.register(modBus);
        CSMenuTypes.MENU_TYPES.register(modBus);
        CSVisualTypes.VISUALS.register(modBus);
        CSStructures.STRUCTURE_TYPE.register(modBus);
        CSStructures.STRUCTURE_PIECE.register(modBus);
        CSCreativeTabs.CREATIVE_MODE_TABS.register(modBus);
        CSLootModifiers.LOOT_MODIFIERS.register(modBus);
        CSMobEffects.MOB_EFFECTS.register(modBus);
        CSPlayerAnimations.ANIMATIONS.register(modBus);
        CSStructureModifiers.STRUCTURE_MODIFIER_SERIALIZERS.register(modBus);
    }
}
