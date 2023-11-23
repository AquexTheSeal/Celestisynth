package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.block.CelestialCraftingTable;
import com.aqutheseal.celestisynth.common.block.SolarCrystalBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CSBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Celestisynth.MODID);

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

    public static final RegistryObject<Block> CELESTIAL_CRAFTING_TABLE = registerBlock("celestial_crafting_table",
            () -> new CelestialCraftingTable(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.TERRACOTTA_LIGHT_BLUE).sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion().requiresCorrectToolForDrops().strength(60.5F, 9.0F).lightLevel((a) -> 7)
            )
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return CSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(CSCreativeTabs.CELESTISYNTH)));
    }
}
