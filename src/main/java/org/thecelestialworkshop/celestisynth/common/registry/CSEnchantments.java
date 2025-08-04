package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.enchantments.PulsationEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Celestisynth.MODID);

    public static final RegistryObject<Enchantment> PULSATION = ENCHANTMENTS.register("pulsation", () -> new PulsationEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON,
            new EquipmentSlot[]{EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND}
            )
    );
}
