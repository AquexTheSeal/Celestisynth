package com.aqutheseal.celestisynth.common.attack.aquaflora;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.api.entity.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
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
    public AnimationManager.AnimationsList getAnimation() {
        
        if (player.getMainHandItem() == stack && player.getOffhandItem() == stack) return level.random.nextBoolean() ? AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_LEFT : AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_RIGHT;
        else return player.getMainHandItem() == stack ? AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_RIGHT : AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_LEFT;
    }

    @Override
    public int getCooldown() {
        return CSConfigManager.COMMON.aquafloraSkillCD.get();
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

            List<Entity> entities = iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 4.5), (int) (1 + (calculateYLook(player) * 4.5)), (int) (calculateZLook(player) * 4.5)), 2));
            entities.addAll(iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 3), (int) (1 + (calculateYLook(player) * 3)), (int) (calculateZLook(player) * 3)), 2)));
            entities.addAll(iterateEntities(level, createAABB(player.blockPosition().offset((int) (calculateXLook(player) * 1.5), (int) (1 + (calculateYLook(player) * 1.5)), (int) (calculateZLook(player) * 1.5)), 2)));

            if (!entities.isEmpty()) player.playSound(CSSoundEvents.BLING.get(), 0.15F, 1F + level.random.nextFloat());

            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        initiateAbilityAttack(player, target, (float) (double) CSConfigManager.COMMON.aquafloraSkillDmg.get() + getSharpnessValue(getStack(), 0.15F), AttackHurtTypes.RAPID_NO_KB);
                        createHitEffect(getStack(), level, player, target);
                    }
                }
            }
        }
    }

    @Override
    public void stopUsing() {
    }
}
