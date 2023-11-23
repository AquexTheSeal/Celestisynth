package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.block.CelestialCraftingTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Celestisynth.MODID);

    public static final RegistryObject<BlockEntityType<CelestialCraftingTableBlockEntity>> CELESTIAL_CRAFTING_TABLE_TILE = BLOCK_ENTITY_TYPES.register("celestial_crafting_table",
            () -> BlockEntityType.Builder.of(CelestialCraftingTableBlockEntity::new, CSBlocks.CELESTIAL_CRAFTING_TABLE.get()).build(null));
}
