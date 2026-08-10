package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class CSCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Celestisynth.MODID);

    public static final Supplier<CreativeModeTab> CELESTISYNTH = CREATIVE_MODE_TABS.register("celestisynth_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(CSItems.FROSTBOUND.get()))
                    .title(Component.translatable("creativetab.celestisynth_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        acceptItemRegistry(pOutput, CSItems.ITEMS.getEntries());
                        acceptBlockRegistry(pOutput, CSBlocks.BLOCKS.getEntries());
                        acceptEnchantments(pParameters, pOutput);
                    }).build()
    );

    public static void acceptItemRegistry(CreativeModeTab.Output output, Collection<? extends Supplier<? extends Item>> registry) {
        for (Supplier<? extends Item> item : registry) {
            if (!getBlackList().contains(item)) {
                output.accept(item.get());
            }
        }
    }

    public static void acceptBlockRegistry(CreativeModeTab.Output output, Collection<? extends Supplier<? extends Block>> registry) {
        for (Supplier<? extends Block> block : registry) {
            if (!getBlackList().contains(block)) {
                output.accept(block.get());
            }
        }
    }

    public static void acceptEnchantments(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        HolderLookup.RegistryLookup<Enchantment> lookup = parameters.holders().lookupOrThrow(Registries.ENCHANTMENT);
        for (var key : CSEnchantments.ALL) {
            Optional<Holder.Reference<Enchantment>> holder = lookup.get(key);
            holder.ifPresent(enchantment -> {
                for (int i = enchantment.value().getMinLevel(); i <= enchantment.value().getMaxLevel(); i++) {
                    output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i)));
                }
            });
        }
    }

    public static List<Supplier<? extends ItemLike>> getBlackList() {
        return List.of(
                CSItems.TEMPEST_SPAWN_EGG, CSItems.CELESTIAL_DEBUGGER
        );
    }
}
