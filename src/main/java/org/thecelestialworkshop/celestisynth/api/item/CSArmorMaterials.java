package org.thecelestialworkshop.celestisynth.api.item;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

/**
 * ArmorMaterial is a registry record as of 1.20.5. Durability multipliers
 * (25 solar / 29 lunar) now live at the item registration via
 * {@link ArmorItem.Type#getDurability(int)}.
 */
public class CSArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Celestisynth.MODID);

    public static final int SOLAR_CRYSTAL_DURABILITY_MULTIPLIER = 25;
    public static final int LUNAR_STONE_DURABILITY_MULTIPLIER = 29;

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SOLAR_CRYSTAL = ARMOR_MATERIALS.register("solar_crystal", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.BOOTS, 2);
                map.put(ArmorItem.Type.LEGGINGS, 5);
                map.put(ArmorItem.Type.CHESTPLATE, 7);
                map.put(ArmorItem.Type.HELMET, 2);
            }),
            8,
            SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(CSBlocks.SOLAR_CRYSTAL.get()),
            List.of(new ArmorMaterial.Layer(Celestisynth.prefix("solar_crystal"))),
            1.0F,
            0.0F
    ));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LUNAR_STONE = ARMOR_MATERIALS.register("lunar_stone", () -> new ArmorMaterial(
            Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.BOOTS, 4);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 7);
                map.put(ArmorItem.Type.HELMET, 3);
            }),
            8,
            SoundEvents.ARMOR_EQUIP_GOLD,
            () -> Ingredient.of(CSBlocks.LUNAR_STONE.get()),
            List.of(new ArmorMaterial.Layer(Celestisynth.prefix("lunar_stone"))),
            1.5F,
            2.0F
    ));
}
