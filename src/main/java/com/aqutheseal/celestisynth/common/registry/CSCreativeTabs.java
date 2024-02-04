package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CSCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Celestisynth.MODID);

    public static final RegistryObject<CreativeModeTab> CELESTISYNTH = CREATIVE_MODE_TABS.register("celestisynth_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(CSItems.FROSTBOUND.get()))
                    .title(Component.translatable("creativetab.celestisynth_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        for (RegistryObject<Item> item : CSItems.ITEMS.getEntries()) {
                            if (item != CSItems.FROSTBOUND && item != CSItems.TEMPEST_SPAWN_EGG) {
                                pOutput.accept(item.get());
                            }
                        }
                        for (RegistryObject<Block> block : CSBlocks.BLOCKS.getEntries()) {
                            pOutput.accept(block.get());
                        }
                    }).build()
    );
}
