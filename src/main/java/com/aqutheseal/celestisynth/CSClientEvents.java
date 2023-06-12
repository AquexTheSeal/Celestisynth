package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.item.helpers.CSRarityTypes;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CSClientEvents {

    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {
        int argb = (0xFF << 24);
        if (event.getItemStack().getRarity() == CSRarityTypes.CELESTIAL) {
            event.setBackgroundStart(argb + 0x0E1F41);
            event.setBackgroundEnd(argb + 0x0E0B29);
            event.setBorderStart(argb + 0xEed042);
            event.setBorderEnd(argb + 0xBF512A);
        }
    }

    @SubscribeEvent
    public static void onToolTipComponent(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
        if(!stack.isEmpty() && stack.getRarity() == CSRarityTypes.CELESTIAL) {
            List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();

            List<Either<FormattedText, TooltipComponent>> elementsToAdd = new ArrayList<>();

            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.celestial_tier").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD)));
            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.shift_notice").withStyle(ChatFormatting.GREEN)));

            if (Screen.hasShiftDown() || Screen.hasControlDown()) {
                elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".skill_0").withStyle(ChatFormatting.GOLD)));
                elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".desc_0").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)));
                elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".skill_1").withStyle(ChatFormatting.GOLD)));
                elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".desc_1").withStyle(ChatFormatting.DARK_AQUA).withStyle(ChatFormatting.ITALIC)));
            }

            ListIterator<Either<FormattedText, TooltipComponent>> iterator = elementsToAdd.listIterator(elementsToAdd.size());
            while (iterator.hasPrevious()) {
                elements.add(1, iterator.previous());
            }
        }
    }
}
