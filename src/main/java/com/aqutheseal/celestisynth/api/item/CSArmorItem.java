package com.aqutheseal.celestisynth.api.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public abstract class CSArmorItem extends ArmorItem {

    public CSArmorItem(ArmorMaterial pMaterial, EquipmentSlot pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
    }

    public abstract CSArmorProperties getArmorProperties();
}
