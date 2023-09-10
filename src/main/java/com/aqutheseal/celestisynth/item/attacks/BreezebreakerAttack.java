package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.item.helpers.WeaponAttackInstance;
import com.aqutheseal.celestisynth.item.weapons.TestBreezebreakerItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class BreezebreakerAttack extends WeaponAttackInstance {
    public BreezebreakerAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    public void addComboPoint() {
        if (getTagExtras().getInt(TestBreezebreakerItem.BB_COMBO_POINTS) < 15) {
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
            getTagExtras().putInt(TestBreezebreakerItem.BB_COMBO_POINTS, getTagExtras().getInt(TestBreezebreakerItem.BB_COMBO_POINTS) + 1);
        } else {
            player.playSound(SoundEvents.BEACON_ACTIVATE);
            getTagExtras().putBoolean(TestBreezebreakerItem.AT_BUFF_STATE, !getTagExtras().getBoolean(TestBreezebreakerItem.AT_BUFF_STATE));
            getTagExtras().putInt(TestBreezebreakerItem.BB_COMBO_POINTS, 0);
        }
    }

    @Override
    public void startUsing() {
        addComboPoint();
    }

    public int buffStateModified(int originalValue) {
        if (getTagExtras().getBoolean(TestBreezebreakerItem.AT_BUFF_STATE)) {
            return originalValue / 2;
        }
        return originalValue;
    }
}
