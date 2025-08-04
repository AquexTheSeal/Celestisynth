package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.compat.spellbooks.ISSCompatItems;
import org.thecelestialworkshop.celestisynth.manager.CSIntegrationManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
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
                        acceptEnchantmentRegistry(pOutput, CSEnchantments.ENCHANTMENTS.getEntries());
                        if (CSIntegrationManager.checkIronsSpellbooks()) {
                            acceptItemRegistry(pOutput, ISSCompatItems.SPELLBOOKS_ITEMS.getEntries());
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

    public static void acceptEnchantmentRegistry(CreativeModeTab.Output output, Collection<RegistryObject<Enchantment>> registry) {
        for (RegistryObject<Enchantment> enchantment : registry) {
            if (!getEnchantmentBlackList().contains(enchantment)) {
                for (int i = enchantment.get().getMinLevel(); i <= enchantment.get().getMaxLevel(); i++) {
                    output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment.get(), i)));
                }
            }
        }
    }

    public static List<RegistryObject<? extends ItemLike>> getBlackList() {
        return List.of(
                CSItems.TEMPEST_SPAWN_EGG, CSItems.CELESTIAL_DEBUGGER
        );
    }

    public static List<RegistryObject<Enchantment>> getEnchantmentBlackList() {
        return List.of(
        );
    }
}
