package com.aqutheseal.celestisynth.api.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public abstract class CSArmorItem extends ArmorItem {

    public CSArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    public abstract CSArmorProperties getArmorProperties();
}
