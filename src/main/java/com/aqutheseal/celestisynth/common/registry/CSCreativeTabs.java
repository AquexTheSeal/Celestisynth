package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.compat.spellbooks.ISSCompatItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.List;

public class CSCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Celestisynth.MODID);

    public static final RegistryObject<CreativeModeTab> CELESTISYNTH = CREATIVE_MODE_TABS.register("celestisynth_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(CSItems.FROSTBOUND.get()))
                    .title(Component.translatable("creativetab.celestisynth_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        acceptItemRegistry(pOutput, CSItems.ITEMS.getEntries());
                        acceptBlockRegistry(pOutput, CSBlocks.BLOCKS.getEntries());
                        if (CSCompatManager.checkIronsSpellbooks()) {
                            acceptItemRegistry(pOutput, ISSCompatItemRegistry.SPELLBOOKS_ITEMS.getEntries());
                        }
                    }).build()
    );

    public static void acceptItemRegistry(CreativeModeTab.Output output, Collection<RegistryObject<Item>> registry) {
        for (RegistryObject<Item> item : registry) {
            if (!getBlackList().contains(item)) {
                output.accept(item.get());
            }
        }
    }

    public static void acceptBlockRegistry(CreativeModeTab.Output output, Collection<RegistryObject<Block>> registry) {
        for (RegistryObject<Block> block : registry) {
            if (!getBlackList().contains(block)) {
                output.accept(block.get());
            }
        }
    }

    public static List<RegistryObject<? extends ItemLike>> getBlackList() {
        return List.of(
                CSItems.TEMPEST_SPAWN_EGG, CSItems.CELESTIAL_DEBUGGER
        );
    }
}
