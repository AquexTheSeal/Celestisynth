package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.common.block.CSBlocks;
import com.aqutheseal.celestisynth.common.item.CSItems;
import com.aqutheseal.celestisynth.reg.BlockRegistryObject;
import com.aqutheseal.celestisynth.reg.RegistryObject;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CSCreativeTabRegistry {
    public static CreativeModeTab CELESTISYNTH_TAB;

    @SubscribeEvent
    public static void registerCreativeTab(CreativeModeTabEvent.Register event) {
        CELESTISYNTH_TAB = event.registerCreativeModeTab(new ResourceLocation(Celestisynth.MODID, "tab"), builder -> builder
                .icon(() -> new ItemStack(CSItems.SOLARIS.get()))
                .title(Component.translatable("tab" + Celestisynth.MODID + "tab"))
        );
    }

    @SubscribeEvent
    public static void registerCreativeTab(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CSCreativeTabRegistry.CELESTISYNTH_TAB) {
            for (RegistryObject<Item> items : CSItems.ITEM.getEntries()) {
                event.accept(items);
            }
            for (RegistryObject<Block> blocks : CSBlocks.BLOCK.getEntries()) {
                event.accept(blocks);
            }
        }
    }
}
