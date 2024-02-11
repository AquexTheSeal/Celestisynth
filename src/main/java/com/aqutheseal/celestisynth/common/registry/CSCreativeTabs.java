package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class CSCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Celestisynth.MODID);

    public static final RegistryObject<CreativeModeTab> CELESTISYNTH = CREATIVE_MODE_TABS.register("celestisynth_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(CSItems.FROSTBOUND.get()))
                    .title(Component.translatable("creativetab.celestisynth_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        for (RegistryObject<Item> item : CSItems.ITEMS.getEntries()) {
                            if (!getBlackList().contains(item)) {
                                pOutput.accept(item.get());
                            }
                        }
                        for (RegistryObject<Block> block : CSBlocks.BLOCKS.getEntries()) {
                            if (!getBlackList().contains(block)) {
                                pOutput.accept(block.get());
                            }
                        }
                    }).build()
    );

    public static List<RegistryObject<? extends ItemLike>> getBlackList() {
        return List.of(
                CSItems.TEMPEST_SPAWN_EGG, CSItems.CELESTIAL_DEBUGGER
        );
    }
}
