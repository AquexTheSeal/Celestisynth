package org.thecelestialworkshop.celestisynth.common.attack.aquaflora;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AquafloraBlastOffAttack extends AquafloraAttack {

    public AquafloraBlastOffAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_AQUAFLORA_BASH.get();
    }

    @Override
    public int getCooldown() {
        return 30;
    }

    @Override
    public int getAttackStopTime() {
        return 0;
    }

    @Override
    public boolean getCondition() {
        return !getTagController().getBoolean(CHECK_PASSIVE) && player.isCrouching();
    }

    @Override
    public void startUsing() {
        List<Entity> surroundingEntities = iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 4), (int) (2 + (calculateYLook(player) * 3)), (int) (calculateZLook(player) * 4)), 3));

        player.playSound(SoundEvents.WITHER_BREAK_BLOCK, 0.7F, 1.5F);
        CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_BASH.get(), calculateXLook(player) * 2, 1.5, calculateZLook(player) * 2);

        for (Entity entityBatch : surroundingEntities) {
            if (entityBatch instanceof LivingEntity target) {
                if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                    target.setDeltaMovement((target.getX() - player.getX()) * 0.4,   1, (target.getZ() - player.getZ()) * 0.4);
                    this.attributeDependentAttack(player, target, stack, 1.3F, AttackHurtTypes.NO_KB);
                    this.createHitEffect(getStack(), level, player, target);
                    CSWeaponUtil.disableRunningWeapon(target);
                }
            }
        }

        double check = player.onGround() ? 0.3 : 0.14;

        if (level.isClientSide()) shakeScreens(player, 3, 2, 0.015F);

        player.setDeltaMovement(player.getDeltaMovement().add(calculateXLook(player) * check, 0, calculateZLook(player) * check));
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(CHECK_PASSIVE, true);
    }
}
