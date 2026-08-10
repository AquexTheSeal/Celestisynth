package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.block.CelestialCraftingTable;
import org.thecelestialworkshop.celestisynth.common.block.SolarCrystalBlock;
import org.thecelestialworkshop.celestisynth.common.block.StarlitFactoryBlock;
import org.thecelestialworkshop.celestisynth.common.item.misc.StarlitFactoryBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class CSBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Celestisynth.MODID);

    public static final Supplier<Block> SOLAR_CRYSTAL = registerBlock("solar_crystal",
            () -> new SolarCrystalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).mapColor(MapColor.COLOR_RED).sound(SoundType.GLASS)
                    .requiresCorrectToolForDrops().strength(3.0F, 9.0F).forceSolidOn().noOcclusion().emissiveRendering((a, b, c) -> true).lightLevel((a) -> 15)
            )
    );
    public static final Supplier<Block> LUNAR_STONE = registerBlock("lunar_stone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_BLUE).sound(SoundType.STONE)
                    .requiresCorrectToolForDrops().strength(4.0F, 9.0F).lightLevel((a) -> 3)
            )
    );
    public static final Supplier<Block> ZEPHYR_DEPOSIT = registerBlock("zephyr_deposit",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_LIGHT_GREEN).sound(SoundType.GILDED_BLACKSTONE)
                    .requiresCorrectToolForDrops().strength(60.5F, 9.0F).lightLevel((a) -> 3)
            )
    );
    public static final Supplier<Block> WINTEREIS = registerBlock("wintereis",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ICE).mapColor(MapColor.COLOR_LIGHT_BLUE).sound(SoundType.AMETHYST)
                    .requiresCorrectToolForDrops().friction(0.7F).strength(55F, 8.0F).lightLevel((a) -> 3)
            )
    );

    public static final Supplier<Block> CELESTIAL_CRAFTING_TABLE = registerBlock("celestial_crafting_table",
            () -> new CelestialCraftingTable(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL).mapColor(MapColor.TERRACOTTA_BLUE).sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion().requiresCorrectToolForDrops().strength(60.5F, 9.0F).lightLevel((a) -> 7)
            )
    );

    public static final Supplier<Block> STARLIT_FACTORY = registerStarlitFactoryBlockItem("starlit_factory",
            () -> new StarlitFactoryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL).mapColor(MapColor.TERRACOTTA_BLUE).sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion().requiresCorrectToolForDrops().strength(60.5F, 9.0F).lightLevel((a) -> 7)
            )
    );

    private static <T extends Block> Supplier<T> registerStarlitFactoryBlockItem(String name, Supplier<T> block) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
        CSItems.ITEMS.register(name, () -> new StarlitFactoryBlockItem(toReturn.get(), new Item.Properties().fireResistant().rarity(CSRarityTypes.celestial())));
        return toReturn;
    }

    private static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block) {
        return CSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
