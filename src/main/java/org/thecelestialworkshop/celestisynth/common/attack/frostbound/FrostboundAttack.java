package org.thecelestialworkshop.celestisynth.common.attack.frostbound;

import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.item.weapons.FrostboundItem;
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
            this.shootShard(xx, yy, zz);
        }
        if (FrostboundItem.getShard(player) != ItemStack.EMPTY) {
            player.playSound(SoundEvents.BLAZE_SHOOT);
        }
    }

    public void shootShard(double xx, double yy, double zz) {
        FrostboundItem.shootShard(this, stack, player, level, xx, yy, zz);
    }
}
