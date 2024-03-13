package com.aqutheseal.celestisynth.common.entity.helper;

import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CSSkinSet implements CSWeaponUtil {
    public final ItemStack stack;
    public final Player player;
    public final Level level;

    public CSSkinSet(ItemStack stack, Player player) {
        this.stack = stack;
        this.player = player;
        this.level = player.level();
    }
}
