package com.aqutheseal.celestisynth.common.attack.frostbound;

import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.item.weapons.FrostboundItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class FrostboundAttack extends WeaponAttackInstance {
    public FrostboundAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    public FrostboundAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    public void shootShard(int amount) {
        for (int i = 0; i < amount; i++) {
            double xx = level.random.nextGaussian() * 1.5;
            double yy = level.random.nextGaussian() * 1.5;
            double zz = level.random.nextGaussian() * 1.5;
            shootShard(xx, yy, zz);
        }
        if (FrostboundItem.getShard(player) != ItemStack.EMPTY) {
            player.playSound(SoundEvents.BLAZE_SHOOT);
        }
    }

    public void shootShard(double xx, double yy, double zz) {
        FrostboundItem.shootShard(this, player, level, xx, yy, zz);
    }
}
