package org.thecelestialworkshop.celestisynth.api.item;

import org.thecelestialworkshop.celestisynth.api.animation.player.AnimationManager;
import org.thecelestialworkshop.celestisynth.api.animation.player.LayerManager;
import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface CSWeapon extends CSWeaponUtil {

    int getSkillsAmount();

    default int getPassiveAmount() {
        return 0;
    }

    default boolean hasPassive() {
        return false;
    }

    default void onPlayerHurt(LivingHurtEvent event, ItemStack stack) {
    }

    default void resetExtraValues(ItemStack stack, Player player) {
    }

    ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration);

    default void executeAnimation(Level level, PlayerAnimationContainer animation, WeaponAttackInstance attack, InteractionHand hand) {
        if (attack.sameAnimationForBothHands()) {
            if (hand == InteractionHand.OFF_HAND) {
                AnimationManager.playAnimation(level, animation, LayerManager.MIRRORED_LAYER);
            } else {
                AnimationManager.playAnimation(level, animation);
            }
        } else {
            AnimationManager.playAnimation(level, animation);
        }
    }
}
