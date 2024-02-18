package com.aqutheseal.celestisynth.client.renderers.misc;

import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.common.registry.CSRarityTypes;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CSTooltipRenderer {
    @OnlyIn(Dist.CLIENT)
    public static int menu;
    @OnlyIn(Dist.CLIENT)
    public static int scroll;

    public static void manageCelestialTooltips(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();

        Style tierColor = Style.EMPTY.withColor(0x8BDEFF);
        Style navigationNoticeColor = Style.EMPTY.withColor(0x96D400);
        Style abilityNoticeColor = Style.EMPTY.withColor(16755200);
        Style highlightedAbilityColor = Style.EMPTY.withColor(0x50ACFF);
        Style defaultAbilityColor = Style.EMPTY.withColor(5592405);
        Style abilityConditionColor = Style.EMPTY.withColor(0x8A89FF);
        Style descColor = Style.EMPTY.withColor(0xA9B8BB);

        if (!stack.isEmpty() && stack.getItem() instanceof CSWeapon cs) {
            List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
            List<Either<FormattedText, TooltipComponent>> elementsToAdd = new ArrayList<>();
            final Component empty = Component.literal(" ");

            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.celestial_tier").withStyle(tierColor).withStyle(ChatFormatting.BOLD)));
            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.shift_notice").withStyle(navigationNoticeColor)));
            elementsToAdd.add(Either.left(empty));

            MutableComponent passiveNotice = Component.empty();
            if (cs.getPassiveAmount() > 0) {
                passiveNotice = Component.translatable("item.celestisynth.passive_notice").withStyle(menu % 2 == 0 ? abilityNoticeColor : descColor);
            }
            MutableComponent skillNotice = Component.empty();
            if (cs.getSkillsAmount() > 0) {
                skillNotice = Component.translatable("item.celestisynth.skill_notice").withStyle(menu % 2 == 1 ? abilityNoticeColor : descColor);
            }
            MutableComponent border = Component.empty();
            if (cs.getPassiveAmount() > 0 && cs.getSkillsAmount() > 0) {
                border = Component.literal(" | ").withStyle(descColor);
            }
            elementsToAdd.add(Either.left(passiveNotice.append(border).append(skillNotice)));

            elementsToAdd.add(Either.left(empty));
            addBorders(elementsToAdd);
            elementsToAdd.add(Either.left(empty));

            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.scroll_notice").withStyle(navigationNoticeColor)));
            elementsToAdd.add(Either.left(empty));

            if (menu % 2 == 0) {
                if (cs.getPassiveAmount() > 0) {
                    for (int v = 1; v < cs.getPassiveAmount() + 1; v++) {
                        int i = (scroll % cs.getPassiveAmount()) + 1;
                        boolean shouldHighlight = v == i;
                        if (shouldHighlight && i != 1) {
                            elementsToAdd.add(Either.left(empty));
                        }
                        elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".passive_" + v)
                                .withStyle(shouldHighlight ? ChatFormatting.UNDERLINE : ChatFormatting.RESET)
                                .withStyle(shouldHighlight ? highlightedAbilityColor : defaultAbilityColor))
                        );
                        if (shouldHighlight) {
                            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".passive_desc_" + v).withStyle(descColor).withStyle(ChatFormatting.ITALIC)));
                        }
                        if (shouldHighlight) {
                            elementsToAdd.add(Either.left(empty));
                        }
                    }
                }
            } else if (menu % 2 == 1) {
                if (cs.getSkillsAmount() > 0) {
                    for (int v = 1; v < cs.getSkillsAmount() + 1; v++) {
                        int i = (scroll % cs.getSkillsAmount()) + 1;
                        boolean shouldHighlight = v == i;
                        if (shouldHighlight && i != 1) {
                            elementsToAdd.add(Either.left(empty));
                        }
                        elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".skill_" + v)
                                .withStyle(shouldHighlight ? ChatFormatting.UNDERLINE : ChatFormatting.RESET)
                                .withStyle(shouldHighlight ? highlightedAbilityColor : defaultAbilityColor))
                        );
                        if (shouldHighlight) {
                            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".condition_" + v).withStyle(abilityConditionColor)));
                            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".desc_" + v).withStyle(descColor).withStyle(ChatFormatting.ITALIC)));
                        }
                        if (shouldHighlight) {
                            elementsToAdd.add(Either.left(empty));
                        }
                    }
                }
            }

            elementsToAdd.add(Either.left(empty));
            addBorders(elementsToAdd);

            ListIterator<Either<FormattedText, TooltipComponent>> iterator = elementsToAdd.listIterator(elementsToAdd.size());
            while (iterator.hasPrevious()) elements.add(1, iterator.previous());
        }
    }

    public static void manageTooltipScrolling(double delta) {
        if (delta < 0) {
            scroll = scroll + 1 == Integer.MAX_VALUE ? 0 : scroll + 1;
        }
        if (delta > 0) {
            scroll = scroll - 1 == 0 ? Integer.MAX_VALUE : scroll - 1;
        }
    }

    public static void manageKeyPress(double keyCode) {
        if (keyCode == 340 || keyCode == 341) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.ENDER_EYE_DEATH, 1.0F));
            menu = menu + 1 == Integer.MAX_VALUE ? 0 : menu + 1;
        }
    }

    private static void addBorders(List<Either<FormattedText, TooltipComponent>> list) {
        MutableComponent border = Component.literal("------------{ o }------------");
        list.add(Either.left(border.withStyle(ChatFormatting.GRAY)));
    }

    public static void manageTooltipColors(RenderTooltipEvent.Color event) {
        int argb = (0xFF << 24);
        if (event.getItemStack().getRarity() == CSRarityTypes.CELESTIAL) {
            event.setBackgroundStart(argb + 0x000002);
            event.setBackgroundEnd(argb + 0x00003f);
            event.setBorderStart(argb + 0xeab80f);
            event.setBorderEnd(argb + 0x06deb1);
        }
    }
}
