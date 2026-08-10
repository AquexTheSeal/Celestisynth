package org.thecelestialworkshop.celestisynth.datagen.providers;

import org.thecelestialworkshop.celestisynth.common.registry.CSEnchantments;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Data-driven replacement for the 1.20.1 code-based Pulsation enchantment
 * (Rarity.RARE, EnchantmentCategory.WEAPON, mainhand + offhand).
 */
public class CSEnchantmentProvider {

    public static void bootstrap(BootstrapContext<Enchantment> ctx) {
        HolderGetter<Item> items = ctx.lookup(Registries.ITEM);

        ctx.register(CSEnchantments.PULSATION, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.dynamicCost(11, 10),
                        Enchantment.dynamicCost(16, 10),
                        4,
                        EquipmentSlotGroup.MAINHAND, EquipmentSlotGroup.OFFHAND
                )
        ).build(CSEnchantments.PULSATION.location()));
    }
}
