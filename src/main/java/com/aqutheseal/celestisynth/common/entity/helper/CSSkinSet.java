package com.aqutheseal.celestisynth.common.entity.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CSSkinSet {
    public final ItemStack stack;
    public final Player player;

    public CSSkinSet(ItemStack stack, Player player) {
        this.stack = stack;
        this.player = player;
    }
}
