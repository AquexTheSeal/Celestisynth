package org.thecelestialworkshop.celestisynth.common.attack.aquaflora;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AquafloraPetalPiercesAttack extends AquafloraAttack {
    public AquafloraPetalPiercesAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_AQUAFLORA_PIERCE_RIGHT.get();
    }

    @Override
    public boolean sameAnimationForBothHands() {
        return true;
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return !getTagController().getBoolean(CHECK_PASSIVE) && !player.isCrouching();
    }

    @Override
    public void startUsing() {
        CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_PIERCE_START.get(), calculateXLook(player) * 3, 1.2 + calculateYLook(player) * 3, calculateZLook(player) * 3);
        player.playSound(CSSoundEvents.BLING.get(), 0.15F, 0.5F);

        if (level.isClientSide()) shakeScreens(player, 15, 5, 0.02F);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() >= 0 && getTimerProgress() <= 15) {
            player.playSound(CSSoundEvents.AIR_SWING.get(), 0.25F, 1.3F + level.random.nextFloat());
            CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_STAB.get(), -0.5 + level.random.nextDouble() + calculateXLook(player) * 3, (-0.5 + level.random.nextDouble()) + (2 + calculateYLook(player) * 3), -0.5 + level.random.nextDouble() + calculateZLook(player) * 3);

            List<Entity> entities = this.iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 4.5), (int) (1 + (calculateYLook(player) * 4.5)), (int) (calculateZLook(player) * 4.5)), 2));
            entities.addAll(iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 3), (int) (1 + (calculateYLook(player) * 3)), (int) (calculateZLook(player) * 3)), 2)));
            entities.addAll(iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 1.5), (int) (1 + (calculateYLook(player) * 1.5)), (int) (calculateZLook(player) * 1.5)), 2)));

            if (!entities.isEmpty()) player.playSound(CSSoundEvents.BLING.get(), 0.15F, 1F + level.random.nextFloat());

            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        this.attributeDependentAttack(player, target, stack, 0.1F, AttackHurtTypes.RAPID_NO_KB);
                        this.createHitEffect(getStack(), level, player, target);
                    }
                }
            }
        }
    }

    @Override
    public void stopUsing() {
    }
}
