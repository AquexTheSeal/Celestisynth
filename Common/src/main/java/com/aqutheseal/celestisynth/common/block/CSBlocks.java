package com.aqutheseal.celestisynth.common.block;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.item.CSItems;
import com.aqutheseal.celestisynth.reg.BlockRegistryObject;
import com.aqutheseal.celestisynth.reg.RegistrationProvider;
import com.aqutheseal.celestisynth.reg.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Supplier;

public class CSBlocks {
    public static final RegistrationProvider<Block> BLOCK = RegistrationProvider.get(Registries.BLOCK, Celestisynth.MODID);

    public static final RegistryObject<Block> SOLAR_CRYSTAL = registerBlock("solar_crystal",
            () -> new SolarCrystalBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED).sound(SoundType.GLASS)
                    .requiresCorrectToolForDrops().strength(3.0F, 9.0F).noOcclusion().emissiveRendering((a, b, c) -> true).lightLevel((a) -> 15)
            )
    );
    public static final RegistryObject<Block> LUNAR_STONE = registerBlock("lunar_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_BLUE).sound(SoundType.STONE)
                    .requiresCorrectToolForDrops().strength(4.0F, 9.0F).lightLevel((a) -> 3)
            )
    );
    public static final RegistryObject<Block> ZEPHYR_DEPOSIT = registerBlock("zephyr_deposit",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_GREEN).sound(SoundType.GILDED_BLACKSTONE)
                    .requiresCorrectToolForDrops().strength(60.5F, 9.0F).lightLevel((a) -> 3)
            )
    );

    private static <B extends Block> BlockRegistryObject<B> registerBlock(String name, Supplier<B> block) {
        final var ro = BLOCK.register(name, block);
        return BlockRegistryObject.wrap(ro);
    }

    private static <B extends Block> RegistryObject<BlockItem> registerBlockItem(String name, Supplier<? extends B> block) {
        return CSItems.ITEM.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }
    public static void init(){}
}
