package com.aqutheseal.celestisynth.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CSCreativeTabRegistry {

    public static final CreativeModeTab CELESTISYNTH = new CreativeModeTab(".celestisynth.celestisynth_tab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CSItemRegistry.SOLARIS.get());
        }
    };
}
