package com.aqutheseal.celestisynth.common.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CSCreativeTabs {

    public static final CreativeModeTab CELESTISYNTH = new CreativeModeTab("celestisynth.celestisynth_tab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CSItems.SOLARIS.get());
        }
    };
}
