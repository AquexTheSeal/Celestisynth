package com.aqutheseal.celestisynth.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CSCreativeTabRegistry {
    public static CreativeModeTab CELESTISYNTH_TAB;

    @SubscribeEvent
    public static void registerCreativeTab(CreativeModeTabEvent.Register event) {
        CELESTISYNTH_TAB = event.registerCreativeModeTab(new ResourceLocation(Celestisynth.MODID, "celestisynth_tab"), builder -> builder
                .icon(() -> new ItemStack(CSItemRegistry.SOLARIS.get()))
                .title(Component.translatable("itemGroup.celestisynth.celestisynth_tab"))
        );
    }

    @SubscribeEvent
    public static void registerCreativeTab(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CSCreativeTabRegistry.CELESTISYNTH_TAB) {
            for (RegistryObject<Item> items : CSItemRegistry.ITEMS.getEntries()) {
                event.accept(items);
            }
        }
    }
}
