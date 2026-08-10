package org.thecelestialworkshop.celestisynth.common.registry;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.block.CelestialCraftingTableBlockEntity;
import org.thecelestialworkshop.celestisynth.common.block.StarlitFactoryBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CSBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Celestisynth.MODID);

    public static final Supplier<BlockEntityType<CelestialCraftingTableBlockEntity>> CELESTIAL_CRAFTING_TABLE_TILE = BLOCK_ENTITY_TYPES.register("celestial_crafting_table",
            () -> BlockEntityType.Builder.of(CelestialCraftingTableBlockEntity::new, CSBlocks.CELESTIAL_CRAFTING_TABLE.get()).build(null));

    public static final Supplier<BlockEntityType<StarlitFactoryBlockEntity>> STARLIT_FACTORY_TILE = BLOCK_ENTITY_TYPES.register("starlit_factory",
            () -> BlockEntityType.Builder.of(StarlitFactoryBlockEntity::new, CSBlocks.STARLIT_FACTORY.get()).build(null));
}
