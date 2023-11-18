package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
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
        if (player.getMainHandItem() == stack && player.getOffhandItem() == stack) {
            if (getPlayer().level.random.nextBoolean()) {
                return AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_RIGHT;
            } else {
                return AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_LEFT;
            }
        } else if (player.getMainHandItem() == stack) {
            return AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_RIGHT;
        } else {
            return AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_LEFT;
        }
    }

    @Override
    public int getCooldown() {
        return CSConfig.COMMON.aquafloraSkillCD.get();
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
        CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_PIERCE_START, calculateXLook(player) * 3, 1.2 + calculateYLook(player) * 3, calculateZLook(player) * 3);
        player.playSound(CSSoundRegistry.CS_BLING.get(), 0.15F, 0.5F);
        if (getPlayer().level.isClientSide()) {
            shakeScreens(player, 15, 5, 0.02F);
        }
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() >= 0 && getTimerProgress() <= 15) {
            player.playSound(CSSoundRegistry.CS_AIR_SWING.get(), 0.25F, 1.3F + getPlayer().level.random.nextFloat());
            CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_STAB, -0.5 + getPlayer().level.random.nextDouble() + calculateXLook(player) * 3, (-0.5 + getPlayer().level.random.nextDouble()) + (2 + calculateYLook(player) * 3), -0.5 + getPlayer().level.random.nextDouble() + calculateZLook(player) * 3);
            List<Entity> entities = iterateEntities(getPlayer().level, createAABB(player.blockPosition().offset(calculateXLook(player) * 4.5, 1 + (calculateYLook(player) * 4.5), calculateZLook(player) * 4.5), 2));
            entities.addAll(iterateEntities(getPlayer().level, createAABB(player.blockPosition().offset(calculateXLook(player) * 3, 1 + (calculateYLook(player) * 3), calculateZLook(player) * 3), 2)));
            entities.addAll(iterateEntities(getPlayer().level, createAABB(player.blockPosition().offset(calculateXLook(player) * 1.5, 1 + (calculateYLook(player) * 1.5), calculateZLook(player) * 1.5), 2)));
            if (entities.size() > 0) {
                player.playSound(CSSoundRegistry.CS_BLING.get(), 0.15F, 1F + getPlayer().level.random.nextFloat());
            }
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        hurtNoKB(player, target, (float) (double) CSConfig.COMMON.aquafloraSkillDmg.get() + getSharpnessValue(getStack(), 0.15F));
                        createHitEffect(getStack(), getPlayer().level, player, target);
                    }
                }
            }
        }
    }

    @Override
    public void stopUsing() {
    }
}
